class IDGenerator(object):
  """
  Generate a 64-bit UUID having the format:
  type_code(8 bits)|timestamp(48 bits)|unused(8 bits)
  By including the creation time in the id, there's no need for a created_at column in the DB table.
  """

  @staticmethod
  def generate(type_code):
    """
    Generate a 64-bit id.
    Type code is a 2-byte integer common to a resource type.
    E.g 10 for users, 20 for roles etc
    @param type_code integer
    """
    from datetime import datetime
    from math import floor
    from uuid import uuid4

    # Get the binary representation of the current timestamp (in milliseconds)
    # ...[2:] is to remove the '0b' Python tacks on
    ts_bits = IDGenerator.pad_bits(bin(floor(datetime.timestamp(datetime.utcnow()) * 1000))[2:], 48)

    _uuid = bin(uuid4().fields[2])[2:] # Get the last 16-bits of the first 64-bit chunk
     # We're only interested in the last 8-bits
    unused_bits = IDGenerator.pad_bits(_uuid[7:]) if len(_uuid) == 15 else IDGenerator.pad_bits(_uuid[8:])

    type_code_bits = IDGenerator.pad_bits(bin(type_code)[2:])

    return int(type_code_bits + ts_bits + unused_bits, 2)


  @staticmethod
  def to_string(id):
    """
    Convert 64-bit to hex string.
    """
    return hex(id)[2:] # ...without '0x'


  @staticmethod
  def from_string(bit_str):
    """
    Convert hex string to 64-bit id.
    """
    return int(bit_str, 16)


  @staticmethod
  def pad_bits(bit_str, length=8):
    """
    Pad bit string with leading zeros. Be sure to remove '0b' from string
    """
    format_str = '{:0>' + str(length) + '}'
    return format_str.format(bit_str) # Only return the last length-bits
