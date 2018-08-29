package driver;

import database.MainDatabase;

import io.InputOperations;
import io.SaveOperations;

import travel.Itinerary;
import travel.SingleTravel;
import travel.Travel;
import travel.TravelType;

import users.User;
import users.UserType;

import util.Privileges;
import util.TimeFormat;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Driver class for interacting with the user.
 */
public class UserControl {
  private static final Logger log = Logger.getLogger(UserControl.class.getName());

  private User user = null;
  private MainDatabase database;

  private static UserControl instance = new UserControl();

  /**
   * Allows access to this instance of the user control.
   * 
   * @return an instance of the driver
   */
  public static UserControl getInstance() {
    return instance;
  }

  /**
   * Creates a new UserControl instance.
   */
  private UserControl() {
    this.database = SaveOperations.deserializeDatabase();
  }

  /**
   * Saves this database to a file.
   */
  public void save() {
    SaveOperations.serializeDatabase(database);
  }

  /**
   * Gets the main database of this instance.
   * 
   * @return the main database
   */
  public MainDatabase getDatabase() {
    return database;
  }

  /**
   * Uploads user information from the file at the given path.
   * 
   * @param path
   *          the path to an input csv file of user information
   * @param type
   *          the type of the users we are adding
   */
  public void uploadClientInfo(String path, UserType type) {
    // parse the data into a collection, then add it to the users
    if (user.hasPrivilege(Privileges.UPLOAD_USER)) {
      database.addUsers(InputOperations.parseData(path, type));
    }
  }

  /**
   * Uploads travel information from the file at the given path.
   * 
   * @param path
   *          the path to an input csv file of travel information
   * @param type
   *          the type of the travels we are adding
   */
  public void uploadTravelInfo(String path, TravelType type) {
    // parse the data into a collection, then add it to the travels
    if (user.hasPrivilege(Privileges.UPLOAD_TRAVEL)) {
      database.addTravels(InputOperations.parseData(path, type));
    }
  }

  /**
   * Returns all travels that depart from origin and arrive at destination on
   * the given date.
   * 
   * @param date
   *          a departure date (in the format YYYY-MM-DD)
   * @param origin
   *          a travel origin
   * @param destination
   *          a travel destination
   * @return the travels that depart from origin and arrive at destination on
   *         the given date
   */
  public List<SingleTravel> searchTravels(String date, String origin, String destination,
      TravelType type) {
    try {
      if (user.hasPrivilege(Privileges.SEARCH_SINGLE)) {
        return database.searchTravels(TimeFormat.DATE.parseString(date), origin, destination,
            type);
      }
    } catch (ParseException e) {
      log.log(Level.SEVERE, "Incorrect date format.", e);
    }
    return Collections.emptyList();
  }

  /**
   * Returns all itineraries that depart from origin and arrive at destination
   * on the given date.
   * 
   * @param date
   *          a departure date (in the format YYYY-MM-DD)
   * @param origin
   *          a travel origin
   * @param destination
   *          a travel destination
   * @return itineraries that depart from origin and arrive at destination on
   *         the given date
   */
  public List<Itinerary> getItineraries(String date, String origin, String destination) {
    try {
      if (user.hasPrivilege(Privileges.SEARCH_MULTI)) {
        return database.searchItineraries(TimeFormat.DATE.parseString(date), origin, destination,
            null);
      }
    } catch (ParseException e) {
      log.log(Level.SEVERE, "Incorrect date format.", e);
    }
    return Collections.emptyList();
  }

  /**
   * Sorts the list of travels with the comparison method provided.
   * 
   * @param travels
   *          the list of travels to sort
   * @param order
   *          the comparison method
   */
  public void sortTravels(List<? extends Travel> travels, Comparator<Travel> order) {
    if (user.hasPrivilege(Privileges.SORT_TRAVEL)) {
      Collections.sort(travels, order);
    }
  }
}
