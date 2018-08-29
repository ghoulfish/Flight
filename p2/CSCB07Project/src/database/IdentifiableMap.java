package database;

import interfaces.Identifiable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * A sorted map that maps an identifier to an identifiable object.
 * 
 * @param <K>
 *          the identifier that will be sorted against
 * @param <V>
 *          the identifiable type
 */
public class IdentifiableMap<K extends Comparable<K> & Serializable, V extends Identifiable<K>>
    implements Serializable {
  private static final long serialVersionUID = -8007512760928250095L;
  private final Map<K, V> map;

  public IdentifiableMap() {
    this.map = new TreeMap<>();
  }

  /**
   * Gets a object by an ID.
   * 
   * @param id
   *          the ID to check.
   * @return the corresponding object.
   */
  public V getById(K id) {
    return map.get(id);
  }

  /**
   * Gets all sorted entries in the database.
   * 
   * @return a collection of all the entries
   */
  public Set<Entry<K, V>> getEntries() {
    return map.entrySet();
  }

  /**
   * Gets all objects in the database.
   * 
   * @return a collection of all the objects
   */
  public Collection<V> getValues() {
    return map.values();
  }

  /**
   * Checks if the ID exists.
   * 
   * @param id
   *          the ID to check.
   * @return True iff the ID exists in the database.
   */
  public boolean containsId(K id) {
    return map.containsKey(id);
  }

  /**
   * Adds info to this database. Returns the object that was replaced.
   * 
   * @param val
   *          the object to add to this database.
   * @return the old value that was in this database; null if none was there
   */
  public V add(V val) {
    V oldObject = map.get(val.getIdentifier());
    map.put(val.getIdentifier(), val);
    return oldObject;
  }

  /**
   * Registers multiple objects.
   * 
   * @param toAdd
   *          The objects to add.
   */
  public void addAll(Collection<V> toAdd) {
    for (V add : toAdd) {
      add(add);
    }
  }

  /**
   * Get an object of a specific id.
   * 
   * @param id
   *          the id of the object
   * @return the object
   */
  public V get(K id) {
    return map.get(id);
  }

  /**
   * Clears all information from this database.
   */
  public void clear() {
    map.clear();
  }
}
