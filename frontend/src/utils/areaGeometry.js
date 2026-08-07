export const CORNER_KEYS = ['leftUp', 'rightUp', 'rightDown', 'leftDown']

export const EDGES = [
  ['leftUp', 'rightUp'],
  ['rightUp', 'rightDown'],
  ['rightDown', 'leftDown'],
  ['leftDown', 'leftUp'],
]

export function areaToPoints(area) {
  return [
    area.leftUpX, area.leftUpY,
    area.rightUpX, area.rightUpY,
    area.rightDownX, area.rightDownY,
    area.leftDownX, area.leftDownY,
  ]
}

export function cornerPos(area, key) {
  return { x: area[`${key}X`], y: area[`${key}Y`] }
}

export function edgeMidpoint(area, keyA, keyB) {
  const a = cornerPos(area, keyA)
  const b = cornerPos(area, keyB)
  return { x: (a.x + b.x) / 2, y: (a.y + b.y) / 2 }
}

export function edgeLength(area, keyA, keyB) {
  const a = cornerPos(area, keyA)
  const b = cornerPos(area, keyB)
  return Math.hypot(b.x - a.x, b.y - a.y).toFixed(1)
}
