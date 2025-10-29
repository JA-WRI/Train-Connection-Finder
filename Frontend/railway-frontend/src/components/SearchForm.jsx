import React, { useState } from "react";
import "../styles/search.css";

const SearchForm = ({ onSearch }) => {
  const [departure, setDeparture] = useState("");
  const [arrival, setArrival] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    onSearch({ departure, arrival });
  };

  return (
    <form className="search-form" onSubmit={handleSubmit}>
      <h2 className="search-title">Search Train Connections</h2>

      <div className="input-row">
        <div className="input-group">
          <label>Departure City</label>
          <input
            type="text"
            placeholder="e.g., Montreal"
            value={departure}
            onChange={(e) => setDeparture(e.target.value)}
            required
          />
        </div>

        <div className="input-group">
          <label>Arrival City</label>
          <input
            type="text"
            placeholder="e.g., Toronto"
            value={arrival}
            onChange={(e) => setArrival(e.target.value)}
            required
          />
        </div>
      </div>

      <button type="submit" className="search-button">
        Search
      </button>
    </form>
  );
};

export default SearchForm;
