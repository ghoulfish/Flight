package interfaces;

import java.io.Serializable;

/**
 * A class for storing a unique identifier.
 * 
 * @param <K>
 *          the type of identifier, can be ordered
 * @param <T>
 *          the type of this object
 */
public interface Identifiable<K extends Comparable<K> & Serializable> extends Serializable {

  /**
   * Get the identifier of this object.
   * 
   * @return the identifier
   */
  public K getIdentifier();

  /**
   * Sets the identifier of this object.
   * 
   * @param id
   *          the identifier to set
   */
  public void setIdentifier(K id);
}
