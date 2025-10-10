import java.util.ArrayList;
import java.util.List;

public class filterConnections {

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
    public void filterByFirstClassTickets(){ // only show first class tickets and prices

    }

    public void sortByPrice(){} //lowest price first

    public void filterByDayOfDeparture(){}; //only show flights that leave on the day user specifies

    public void filterBYDatOfArrival(){};  //only show flights that arrive on the day specified

}
