package io;

import database.MainDatabase;

import travel.Itinerary;
import travel.SingleTravel;
import travel.TravelType;

import users.RegisteredUser;

import util.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A class for internally loading and saving progress.
 */
public class SaveOperations {
  private static final Logger log = Logger.getLogger(SaveOperations.class.getName());

  /**
   * Gets the encryption cipher for the file.
   * 
   * @param salt
   *          the salt to use when creating the key
   * @param iv
   *          the initialization vector to start with; if null, the cipher is in
   *          encrypt mode and generates a random IV. otherwise, the cipher is
   *          in decrypt mode with the given IV.
   * @return the encryption cipher
   * @throws GeneralSecurityException
   *           when the padding or algorithm does not exist or is invalid
   * @throws IOException
   *           when the encoding is invalid
   */
  private static Cipher getEncryptionCipher(byte[] salt, byte[] iv)
      throws GeneralSecurityException, IOException {

    // key is a static password + random salt, iv is random
    byte[] password = InputOperations
        .cryptPassword(Constants.CIPHER_PASSWORD + new String(salt, Constants.FILE_ENCODING));
    SecretKey secretKey = new SecretKeySpec(password, Constants.CIPHER);

    Cipher cipher = Cipher.getInstance(Constants.CIPHER_PADDING);
    if (iv == null) {
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    } else {
      cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
    }
    return cipher;
  }

  /**
   * Generates a random salt for the encryption cipher.
   * 
   * @return a byte array of the salt
   */
  private static byte[] generateSalt() {
    byte[] salt = new byte[Constants.CIPHER_SALT_LENGTH];
    SecureRandom rand = new SecureRandom();
    rand.nextBytes(salt);
    return salt;
  }

  /**
   * Serializes the database to a file.
   * 
   * @param data
   *          the database to serialize
   */
  public static void serializeDatabase(MainDatabase data) {
    // truncate the file by default.
    try (FileOutputStream out = new FileOutputStream(Constants.SAVE_FILE);
        BufferedOutputStream writer = new BufferedOutputStream(out)) {

      byte[] salt = generateSalt();
      Cipher enc = getEncryptionCipher(salt, null);
      // un-encrypted header (necessary information for decryption)
      writer.write(salt.length);
      writer.write(salt);
      writer.write(enc.getIV().length);
      writer.write(enc.getIV());

      try (CipherOutputStream cos = new CipherOutputStream(writer, enc);
          ObjectOutputStream oos = new ObjectOutputStream(cos)) {

        // if we need any settings, write them here

        // write the travel database
        oos.writeByte(TravelType.values().length);
        for (TravelType tt : TravelType.values()) {
          // we don't directly serialize the collection because
          // MainDatabase indexes the origins
          // so each SingleTravel must be added one by one on reading
          Collection<SingleTravel> travels = data.getAllTravels(tt);
          oos.writeInt(travels.size());
          for (SingleTravel st : travels) {
            oos.writeObject(st);
          }
        }

        // write the user database
        oos.writeInt(data.getAllUsers().size());
        for (RegisteredUser ru : data.getAllUsers()) {
          // we don't want to directly serialize the itineraries because we want
          // to strictly check them to make sure they're valid
          // plus, we can use the existing TravelDatabase to avoid conflicts
          // with equals() and hashCode() for itineraries
          oos.writeObject(ru);

          oos.writeInt(ru.getBookedItineraries().size());
          for (Itinerary it : ru.getBookedItineraries()) {
            oos.writeInt(it.size());
            for (SingleTravel st : it.getTravels()) {
              oos.writeByte(st.getType().ordinal());
              oos.writeUTF(st.getIdentifier());
            }
          }
        }
      }
    } catch (IOException | GeneralSecurityException e) {
      log.log(Level.SEVERE, "Error saving to file.", e);
    }
  }

  /**
   * Deserializes the database from a file.
   * 
   * @return a new MainDatabase using the data from the save file
   */
  public static MainDatabase deserializeDatabase() {
    MainDatabase data = new MainDatabase();
    // first check if the file exists
    File file = new File(Constants.SAVE_FILE);
    if (!file.exists()) {
      return data;
    }
    try (FileInputStream in = new FileInputStream(Constants.SAVE_FILE);
        BufferedInputStream reader = new BufferedInputStream(in)) {

      // un-encrypted header (salt and iv necessary for decryption)
      byte[] salt = new byte[reader.read()];
      reader.read(salt);
      byte[] iv = new byte[reader.read()];
      reader.read(iv);
      Cipher enc = getEncryptionCipher(salt, iv);

      try (CipherInputStream cis = new CipherInputStream(reader, enc);
          ObjectInputStream ois = new ObjectInputStream(cis)) {

        // if we need any settings, read them here

        // read the travel database
        int count = ois.readByte();
        for (int i = 0; i < count; i++) {
          int size = ois.readInt();
          for (int j = 0; j < size; j++) {
            data.addTravel((SingleTravel) ois.readObject());
          }
        }

        // read the user database
        count = ois.readInt();
        for (int i = 0; i < count; i++) {
          RegisteredUser ru = (RegisteredUser) ois.readObject();
          data.addUser(ru);

          int size = ois.readInt();
          for (int j = 0; j < size; j++) {
            int itSize = ois.readInt();
            // check the itinerary to make sure it is still valid
            boolean validItinerary = (itSize > 0);
            Itinerary it = new Itinerary();
            for (int k = 0; k < itSize; k++) {
              try {
                SingleTravel st = data.getTravel(TravelType.values()[ois.readByte()],
                    ois.readUTF());
                if (st == null) { // doesn't exist; do not add itinerary
                  validItinerary = false;
                } else {
                  it.add(st);
                }
              } catch (IllegalArgumentException e) {
                validItinerary = false;
                // we don't need to log this -- probably a travel expired
              }
            }
            if (validItinerary) {
              ru.bookItinerary(it);
            }
          }
        }
      }
    } catch (IOException | GeneralSecurityException | ClassNotFoundException e) {
      log.log(Level.SEVERE, "Error reading from file.", e);
    }
    return data;
  }
}
