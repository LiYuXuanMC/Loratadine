package net.moran.loratadine.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Generated;
import net.moran.loratadine.modules.Module;

public abstract class Setting<T> {
   protected final String name;
   protected Module present;
   protected T value;
   protected HideIf hideIf;

   public Setting(String name, Module present, T value, HideIf hideIf) {
      this.name = name;
      this.present = present;
      present.getSettings().add(this);
      this.value = value;
      this.hideIf = hideIf;
   }

   public abstract void toJson(JsonObject var1);

   public abstract void formJson(JsonElement var1);

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public Module getPresent() {
      return this.present;
   }

   @Generated
   public T getValue() {
      return this.value;
   }

   @Generated
   public HideIf getHideIf() {
      return this.hideIf;
   }

   @Generated
   public void setValue(T value) {
      this.value = value;
   }
}
