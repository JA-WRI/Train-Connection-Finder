import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "../styles/bookingPage.css";

const BookingPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { connection } = location.state || {};

  const [formData, setFormData] = useState({
    name: "",
    age: "",
    id: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [bookingSuccess, setBookingSuccess] = useState(false);
  const [reservationId, setReservationId] = useState(null);
  const [travelers, setTravelers] = useState([]);
  const [showAddTraveler, setShowAddTraveler] = useState(false);
  const [travelerFormData, setTravelerFormData] = useState({
    name: "",
    age: "",
    id: "",
  });
  const [addingTraveler, setAddingTraveler] = useState(false);

  if (!connection) {
    return (
      <div className="booking-page">
        <div className="error-message">
          <p>No connection selected. Please go back and select a connection.</p>
          <button onClick={() => navigate("/connections")}>Go Back</button>
        </div>
      </div>
    );
  }

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleTravelerChange = (e) => {
    const { name, value } = e.target;
    setTravelerFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleAddTraveler = async (e) => {
    e.preventDefault();
    setAddingTraveler(true);
    setError(null);

    // Validation
    if (!travelerFormData.name.trim() || !travelerFormData.age || !travelerFormData.id.trim()) {
      setError("Please fill in all fields");
      setAddingTraveler(false);
      return;
    }

    const ageNum = parseInt(travelerFormData.age);
    if (isNaN(ageNum) || ageNum < 0) {
      setError("Please enter a valid age");
      setAddingTraveler(false);
      return;
    }

    try {
      const response = await fetch("http://localhost:4567/addTraveler", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          bookerReservationId: parseInt(reservationId),
          connection: connection,
          traveler: {
            name: travelerFormData.name.trim(),
            age: ageNum,
            id: travelerFormData.id.trim(),
          },
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Failed to add traveler");
      }

      const result = await response.json();
      
      // Add traveler to the list
      setTravelers([...travelers, {
        name: travelerFormData.name.trim(),
        age: ageNum,
        id: travelerFormData.id.trim(),
        reservationId: result.reservationId,
      }]);

      // Reset form and hide it
      setTravelerFormData({ name: "", age: "", id: "" });
      setShowAddTraveler(false);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setAddingTraveler(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    // Validation
    if (!formData.name.trim() || !formData.age || !formData.id.trim()) {
      setError("Please fill in all fields");
      setLoading(false);
      return;
    }

    const ageNum = parseInt(formData.age);
    if (isNaN(ageNum) || ageNum < 0) {
      setError("Please enter a valid age");
      setLoading(false);
      return;
    }

    try {
      const response = await fetch("http://localhost:4567/bookConnection", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          connection: connection,
          user: {
            name: formData.name.trim(),
            age: ageNum,
            id: formData.id.trim(),
          },
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Failed to book connection");
      }

      const result = await response.json();
      setReservationId(result.reservationId);
      setBookingSuccess(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (bookingSuccess) {
    return (
      <div className="booking-page">
        <div className="success-container">
          <div className="success-icon">✓</div>
          <h2>Booking Confirmed!</h2>
          <p className="success-message">
            Your trip has been successfully booked.
          </p>
          {reservationId && (
            <p className="reservation-id">
              Reservation ID: <strong>{reservationId}</strong>
            </p>
          )}
          <div className="booking-details">
            <h3>Booking Details</h3>
            <div className="detail-row">
              <span className="label">Name:</span>
              <span>{formData.name}</span>
            </div>
            <div className="detail-row">
              <span className="label">Age:</span>
              <span>{formData.age}</span>
            </div>
            <div className="detail-row">
              <span className="label">ID:</span>
              <span>{formData.id}</span>
            </div>
            <div className="detail-row">
              <span className="label">Route:</span>
              <span>
                {connection.departureCity} → {connection.arrivalCity}
              </span>
            </div>
            <div className="detail-row">
              <span className="label">Departure:</span>
              <span>{connection.departureTime}</span>
            </div>
            <div className="detail-row">
              <span className="label">Arrival:</span>
              <span>{connection.arrivalTime}</span>
            </div>
            <div className="detail-row">
              <span className="label">Price:</span>
              <span>${connection.price}</span>
            </div>
            <div className="detail-row">
              <span className="label">Class:</span>
              <span>{connection.cabinType}</span>
            </div>
          </div>
          {travelers.length > 0 && (
            <div className="travelers-list">
              <h3>Added Travelers</h3>
              {travelers.map((traveler, index) => (
                <div key={index} className="traveler-item">
                  <div className="detail-row">
                    <span className="label">Traveler {index + 1}:</span>
                    <span>{traveler.name} (Age: {traveler.age}, ID: {traveler.id})</span>
                  </div>
                </div>
              ))}
            </div>
          )}

          {!showAddTraveler ? (
            <div className="button-group">
              <button
                onClick={() => setShowAddTraveler(true)}
                className="add-traveler-button"
              >
                Add Traveler
              </button>
              <button onClick={() => navigate("/")} className="home-button">
                Back to Home
              </button>
            </div>
          ) : (
            <form onSubmit={handleAddTraveler} className="traveler-form">
              <h3>Add Traveler Information</h3>

              <div className="form-group">
                <label htmlFor="traveler-name">Full Name *</label>
                <input
                  type="text"
                  id="traveler-name"
                  name="name"
                  value={travelerFormData.name}
                  onChange={handleTravelerChange}
                  placeholder="Enter traveler's full name"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="traveler-age">Age *</label>
                <input
                  type="number"
                  id="traveler-age"
                  name="age"
                  value={travelerFormData.age}
                  onChange={handleTravelerChange}
                  placeholder="Enter traveler's age"
                  min="0"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="traveler-id">ID *</label>
                <input
                  type="text"
                  id="traveler-id"
                  name="id"
                  value={travelerFormData.id}
                  onChange={handleTravelerChange}
                  placeholder="Enter traveler's ID"
                  required
                />
              </div>

              {error && <div className="error-message">{error}</div>}

              <div className="button-group">
                <button
                  type="button"
                  onClick={() => {
                    setShowAddTraveler(false);
                    setTravelerFormData({ name: "", age: "", id: "" });
                    setError(null);
                  }}
                  className="cancel-button"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="submit-button"
                  disabled={addingTraveler}
                >
                  {addingTraveler ? "Adding..." : "Add Traveler"}
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className="booking-page">
      <div className="booking-container">
        <h2>Book Your Trip</h2>

        <div className="connection-summary">
          <h3>Selected Connection</h3>
          <div className="summary-details">
            <div className="summary-row">
              <span className="summary-label">Route:</span>
              <span>
                {connection.departureCity} → {connection.arrivalCity}
              </span>
            </div>
            <div className="summary-row">
              <span className="summary-label">Departure:</span>
              <span>{connection.departureTime}</span>
            </div>
            <div className="summary-row">
              <span className="summary-label">Arrival:</span>
              <span>{connection.arrivalTime}</span>
            </div>
            <div className="summary-row">
              <span className="summary-label">Duration:</span>
              <span>{connection.duration}</span>
            </div>
            <div className="summary-row">
              <span className="summary-label">Price:</span>
              <span className="price-highlight">${connection.price}</span>
            </div>
            <div className="summary-row">
              <span className="summary-label">Class:</span>
              <span>{connection.cabinType}</span>
            </div>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="booking-form">
          <h3>Passenger Information</h3>

          <div className="form-group">
            <label htmlFor="name">Full Name *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="Enter your full name"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="age">Age *</label>
            <input
              type="number"
              id="age"
              name="age"
              value={formData.age}
              onChange={handleChange}
              placeholder="Enter your age"
              min="0"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="id">ID *</label>
            <input
              type="text"
              id="id"
              name="id"
              value={formData.id}
              onChange={handleChange}
              placeholder="Enter your ID"
              required
            />
          </div>

          {error && <div className="error-message">{error}</div>}

          <div className="button-group">
            <button
              type="button"
              onClick={() => navigate(-1)}
              className="cancel-button"
            >
              Cancel
            </button>
            <button type="submit" className="submit-button" disabled={loading}>
              {loading ? "Booking..." : "Confirm Booking"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default BookingPage;

