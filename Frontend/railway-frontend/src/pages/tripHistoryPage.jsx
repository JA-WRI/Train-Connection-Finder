import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/tripHistoryPage.css";

const TripHistoryPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    lastName: "",
    userId: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [currentTrips, setCurrentTrips] = useState([]);
  const [pastTrips, setPastTrips] = useState([]);
  const [tripsLoaded, setTripsLoaded] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    if (!formData.lastName.trim() || !formData.userId.trim()) {
      setError("Please enter both last name and ID");
      setLoading(false);
      return;
    }

    try {
      const response = await fetch("http://localhost:4567/getUserTrips", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          lastName: formData.lastName.trim(),
          userId: formData.userId.trim(),
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Failed to retrieve trips");
      }

      const result = await response.json();
      setCurrentTrips(result.current || []);
      setPastTrips(result.past || []);
      setTripsLoaded(true);
    } catch (err) {
      setError(err.message);
      setTripsLoaded(false);
    } finally {
      setLoading(false);
    }
  };

  const capitalizeFirstLetter = (str) => {
    if (!str) return "";
    return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
  };

  const TripCard = ({ trip, isPast }) => (
    <div className={`trip-card ${isPast ? "past-trip" : "current-trip"}`}>
      <div className="trip-header">
        <h4>
          {capitalizeFirstLetter(trip.departureCity)} → {capitalizeFirstLetter(trip.arrivalCity)}
        </h4>
        {isPast && <span className="past-badge">Past Trip</span>}
        {!isPast && <span className="current-badge">Current Trip</span>}
      </div>
      <div className="trip-details">
        <div className="trip-row">
          <span className="trip-label">Passenger:</span>
          <span>{trip.firstName} {trip.lastName}</span>
        </div>
        <div className="trip-row">
          <span className="trip-label">Departure:</span>
          <span>{trip.departureTime} from {capitalizeFirstLetter(trip.departureCity)}</span>
        </div>
        <div className="trip-row">
          <span className="trip-label">Arrival:</span>
          <span>{trip.arrivalTime} at {capitalizeFirstLetter(trip.arrivalCity)}</span>
        </div>
        <div className="trip-row">
          <span className="trip-label">Duration:</span>
          <span>{trip.duration}</span>
        </div>
        <div className="trip-row">
          <span className="trip-label">Price:</span>
          <span className="trip-price">${trip.price.toFixed(2)}</span>
        </div>
        <div className="trip-row">
          <span className="trip-label">Routes:</span>
          <span>{trip.numOfRoutes} {trip.numOfRoutes === 1 ? "route" : "routes"}</span>
        </div>
        <div className="trip-row">
          <span className="trip-label">Reservation ID:</span>
          <span>{trip.reservationId}</span>
        </div>
      </div>
    </div>
  );

  return (
    <div className="trip-history-page">
      <div className="trip-history-container">
        <h2>My Trips</h2>
        <p className="page-description">
          Enter your last name and ID to view your current and past trips
        </p>

        <form onSubmit={handleSubmit} className="trip-search-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="lastName">Last Name *</label>
              <input
                type="text"
                id="lastName"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                placeholder="Enter your last name"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="userId">ID *</label>
              <input
                type="text"
                id="userId"
                name="userId"
                value={formData.userId}
                onChange={handleChange}
                placeholder="Enter your ID"
                required
              />
            </div>
          </div>

          {error && <div className="error-message">{error}</div>}

          <div className="button-group">
            <button type="submit" className="submit-button" disabled={loading}>
              {loading ? "Loading..." : "View My Trips"}
            </button>
            <button
              type="button"
              onClick={() => navigate("/")}
              className="home-button"
            >
              Back to Home
            </button>
          </div>
        </form>

        {tripsLoaded && (
          <div className="trips-results">
            <div className="current-trips-section">
              <h3>Current Trips</h3>
              {currentTrips.length > 0 ? (
                <div className="trips-grid">
                  {currentTrips.map((trip, index) => (
                    <TripCard key={index} trip={trip} isPast={false} />
                  ))}
                </div>
              ) : (
                <p className="no-trips">No current trips found.</p>
              )}
            </div>

            <div className="past-trips-section">
              <h3>Trip History</h3>
              {pastTrips.length > 0 ? (
                <div className="trips-grid">
                  {pastTrips.map((trip, index) => (
                    <TripCard key={index} trip={trip} isPast={true} />
                  ))}
                </div>
              ) : (
                <p className="no-trips">No past trips found.</p>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default TripHistoryPage;

