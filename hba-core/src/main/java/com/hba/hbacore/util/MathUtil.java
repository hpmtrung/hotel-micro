package com.hba.hbacore.util;

public final class MathUtil {
  private MathUtil() {}

  public static int roundHalfThousand(double value) {
    return (int) (Math.round(value / 500.0) * 500);
  }
}