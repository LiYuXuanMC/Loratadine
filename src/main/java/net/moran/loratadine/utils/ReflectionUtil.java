package net.moran.loratadine.utils;

import java.lang.reflect.Field;
import net.minecraft.network.chat.TextComponent;

public class ReflectionUtil {
   public static Field textComponentText;

   public static void init() {
      for (Field field : TextComponent.class.getDeclaredFields()) {
         if (field.getType() == String.class) {
            textComponentText = field;
         }
      }
   }

   public static void set(Field field, Object ins, Object obj) {
      try {
         boolean canAccess = field.canAccess(ins);
         if (!canAccess) {
            field.setAccessible(true);
         }

         field.set(ins, obj);
         if (!canAccess) {
            field.setAccessible(false);
         }
      } catch (Throwable var4) {
      }
   }
}
