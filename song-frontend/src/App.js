import './App.css';
import Error404 from './components/Error404/Error404';
import Home from './components/Home/Home';
import Login from './components/Login/Login';
import { Routes, Route, BrowserRouter } from "react-router-dom"
import Upload from './components/Upload/Upload';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='*' element={<Error404 />}/>
          <Route path="/login" element={<Login/>}/>
          <Route path="/home" element={<Home/>}/>
          <Route path="/upload" element={<Upload/>}/>
        </Routes>
      </BrowserRouter>

    </div>
  );
}

export default App;
