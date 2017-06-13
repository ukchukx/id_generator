<?php

/**
 * Generate a 64-bit UUID having the format:
 * type_code(8 bits)|timestamp(48 bits)|unused(8 bits)
 * By including the creation time in the id, there's no need for a created_at column in the DB table.
 * @author Uk
 */
class ID {

  /**
   * Generate a 64-bit id.
   * Type code is a 1-byte integer common to a resource type.
   * E.g 10 for users, 20 for roles etc
   * @param $typeCode integer
   * @return integer
   */
  public static function generate(int $typeCode) {
    $unusedBits = self::padBits(base_convert(bin2hex(random_bytes(1)), 16, 2), 8);
    $tsBits = self::padBits(base_convert(time() * 1000, 10, 2), 48); // We want timestamp in milliseconds
    $typeCodeBits = self::padBits(base_convert($typeCode, 10, 2));
    return base_convert($typeCodeBits . $tsBits . $unusedBits, 2, 10);
  }


  /**
   * Convert 64-bit id to hex string.
   * @param $id integer
   * @return string
   */
  public static function toString(int $id) {
    return base_convert($id, 10, 16);
  }

  /**
   * Convert hex string to 64-bit id.
   * @param $str string
   * @return integer
   */
  public static function fromString(string $str) {
    return base_convert($str, 16, 10);
  }

  /**
   * Pad bit string with leading zeros.
   * @param $binString string
   * @param int $length integer
   * @return string
   */
  public static function padBits($binString, $length=8) {
    return str_pad($binString, $length, '0', STR_PAD_LEFT);
  }
}
