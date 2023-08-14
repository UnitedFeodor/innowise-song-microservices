import React, { useEffect, useState } from 'react'
import { getAuthCode } from '../../service/AuthService';
import Button from '@mui/material/Button';
import { getAllSongs } from '../../service/SongService';
import SongList from '../SongList/SongList';
import FileUpload from '../FileUpload/FileUpload';
import { Link } from 'react-router-dom';

function Home() {

  const [songData, setSongData] = useState([]);

  useEffect(() => {
    fetchSongMetadata();
  }, []);

  const fetchSongMetadata = async () => {
    try {
      const response = await getAllSongs(); // Call the API function
      setSongData(response); // Set fetched data to state
    } catch (error) {
      console.error('Error fetching songs:', error);
    }
  };

  const handleLogin = async () => {
    try {
      const authCode = await getAuthCode();
      if (authCode) {
        console.log('Got auth code');
      } else {
        console.error('Error obtaining auth code');
      }
    } catch (error) {
      console.error('Error:', error.message);
    }
  };

  return (
    <div>
      

      <Button variant="contained" onClick={handleLogin}>Login</Button>
      <Link to="/upload">Upload file</Link>

      <h1>Song List</h1>
      {songData.length > 0 ? (
        <SongList songMetadataList={songData} />
      ) : (
        <p>Loading...</p>
      )}

    </div>
  )
}

export default Home