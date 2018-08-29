package util;

/**
 * A class for listing any miscellaneous constants.
 */
public class Constants {

  // maximum amount of stopover between travels, in milliseconds
  public static final long MAX_STOPOVER = 6 * 60 * 60 * 1000L;

  // the encoding of files
  public static final String FILE_ENCODING = "US-ASCII";

  // hashing: MD5 (insecure), SHA-256, SHA-1 (insecure), or empty string
  public static final String PASS_ENCODING = "SHA-256";

  // for internally saving and loading
  public static final String SAVE_FILE = "T0003-1.sav";

  // the ciphers for internally saving and loading
  public static final String CIPHER_PASSWORD = "CSCB07H3F-2015-ProjectP2-T0003-1";
  public static final String CIPHER = "AES"; // DES is insecure
  public static final String CIPHER_PADDING = "AES/CBC/PKCS5Padding";
  public static final byte CIPHER_IV_LENGTH = 16; // should not be changed
  public static final byte CIPHER_SALT_LENGTH = 16; // recommended minimum 8

}
