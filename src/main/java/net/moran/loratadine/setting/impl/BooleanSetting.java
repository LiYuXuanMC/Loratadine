package net.moran.loratadine.setting.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.setting.HideIf;
import net.moran.loratadine.setting.Setting;

public class BooleanSetting extends Setting<Boolean> {
   public BooleanSetting(String name, Module present, Boolean value, HideIf hideIf) {
      super(name, present, value, hideIf);
   }

   public BooleanSetting(String name, Module present, Boolean value) {
      this(name, present, value, new HideIf() {
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
      this.setValue(Boolean.valueOf(element.getAsBoolean()));
   }
}
