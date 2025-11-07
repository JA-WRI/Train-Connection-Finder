import React from "react";
import "../styles/connectionDetails.css";

const ConnectionDetailsModal = ({ connection, onClose }) => {
  if (!connection) return null;

  const capitalizeFirstLetter = (str) => {
    if (!str) return '';
    return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
  };

  return (
    <div className="modal-overlay">
      <div className="modal">
        <div className="modal-header">
          <h2>Connection Details</h2>
          <button onClick={onClose} className="close-button">×</button>
        </div>

        {connection.routes && connection.routes.length > 0 ? (
          connection.routes.map((route, idx) => (
            <div key={idx} className="route-section">
              <div className="route-header">
                <h4>
                  {capitalizeFirstLetter(route.departureCity)} → {capitalizeFirstLetter(route.arrivalCity)}
                </h4>
                <p className="route-time">
                  {route.departureTime} - {route.arrivalTime}
                </p>
              </div>
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
