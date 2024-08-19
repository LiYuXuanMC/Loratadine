package net.moran.loratadine.utils;

import com.google.gson.JsonObject;

public class JsonUtil {
   private final JsonObject object;

   public JsonUtil(JsonObject object) {
      this.object = object;
   }

   public String getString(String name, String defaultValue) {
      return this.object.has(name) ? this.object.get(name).getAsString() : defaultValue;
   }

   public long getLong(String name, long defaultValue) {
      return this.object.has(name) ? this.object.get(name).getAsLong() : defaultValue;
   }

   public boolean getBoolean(String name, boolean defaultValue) {
      return this.object.has(name) ? this.object.get(name).getAsBoolean() : defaultValue;
   }

   public int getInt(String name, int defaultValue) {
      return this.object.has(name) ? this.object.get(name).getAsInt() : defaultValue;
   }
}
