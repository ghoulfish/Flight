package users;

import travel.Itinerary;
import travel.SingleTravel;

import util.TimeFormat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An abstract class for registered users (in the database).
 */

public abstract class RegisteredUser implements User {
  private static final long serialVersionUID = -2465596487306282569L;
  private String email;
  private final UserType privilege;
  private String password;

  private transient Set<Itinerary> booked;
  private String firstNames;
  private String lastName;
  private String address;

  private String creditCard;
  private Date expiryDate;

  /**
   * Creates a RegisteredUser with the given information.
   * 
   * @param email
   *          The email of this user.
   * @param privilege
   *          The privilege level of this user.
   * @param firstNames
   *          The first names of this user.
   * @param lastName
   *          The last name of this user.
   * @param address
   *          The address of this user.
   * @param creditCard
   *          The credit card number of this user.
   * @param expiryDate
   *          The expiry date of the credit card.
   */
  public RegisteredUser(String email, UserType privilege, String firstNames, String lastName,
      String address, String creditCard, Date expiryDate) {
    this.email = email;
    this.privilege = privilege;
    this.firstNames = firstNames;
    this.lastName = lastName;
    this.address = address;
    this.creditCard = creditCard;
    this.expiryDate = expiryDate;
    this.password = ""; // no password by default

    this.booked = new LinkedHashSet<>();
  }

  @Override
  public String getIdentifier() {
    return email;
  }

  @Override
  public void setIdentifier(String email) {
    this.email = email;
  }

  @Override
  public boolean hasPrivilege(int check) {
    return privilege.hasPrivilege(check);
  }

  @Override
  public UserType getType() {
    return privilege;
  }

  /**
   * Gets the password for this user, if there is one.
   * 
   * @return the password; empty string if there is no password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets a password for this user.
   * 
   * @param password
   *          the password to set; empty string removes the password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Gets the booked itineraries of this user.
   * 
   * @return the booked itineraries
   */
  public Set<Itinerary> getBookedItineraries() {
    return booked;
  }

  /**
   * Books an itinerary for this user.
   * 
   * @param it
   *          the itinerary to book.
   */
  public void bookItinerary(Itinerary it) {
    booked.add(it);
  }

  /**
   * Removes an itinerary for this user.
   * 
   * @param it
   *          the itinerary to remove.
   */
  public void removeItinerary(Itinerary it) {
    booked.remove(it);
  }

  /**
   * Removes any itineraries with the specified travel.
   * 
   * @param st
   *          the travel to remove
   */
  public void removeTravel(SingleTravel st) {
    Iterator<Itinerary> itinIterator = booked.iterator();
    while (itinIterator.hasNext()) {
      Itinerary it = itinIterator.next();
      if (it.containsTravel(st)) {
        itinIterator.remove();
      }
    }
  }

  /**
   * Gets the first names of this user.
   * 
   * @return the firstNames
   */
  public String getFirstNames() {
    return firstNames;
  }

  /**
   * Sets the first names of this user.
   * 
   * @param firstNames
   *          the firstNames to set
   */
  public void setFirstNames(String firstNames) {
    this.firstNames = firstNames;
  }

  /**
   * Gets the last name of this user.
   * 
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of this user.
   * 
   * @param lastName
   *          the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets the address of this user.
   * 
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address of this user.
   * 
   * @param address
   *          the address to set
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Gets the credit card of this user.
   * 
   * @return the creditCard
   */
  public String getCreditCard() {
    return creditCard;
  }

  /**
   * Sets the credit card of this user.
   * 
   * @param creditCard
   *          the creditCard to set
   */
  public void setCreditCard(String creditCard) {
    this.creditCard = creditCard;
  }

  /**
   * Gets the expiry date of this user.
   * 
   * @return the expiryDate as a string
   */
  public String getExpiryDate() {
    return TimeFormat.DATE.formatDate(expiryDate);
  }

  /**
   * Sets the expiry date of this user.
   * 
   * @param expiryDate
   *          the expiryDate to set
   */
  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  @Override
  public String toString() {
    // LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
    return String.format("%s,%s,%s,%s,%s,%s", getLastName(), getFirstNames(), getIdentifier(),
        getAddress(), getCreditCard(), getExpiryDate());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + address.hashCode();
    result = prime * result + creditCard.hashCode();
    result = prime * result + email.hashCode();
    result = prime * result + expiryDate.hashCode();
    result = prime * result + firstNames.hashCode();
    result = prime * result + lastName.hashCode();
    result = prime * result + password.hashCode();
    result = prime * result + privilege.hashCode();
    return result;
  }

  /**
   * Provides a custom reading method for de-serializing.
   * 
   * @param ois
   *          the stream to read this object
   * @throws IOException
   *           if there is an error in reading
   * @throws ClassNotFoundException
   *           if a class is not found
   */
  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    // read all non-transient fields
    ois.defaultReadObject();

    // booked is transient because we want to do itinerary reading ourselves
    this.booked = new LinkedHashSet<>();

    // booked is read by the SaveOperations class
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    RegisteredUser other = (RegisteredUser) obj;
    if (!address.equals(other.address) || !booked.equals(other.booked)
        || !creditCard.equals(other.creditCard) || !email.equals(other.email)
        || !expiryDate.equals(other.expiryDate) || !firstNames.equals(other.firstNames)
        || !lastName.equals(other.lastName) || !password.equals(other.password)
        || privilege != other.privilege) {
      return false;
    }
    return true;
  }
}
