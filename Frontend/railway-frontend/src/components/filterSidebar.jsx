import React from "react";
import "./filterSidebar.css";

const SortSidebar = ({ connections, onSortChange, sortCriteria }) => {
  const handleSortChange = (criteria, order) => {
    onSortChange(criteria, order);
  };

  return (
    <div className="sort-sidebar">
      <h3 className="sidebar-title">Sort Connections</h3>
      
      <div className="sort-section">
        <h4 className="sort-subtitle">Duration</h4>
        <div className="sort-options">
          <button
            className={`sort-button ${sortCriteria.criteria === 'duration' && sortCriteria.order === 'asc' ? 'active' : ''}`}
            onClick={() => handleSortChange('duration', 'asc')}
          >
            Shortest First
          </button>
          <button
            className={`sort-button ${sortCriteria.criteria === 'duration' && sortCriteria.order === 'desc' ? 'active' : ''}`}
            onClick={() => handleSortChange('duration', 'desc')}
          >
            Longest First
          </button>
        </div>
      </div>

      <div className="sort-section">
        <h4 className="sort-subtitle">Price - First Class</h4>
        <div className="sort-options">
          <button
            className={`sort-button ${sortCriteria.criteria === 'priceFirst' && sortCriteria.order === 'asc' ? 'active' : ''}`}
            onClick={() => handleSortChange('priceFirst', 'asc')}
          >
            Lowest First
          </button>
          <button
            className={`sort-button ${sortCriteria.criteria === 'priceFirst' && sortCriteria.order === 'desc' ? 'active' : ''}`}
            onClick={() => handleSortChange('priceFirst', 'desc')}
          >
            Highest First
          </button>
        </div>
      </div>

      <div className="sort-section">
        <h4 className="sort-subtitle">Price - Second Class</h4>
        <div className="sort-options">
          <button
            className={`sort-button ${sortCriteria.criteria === 'priceSecond' && sortCriteria.order === 'asc' ? 'active' : ''}`}
            onClick={() => handleSortChange('priceSecond', 'asc')}
          >
            Lowest First
          </button>
          <button
            className={`sort-button ${sortCriteria.criteria === 'priceSecond' && sortCriteria.order === 'desc' ? 'active' : ''}`}
            onClick={() => handleSortChange('priceSecond', 'desc')}
          >
            Highest First
          </button>
        </div>
      </div>

      <div className="sort-section">
        <h4 className="sort-subtitle">Departure Time</h4>
        <div className="sort-options">
          <button
            className={`sort-button ${sortCriteria.criteria === 'departureTime' && sortCriteria.order === 'asc' ? 'active' : ''}`}
            onClick={() => handleSortChange('departureTime', 'asc')}
          >
            Earliest First
          </button>
          <button
            className={`sort-button ${sortCriteria.criteria === 'departureTime' && sortCriteria.order === 'desc' ? 'active' : ''}`}
            onClick={() => handleSortChange('departureTime', 'desc')}
          >
            Latest First
          </button>
        </div>
      </div>

      <div className="sort-section">
        <h4 className="sort-subtitle">Number of Routes</h4>
        <div className="sort-options">
          <button
            className={`sort-button ${sortCriteria.criteria === 'routesCount' && sortCriteria.order === 'asc' ? 'active' : ''}`}
            onClick={() => handleSortChange('routesCount', 'asc')}
          >
            Fewest First
          </button>
          <button
            className={`sort-button ${sortCriteria.criteria === 'routesCount' && sortCriteria.order === 'desc' ? 'active' : ''}`}
            onClick={() => handleSortChange('routesCount', 'desc')}
          >
            Most First
          </button>
        </div>
      </div>
    </div>
  );
};

export default SortSidebar;