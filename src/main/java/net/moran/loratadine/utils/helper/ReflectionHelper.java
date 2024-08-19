package net.moran.loratadine.utils.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.moran.loratadine.utils.unsafe.UnsafeUtils;
import sun.misc.Unsafe;

public class ReflectionHelper {
   public static Field findField(Class<?> clazz, String... fieldNames) {
      if (clazz != null && fieldNames != null && fieldNames.length != 0) {
         Exception failed = null;

         for (Class<?> currentClass = clazz; currentClass != null; currentClass = currentClass.getSuperclass()) {
            for (String fieldName : fieldNames) {
               if (fieldName != null) {
                  try {
                     Field f = currentClass.getDeclaredField(fieldName);
                     f.setAccessible(true);
                     UnsafeUtils.putInt(f, (long)Unsafe.ARRAY_BOOLEAN_BASE_OFFSET, f.getModifiers() & -17);
                     return f;
                  } catch (Exception var9) {
                     failed = var9;
                  }
               }
            }
         }

         throw new UnableToFindFieldException(failed);
      } else {
         throw new IllegalArgumentException("Class and fieldNames must not be null or empty");
      }
   }

   public static Method findMethod(Class<?> clazz, String obfName, String deobfName, Class<?>... parameterTypes) {
      if (clazz != null && (obfName != null || deobfName != null)) {
         Exception failed = null;

         for (Class<?> currentClass = clazz; currentClass != null; currentClass = currentClass.getSuperclass()) {
            try {
               Method m = null;
               if (obfName != null) {
                  try {
                     m = currentClass.getDeclaredMethod(obfName, parameterTypes);
                  } catch (NoSuchMethodException var8) {
                  }
               }

               if (m == null && deobfName != null) {
                  m = currentClass.getDeclaredMethod(deobfName, parameterTypes);
               }

               if (m != null) {
                  m.setAccessible(true);
                  UnsafeUtils.putInt(m, (long)Unsafe.ARRAY_BOOLEAN_BASE_OFFSET, m.getModifiers() & -17);
                  return m;
               }
            } catch (Exception var9) {
               failed = var9;
            }
         }

         throw new UnableToFindMethodException(failed);
      } else {
         throw new IllegalArgumentException("Class and at least one method name must not be null");
      }
   }

   private static class UnableToFindFieldException extends RuntimeException {
      private static final long serialVersionUID = 1L;

      public UnableToFindFieldException(Exception e) {
         super(e);
      }
   }

   private static class UnableToFindMethodException extends RuntimeException {
      private static final long serialVersionUID = 1L;

      public UnableToFindMethodException(Exception e) {
         super(e);
      }
   }
}
