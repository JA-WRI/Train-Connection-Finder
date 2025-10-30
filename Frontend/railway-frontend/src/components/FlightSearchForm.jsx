import React, { useState } from "react";
import "../styles/flightSearchForm.css"; // optional styling file

const FlightSearchForm = ({ onFilterChange }) => {
  const [filters, setFilters] = useState({
    departureDate: "",
    arrivalDate: "",
    departureTime: "",
    arrivalTime: "",
    ticketClass: "second-class",
    maxPrice: "",
    maxDuration: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onFilterChange(filters);
  };

  return (
    <form className="flight-search-form" onSubmit={handleSubmit}>
      <div className="form-row">
        <label>Departure Date:</label>
        <input
          type="date"
          name="departureDate"
          value={filters.departureDate}
          onChange={handleChange}
        />

        <label>Arrival Date:</label>
        <input
          type="date"
          name="arrivalDate"
          value={filters.arrivalDate}
          onChange={handleChange}
        />
      </div>

      <div className="form-row">
        <label>Departure Time:</label>
        <input
          type="time"
          name="departureTime"
          value={filters.departureTime}
          onChange={handleChange}
        />

        <label>Arrival Time:</label>
        <input
          type="time"
          name="arrivalTime"
          value={filters.arrivalTime}
          onChange={handleChange}
        />
      </div>

      <div className="form-row">
        <label>Ticket Class:</label>
        <select
          name="ticketClass"
          value={filters.ticketClass}
          onChange={handleChange}
        >
          <option value="first-class">First Class</option>
          <option value="second-class">Second Class</option>
        </select>

        <label>Max Price:</label>
        <input
          type="number"
          name="maxPrice"
          min="0"
          value={filters.maxPrice}
          onChange={handleChange}
          placeholder="e.g. 200"
        />

        <label>Max Duration (hrs):</label>
        <input
          type="number"
          name="maxDuration"
          min="0"
          value={filters.maxDuration}
          onChange={handleChange}
          placeholder="e.g. 5"
        />
      </div>

      <button type="submit">Apply Filters</button>
    </form>
  );
};

export default FlightSearchForm;
