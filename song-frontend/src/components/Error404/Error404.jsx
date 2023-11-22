import React from 'react'
import { Link } from 'react-router-dom'

function Error404() {
  return (
    <div data-testid="error404-1" >
      <h1>Unknown route, sorry!</h1>
      <Link to="/home">Return home</Link>
    </div>
  )
}

export default Error404