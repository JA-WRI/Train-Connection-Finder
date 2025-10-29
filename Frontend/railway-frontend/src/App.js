import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./pages/home";
import ConnectionsPage from "./pages/connectionsPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/connections" element={<ConnectionsPage />} />
      </Routes>
    </Router>
  );
}

export default App;
