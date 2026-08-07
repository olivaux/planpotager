<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import {
  getGarden,
  getGardenPlants,
  getGardenAreas,
  addPlantToGarden,
  updatePlantPosition,
  getAvailableStates,
  setPlantState,
  removePlantFromGarden,
} from '../../services/gardenService.js'
import { getAvailablePlants } from '../../services/plantService.js'
import { useKonvaZoomPan } from '../../composables/useKonvaZoomPan.js'
import { EDGES, areaToPoints, edgeMidpoint, edgeLength } from '../../utils/areaGeometry.js'

const route = useRoute()
const gardenId = Number(route.params.id)

const garden = ref(null)
const plants = ref([]) // GardenPlantDTO[]
const areas = ref([]) // AreaDTO[]
const availableStates = ref([])
const ownedPlants = ref([]) // PlantDTO[], toutes les plantes du compte (placées ou non)
const loading = ref(true)
const error = ref(null)

const { stageConfig, stagePos, scale, onWheel } = useKonvaZoomPan({ width: 900, height: 560 })

const plantsById = computed(() => new Map(ownedPlants.value.map((p) => [p.id, p])))

const unplacedPlants = computed(() => {
  const placedIds = new Set(plants.value.map((p) => p.plantId))
  return ownedPlants.value.filter((p) => !placedIds.has(p.id))
})

const selectedPlantId = ref(null)
const selectedPlant = computed(
  () => plants.value.find((p) => p.plantId === selectedPlantId.value) ?? null,
)

async function loadAll() {
  loading.value = true
  error.value = null
  try {
    const [gardenResult, plantsResult, areasResult, statesResult, ownedResult] = await Promise.all([
      getGarden(gardenId),
      getGardenPlants(gardenId),
      getGardenAreas(gardenId),
      getAvailableStates(gardenId, 0),
      getAvailablePlants(),
    ])
    garden.value = gardenResult
    plants.value = plantsResult
    areas.value = areasResult
    availableStates.value = statesResult
    ownedPlants.value = ownedResult
  } catch {
    error.value = 'Impossible de charger ce potager.'
  } finally {
    loading.value = false
  }
}

onMounted(loadAll)

// --- Placement d'une plante (drag depuis la palette, drop sur le canvas) ---

function onPaletteDragStart(event, plant) {
  event.dataTransfer.setData('text/plain', String(plant.id))
}

async function onCanvasDrop(event) {
  const plantId = Number(event.dataTransfer.getData('text/plain'))
  if (!plantId) {
    return
  }

  const rect = event.currentTarget.getBoundingClientRect()
  const x = Math.round((event.clientX - rect.left - stagePos.value.x) / scale.value)
  const y = Math.round((event.clientY - rect.top - stagePos.value.y) / scale.value)

  try {
    await addPlantToGarden(gardenId, { plantId, x, y })
    plants.value = [...plants.value, { id: null, x, y, state: 'A_PLANTER', plantId }]
  } catch {
    error.value = 'Impossible de placer cette plante.'
  }
}

// --- Déplacement d'une plante déjà placée ---

async function onPlantDragEnd(plant, konvaEvent) {
  const x = Math.round(konvaEvent.target.x())
  const y = Math.round(konvaEvent.target.y())
  plant.x = x
  plant.y = y
  try {
    await updatePlantPosition(gardenId, plant.plantId, { x, y })
  } catch {
    error.value = 'Impossible de déplacer cette plante.'
  }
}

// --- Sélection / état / retrait d'une plante ---

function selectPlant(plant) {
  selectedPlantId.value = plant.plantId
}

async function changeState(newState) {
  if (!selectedPlant.value || !newState) {
    return
  }
  try {
    await setPlantState(gardenId, selectedPlant.value.plantId, newState)
    selectedPlant.value.state = newState
  } catch {
    error.value = "Impossible de changer l'état de cette plante."
  }
}

