import axios from 'axios';
import { environment } from '../config/environment';
import { getLocalStorageToken } from './AuthService';

const API_BASE_URL = environment.baseUrl + '/file-api' // TODO Replace all with actual access token


const uploadFile = async (username, file) => {
  const formData = new FormData();
  formData.append('file', file);

  try {
    const response = await axios.post(`${API_BASE_URL}/files/${username}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: 'Bearer ' + getLocalStorageToken(),
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error uploading file:', error);
    throw error;
  }
};

const downloadFileByUsernameAndHashedFilename = async (username, hashedFilename) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/files/${username}/${hashedFilename}`, {
      responseType: 'arraybuffer',
      headers: { Authorization: 'Bearer ' + getLocalStorageToken() },
    });
    return response.data;
  } catch (error) {
    console.error('Error downloading file:', error);
    throw error;
  }
};

const downloadFileBySongId = async (songId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/files/${songId}`, {
      responseType: 'arraybuffer',
      headers: { Authorization: 'Bearer ' + getLocalStorageToken() }, 
    });
    return response.data;
  } catch (error) {
    console.error('Error downloading file:', error);
    throw error;
  }
};

const deleteFileById = async (songId) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/files/${songId}`, {
      headers: { Authorization: 'Bearer ' + getLocalStorageToken() }, 
    });
    return response.data;
  } catch (error) {
    console.error('Error deleting file:', error);
    throw error;
  }
};

export { uploadFile, downloadFileByUsernameAndHashedFilename, downloadFileBySongId, deleteFileById };
