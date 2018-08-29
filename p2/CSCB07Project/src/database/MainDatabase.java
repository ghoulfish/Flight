package database;

import travel.Itinerary;
import travel.SingleTravel;
import travel.Travel;
import travel.TravelType;

import users.RegisteredUser;

import util.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main database, storing the user and travel databases.
 */
public class MainDatabase implements Serializable {
  private static final long serialVersionUID = 877239344574478509L;
  private static final Logger log = Logger.getLogger(MainDatabase.class.getName());

  private final UserDatabase users;
  private final Map<TravelType, TravelDatabase> travels;
  private final Map<String, List<SingleTravel>> travelSearch;

  /**
   * Creates a new empty main database.
   */
  public MainDatabase() {
    this.users = new UserDatabase();
    this.travelSearch = new LinkedHashMap<>();

    // initialize completely and make final
    EnumMap<TravelType, TravelDatabase> travels = new EnumMap<>(TravelType.class);
    for (TravelType tt : TravelType.values()) {
      travels.put(tt, new TravelDatabase(tt));
    }
    this.travels = Collections.unmodifiableMap(travels);
  }

  /**
   * Adds travel info to this database.
   * 
   * @param travel
   *          the travel to add to this database.
   */
  public void addTravel(SingleTravel travel) {
    // it's invalid if it's expired, or cyclic, or has invalid time
    if (travel.getEndTime().before(travel.getStartTime())
        || travel.getOrigin().equals(travel.getDestination())) {
      log.log(Level.INFO, "A travel of ID {0} and type {1} has invalid data. Skipping.",
          new Object[] { travel.getIdentifier(), travel.getType() });
      return;
    }
    SingleTravel replaced = travels.get(travel.getType()).add(travel);

    if (replaced != null) {
      removeTravelIndex(replaced);

      // to remove this travel completely, we remove it from the itineraries as
      // well (thus making the itinerary invalid and needed to be replaced)
      for (RegisteredUser ru : users.getValues()) {
        ru.removeTravel(replaced);
      }
    }
    addTravelIndex(travel);
  }

  /**
   * Removes the index of a travel for searching.
   * 
   * @param travel
   *          the travel object to remove
   */
  private void removeTravelIndex(SingleTravel travel) {
    // if first occurrence, add an empty set
    if (travelSearch.containsKey(travel.getOrigin())) {
      List<SingleTravel> index = travelSearch.get(travel.getOrigin());
      index.remove(travel);
      if (index.isEmpty()) { // remove it altogether
        travelSearch.remove(travel.getOrigin());
      }
    }
  }

  /**
   * Indexes a travel for searching.
   * 
   * @param travel
   *          the travel object to index
   */
  private void addTravelIndex(SingleTravel travel) {
    // if first occurrence, add an empty set
    if (!travelSearch.containsKey(travel.getOrigin())) {
      travelSearch.put(travel.getOrigin(), new ArrayList<SingleTravel>());
    }
    travelSearch.get(travel.getOrigin()).add(travel);
  }

  /**
   * Adds travel info to this database.
   * 
   * @param travels
   *          the travels to add to this database.
   */
  public void addTravels(Collection<SingleTravel> travels) {
    for (SingleTravel tr : travels) {
      addTravel(tr);
    }
  }

  /**
   * Adds user info to this database.
   * 
   * @param user
   *          the user to add to this database. Existing ones are replaced.
   */
  public void addUser(RegisteredUser user) {
    users.add(user);
  }

  /**
   * Adds user info to this database.
   * 
   * @param users
   *          the users to add to this database. Existing ones are replaced.
   */
  public void addUsers(Collection<RegisteredUser> user) {
    users.addAll(user);
  }

  /**
   * Gets user info from this database.
   * 
   * @param id
   *          the identifier to look for
   */
  public RegisteredUser getUser(String id) {
    return users.get(id);
  }

  /**
   * Gets all users from this database.
   * 
   * @return a collection of all the users
   */
  public Collection<RegisteredUser> getAllUsers() {
    return users.getValues();
  }

  /**
   * Gets all travels in the database.
   * 
   * @param type
   *          the type to get all travels for
   * @return a collection of all the travels
   */
  public Collection<SingleTravel> getAllTravels(TravelType type) {
    return travels.get(type).getValues();
  }

  /**
   * Gets a travel with the corresponding id and type.
   * 
   * @param tt
   *          the type of travel
   * @param id
   *          the id of the travel
   * @return the travel with the respective data
   */
  public SingleTravel getTravel(TravelType tt, String id) {
    return travels.get(tt).getById(id);
  }

  /**
   * Clears all information from this database.
   */
  public void clear() {
    users.clear();
    for (TravelType tt : TravelType.values()) {
      travels.get(tt).clear();
    }
    travelSearch.clear();
  }

  /**
   * Returns all travels that depart from origin and arrive at destination on
   * the given date, of the given type.
   * 
   * @param date
   *          the date to start the travel
   * @param origin
   *          the location to start the travel
   * @param destination
   *          the location to end the travel
   * @param type
   *          the type of travel
   * @return a set of the travels that match the specifications
   */
  public List<SingleTravel> searchTravels(Date date, String origin, String destination,
      TravelType type) {
    return searchTravels(date, origin, destination, type, null);
  }

