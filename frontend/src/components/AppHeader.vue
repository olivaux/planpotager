<script setup>
import logo from '../assets/logo.png'
</script>

<template>
  <header class="app-header">
    <RouterLink :to="{ name: 'garden-list' }" class="app-header-logo">
      <img :src="logo" alt="PlanPotager" />
    </RouterLink>

    <RouterLink :to="{ name: 'profile' }" class="btn">Mon compte</RouterLink>
  </header>
</template>

<style scoped>
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 20px;
  border-bottom: 1px solid var(--border);
}

.app-header-logo {
  display: inline-flex;
  align-items: center;
}

.app-header-logo img {
  height: 40px;
  display: block;
}
</style>
