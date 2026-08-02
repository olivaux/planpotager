<script setup>
import { provide } from 'vue'
import { useAuthStore } from './composables/useAuthStore.js'

provide('authStore', useAuthStore())
</script>

<template>
  <router-view />
</template>
