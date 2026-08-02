import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../composables/useAuthStore.js'
import AuthAction from '../views/Auth/AuthAction.vue'
import Profile from '../views/Profile/Profile.vue'


// Les routes des vues 
const routes = [
  { path: '/signup', name: 'signup', component: AuthAction, meta: { mode: 'signup' } },
  { path: '/login', name: 'login', component: AuthAction, meta: { mode: 'login' } },
  { path: '/profile', name: 'profile', component: Profile, meta: { requiresAuth: true } },
  ]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(async (to) => {
  const { isAuthenticated, fetchCurrentUser } = useAuthStore()

  if (!isAuthenticated.value) {
    await fetchCurrentUser()
  }

  if (to.path === '/') {
    return { path: isAuthenticated.value ? '/profile' : '/login' }
  }

  const isAuthRoute = to.path === '/login' || to.path === '/signup'
  if (isAuthRoute && isAuthenticated.value) {
    return { path: '/profile' }
  }

  if (to.meta.requiresAuth && !isAuthenticated.value) {
    return { path: '/login' }
  }

  return true
})

export default router
