package travel;

import interfaces.Identifiable;

import java.util.Date;

/**
 * A class that represents a single travel.
 */
public abstract class SingleTravel extends Travel implements Identifiable<String> {
  private static final long serialVersionUID = 4953963860520338679L;
  private String id;
  private final double cost;
  private final TravelType type; // Enums are Serializable by default
  private final Date start;
  private final Date end;
  private final String origin;
  private final String destination;

  /**
   * Create a SingleTravel instance with given parameters.
   * 
   * @param type
   *          the type of this SingleTravel
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
   */
  public SingleTravel(TravelType type, String id, Date start, Date end, String origin,
      String destination, double cost) {
    this.id = id;
    this.type = type;
    this.start = start;
    this.end = end;
    this.origin = origin;
    this.destination = destination;
    this.cost = cost;
  }

  @Override
  public String getIdentifier() {
    return id;
  }

  @Override
  public void setIdentifier(String id) {
    this.id = id;
  }

  /**
   * Gets the type of this object.
   * 
   * @return the type of this object
   */
  public TravelType getType() {
    return type;
  }

  @Override
  public Date getStartTime() {
    return start;
  }

  @Override
  public Date getEndTime() {
    return end;
  }

  @Override
  public String getOrigin() {
    return origin;
  }

  @Override
  public String getDestination() {
    return destination;
  }

  @Override
  public double getCost() {
    return cost;
  }

  @Override
  public String toString() {
    return toString(true);
  }

  /**
   * Formats this travel into a string representation.
   * 
   * @param includePrice
   *          if the price of this travel should be included
   * @return a string representing this travel
   */
  public abstract String toString(boolean includePrice);

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp = Double.doubleToLongBits(cost);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + destination.hashCode();
    result = prime * result + end.hashCode();
    result = prime * result + id.hashCode();
    result = prime * result + origin.hashCode();
    result = prime * result + start.hashCode();
    result = prime * result + type.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    SingleTravel other = (SingleTravel) obj;
    if (Double.doubleToLongBits(cost) != Double.doubleToLongBits(other.cost) || type != other.type
        || !origin.equals(other.origin) || !start.equals(other.start)
        || !destination.equals(other.destination) || !end.equals(other.end)
        || !id.equals(other.id)) {
      return false;
    }
    return true;
  }

}
