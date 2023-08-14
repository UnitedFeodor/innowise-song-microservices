import React, { useEffect } from 'react'
import { getToken } from '../../service/AuthService';
import { useNavigate } from 'react-router-dom';

export default function Authorized() {
  const navigate = useNavigate();

  // This function will be called when the user is redirected back to app after authorization
  const handleAuthorizationCallback = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const authCode = urlParams.get('code');
    console.log(`authCode from url ${authCode}`)

    if (authCode) {
      try {
        const tokenResponse = await getToken(authCode);
        console.log(`tokenResponse ${tokenResponse}`)
        if (tokenResponse.access_token) {
          localStorage.setItem('access_token', tokenResponse.access_token);
          navigate('/home'); // Redirect to dashboard or other protected route
        } else {
          console.error('Error obtaining access token');
        }
      } catch (error) {
        console.error('Error:', error.message);
      }
    } else {
      console.error('No auth code in the URL parameters');
    }
  };

  // Call the callback function when the component mounts
  useEffect(() => {
    handleAuthorizationCallback();
  }, []);
  return (
    <div>Authorized</div>
  )
}
