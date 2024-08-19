package net.moran.loratadine.utils.unsafe;

import java.lang.reflect.Field;
import lombok.Generated;
import sun.misc.Unsafe;

public class UnsafeUtils {
   private static final Unsafe unsafe;

   private static Unsafe getUnsafeInstance() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
      Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
      Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
      theUnsafeField.setAccessible(true);
      return (Unsafe)theUnsafeField.get(null);
   }

   public static long allocateMemory(long size) {
      return unsafe.allocateMemory(size);
   }

   public static void freeMemory(long address) {
      unsafe.freeMemory(address);
   }

   public static void putLong(long address, long value) {
      unsafe.putLong(address, value);
   }

   public static void putInt(long address, int value) {
      unsafe.putInt(address, value);
   }

   public static void putInt(Object object, long offset, int value) {
      unsafe.putInt(object, offset, value);
   }

   public static int getInt(long address) {
      return unsafe.getInt(address);
   }

   public static int getInt(Object object, long offset) {
      return unsafe.getInt(object, offset);
   }

   public static long getLong(long address) {
      return unsafe.getLong(address);
   }

   public static Object getObject(Object object, long offset) {
      return unsafe.getObject(object, offset);
   }

   public static void putObject(Object object, long offset, Object value) {
      unsafe.putObject(object, offset, value);
   }

   public static long getFieldOffset(Field field) {
      return unsafe.objectFieldOffset(field);
   }

   public static Object createInstance(Class<?> clazz) throws InstantiationException {
      return unsafe.allocateInstance(clazz);
   }

   public static void loadClass(Class<?> clazz) {
      unsafe.ensureClassInitialized(clazz);
   }

   public static long getArrayBaseOffset(Class<?> clazz) {
      return (long)unsafe.arrayBaseOffset(clazz);
   }

   public static long getArrayIndexScale(Class<?> clazz) {
      return (long)unsafe.arrayIndexScale(clazz);
   }

   @Generated
   public static Unsafe getUnsafe() {
      return unsafe;
   }

   static {
      try {
         unsafe = getUnsafeInstance();
         if (unsafe == null) {
            throw new RuntimeException("Unable to get Unsafe instance");
         }
      } catch (Exception var1) {
         throw new RuntimeException("Unable to get Unsafe instance", var1);
      }
   }
}
