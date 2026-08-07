<script setup>
import { ref, onMounted } from 'vue'
import { getAllSpecies, getVarietiesBySpecies } from '../../services/registryService.js'

const species = ref([])
const error = ref(null)

const selectedSpecies = ref(null)
const varieties = ref([])
const varietiesError = ref(null)
const loadingVarieties = ref(false)

onMounted(async () => {
  try {
    species.value = await getAllSpecies()
  } catch {
    error.value = 'Impossible de charger la liste des espèces.'
  }
})


async function selectSpecies(speciesName) {
  selectedSpecies.value = speciesName
  varieties.value = []
  varietiesError.value = null
  loadingVarieties.value = true
  try {
    varieties.value = await getVarietiesBySpecies(speciesName)
  } catch {
    varietiesError.value = 'Impossible de charger les variétés de cette espèce.'
  } finally {
    loadingVarieties.value = false
  }
}
</script>

<template>
  <div class="page">
    <h1>Catalogue</h1>

    <div class="columns">
      <section class="species">
        <h2>Espèces</h2>
        <p v-if="error" class="error">{{ error }}</p>
        <ul v-else class="list-reset">
          <li v-for="s in species" :key="s.name">
            <button
              type="button"
              class="btn"
              :class="{ selected: selectedSpecies === s.name }"
              @click="selectSpecies(s.name)"
            >
              {{ s.name }}
            </button>
          </li>
        </ul>
      </section>

      <section class="varieties">
        <h2>Variétés</h2>
        <p v-if="!selectedSpecies">Sélectionner une espèce pour voir ses variétés.</p>
        <p v-else-if="loadingVarieties">Chargement…</p>
        <p v-else-if="varietiesError" class="error">{{ varietiesError }}</p>
        <p v-else-if="varieties.length === 0">Aucune variété enregistrée pour cette espèce.</p>
        <ul v-else class="list-reset">
          <li v-for="v in varieties" :key="v.name" class="list-card">
            <strong>{{ v.name }}</strong>
            <span class="details">rayon {{ v.radius }} · plantation {{ v.plantationStart }}–{{ v.plantationEnd }} · récolte {{ v.harvestDuration }} mois après plantation</span>
          </li>
        </ul>
      </section>
    </div>
  </div>
</template>

<style scoped>
.columns {
  display: flex;
  gap: 32px;
  flex-wrap: wrap;
}

.species,
.varieties {
  flex: 1 1 280px;
}

ul {
  gap: 4px;
}

.species button {
  width: 100%;
  text-align: left;
}

.species button.selected {
  border-color: var(--accent-border);
  background: var(--accent-bg);
}

.varieties li {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.varieties .details {
  font-size: 14px;
  color: var(--text);
}
</style>
