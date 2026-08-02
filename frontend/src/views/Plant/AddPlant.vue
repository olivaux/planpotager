<script setup>
import { ref, computed, onMounted } from 'vue'
import { getAllSpecies, getVarietiesBySpecies } from '../../services/registryService.js'
import { addPlant } from '../../services/plantService.js'


const species = ref([])
const speciesError = ref(null)

const selectedSpecies = ref('')
const varieties = ref([])
const varietiesError = ref(null)
const loadingVarieties = ref(false)

const selectedVariety = ref('')
const supplier = ref('')

const submitting = ref(false)
const submitError = ref(null)
const submitted = ref(false)

const canSubmit = computed(() => selectedVariety.value !== '' && !submitting.value)

onMounted(async () => {
  try {
    species.value = await getAllSpecies()
  } catch {
    speciesError.value = 'Impossible de charger la liste des espèces.'
  }
})

async function onSpeciesChange() {
  selectedVariety.value = ''
  varieties.value = []
  varietiesError.value = null

  if (!selectedSpecies.value) {
    return
  }

  loadingVarieties.value = true
  try {
    varieties.value = await getVarietiesBySpecies(selectedSpecies.value)
  } catch {
    varietiesError.value = 'Impossible de charger les variétés de cette espèce.'
  } finally {
    loadingVarieties.value = false
  }
}

async function submit() {
  if (!canSubmit.value) {
    return
  }

  submitting.value = true
  submitError.value = null
  submitted.value = false

  try {
    await addPlant({ variety: selectedVariety.value, supplier: supplier.value })
    submitted.value = true
    selectedVariety.value = ''
    supplier.value = ''
  } catch {
    submitError.value = "Impossible d'ajouter cette plante."
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="add-plant">
    <h1>Ajouter une plante</h1>

    <p v-if="submitted" class="success">Plante ajoutée à votre compte.</p>
    <p v-if="submitError" class="error">{{ submitError }}</p>
    <p v-if="speciesError" class="error">{{ speciesError }}</p>

    <form @submit.prevent="submit">
      <label>
        Espèce
        <select v-model="selectedSpecies" @change="onSpeciesChange">
          <option value="" disabled>— Choisir une espèce —</option>
          <option v-for="s in species" :key="s.name" :value="s.name">{{ s.name }}</option>
        </select>
      </label>

      <label>
        Variété
        <select v-model="selectedVariety" :disabled="!selectedSpecies || loadingVarieties">
          <option value="" disabled>
            {{ loadingVarieties ? 'Chargement…' : '— Choisir une variété —' }}
          </option>
          <option v-for="v in varieties" :key="v.name" :value="v.name">{{ v.name }}</option>
        </select>
      </label>
      <p v-if="varietiesError" class="error">{{ varietiesError }}</p>

      <label>
        Fournisseur
        <input v-model="supplier" type="text" />
      </label>

      <button type="submit" :disabled="!canSubmit">Ajouter</button>
    </form>
  </div>
</template>

<style scoped>
.add-plant {
  padding: 32px 20px;
  max-width: 420px;
  margin: 0 auto;
  text-align: left;
}

form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 4px;
  color: var(--text);
}

select,
input {
  padding: 8px;
  border-radius: 6px;
  border: 1px solid var(--border);
  background: var(--bg);
  color: var(--text-h);
  font: inherit;
}

button {
  align-self: flex-start;
  padding: 10px 20px;
  border-radius: 6px;
  border: 2px solid var(--accent-border);
  background: var(--accent-bg);
  color: var(--text-h);
  cursor: pointer;
}

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.error {
  color: #d0342c;
}

.success {
  color: #2c8a3d;
}
</style>
