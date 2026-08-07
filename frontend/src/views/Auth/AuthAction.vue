<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const isSignUp = computed(() => route.meta.mode === 'signup')
const title = computed(() => (isSignUp.value ? 'Créer un compte' : 'Se connecter'))
const ctaLabel = computed(() =>
  isSignUp.value ? 'Créer un compte avec Google' : 'Se connecter avec Google',
)


</script>

<template>
  <div class="auth-action">
    <h1>{{ title }}</h1>
        <a class="btn btn-primary cta" href="/oauth2/authorization/google">{{ ctaLabel }}</a>
  </div>
</template>

<style scoped>
.auth-action {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 48px 20px;
}

.cta {
  transition: box-shadow 0.3s;
}

.cta:hover {
  box-shadow: var(--shadow);
}
</style>
