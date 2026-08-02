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
  <div class="my-plants">
    <h1>Mes plantes</h1>

    <p v-if="error" class="error">{{ error }}</p>

    <p v-else-if="plants.length === 0">Aucune plante enregistrée pour l'instant.</p>

    <ul v-else>
      <li v-for="plant in plants" :key="plant.id">
        <span>
          <strong>{{ plant.variety }}</strong>
          <span v-if="plant.supplier" class="supplier"> · {{ plant.supplier }}</span>
        </span>
        <button type="button" @click="confirmRemove(plant.id)">Supprimer</button>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.my-plants {
  padding: 32px 20px;
  max-width: 480px;
  margin: 0 auto;
  text-align: left;
}

ul {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

li {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  border-radius: 6px;
  border: 1px solid var(--border);
}

.supplier {
  color: var(--text);
}

button {
  padding: 6px 12px;
  border-radius: 6px;
  border: 1px solid var(--border);
  background: none;
  color: var(--text-h);
  cursor: pointer;
}

.error {
  color: #d0342c;
}
</style>
