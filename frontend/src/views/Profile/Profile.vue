<script setup>
import { ref, onMounted } from 'vue'
import { getProfile } from '../../services/profileService.js'

const profile = ref(null)
const error = ref(null)

onMounted(async () => {
  try {
    profile.value = await getProfile()
  } catch {
    error.value = "Impossible de charger le profil."
  }
})
</script>

<template>
  <div class="profile">
    <h1>Mon profil</h1>

    <p v-if="error" class="error">{{ error }}</p>

    <dl v-else-if="profile">
      <dt>Email</dt>
      <dd>{{ profile.email }}</dd>

      <dt>Unité</dt>
      <dd>{{ profile.unit }}</dd>

      <dt>Langue</dt>
      <dd>{{ profile.language }}</dd>

      <dt>Fournisseur de connexion</dt>
      <dd>{{ profile.provider }}</dd>
    </dl>

    <p v-else>Chargement…</p>
  </div>
</template>

<style scoped>
.profile {
  padding: 32px 20px;
  max-width: 480px;
  margin: 0 auto;
  text-align: left;
}

dl {
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 8px 16px;
}

dt {
  color: var(--text);
  font-weight: 500;
}

dd {
  margin: 0;
  color: var(--text-h);
}

.error {
  color: #d0342c;
}
</style>
