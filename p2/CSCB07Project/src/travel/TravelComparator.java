package travel;

import java.util.Collections;
import java.util.Comparator;

/**
 * An enum for comparing Travels. Provides multiple forms of comparisons.
 */
public enum TravelComparator implements Comparator<Travel> {
  Cost {
    @Override
    public int compare(Travel o1, Travel o2) {
      return Double.valueOf(o1.getCost()).compareTo(o2.getCost());
    }
  },
  Time {
    @Override
    public int compare(Travel o1, Travel o2) {
      return Long.valueOf(o1.getTravelTime()).compareTo(o2.getTravelTime());
    }
  };

  /**
   * Returns a Comparator of this type but in descending order.
   * 
   * @return a Comparator of this type but in reverse order
   */
  public Comparator<Travel> reverse() {
    return Collections.reverseOrder(this);
  }
}
