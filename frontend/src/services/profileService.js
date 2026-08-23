import { get, put } from './httpClient.js'

export const getProfile = () => get('/profile')

export const updateProfile = ({ unit, language }) => put('/profile', { unit, language })
