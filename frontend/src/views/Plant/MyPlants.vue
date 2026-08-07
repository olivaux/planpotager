<script setup>
import { ref, onMounted } from 'vue'
import { getAvailablePlants, removePlant } from '../../services/plantService.js'

const plants = ref([])
const error = ref(null)

async function loadPlants() {
  try {
    plants.value = await getAvailablePlants()
  } catch {
    error.value = 'Impossible de charger vos plantes.'
  }
}

onMounted(loadPlants)

async function confirmRemove(plantId) {
  if (!window.confirm('Supprimer cette plante de votre compte ?')) {
    return
  }
  try {
    await removePlant(plantId)
    await loadPlants()
  } catch {
    error.value = 'Impossible de supprimer cette plante.'
  }
}
</script>

<template>
  <div class="page page-medium">
    <h1>Mes plantes</h1>

    <p v-if="error" class="error">{{ error }}</p>

    <p v-else-if="plants.length === 0">Aucune plante enregistrée pour l'instant.</p>

    <ul v-else class="list-reset">
      <li v-for="plant in plants" :key="plant.id" class="list-card row">
        <span>
          <strong>{{ plant.variety }}</strong>
          <span v-if="plant.supplier" class="supplier"> · {{ plant.supplier }}</span>
        </span>
        <button type="button" class="btn" @click="confirmRemove(plant.id)">Supprimer</button>
      </li>
    </ul>
  </div>
</template>

<style scoped>
ul {
  gap: 8px;
}

.row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.supplier {
  color: var(--text);
}
</style>
