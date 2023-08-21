package com.fs.services;

import com.fs.util.ByteUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class BlockEncryptorTest {

  BlockEncryptor encryptor = new BlockEncryptor("secret");

  @Test
  void testEncryptAndDecryptCorrect() {
    String data = "hello";
    byte[] encrypted = encryptor.encrypt(data.getBytes(StandardCharsets.UTF_8));
    byte[] decrypted = encryptor.decrypt(encrypted);
    assertEquals(data, new String(decrypted, StandardCharsets.UTF_8));
    assertNotEquals(data, new String("dweoi".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
  }

}