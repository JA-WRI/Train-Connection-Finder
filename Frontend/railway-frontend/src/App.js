import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./pages/home";
import ConnectionsPage from "./pages/connectionsPage";
import BookingPage from "./pages/bookingPage";
import TripHistoryPage from "./pages/tripHistoryPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/connections" element={<ConnectionsPage />} />
        <Route path="/booking" element={<BookingPage />} />
        <Route path="/trips" element={<TripHistoryPage />} />
      </Routes>
    </Router>
  );
}

export default App;
