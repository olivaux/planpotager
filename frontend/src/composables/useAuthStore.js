import { ref, computed } from 'vue'
import { get, onUnauthorized } from '../services/httpClient.js'

const currentUser = ref(null)
const resolved = ref(false)

function clear() {
  currentUser.value = null
  resolved.value = false
}

onUnauthorized(clear)

async function fetchCurrentUser() {
  if (resolved.value) {
    return currentUser.value
  }
  try {
    currentUser.value = await get('/auth/me')
  } catch {
    currentUser.value = null
  }
  resolved.value = true
  return currentUser.value
}

const isAuthenticated = computed(() => currentUser.value !== null)

export function useAuthStore() {
  return { currentUser, isAuthenticated, fetchCurrentUser, clear }
}
