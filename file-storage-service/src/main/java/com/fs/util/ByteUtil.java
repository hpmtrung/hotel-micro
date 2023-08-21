package com.fs.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public class ByteUtil {

  private ByteUtil() {}

  public static byte[] compress(byte[] in) throws IOException {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        DeflaterOutputStream defl = new DeflaterOutputStream(out)) {
      defl.write(in);
      defl.flush();
      defl.finish();
      return out.toByteArray();
    }
  }

  public static byte[] decompress(byte[] in) throws IOException {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        InflaterOutputStream infl = new InflaterOutputStream(out)) {
      infl.write(in);
      infl.flush();
      infl.finish();
      return out.toByteArray();
    }
  }

  public static String convertBytesToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    for (byte temp : bytes) {
      result.append(String.format("%02x", temp));
    }
    return result.toString();
  }
}