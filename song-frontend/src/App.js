import './App.css';
import Error404 from './components/Error404/Error404';
import Home from './components/Home/Home';
import PrivateRoute from './components/PrivateRoute/PrivateRoute'
import {Routes, Route, BrowserRouter } from "react-router-dom"
import Upload from './components/Upload/Upload';
import Authorized from './components/Authorzied/Authorized';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='*' element={
              <Error404 />
          }/>
          <Route path="/authorized" element={<Authorized/>}/>
          <Route path="/home" element={
              <Home/>
          }/>
          <Route path="/upload" element={
            <PrivateRoute>
              <Upload/>
            </PrivateRoute>
          }/>
        </Routes>
      </BrowserRouter>

    </div>
  );
}

export default App;
