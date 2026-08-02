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
  <div class="catalog">
    <h1>Catalogue</h1>

    <div class="columns">
      <section class="species">
        <h2>Espèces</h2>
        <p v-if="error" class="error">{{ error }}</p>
        <ul v-else>
          <li v-for="s in species" :key="s.name">
            <button
              type="button"
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
        <ul v-else>
          <li v-for="v in varieties" :key="v.name">
            <strong>{{ v.name }}</strong>
            <span class="details">rayon {{ v.radius }} · plantation {{ v.plantationStart }}–{{ v.plantationEnd }} · récolte {{ v.harvestDuration }} mois après plantation</span>
          </li>
        </ul>
      </section>
    </div>
  </div>
</template>

<style scoped>
.catalog {
  padding: 32px 20px;
  text-align: left;
}

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
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.species button {
  width: 100%;
  text-align: left;
  padding: 8px 12px;
  border-radius: 6px;
  border: 1px solid var(--border);
  background: none;
  color: var(--text-h);
  cursor: pointer;
}

.species button.selected {
  border-color: var(--accent-border);
  background: var(--accent-bg);
}

.varieties li {
  padding: 8px 12px;
  border-radius: 6px;
  border: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.varieties .details {
  font-size: 14px;
  color: var(--text);
}

.error {
  color: #d0342c;
}
</style>
