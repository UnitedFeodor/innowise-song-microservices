import { useNavigate } from "react-router-dom";
import { getLocalStorageToken, isLoggedIn } from "../../service/AuthService";

  
function PrivateRoute({ children }) {
    const isAuth = isLoggedIn()
    console.log('PrivateRoute: isAuth is ', isAuth)
    let navigate = useNavigate();
    return (
      <>
        {
            isAuth ? children : navigate("/home") 
        }
      </>
    );
  }

  export default PrivateRoute