import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/connectionsCard.css";
import ConnectionDetails from "./connectionDetails"

const ConnectionCard = ({ connection }) => {
  const {
    departureTime,
    arrivalTime,
    departureCity,
    arrivalCity,
    duration,
    stops,
    price,
    cabinType,
    detailsAvailable,
  } = connection;

  const [showDetails, setShowDetails] = useState(false);
  const navigate = useNavigate();

  const handleBook = () => {
    navigate("/booking", { state: { connection } });
  };

  return (
    <>
    <div className="connection-card">
      <div className="connection-main">
        <div className="connection-left">
          <div className="time">{departureTime}</div>
          <div className="city">{departureCity}</div>
        </div>

        <div className="connection-middle">
          <div className="route-line">
            <span className="dot"></span>
            <span className="line"></span>
            <span className="dot"></span>
          </div>
          <div className="stops">
            {stops === 0 ? "Direct" : `${stops} stop${stops > 1 ? "s" : ""}`} • {duration}
          </div>
        </div>

        <div className="connection-right">
          <div className="time">{arrivalTime}</div>
          <div className="city">{arrivalCity}</div>
        </div>
      </div>

      <div className="connection-footer">
        <div className="details">
          {detailsAvailable ? (
            <button
              className="link-button"
              onClick={() => setShowDetails(true)}
            >
              Details
            </button>
          ) : (
            <span className="unavailable">Not available</span>
          )}
        </div>

        <div className="price-section">
          <div className="price">
            <span className="price-value">${price}</span>
            <div className="cabin">{cabinType}</div>
          </div>
          <button className="book-button" onClick={handleBook}>
            Book Now
          </button>
        </div>
      </div>
    </div>
    {/* Modal */}
      {showDetails && (
        <ConnectionDetails
          connection={connection}
          onClose={() => setShowDetails(false)}
        />
      )}
    </>
  );
};

export default ConnectionCard;
