package interfaces;

import java.text.ParseException;

/**
 * An interface for parsing objects from a single line.
 * 
 * @param <T>
 *          the type of object to be parsed
 */
public interface LineParse<T> {
  /**
   * Creates a new object of the appropriate type, from the file.
   * 
   * @return a new object of the appropriate type
   * @throws ParseException
   *           when a date argument is invalid
   * @throws UnsupportedOperationException
   *           when this class does not support this operation
   */
  public T create(String[] args) throws ParseException, UnsupportedOperationException;

  /**
   * Gets the number of arguments expected for parsing from a file.
   * 
   * @return the number of arguments
   */
  public int getNumArguments();

  /**
   * Gets the delimiter for parsing the line.
   * 
   * @return the delimiter
   */
  public String getDelimiter();
}
