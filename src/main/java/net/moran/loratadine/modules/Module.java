package net.moran.loratadine.modules;

import java.util.ArrayList;
import lombok.Generated;
import net.moran.loratadine.Loratadine;
import net.moran.loratadine.setting.Setting;
import net.moran.loratadine.utils.ClientUtils;
import net.moran.loratadine.utils.wrapper.Wrapper;

public abstract class Module implements Wrapper {
   private final String name;
   private final Category category;
   private final ArrayList<Setting<?>> settings;
   private int key;
   private boolean enabled;
   private String suffix;

   public Module(String name, Category category, int key) {
      this.name = name;
      this.category = category;
      this.key = key;
      this.settings = new ArrayList<>();
   }

   public Module(String name, Category category) {
      this.name = name;
      this.category = category;
      this.key = 0;
      this.settings = new ArrayList<>();
   }

   public Setting<?> findSetting(String name) {
      for (Setting<?> setting : this.getSettings()) {
         if (setting.getName().replace(" ", "").equalsIgnoreCase(name)) {
            return setting;
         }
      }

      return null;
   }

   public void toggle() {
      this.setEnabled(!this.enabled);
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
      if (enabled) {
         ClientUtils.mc_debugMessage("Module " + this.name + " enable!");
         this.onEnable();
         Loratadine.INSTANCE.getEventManager().register(this);
      } else {
         ClientUtils.mc_debugMessage("Module " + this.name + " disable!");
         Loratadine.INSTANCE.getEventManager().unregister(this);
         this.onDisable();
      }
   }

   protected void onEnable() {
   }

   protected void onDisable() {
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public Category getCategory() {
      return this.category;
   }

   @Generated
   public ArrayList<Setting<?>> getSettings() {
      return this.settings;
   }

   @Generated
   public int getKey() {
      return this.key;
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   @Generated
   public String getSuffix() {
      return this.suffix;
   }

   @Generated
   public void setKey(int key) {
      this.key = key;
   }

   @Generated
   public void setSuffix(String suffix) {
      this.suffix = suffix;
   }
}
