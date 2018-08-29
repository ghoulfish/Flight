package users;

import interfaces.LineParse;

import util.Privileges;
import util.TimeFormat;

import java.text.ParseException;

/**
 * A class for storing user types.
 */
public enum UserType implements LineParse<RegisteredUser> {
  Client(Privileges.CLIENT_LEVEL, 6) {
    @Override
    public RegisteredUser create(String[] args)
        throws ParseException, UnsupportedOperationException {
      // LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
      return new Client(args[2], this, args[1], args[0], args[3], args[4],
          TimeFormat.DATE.parseString(args[5]));
    }

  },
  Administrator(Privileges.ADMIN_LEVEL, 6) {
    @Override
    public RegisteredUser create(String[] args)
        throws ParseException, UnsupportedOperationException {
      // LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
      return new Administrator(args[2], this, args[1], args[0], args[3], args[4],
          TimeFormat.DATE.parseString(args[5]));
    }

  };

  private final int level;
  private final int numArgs;

  /**
   * Defines a new UserType.
   * 
   * @param level
   *          the privileges of the user type
   * @param numArgs
   *          the number of expected arguments in a file
   */
  private UserType(int level, int numArgs) {
    this.level = level;
    this.numArgs = numArgs;
  }

  @Override
  public int getNumArguments() {
    return numArgs;
  }

  @Override
  public String getDelimiter() {
    return ",";
  }

  /**
   * Gets the privilege level constant.
   * 
   * @return the privilege level
   */
  public int getPrivilege() {
    return level;
  }

  /**
   * Checks if this privilege level is high enough.
   * 
   * @param check
   *          the privilege level to check against this privilege
   * @return True iff this privilege level is greater than or equal to check.
   */
  public boolean hasPrivilege(int check) {
    return level >= check;
  }

  @Override
  public RegisteredUser create(String[] args)
      throws ParseException, UnsupportedOperationException {
    // default -- if not specified
    throw new UnsupportedOperationException("This type does not support creation.");
  }
}