  /**
   * Returns all travels that depart from origin and arrive at destination on
   * the given date, in the order specified.
   * 
   * @param date
   *          the date to start the travel
   * @param origin
   *          the location to start the travel
   * @param destination
   *          the location to end the travel; a null value will not search
   * @param type
   *          the type of travel; a null value will return all travels
   * @param order
   *          the order to set the travels in; a null value will do nothing
   * @return a set of the travels that match the specifications
   */
  public List<SingleTravel> searchTravels(Date date, String origin, String destination,
      TravelType type, Comparator<Travel> order) {
    List<SingleTravel> ret;
    if (type != null) { // search only type
      ret = travels.get(type).searchTravels(date, origin, destination);
    } else { // search all
      ret = listTravels(date, null, origin, destination);
    }
    if (order != null) {
      Collections.sort(ret, order);
    }
    return ret;
  }

  /**
   * Finds travel information matching the specified arguments, and puts them
   * into a list. THe list is then copied if needed.
   * 
   * @param lower
   *          the date to start the travel; lower bound
   * @param upper
   *          the date to start the travel; upper bound; null value skips check
   * @param origin
   *          the location to start the travel
   * @param destination
   *          the destination to match; null will match all
   * @return a list containing the travel information matched.
   */
  private List<SingleTravel> listTravels(Date lower, Date upper, String origin,
      String destination) {
    if (travelSearch.containsKey(origin)) {
      List<SingleTravel> ret = new ArrayList<>();
      for (SingleTravel travel : travelSearch.get(origin)) {
        if ((destination == null || travel.getDestination().equals(destination))
            && travel.startsWithin(lower, upper)) {
          ret.add(travel);
        }
      }
      return ret;
    }
    return Collections.emptyList();
  }

  /**
   * Returns all itineraries that depart from origin and arrive at destination
   * on the given date.
   * 
   * @param date
   *          the date to start the sequence
   * @param origin
   *          the location to start the sequence
   * @param destination
   *          the location to end the sequence
   * @return a set of the itineraries that match the specifications
   */
  public List<Itinerary> searchItineraries(Date date, String origin, String destination) {
    return searchItineraries(date, origin, destination, null);
  }

  /**
   * Returns all itineraries that depart from origin and arrive at destination
   * on the given date, in the order specified.
   * 
   * @param date
   *          the date to start the sequence
   * @param origin
   *          the location to start the sequence
   * @param destination
   *          the location to end the sequence
   * @param order
   *          the order to set the itineraries in; a null value will do nothing
   * @return a set of the itineraries that match the specifications
   */
  public List<Itinerary> searchItineraries(Date date, String origin, String destination,
      Comparator<Travel> order) {
    if (origin.equals(destination)) {
      log.log(Level.WARNING, "Incorrect input. origin and destination are the same.");
      return Collections.emptyList();
    }
    List<Itinerary> ret = generateItineraries(date, origin, destination);

    if (order != null) {
      Collections.sort(ret, order);
    }
    return ret;
  }

  /**
   * Generates all itineraries that depart from origin and arrive at destination
   * on the given date.
   * 
   * @param date
   *          the date to start the sequence
   * @param origin
   *          the location to start the sequence
   * @param destination
   *          the location to end the sequence
   * @param list
   *          the list to add to
   * @return a list of the itineraries that match the specifications
   */
  private List<Itinerary> generateItineraries(Date date, String origin, String destination) {
    List<Itinerary> list = new ArrayList<>();
    generateItineraries(date, null, origin, destination, list, new Itinerary());
    return list;
  }

  /**
   * Generates all itineraries that start with itin and arrive at destination on
   * the given date, and adds them to an existing list.
   * 
   * @param lower
   *          the date to start the sequence
   * @param upper
   *          the date to start the sequence, upper bound; null will skip check
   * @param origin
   *          the location to start the sequence
   * @param destination
   *          the location to end the sequence
   * @param list
   *          the list to add to
   * @param itin
   *          the running itinerary
   */
  private void generateItineraries(Date lower, Date upper, String origin, String destination,
      List<Itinerary> list, Itinerary itin) {

    if (origin.equals(destination)) {
      // no more itineraries should be made, we've reached the destination
      list.add(itin);
      return;
    }
    // get all travels originating from origin and in the appropriate time range
    List<SingleTravel> travels = listTravels(lower, upper, origin, null);

    for (SingleTravel st : travels) {
      // if we haven't visited this location...
      if (!itin.containsOrigin(st.getDestination())) {
        // repeat process with new date range and origin
        Date newUpper = new Date(st.getEndTime().getTime() + Constants.MAX_STOPOVER);

        Itinerary itinCopy = itin.copy();
        itinCopy.add(st);

        generateItineraries(st.getEndTime(), newUpper, st.getDestination(), destination, list,
            itinCopy);
      }
    }
    // if no travels are found, an itinerary from this location cannot be made
  }
}
