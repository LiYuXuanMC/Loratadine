package net.moran.loratadine.setting.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Generated;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.setting.HideIf;
import net.moran.loratadine.setting.Setting;

public class ModeSetting extends Setting<String> {
   private final String[] values;

   public ModeSetting(String name, Module present, String[] values, String value, HideIf hideIf) {
      super(name, present, value, hideIf);
      this.values = values;
   }

   public ModeSetting(String name, Module present, String[] values, String value) {
      this(name, present, values, value, new HideIf() {
         @Override
         public boolean hide() {
            return false;
         }
      });
   }

   @Override
   public void toJson(JsonObject object) {
      object.addProperty(this.getName(), this.getValue());
   }

   @Override
   public void formJson(JsonElement element) {
      this.setValue(element.getAsString());
   }

   @Generated
   public String[] getValues() {
      return this.values;
   }
}
