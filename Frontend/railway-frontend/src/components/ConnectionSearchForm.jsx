import React, { useState } from "react";
import SortSidebar from "./SortSidebar";
import "../styles/connectionSearchForm.css"; 

const ConnectionSearchForm = ({ onFilterChange, onSortChange, sortCriteria }) => {
  const [filters, setFilters] = useState({
    departureDay: "",
    arrivalDay: "",
    departureTime: "",
    arrivalTime: "",
    ticketClass: "second-class",
    maxPrice: "",
    maxDuration: "",
  });
  const [isSortOpen, setIsSortOpen] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onFilterChange(filters);
  };

  // Generate hour options for military time
  const hours = Array.from({ length: 24 }, (_, i) => {
    const hourStr = i.toString().padStart(2, "0") + ":00";
    return (
      <option key={hourStr} value={hourStr}>
        {hourStr}
      </option>
    );
  });

  // Days of the week
  const daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

  return (
    <form className="connection-search-form" onSubmit={handleSubmit}>
      <div className="form-row">
        <label>Departure Day:</label>
        <select name="departureDay" value={filters.departureDay} onChange={handleChange}>
          <option value="">Any</option>
          {daysOfWeek.map((day) => (
            <option key={day} value={day}>{day}</option>
          ))}
        </select>

        <label>Arrival Day:</label>
        <select name="arrivalDay" value={filters.arrivalDay} onChange={handleChange}>
          <option value="">Any</option>
          {daysOfWeek.map((day) => (
            <option key={day} value={day}>{day}</option>
          ))}
        </select>
      </div>

      <div className="form-row">
        <label>Departure Time:</label>
        <select name="departureTime" value={filters.departureTime} onChange={handleChange}>
          <option value="">Any</option>
          {hours}
        </select>

        <label>Arrival Time:</label>
        <select name="arrivalTime" value={filters.arrivalTime} onChange={handleChange}>
          <option value="">Any</option>
          {hours}
        </select>
      </div>

      <div className="form-row">
        <label>Ticket Class:</label>
        <select name="ticketClass" value={filters.ticketClass} onChange={handleChange}>
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

      <div style={{ display: "flex", gap: "8px" }}>
        <button type="submit">Apply Filters</button>
        <button type="button" onClick={() => setIsSortOpen(true)}>Sort</button>
      </div>

      <SortSidebar
        isOpen={isSortOpen}
        onClose={() => setIsSortOpen(false)}
        onSortChange={(criteria, order) => onSortChange && onSortChange(criteria, order)}
        sortCriteria={sortCriteria || { criteria: null, order: null }}
      />
    </form>
  );
};

export default ConnectionSearchForm;
