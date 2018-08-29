package database;

import users.RegisteredUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Storage of all registered users in the system.
 */
public class UserDatabase extends IdentifiableMap<String, RegisteredUser> {
  private static final long serialVersionUID = -8268532265982086570L;

  /**
   * Returns all users of the given names.
   * 
   * @param firstNames
   *          the first names to check for
   * @param lastName
   *          the last name to check for
   * @return a set of the users that contain the names provided
   */
  public List<RegisteredUser> searchUsers(String firstNames, String lastName) {
    List<RegisteredUser> ret = new ArrayList<>();
    for (RegisteredUser ru : getValues()) {
      if (ru.getFirstNames().contains(firstNames) && ru.getLastName().contains(lastName)) {
        ret.add(ru);
      }
    }
    return ret;
  }
}
