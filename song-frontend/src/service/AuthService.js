import axios from 'axios';
import { environment } from "../config/environment";

const responseType = 'code';
const scope = 'openid profile';
const clientId = environment.clientId;
const clientSecret = environment.clientSecret;
const redirectUri = environment.redirectUri;
const grantType = 'authorization_code';

const AUTH_CODE_ISSURER_URL = environment.issuerUrl + `/oauth2/authorize?response_type=${responseType}&client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}`
const TOKEN_ISSUER_URL = environment.issuerUrl + '/oauth2/token'

const getAuthCode = async () => {
    try {
    	console.log(`getAuthCode from ${AUTH_CODE_ISSURER_URL}`)
      window.location.href = AUTH_CODE_ISSURER_URL;
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

    const basicAuthCredentials = btoa(`${clientId}:${clientSecret}`);
    const headers = {
      Authorization: `Basic ${basicAuthCredentials}`,
      'Content-Type': 'application/x-www-form-urlencoded',
    };

    try {
      const response = await axios.post(TOKEN_ISSUER_URL, data, { headers });
      console.log('getToken response:', response.data);
      return response.data
    } catch (error) {
      console.error('Error:', error.response.data);
    }
};

export { getAuthCode, getToken };