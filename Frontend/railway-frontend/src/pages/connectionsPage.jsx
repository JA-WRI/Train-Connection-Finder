import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import ConnectionCard from "../components/connectionCard";
import FlightSearchForm from "../components/FlightSearchForm";
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
            `http://localhost:4567/searchConnections?departureCity=${encodeURIComponent(
              departure
            )}&arrivalCity=${encodeURIComponent(arrival)}`
          );

          if (!response.ok) {
            throw new Error("Failed to fetch connections");
          }

          const data = await response.json();
          setConnections(data);
          setFilteredConnections(data);
        } catch (err) {
          setError(err.message);
        } finally {
          setLoading(false);
        }
      };

      fetchConnections();
    }
  }, [departure, arrival]);

  
  const handleFilterChange = (filters) => {
    const filtered = connections.filter((conn) => {
      const priceOK =
        !filters.maxPrice || conn.price <= Number(filters.maxPrice);
      const durationOK =
        !filters.maxDuration || conn.duration <= Number(filters.maxDuration);
      const classOK =
        !filters.ticketClass || conn.class === filters.ticketClass;
      return priceOK && durationOK && classOK;
    });
    setFilteredConnections(filtered);
  };

  return (
    <div className="connections-page">
      <h2 className="page-title">
        {departure && arrival
          ? `Connections from ${departure} to ${arrival}`
          : "Available Train Connections"}
      </h2>

      
      <FlightSearchForm onFilterChange={handleFilterChange} />

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
