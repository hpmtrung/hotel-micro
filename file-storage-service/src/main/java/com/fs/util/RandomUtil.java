package com.fs.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public final class RandomUtil {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  static {
    SECURE_RANDOM.nextBytes(new byte[64]);
  }

  public static byte[] generateSalt(int byteSize) {
    byte[] iv = new byte[byteSize];
    SECURE_RANDOM.nextBytes(iv);
    return iv;
  }

  private RandomUtil() {}

  public static String generateAlphanumeric(int length) {
    return RandomStringUtils.random(length, 0, 0, true, true, null, SECURE_RANDOM);
  }

}