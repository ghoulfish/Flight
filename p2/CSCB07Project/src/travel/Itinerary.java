package travel;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * A class representing an itinerary, a sequence of SingleTravel. This sequence
 * is strictly non-empty, contains non-null values, and is sequential.
 */
public class Itinerary extends Travel {
  private final LinkedHashMap<String, SingleTravel> travel;
  private SingleTravel last;
  private double totalCost;

  /**
   * Creates a multiple-element Itinerary
   * 
   * @param travel
   *          The travels to be represented by this itinerary.
   */
  public Itinerary(LinkedHashMap<String, SingleTravel> travel) {
    this.travel = travel;
    calculateCost();
  }

  /**
   * Creates an empty Itinerary.
   */
  public Itinerary() {
    this(new LinkedHashMap<String, SingleTravel>());
  }

  /**
   * Creates a single-element Itinerary.
   * 
   * @param travel
   *          The single travel to be represented by this itinerary.
   */
  public Itinerary(SingleTravel travel) {
    this();
    addTravel(travel);
  }

  /**
   * Adds a trip to this itinerary.
   * 
   * @param toAdd
   *          the travel to add to this itinerary
   * @throws IllegalArgumentException
   *           if the added travel is invalid for this itinerary
   */
  public void add(SingleTravel toAdd) throws IllegalArgumentException {
    // it's invalid if the time and location aren't after the end of this path
    if (!travel.isEmpty() && (toAdd.getStartTime().before(getEndTime())
        || !toAdd.getOrigin().equals(getDestination())
        || containsOrigin(toAdd.getOrigin()))) {
      throw new IllegalArgumentException("Invalid travel. It should start after " + getEndTime()
          + " and it should originate at " + getDestination() + ". Currently it starts from "
          + toAdd.getOrigin() + ", " + toAdd.getStartTime());
    }
    addTravel(toAdd);
  }

  /**
   * Gets whether a travel is in this itinerary.
   * 
   * @param check the travel to check
   * @return True if this itinerary contains check
   */
  public boolean containsTravel(SingleTravel check) {
    return travel.containsValue(check);
  }
  
  /**
   * Gets whether a travel origin is in this itinerary.
   * 
   * @param check the origin to check
   * @return True if this itinerary contains a travel originating from origin
   */
  public boolean containsOrigin(String check) {
    return travel.containsKey(check);
  }

  /**
   * Gets how many travels are in this itinerary.
   * 
   * @return the amount of travels
   */
  public int size() {
    return travel.size();
  }

  /**
   * Gets whether this itinerary is empty.
   * 
   * @return True if this itinerary is empty
   */
  public boolean isEmpty() {
    return travel.isEmpty();
  }

  /**
   * Makes a copy of this Itinerary.
   * 
   * @return a copy of this Itinerary
   */
  public Itinerary copy() {
    return new Itinerary(new LinkedHashMap<>(travel));
  }

  /**
   * Gets the travels in this itinerary.
   * 
   * @return the travels in this itinerary in order
   */
  public Collection<SingleTravel> getTravels() {
    return travel.values();
  }

  /**
   * Adds a trip to this itinerary.
   * 
   * @param toAdd
   *          the travel to add to this itinerary
   */
  private void addTravel(SingleTravel toAdd) {
    travel.put(toAdd.getOrigin(), toAdd);
    this.last = toAdd;
    totalCost += toAdd.getCost();
  }

  /**
   * Calculates the total cost from the trips in this itinerary.
   */
  private void calculateCost() {
    totalCost = 0;
    for (SingleTravel travels : getTravels()) {
      totalCost += travels.getCost();
      this.last = travels; //update while we go through
    }
  }
  
  /**
   * Gets the first travel in this itinerary.
   * @return the first travel
   */
  private SingleTravel getFirst() {
    if (isEmpty()) {
      throw new IndexOutOfBoundsException("Empty itinerary tried to access elements.");
    }
    return travel.values().iterator().next();
  }
  
  /**
   * Gets the last travel in this itinerary.
   * @return the last travel
   */
  private SingleTravel getLast() {
    if (isEmpty() || last == null) {
      throw new IndexOutOfBoundsException("Empty itinerary tried to access elements.");
    }
    return last;
  }

  @Override
  public Date getStartTime() {
    return getFirst().getStartTime();
  }

  @Override
  public Date getEndTime() {
    return getLast().getEndTime();
  }

  @Override
  public String getOrigin() {
    return getFirst().getOrigin();
  }

  @Override
  public String getDestination() {
    return getLast().getDestination();
  }

  @Override
  public double getCost() {
    return totalCost;
  }

  @Override
  public String toString() {
    // Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
    // followed by total price (on its own line, exactly two decimal places),
    // followed by total duration (on its own line, in format HH:MM).
    StringBuilder builder = new StringBuilder();
    for (SingleTravel tr : getTravels()) {
      builder.append(tr.toString(false)).append("\n");
    }
    builder.append(String.format("%.2f", getCost())).append("\n");
    builder.append(formatTravelTime());
    return builder.toString();
  }
}
