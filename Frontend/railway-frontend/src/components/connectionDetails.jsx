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
          connection.routes.map((route, idx) => {
            const isFirstRoute = idx === 0;
            const isLastRoute = idx === connection.routes.length - 1;
            const departureDays = route.daysOfOperation && route.daysOfOperation.length > 0 
              ? route.daysOfOperation.join(", ") 
              : "N/A";
            const arrivalDays = route.daysOfOperation && route.daysOfOperation.length > 0 
              ? route.daysOfOperation.join(", ") 
              : "N/A";

            return (
              <div key={idx} className="route-section">
                <div className="route-header">
                  <h4>
                    {capitalizeFirstLetter(route.departureCity)} → {capitalizeFirstLetter(route.arrivalCity)}
                  </h4>
                  <p className="route-time">
                    {route.departureTime} - {route.arrivalTime}
                  </p>
                  {isFirstRoute && (
                    <p className="route-days">
                      <strong>Departure Days:</strong> {departureDays}
                    </p>
                  )}
                  {isLastRoute && (
                    <p className="route-days">
                      <strong>Arrival Days:</strong> {arrivalDays}
                    </p>
                  )}
                </div>
                <hr />
              </div>
            );
          })
        ) : (
          <p className="direct">This is a direct connection.</p>
        )}
      </div>
    </div>
  );
};

export default ConnectionDetailsModal;
