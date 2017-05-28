import java.util.Date;
import java.util.UUID;
/**
 * Generate a 64-bit UUID having the format:
 * type_code(8 bits)|timestamp(48 bits)|unused(8 bits)
 * By including the creation time in the id, there's no need for a created_at column in the DB table.
 * @author Uk
 */
public class IDGenerator {

  /**
   * Generate a 64-bit id.
   * Type code is a 2-byte integer common to a resource type.
   * E.g 10 for users, 20 for roles etc
   * @param $typeCode integer
   * @return integer
   */
  public static long generate(int typeCode) {
    String typeCodeBits = padString(Integer.toUnsignedString(typeCode, 2), 8);
    String tsBits = padString(Long.toUnsignedString(new Date().getTime(), 2), 48);

    String uuid = UUID.randomUUID().toString().split("-")[4];
    uuid = uuid.replace(uuid.substring(0, 10), "").trim(); // We only want last 8 bits
    String uuidBits = padString(Long.toUnsignedString(Long.parseUnsignedLong(uuid, 16), 2), 8);

    return Long.parseUnsignedLong(String.format("%s%s%s", typeCodeBits, tsBits, uuidBits), 2);
  }

  /**
   * Convert 64-bit id to hex string.
   * @param $id integer
   * @return string
   */
  public static String toString(long id) {
    return Long.toUnsignedString(id, 16);
  }

  /**
   * Convert hex string to 64-bit id.
   * @param $str string
   * @return integer
   */
  public static long fromString(String str) {
    return Long.parseUnsignedLong(str, 16);
  }

  /**
   * Pad bit string with leading zeros.
   * @param $binString string
   * @param int $length integer
   * @return string
   */
  public static String padString(String bitString, int length) {
    if (bitString.length() >= length) {
      return bitString;
    }

    String pad = "";
    for(int p = 1; p <= (length - bitString.length()); ++p) pad += "0";

    return String.format("%s%s", pad, bitString);
  }
}