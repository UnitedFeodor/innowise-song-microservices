import { useNavigate } from "react-router-dom";
import { getLocalStorageToken } from "../../service/AuthService";

  
function PrivateRoute({ children }) {
    const isLoggedIn = getLocalStorageToken() != null
    console.log('isLoggedIn is ', isLoggedIn)
    let navigate = useNavigate();
    return (
      <>
        {
            isLoggedIn ? children : navigate("/home") 
        }
      </>
    );
  }

  export default PrivateRoute