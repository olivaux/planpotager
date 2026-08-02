import { get } from './httpClient.js'

export const getAllSpecies = () => get('/registry/species')

export const getVarietiesBySpecies = (speciesName) =>
  get(`/registry/species/${encodeURIComponent(speciesName)}/varieties`)
