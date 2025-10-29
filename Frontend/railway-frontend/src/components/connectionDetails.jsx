import React from "react";
import "../styles/connectionDetails.css";

const ConnectionDetailsModal = ({ connection, onClose }) => {
  if (!connection) return null;

  return (
    <div className="modal-overlay">
      <div className="modal">
        <div className="modal-header">
          <h2>Connection Details</h2>
          <button onClick={onClose} className="close-button">×</button>
        </div>

        <p className="route-summary">
          {connection.departureCity} → {connection.arrivalCity}
        </p>
        <p className="total-duration">Total duration: {connection.duration}</p>
        <hr />

        {connection.routes && connection.routes.length > 0 ? (
          connection.routes.map((route, idx) => (
            <div key={idx} className="route-section">
              <div className="route-header">
                <h4>
                  {route.departureCity} → {route.arrivalCity}
                </h4>
                <p className="route-time">
                  {route.departureTime} - {route.arrivalTime}
                </p>
              </div>
              <p className="route-duration">Duration: {route.duration}</p>
              <hr />
            </div>
          ))
        ) : (
          <p className="direct">This is a direct connection.</p>
        )}
      </div>
    </div>
  );
};

export default ConnectionDetailsModal;
