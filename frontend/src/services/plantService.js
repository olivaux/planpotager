import { get, post, del } from './httpClient.js'

export const addPlant = ({ variety, supplier }) => post('/plant', { variety, supplier })

export const removePlant = (plantId) => del(`/plant/${plantId}`)

export const getAvailablePlants = () => get('/plant')
