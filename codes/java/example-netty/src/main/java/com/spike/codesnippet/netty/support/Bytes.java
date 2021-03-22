package com.spike.codesnippet.netty.support;

import io.netty.util.CharsetUtil;

import java.math.BigInteger;

import com.google.common.base.Preconditions;

/**
 * @author zhoujiagen
 * @see com.google.common.primitives.Bytes
 */
public class Bytes {
  // ======================================== properties

  // ======================================== methods

  public static byte[] repeats(byte value, int count) {
    Preconditions.checkArgument(count > 0);

    byte[] result = new byte[count];

    for (int i = 0; i < count; i++) {
      result[i] = value;
    }

    return result;
  }

  public static byte[] subSet(byte[] bytes, int offset, int length) {
    Preconditions.checkArgument(offset >= 0, "offset must great than or equal to 0");
    Preconditions.checkArgument(length > 0, "length must great than 0");
    Preconditions.checkArgument(offset + length <= bytes.length,
      "out of index boundary: %s + %s > %s", offset, length, bytes.length);

    byte[] result = new byte[length];
    System.arraycopy(bytes, offset, result, 0, length);
    return result;
  }

  /**
   * 获取字符串的字节表示
   * @param message
   * @return
   */
  public static byte[] wrap(String message) {
    return message.getBytes(CharsetUtil.UTF_8);
  }

  /**
   * 获取字符的字节表示
   * @param message
   * @return
   */
  public static byte wrap(char c) {
    return ((byte) c);
  }

  public static String string(byte[] bytes, int offset, int length) {
    return new String(subSet(bytes, offset, length));
  }

  public static String string(byte[] bytes) {
    // char[] char = new char[bytes.length];
    return new String(bytes);
  }

  public static byte[] xor(byte[] first, byte[] second) {
    Preconditions.checkArgument(first.length == second.length);

    // int length = first.length;
    // byte[] result = new byte[length];
    // for (int i = 0; i < length; i++) {
    // result[i] = (byte) (first[i] ^ second[i]);
    // }
    //
    // return result;
    BigInteger firstBigInteger = new BigInteger(first);
    BigInteger seconBigInteger = new BigInteger(second);
    return firstBigInteger.xor(seconBigInteger).toByteArray();
  }

  public static byte[] lengthEncoded(byte[] payload) {
    return com.google.common.primitives.Bytes.concat(new byte[] { (byte) payload.length }, payload);
  }

  public static byte[] nullEndString(String payload) {
    return com.google.common.primitives.Bytes.concat(payload.getBytes(), new byte[] { 0x00 });
  }

}
