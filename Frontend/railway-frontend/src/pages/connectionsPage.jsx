import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import ConnectionCard from "../components/connectionCard";
import ConnectionSearchForm from "../components/ConnectionSearchForm";
import "../styles/connectionsCard.css";

const ConnectionsPage = () => {
  const location = useLocation();
  const { departure, arrival } = location.state || {};

  const [connections, setConnections] = useState([]);
  const [filteredConnections, setFilteredConnections] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (departure && arrival) {
      const fetchConnections = async () => {
        setLoading(true);
        setError(null);
        try {
          const response = await fetch(
            `http://localhost:4567/searchConnections?departureCity=${encodeURIComponent(departure)}&arrivalCity=${encodeURIComponent(arrival)}`
          );

          if (!response.ok) {
            throw new Error("Failed to fetch connections");
          }

          const data = await response.json();
          setConnections(data); // Store the full list of connections
          setFilteredConnections(data); // Initially set filtered connections to all connections
        } catch (err) {
          setError(err.message);
        } finally {
          setLoading(false);
        }
      };

      fetchConnections();
    }
  }, [departure, arrival]);

 const handleFilterChange = async (filters) => {
  setLoading(true);
  setError(null);
console.log("Connections to send:\n\n\n\n\n\n\n", connections);
  try {
    // Send both the connections and filters in the request body (as JSON)
    const response = await fetch(`http://localhost:4567/filterConnections`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        connections: connections, // Send the full list of connections
        filters: filters,         // Send the filter parameters
      }),
    });

    if (!response.ok) {
      throw new Error("Failed to apply filters");
    }

    const filteredData = await response.json();
    setFilteredConnections(filteredData); // Update filtered connections with the response from the backend
  } catch (err) {
    setError(err.message);
  } finally {
    setLoading(false);
  }
};


  return (
    <div className="connections-page">
      <h2 className="page-title">
        {departure && arrival
          ? `Connections from ${departure} to ${arrival}`
          : "Available Train Connections"}
      </h2>

      {/* Filter form */}
      <ConnectionSearchForm onFilterChange={handleFilterChange} />

      {loading && <p>Loading connections...</p>}
      {error && <p className="error">{error}</p>}

      <div className="connections-list">
        {filteredConnections.length > 0 ? (
          filteredConnections.map((c, i) => (
            <ConnectionCard key={i} connection={c} />
          ))
        ) : (
          !loading && <p>No connections found.</p>
        )}
      </div>
    </div>
  );
};

export default ConnectionsPage;