async function removeSelectedPlant() {
  if (!selectedPlant.value) {
    return
  }
  if (!window.confirm('Retirer cette plante du potager ?')) {
    return
  }
  try {
    await removePlantFromGarden(gardenId, selectedPlant.value.plantId)
    plants.value = plants.value.filter((p) => p.plantId !== selectedPlant.value.plantId)
    selectedPlantId.value = null
  } catch {
    error.value = 'Impossible de retirer cette plante.'
  }
}

// --- Zones (affichage en lecture seule, gérées depuis GardenStructure.vue) ---
</script>

<template>
  <div class="page garden-detail">
    <p v-if="error" class="error">{{ error }}</p>
    <p v-if="loading">Chargement…</p>

    <template v-else-if="garden">
      <div class="garden-header">
        <h1>{{ garden.name }}</h1>
        <nav class="garden-nav">
          <RouterLink :to="{ name: 'garden-structure', params: { id: gardenId } }">
            Structure du potager
          </RouterLink>
          <RouterLink :to="{ name: 'garden-list' }">Mes potagers</RouterLink>
        </nav>
      </div>

      <div class="garden-layout">
        <aside class="garden-sidebar">
          <section>
            <h2>Mes plantes disponibles</h2>
            <p v-if="unplacedPlants.length === 0" class="hint">
              Toutes vos plantes sont déjà placées.
            </p>
            <ul v-else class="list-reset palette">
              <li
                v-for="plant in unplacedPlants"
                :key="plant.id"
                class="list-card"
                draggable="true"
                @dragstart="onPaletteDragStart($event, plant)"
              >
                {{ plant.variety }}
              </li>
            </ul>
            <p class="hint">Glisser une plante sur le potager pour la placer.</p>
          </section>

          <section v-if="selectedPlant" class="plant-panel">
            <h2>{{ plantsById.get(selectedPlant.plantId)?.variety ?? 'Plante' }}</h2>
            <label class="field">
              État
              <select
                :value="selectedPlant.state"
                @change="changeState($event.target.value)"
              >
                <option v-for="s in availableStates" :key="s" :value="s">{{ s }}</option>
              </select>
            </label>
            <button type="button" class="btn" @click="removeSelectedPlant">Retirer du potager</button>
          </section>
        </aside>

        <div class="canvas-wrapper" @dragover.prevent @drop="onCanvasDrop">
          <v-stage :config="stageConfig" @wheel="onWheel">
            <v-layer>
              <template v-for="area in areas" :key="area.id">
                <v-line
                  :config="{
                    points: areaToPoints(area),
                    closed: true,
                    stroke: '#aa3bff',
                    strokeWidth: 2,
                    fill: 'rgba(170, 59, 255, 0.08)',
                  }"
                />
                <v-text
                  v-for="[keyA, keyB] in EDGES"
                  :key="`${area.id}-${keyA}-${keyB}`"
                  :config="{
                    x: edgeMidpoint(area, keyA, keyB).x,
                    y: edgeMidpoint(area, keyA, keyB).y,
                    text: edgeLength(area, keyA, keyB),
                    fontSize: 12,
                    fill: '#6b6375',
                  }"
                />
              </template>

              <v-group
                v-for="plant in plants"
                :key="plant.plantId"
                :config="{ x: plant.x, y: plant.y, draggable: true }"
                @dragend="onPlantDragEnd(plant, $event)"
                @click="selectPlant(plant)"
                @tap="selectPlant(plant)"
              >
                <v-circle
                  :config="{
                    radius: 10,
                    fill: plant.plantId === selectedPlantId ? '#2c8a3d' : '#aa3bff',
                    stroke: '#08060d',
                    strokeWidth: 1,
                  }"
                />
              </v-group>
            </v-layer>
          </v-stage>
        </div>
      </div>
    </template>
  </div>
</template>

<style scoped>
.palette {
  margin-bottom: 8px;
  gap: 4px;
}

.palette li {
  cursor: grab;
}

.hint {
  font-size: 13px;
  color: var(--text);
}

.plant-panel {
  padding: 12px;
  border-radius: 6px;
  border: 1px solid var(--accent-border);
  background: var(--accent-bg);
  display: flex;
  flex-direction: column;
  gap: 8px;
}
</style>
