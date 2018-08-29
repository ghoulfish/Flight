package users;

import interfaces.Identifiable;

/**
 * An interface for User, with a unique string identifier and privilege.
 */

public interface User extends Identifiable<String> {
  /**
   * Gets the type of this object.
   * 
   * @return the type of this object
   */
  public UserType getType();

  /**
   * Checks if this user's privilege level is high enough.
   * 
   * @param check
   *          the privilege level to check against this user
   * @return True iff this user's privilege level is greater than or equal to
   *         check.
   */
  public boolean hasPrivilege(int check);
}
