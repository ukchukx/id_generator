class ID {
  generate(resourceType) {
    const typeBits = this.padBits(Number(resourceType).toString(2));
    const tsBits = this.padBits(new Date().getTime().toString(2), 48);
    const unused = this.padBits(Math.floor((1 + Math.random()) * 0x10).toString(2));
    return Number.parseInt(`${typeBits}${tsBits}${unused}`, 2);
  }

  padBits(bitStr, length = 8) {
    if (bitStr.length >= length) {
      return bitStr;
    }

    let pad = "";
    for(let p = 1; p <= (length - bitStr.length); ++p) pad += "0";

    return `${pad}${bitStr}`;
  }

  toString(id) {
    return id.toString(16);
  }

  fromString(idStr) {
    return Number.parseInt(idStr, 16);
  }
}
