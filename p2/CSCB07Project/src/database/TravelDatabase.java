package database;

import travel.SingleTravel;
import travel.TravelType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Storage of all travels of a specific type in the system.
 */
public class TravelDatabase extends IdentifiableMap<String, SingleTravel> {
  private static final long serialVersionUID = 9124880330389237676L;
  private TravelType type;

  /**
   * Creates a new empty TravelDatabase.
   */
  public TravelDatabase(TravelType type) {
    this.type = type;
  }

  /**
   * Gets the type of this TravelDatabase.
   */
  public TravelType getType() {
    return type;
  }

  /**
   * Returns all travels that depart from origin and arrive at destination on
   * the given date, of this type.
   * 
   * @param date
   *          the date to check for
   * @param origin
   *          the origin to check for
   * @param destination
   *          the destination to check for; a null value will not check it
   * @return a set of the travels that match the specifications
   */
  public List<SingleTravel> searchTravels(Date date, String origin, String destination) {
    List<SingleTravel> ret = new ArrayList<>();
    for (SingleTravel travel : getValues()) {
      if (travel.startsWithin(date) && travel.getOrigin().equals(origin)
          && (destination == null || travel.getDestination().equals(destination))) {
        ret.add(travel);
      }
    }
    return ret;
  }

}
