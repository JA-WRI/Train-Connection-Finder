import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import ConnectionCard from "../components/connectionCard";
import "../styles/connectionsCard.css";

const ConnectionsPage = () => {
  const location = useLocation();
  const { departure, arrival } = location.state || {};

  const [connections, setConnections] = useState([]);
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
        } catch (err) {
          setError(err.message);
        } finally {
          setLoading(false);
        }
      };

      fetchConnections();
    }
  }, [departure, arrival]);


  return (
    <div className="connections-page">
      <h2 className="page-title">
        {departure && arrival
          ? `Connections from ${departure} to ${arrival}`
          : "Available Train Connections"}
      </h2>

      {loading && <p>Loading connections...</p>}
      {error && <p className="error">{error}</p>}

      <div className="connections-list">
        {connections.length > 0 ? (
          connections.map((c, i) => <ConnectionCard key={i} connection={c} />)
        ) : (
          !loading && <p>No connections found.</p>
        )}
      </div>
    </div>
  );
};

export default ConnectionsPage;
