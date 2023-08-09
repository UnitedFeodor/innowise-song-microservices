import axios from 'axios';
import { environment } from '../config/environment';

const API_BASE_URL = environment.baseUrl + '/song-api'

const getAllSongs = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/songs`, {
      headers: { Authorization: 'Bearer your-access-token' }, // TODO Replace with actual access token
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching songs:', error);
    throw error;
  }
};

const getSongById = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/songs/${id}`, {
      headers: { Authorization: 'Bearer your-access-token' }, // TODO Replace with actual access token
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching song by ID:', error);
    throw error;
  }
};

const deleteSongById = async (id) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/songs/${id}`, {
      headers: { Authorization: 'Bearer your-access-token' }, // TODO Replace with actual access token
    });
    return response.data;
  } catch (error) {
    console.error('Error deleting song:', error);
    throw error;
  }
};

export { getAllSongs, getSongById, deleteSongById };
