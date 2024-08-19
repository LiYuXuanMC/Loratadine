package net.moran.loratadine.setting.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Generated;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.setting.HideIf;
import net.moran.loratadine.setting.Setting;

public class NumberSetting extends Setting<Number> {
   private final Number maxValue;
   private final Number minValue;
   private final Number step;

   public NumberSetting(String name, Module module, Number value, Number minValue, Number maxValue, Number step, HideIf hideIf) {
      super(name, module, value, hideIf);
      this.maxValue = maxValue;
      this.minValue = minValue;
      this.step = step;
   }

   public NumberSetting(String name, Module module, Number value, Number minValue, Number maxValue, Number step) {
      this(name, module, value, minValue, maxValue, step, new HideIf() {
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
      this.setValue(element.getAsNumber());
   }

   @Generated
   public Number getMaxValue() {
      return this.maxValue;
   }

   @Generated
   public Number getMinValue() {
      return this.minValue;
   }

   @Generated
   public Number getStep() {
      return this.step;
   }
}
