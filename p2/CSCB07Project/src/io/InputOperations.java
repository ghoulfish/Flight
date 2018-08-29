package io;

import interfaces.LineParse;

import util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

/**
 * A class for input from files and formatting to console. Admins select the
 * file paths.
 */
public class InputOperations {
  private static final Logger log = Logger.getLogger(InputOperations.class.getName());

  /**
   * Parses data from a given file path, parsing objects line by line. The data
   * returned is of class T, and type is of class E, where E can parse lines to
   * create T.
   * 
   * @param file
   *          the path of the file
   * @param type
   *          the type of the objects
   * @return a set containing all users parsed from the file
   */
  public static <T, E extends LineParse<T>> Set<T> parseData(String file, E type) {
    Set<T> ret = new LinkedHashSet<>();

    // open the file for reading with the given path
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(file),
        Charset.forName(Constants.FILE_ENCODING))) {
      // each line is one object
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        String[] splitted = line.split(type.getDelimiter());
        try {
          // invalid input -- wrong number of arguments
          if (splitted.length != type.getNumArguments()) {
            log.log(Level.WARNING, "A line had {0} arguments, but expected was {1}. Skipping.",
                new Object[] { splitted.length, type.getNumArguments() });
          } else {
            ret.add(type.create(splitted));
          }
        } catch (ParseException e) {
          log.log(Level.SEVERE, "A line had incorrect arguments. Skipping.", e);
        }
      }
    } catch (IOException e) {
      log.log(Level.SEVERE, e.toString(), e);
    }
    return ret;
  }

  /**
   * Formats a collection into line-by-line format.
   * 
   * @param collection
   *          the collection of objects to parse
   * @return a String representing the collection
   */
  public static <T> String formatCollection(Collection<T> collection) {
    StringBuilder sb = new StringBuilder();
    for (T it : collection) {
      sb.append(it).append("\n");
    }
    return sb.toString();
  }

  /**
   * Hashes an input string.
   * 
   * @param input
   *          the input password
   * @return a hash of input
   */
  public static String hashPassword(String input) {
    try {
      // convert bytes to string
      return DatatypeConverter.printHexBinary(cryptPassword(input));
    } catch (UnsupportedEncodingException e) {
      log.log(Level.SEVERE, "Error hashing password.", e);
      return input;
    }
  }

  /**
   * Encrypts an input string.
   * 
   * @param input
   *          the input password
   * @return a byte array of the encrypted string
   * @throws UnsupportedEncodingException
   *           when the encoding is invalid
   */
  public static byte[] cryptPassword(String input) throws UnsupportedEncodingException {
    if (Constants.PASS_ENCODING.length() == 0) {
      // no hashing is wanted
      return input.getBytes(Constants.FILE_ENCODING);
    }
    try {
      MessageDigest md = MessageDigest.getInstance(Constants.PASS_ENCODING);
      return md.digest(input.getBytes(Constants.FILE_ENCODING));
    } catch (NoSuchAlgorithmException e) {
      log.log(Level.SEVERE, "Error hashing password.", e);
      return input.getBytes(Constants.FILE_ENCODING);
    }
  }
}
