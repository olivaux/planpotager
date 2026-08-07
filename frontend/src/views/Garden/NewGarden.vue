<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { createGarden } from '../../services/gardenService.js'

const router = useRouter()

const name = ref('')
const longitude = ref('')
const latitude = ref('')

const submitting = ref(false)
const error = ref(null)

const canSubmit = computed(() => name.value.trim() !== '' && !submitting.value)

async function submit() {
  if (!canSubmit.value) {
    return
  }

  submitting.value = true
  error.value = null

  try {
    const garden = await createGarden({
      name: name.value,
      longitude: longitude.value === '' ? null : Number(longitude.value),
      latitude: latitude.value === '' ? null : Number(latitude.value),
    })
    router.push({ name: 'garden-structure', params: { id: garden.id } })
  } catch {
    error.value = 'Impossible de créer ce potager.'
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="page page-narrow">
    <h1>Nouveau potager</h1>

    <p v-if="error" class="error">{{ error }}</p>

    <form @submit.prevent="submit">
      <label class="field">
        Nom
        <input v-model="name" type="text" required />
      </label>

      <label class="field">
        Longitude
        <input v-model="longitude" type="number" step="any" />
      </label>

      <label class="field">
        Latitude
        <input v-model="latitude" type="number" step="any" />
      </label>

      <button type="submit" class="btn btn-primary" :disabled="!canSubmit">Créer</button>
    </form>
  </div>
</template>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

button {
  align-self: flex-start;
}
</style>
