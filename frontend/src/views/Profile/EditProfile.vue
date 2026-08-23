<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getProfile, updateProfile } from '../../services/profileService.js'

const router = useRouter()

const unit = ref('cm')
const language = ref('fr')

const loading = ref(true)
const submitting = ref(false)
const error = ref(null)

const canSubmit = computed(() => !loading.value && !submitting.value)

onMounted(async () => {
  try {
    const profile = await getProfile()
    unit.value = profile.unit
    language.value = profile.language
  } catch {
    error.value = 'Impossible de charger le profil.'
  } finally {
    loading.value = false
  }
})

async function submit() {
  if (!canSubmit.value) {
    return
  }

  submitting.value = true
  error.value = null

  try {
    await updateProfile({ unit: unit.value, language: language.value })
    router.push({ name: 'profile' })
  } catch {
    error.value = 'Impossible de mettre à jour le profil.'
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="page page-medium">
    <h1>Modifier le profil</h1>

    <p v-if="error" class="error">{{ error }}</p>

    <form v-if="!loading" @submit.prevent="submit">
      <label class="field">
        Unité
        <select v-model="unit">
          <option value="cm">Centimètres</option>
          <option value="in">Pouces</option>
        </select>
      </label>

      <label class="field">
        Langue
        <select v-model="language">
          <option value="fr">Français</option>
          <option value="en">English</option>
        </select>
      </label>

      <button type="submit" class="btn btn-primary" :disabled="!canSubmit">Enregistrer</button>
    </form>

    <p v-else>Chargement…</p>
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
