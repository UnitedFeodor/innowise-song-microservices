import React, { useEffect, useState } from 'react'
import { getAuthCode, getLocalStorageToken, isLoggedIn } from '../../service/AuthService';
import Button from '@mui/material/Button';
import { getAllSongs } from '../../service/SongService';
import SongList from '../SongList/SongList';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { environment } from '../../config/environment';

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

  const handleLogout = async () => {
    try {
    //   await axios.get(environment.issuerUrl + '/logout' , 
    //     { headers: {
    //       Authorization: 'Bearer ' + getLocalStorageToken(),
    //     },
    //   }); 
    //TODO implement properly
    localStorage.clear()
    } catch (error) {
      console.error('Error:', error.message);
    }
  };

  return (
    <div>
      

      { !isLoggedIn() ? 
        <Button variant="contained" onClick={handleLogin}>Login</Button> 
        : <Button variant="outlined" onClick={handleLogout}>Logout</Button>
      
      }
      <Link to="/upload">Upload file</Link>

      <h1>Song List</h1>
      {songData.length > 0 ? (
        <SongList songMetadataList={songData} />
      ) : ( isLoggedIn() ?
        <p>Loading songs...</p> : <h3>You have to log in in order to view song metadata</h3>
      )}

    </div>
  )
}

export default Home