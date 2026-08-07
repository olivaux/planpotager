<script setup>
import { ref, onMounted } from 'vue'
import { getGardens, deleteGarden } from '../../services/gardenService.js'

const gardens = ref([])
const error = ref(null)

async function loadGardens() {
  try {
    gardens.value = await getGardens()
  } catch {
    error.value = 'Impossible de charger vos potagers.'
  }
}

onMounted(loadGardens)

async function confirmRemove(gardenId) {
  if (!window.confirm('Supprimer ce potager ?')) {
    return
  }
  try {
    await deleteGarden(gardenId)
    await loadGardens()
  } catch {
    error.value = 'Impossible de supprimer ce potager.'
  }
}
</script>

<template>
  <div class="page page-medium">
    <h1>Mes potagers</h1>

    <p v-if="error" class="error">{{ error }}</p>

    <p v-else-if="gardens.length === 0">Aucun potager pour l'instant.</p>

    <ul v-else class="list-reset">
      <li v-for="garden in gardens" :key="garden.id" class="list-card row">
        <RouterLink :to="{ name: 'garden-detail', params: { id: garden.id } }">
          {{ garden.name }}
        </RouterLink>
        <button type="button" class="btn" @click="confirmRemove(garden.id)">Supprimer</button>
      </li>
    </ul>

    <RouterLink class="btn btn-primary new-garden" :to="{ name: 'garden-new' }">+ Nouveau potager</RouterLink>
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

li a {
  color: var(--text-h);
  text-decoration: none;
  font-weight: 500;
}

.new-garden {
  margin-top: 16px;
}
</style>
