package travel;

import interfaces.LineParse;

import util.TimeFormat;

import java.text.ParseException;

/**
 * An enumeration of the possible types of travel.
 */
public enum TravelType implements LineParse<SingleTravel> {
  Flight(7) {
    @Override
    public SingleTravel create(String[] args)
        throws ParseException, UnsupportedOperationException {
      // Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,Price
      return new Flight(this, args[0], TimeFormat.DATE_TIME.parseString(args[1]),
          TimeFormat.DATE_TIME.parseString(args[2]), args[4], args[5],
          Double.parseDouble(args[6]), args[3]);
    }
  };

  private final int numArgs;

  /**
   * Creates a new TravelType with the specified arguments.
   * 
   * @param numArgs
   *          the number of expected arguments in a file
   */
  private TravelType(int numArgs) {
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
}
