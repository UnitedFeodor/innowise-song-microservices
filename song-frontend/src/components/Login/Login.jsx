import React from 'react';
import { useNavigate } from 'react-router-dom';
import { getAuthCode,getToken } from '../../service/AuthService';

const Login = () => {
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const authCode = await getAuthCode();
      if (authCode) {
        console.log('Got auth code');
        await handleAuthorizationCallback()
      } else {
        console.error('Error obtaining auth code');
      }
    } catch (error) {
      console.error('Error:', error.message);
    }
  };

  // This function will be called when the user is redirected back to your app after authorization
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
          navigate('/home'); // Redirect to your dashboard or other protected route
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
  React.useEffect(() => {
    handleAuthorizationCallback();
  }, []);

  return (
    <div>
      <button onClick={handleLogin}>Login</button>
    </div>
  );
};

export default Login;
