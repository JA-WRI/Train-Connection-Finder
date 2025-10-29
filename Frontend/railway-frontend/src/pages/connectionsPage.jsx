import React from "react";
import { useLocation } from "react-router-dom";
import ConnectionCard from "../components/connectionCard";
import "../styles/connectionsCard.css";

const dummyConnections = [
  {
    departureTime: "08:15",
    arrivalTime: "13:40",
    departureCity: "Montreal",
    arrivalCity: "Toronto",
    duration: "5h 25m",
    stops: 0,
    price: 125,
    cabinType: "Standard",
    detailsAvailable: true,
  },
  {
    departureTime: "10:30",
    arrivalTime: "18:00",
    departureCity: "Montreal",
    arrivalCity: "Toronto",
    duration: "7h 30m",
    stops: 1,
    price: 95,
    cabinType: "Economy",
    detailsAvailable: false,
  },
  {
    departureTime: "12:00",
    arrivalTime: "17:20",
    departureCity: "Montreal",
    arrivalCity: "Toronto",
    duration: "5h 20m",
    stops: 0,
    price: 145,
    cabinType: "First Class",
    detailsAvailable: true,
  },
];

const ConnectionsPage = () => {
  const location = useLocation();
  const { departure, arrival } = location.state || {};

  return (
    <div className="connections-page">
      <h2 className="page-title">
        {departure && arrival
          ? `Connections from ${departure} to ${arrival}`
          : "Available Train Connections"}
      </h2>

      <div className="connections-list">
        {dummyConnections.map((c, i) => (
          <ConnectionCard key={i} connection={c} />
        ))}
      </div>
    </div>
  );
};

export default ConnectionsPage;
