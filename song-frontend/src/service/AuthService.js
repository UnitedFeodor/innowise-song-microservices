import axios from 'axios';
import { environment } from "../config/environment";

const responseType = 'code';
const scope = 'openid profile';
const clientId = environment.clientId;
const redirectUri = environment.redirectUri;
const grantType = 'authorization_code';

const AUTH_CODE_ISSURER_URL = environment.issuerUrl + `/oauth2/authorize?response_type=${responseType}&client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}`
const TOKEN_ISSUER_URL = environment.issuerUrl + '/oauth2/token'

//http://localhost:9000/oauth2/authorize?response_type=code&client_id=song-enricher-client&redirect_uri=http://localhost:9000/login&scope=openid%20profile
const getAuthCode = async () => {
    try {
      const response = await axios.get(AUTH_CODE_ISSURER_URL);
      console.log('getAuthCode response:', response.data);
      return response.data
    } catch (error) {
      console.error('Error:', error.message);
    }
};

const getToken = async (code) => {
    const data = {
      grant_type: grantType,
      scope: scope,
      code: code,
      redirect_uri: redirectUri,
      client_id: clientId
    };
  
    try {
      const response = await axios.post(TOKEN_ISSUER_URL, data);
      console.log('getToken response:', response.data);
      return response.data
    } catch (error) {
      console.error('Error:', error.message);
    }
  }