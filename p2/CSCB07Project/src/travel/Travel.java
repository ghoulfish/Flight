package travel;

import util.TimeFormat;

import java.util.Date;

/**
 * A Travel class used to implement general travel methods. Both single trips
 * and itineraries (sequences of trips) have these methods.
 */
public abstract class Travel {

  /**
   * Get the arrival date and time of the travel.
   * 
   * @return a Date representative of the arrival date and time of the travel
   */
  public abstract Date getStartTime();

  /**
   * Get the departure date and time of the travel.
   * 
   * @return a string representative of the departure date and time of the
   *         travel
   */
  public abstract Date getEndTime();

  /**
   * Get the origin of the travel.
   * 
   * @return a string representative of the origin of the travel
   */
  public abstract String getOrigin();

  /**
   * Get the destination of the travel.
   * 
   * @return a string representative of the destination of the travel
   */
  public abstract String getDestination();

  /**
   * Get the cost of the travel.
   * 
   * @return the cost of the travel
   */
  public abstract double getCost();

  /**
   * Get the travel time (in milliseconds) of the travel.
   * 
   * @return the travel time of the travel
   */
  public long getTravelTime() {
    return getEndTime().getTime() - getStartTime().getTime();
  }

  /**
   * Formats the travel time into a proper string format.
   * 
   * @return a string representing the travel time
   */
  public String formatTravelTime() {
    return TimeFormat.formatMillis(getTravelTime());
  }

  /**
   * Formats the departure time into a proper string format.
   * 
   * @return a string representing the departure time
   */
  public String formatStartTime() {
    return TimeFormat.DATE_TIME.formatDate(getStartTime());
  }

  /**
   * Formats the arrival time into a proper string format.
   * 
   * @return a string representing the arrival time
   */
  public String formatEndTime() {
    return TimeFormat.DATE_TIME.formatDate(getEndTime());
  }

  /**
   * Returns whether this travel starts on the specified day.
   * 
   * @param date
   *          the date to check
   * @return True if this travel matches the date
   */
  public boolean startsWithin(Date date) {
    return startsWithin(date, null);
  }

  /**
   * Returns whether this travel starts between the two dates, inclusively.
   * 
   * @param dateLower
   *          the date to check as a lower bound
   * @param dateUpper
   *          the date to check as an upper bound; null value skips check
   * @return True if this travel starts in between dateLower and dateUpper
   */
  public boolean startsWithin(Date dateLower, Date dateUpper) {
    String format = TimeFormat.DATE.formatDate(getStartTime());
    return (getStartTime().after(dateLower) || getStartTime().equals(dateLower))
        && (dateUpper == null || getStartTime().before(dateUpper)
            || getStartTime().equals(dateUpper))
        && (format.equals(TimeFormat.DATE.formatDate(dateLower))
            || (dateUpper != null && format.equals(TimeFormat.DATE.formatDate(dateUpper))));
  }

  @Override
  public abstract String toString();

}
