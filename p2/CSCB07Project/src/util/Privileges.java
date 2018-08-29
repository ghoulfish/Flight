package util;

/**
 * A class that lists all privilege constants.
 */
public class Privileges {
  // --------------Constants----------------
  public static final int CLIENT_LEVEL = 1;
  public static final int ADMIN_LEVEL = 100;

  // -----------------All-------------------
  public static final int SEARCH_MULTI = 0; // Itineraries
  public static final int SEARCH_SINGLE = 0; // SingleTravels
  public static final int SORT_TRAVEL = 0;

  // ----------------Client-----------------
  public static final int BOOK_TRAVEL = 1;
  public static final int EDIT_SELF = 1;

  // ----------------Admin------------------
  public static final int EDIT_OTHER = 100;
  public static final int VIEW_OTHER = 100;
  public static final int UPLOAD_USER = 100;
  public static final int UPLOAD_TRAVEL = 100;
}
