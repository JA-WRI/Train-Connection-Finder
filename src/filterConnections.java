import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class filterConnections {
    //Sort connections from low to high duration
    public List<Connection> AscenSortDuration(List<Connection> connections){
        connections.sort(Comparator.comparingDouble(Connection::getDuration));
        return connections;
    }
    //Sort connections from high to low duration
    public List<Connection> DescenSortDuration(List<Connection> connections){
        connections.sort(Comparator.comparingDouble(Connection::getDuration).reversed());
        return connections;
    }
    //Filtering the connections based on a given max duration
    public List<Connection> filterDuration(int givenDuration, List<Connection> connections){
        double tripDuration = 0.0;
        List<Connection> filteredConnection = new ArrayList<>();
        for (Connection connection: connections){
            tripDuration = connection.getDuration();
            if (tripDuration<=givenDuration){
                filteredConnection.add(connection);
            }
        }

        return filteredConnection;
    }
    //Filtering the connections based on a given max price for first class
    public List<Connection> filterPriceFirstClass(int givenPrice,List<Connection> connections) {
        double tripPrice = 0.0;
        List<Connection> filteredConnection = new ArrayList<>();
        for (Connection connection : connections) {
            tripPrice = connection.getFirstClassPrice();
            if (tripPrice <= givenPrice) {
                filteredConnection.add(connection);
            }
        }
        return filteredConnection;
    }
    //Filtering the connections based on a given max price for second class
    public List<Connection> filterPriceSecondClass(int givenPrice,List<Connection> connections) {
        double tripPrice = 0.0;
        List<Connection> filteredConnection = new ArrayList<>();
        for (Connection connection : connections) {
            tripPrice = connection.getSecondClassPrice();
            if (tripPrice <= givenPrice) {
                filteredConnection.add(connection);
            }
        }
        return filteredConnection;
    }
}
