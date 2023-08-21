package com.fs.services;

import com.fs.util.RandomUtil;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static javax.crypto.Cipher.ENCRYPT_MODE;

public class BlockEncryptor {

  private static final int SALT_BYTES = 12;
  private static final int AES_KEY_LENGTH = 128; // AES-128
  private static final String TRANSFORMATION = "AES/GCM/NoPadding";
  private final char[] aesKey;
  private final SecretKeyFactory secretKeyFactory;

  public BlockEncryptor(String aesKey) {
    this.aesKey = aesKey.toCharArray();
    try {
      this.secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    } catch (NoSuchAlgorithmException e) {
      throw new BlockEncryptorException("Fail to initiate block encryptor.", e);
    }
  }

  public byte[] encrypt(byte[] data) {
    try {
      byte[] salt = RandomUtil.generateSalt(SALT_BYTES);
      GCMParameterSpec parameterSpec = new GCMParameterSpec(AES_KEY_LENGTH, salt);
      SecretKey secretKey = generateSecretKey(aesKey, salt);
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(ENCRYPT_MODE, secretKey, parameterSpec);
      byte[] encrypted = cipher.doFinal(data);
      ByteBuffer byteBuffer = ByteBuffer.allocate(SALT_BYTES + encrypted.length);
      byteBuffer.put(salt);
      byteBuffer.put(encrypted);
      return byteBuffer.array();
    } catch (InvalidKeyException
        | InvalidAlgorithmParameterException
        | IllegalBlockSizeException
        | BadPaddingException
        | InvalidKeySpecException
        | NoSuchPaddingException
        | NoSuchAlgorithmException e) {
      throw new BlockEncryptorException("Fail to encrypt data.", e);
    }
  }

  public byte[] decrypt(byte[] data) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
    byte[] salt = new byte[SALT_BYTES];
    byteBuffer.get(salt);
    byte[] encrypted = new byte[byteBuffer.remaining()];
    byteBuffer.get(encrypted);
    try {
      SecretKey secretKey = generateSecretKey(aesKey, salt);
      GCMParameterSpec parameterSpec = new GCMParameterSpec(AES_KEY_LENGTH, salt);
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
      return cipher.doFinal(encrypted);
    } catch (InvalidKeySpecException
        | InvalidKeyException
        | InvalidAlgorithmParameterException
        | IllegalBlockSizeException
        | BadPaddingException
        | NoSuchPaddingException
        | NoSuchAlgorithmException e) {
      throw new BlockEncryptorException("Fail to decrypt data.", e);
    }
  }

  private SecretKey generateSecretKey(final char[] password, byte[] salt)
      throws InvalidKeySpecException {
    KeySpec spec = new PBEKeySpec(password, salt, 65536, AES_KEY_LENGTH);
    byte[] key = secretKeyFactory.generateSecret(spec).getEncoded();
    return new SecretKeySpec(key, "AES");
  }

  static class BlockEncryptorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BlockEncryptorException(String message) {
      super(message);
    }

    public BlockEncryptorException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}