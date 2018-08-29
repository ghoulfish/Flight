package travel;

import java.util.Date;

/**
 * A class that represents a flight.
 */
public class Flight extends SingleTravel {
  private static final long serialVersionUID = 7821628923036367887L;
  private String airline;

  /**
   * Create a SingleTravel instance with given parameters.
   * 
   * @param type
   *          the type of this travel
   * @param id
   *          the given travel id
   * @param start
   *          the departure date and time
   * @param end
   *          the arrival date and time
   * @param origin
   *          the given origin
   * @param destination
   *          the given destination
   * @param cost
   *          the given cost
   * @param airline
   *          the given airline
   */
  public Flight(TravelType type, String id, Date start, Date end, String origin,
      String destination, double cost, String airline) {
    super(type, id, start, end, origin, destination, cost);
    this.airline = airline;
  }

  /**
   * Get the airline.
   * 
   * @return the airline
   */
  public String getAirline() {
    return airline;
  }

  /**
   * Set the airline of the flight.
   * 
   * @param airline
   *          the airline to set
   */
  public void setAirline(String airline) {
    this.airline = airline;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + airline.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj) || getClass() != obj.getClass()) {
      return false;
    }
    Flight other = (Flight) obj;
    if (!airline.equals(other.airline)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString(boolean includePrice) {
    // Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
    String ret = String.format("%s,%s,%s,%s,%s,%s", getIdentifier(), formatStartTime(),
        formatEndTime(), getAirline(), getOrigin(), getDestination());
    if (includePrice) {
      ret += String.format(",%.2f", getCost());
    }
    return ret;
  }

}
