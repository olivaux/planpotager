import { ref } from 'vue'

const loadedImages = new Map()

export function useImage(url) {
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
