import { ref } from 'vue'
import defaultImage from '../assets/default.png'

const speciesImageUrls = import.meta.glob('../assets/*.png', { eager: true, import: 'default' })

export function resolveSpeciesImageUrl(species) {
  if (!species) {
    return defaultImage
  }
  return speciesImageUrls[`../assets/${species}.png`] ?? defaultImage
}

const loadedImages = new Map()

export function usePlantImage(species) {
  const url = resolveSpeciesImageUrl(species)

  if (!loadedImages.has(url)) {
    const image = ref(null)
    const el = new window.Image()
    el.onload = () => {
      image.value = el
    }
    el.src = url
    loadedImages.set(url, image)
  }

  return loadedImages.get(url)
}
