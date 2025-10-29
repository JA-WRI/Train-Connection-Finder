import React from "react";
import { useNavigate } from "react-router-dom";
import SearchForm from "../components/SearchForm";

const HomePage = () => {
  const navigate = useNavigate();

  const handleSearch = ({ departure, arrival }) => {
    // navigate to connections page, passing city names as state
    navigate("/connections", {
      state: { departure, arrival },
    });
  };

  return (
    <div className="homepage">
      <div className="search-container">
        <SearchForm onSearch={handleSearch} />
      </div>
    </div>
  );
};

export default HomePage;
