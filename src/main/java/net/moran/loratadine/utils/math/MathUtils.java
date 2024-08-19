package net.moran.loratadine.utils.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Random;
import lombok.Generated;

public final class MathUtils {
   public static final DecimalFormat DF_0 = new DecimalFormat("0");
   public static final DecimalFormat DF_1 = new DecimalFormat("0.0");
   public static final DecimalFormat DF_2 = new DecimalFormat("0.00");
   public static final float PI = (float) Math.PI;
   public static final float TO_RADIANS = (float) (Math.PI / 180.0);
   public static final float TO_DEGREES = 180.0F / (float)Math.PI;

   public static boolean approximatelyEquals(float a, float b) {
      return Math.abs(b - a) < 1.0E-5F;
   }

   public static boolean approximatelyEquals(double a, double b) {
      return Math.abs(b - a) < 1.0E-5F;
   }

   public static double roundToPlace(double value, int place) {
      if (place < 0) {
         return value;
      } else {
         BigDecimal bd = new BigDecimal(value);
         bd = bd.setScale(place, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   public static float clamp2(float num, float min, float max) {
      return num < min ? min : Math.min(num, max);
   }

   public static Double clamp(double num, double min, double max) {
      return Math.min(Math.max(num, min), max);
   }

   public static double getRandomInRange(double min, double max) {
      SecureRandom random = new SecureRandom();
      return min == max ? min : random.nextDouble() * (max - min) + min;
   }

   public static int getRandomNumberUsingNextInt(int min, int max) {
      Random random = new Random();
      return random.nextInt(max - min) + min;
   }

   public static double lerp(double old, double newVal, double amount) {
      return (1.0 - amount) * old + amount * newVal;
   }

   public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
      return oldValue + (newValue - oldValue) * interpolationValue;
   }

   public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
      return interpolate((double)oldValue, (double)newValue, (double)((float)interpolationValue)).floatValue();
   }

   public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
      return interpolate((double)oldValue, (double)newValue, (double)((float)interpolationValue)).intValue();
   }

   public static float calculateGaussianValue(float x, float sigma) {
      double output = 1.0 / Math.sqrt((Math.PI * 2) * (double)(sigma * sigma));
      return (float)(output * Math.exp((double)(-(x * x)) / (2.0 * (double)(sigma * sigma))));
   }

   public static double roundToHalf(double d) {
      return (double)Math.round(d * 2.0) / 2.0;
   }

   public static double round(double num, double increment) {
      BigDecimal bd = new BigDecimal(num);
      bd = bd.setScale((int)increment, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }

   public static double round(double value, int places) {
      if (places < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = new BigDecimal(value);
         bd = bd.setScale(places, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   public static String round(String value, int places) {
      if (places < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = new BigDecimal(value);
         bd = bd.stripTrailingZeros();
         bd = bd.setScale(places, RoundingMode.HALF_UP);
         return bd.toString();
      }
   }

   public static float getRandomFloat(float max, float min) {
      SecureRandom random = new SecureRandom();
      return random.nextFloat() * (max - min) + min;
   }

   public static int getNumberOfDecimalPlace(double value) {
      BigDecimal bigDecimal = new BigDecimal(value);
      return Math.max(0, bigDecimal.stripTrailingZeros().scale());
   }

   public static boolean equals(float a, float b) {
      return (double)Math.abs(a - b) < 1.0E-4;
   }

   @Generated
   private MathUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
