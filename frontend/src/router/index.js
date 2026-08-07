import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../composables/useAuthStore.js'
import AuthAction from '../views/Auth/AuthAction.vue'
import Profile from '../views/Profile/Profile.vue'
import CatalogView from '../views/Registry/CatalogView.vue'
import AddPlant from '../views/Plant/AddPlant.vue'
import MyPlants from '../views/Plant/MyPlants.vue'
import GardenList from '../views/Garden/GardenList.vue'
import NewGarden from '../views/Garden/NewGarden.vue'
import GardenStructure from '../views/Garden/GardenStructure.vue'
import GardenDetail from '../views/Garden/GardenDetail.vue'

// Les routes des vues
const routes = [
  { path: '/signup', name: 'signup', component: AuthAction, meta: { mode: 'signup' } },
  { path: '/login', name: 'login', component: AuthAction, meta: { mode: 'login' } },
  { path: '/profile', name: 'profile', component: Profile, meta: { requiresAuth: true } },
  { path: '/catalog', name: 'catalog', component: CatalogView, meta: { requiresAuth: true } },
  { path: '/plant/add', name: 'plant-add', component: AddPlant, meta: { requiresAuth: true } },
  { path: '/plant/list', name: 'plant-list', component: MyPlants, meta: { requiresAuth: true } },
  { path: '/garden', name: 'garden-list', component: GardenList, meta: { requiresAuth: true } },
  { path: '/garden/new', name: 'garden-new', component: NewGarden, meta: { requiresAuth: true } },
  { path: '/garden/:id/structure', name: 'garden-structure', component: GardenStructure, meta: { requiresAuth: true } },
  { path: '/garden/:id/plants', name: 'garden-detail', component: GardenDetail, meta: { requiresAuth: true } },
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
