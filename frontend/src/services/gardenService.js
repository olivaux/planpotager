import { get, post, put, del } from './httpClient.js'

export const getGardens = () => get('/garden')

export const createGarden = ({ name, longitude, latitude }) =>
  post('/garden', { name, longitude, latitude })

export const getGarden = (id) => get(`/garden/${id}`)

export const getGardenPlants = (id) => get(`/garden/${id}/plants`)

export const getGardenAreas = (id) => get(`/garden/${id}/areas`)

export const updateGarden = (id, { name, longitude, latitude }) =>
  put(`/garden/${id}`, { name, longitude, latitude })

export const deleteGarden = (id) => del(`/garden/${id}`)

export const addPlantToGarden = (gardenId, { plantId, x, y }) =>
  post(`/garden/${gardenId}/plant`, { plantId, x, y })

export const updatePlantPosition = (gardenId, plantId, { x, y }) =>
  put(`/garden/${gardenId}/plant/${plantId}/position`, { x, y })

export const getAvailableStates = (gardenId, plantId) =>
  get(`/garden/${gardenId}/plant/${plantId}/states`)

export const setPlantState = (gardenId, plantId, state) =>
  put(`/garden/${gardenId}/plant/${plantId}/state`, { state })

export const removePlantFromGarden = (gardenId, plantId) =>
  del(`/garden/${gardenId}/plant/${plantId}`)

export const createArea = (gardenId, area) => post(`/garden/${gardenId}/area`, area)

export const updateArea = (gardenId, areaId, area) =>
  put(`/garden/${gardenId}/area/${areaId}`, area)

export const deleteArea = (gardenId, areaId) => del(`/garden/${gardenId}/area/${areaId}`)
