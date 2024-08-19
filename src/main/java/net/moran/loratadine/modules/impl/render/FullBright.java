package net.moran.loratadine.modules.impl.render;

import net.moran.loratadine.event.annotations.EventPriority;
import net.moran.loratadine.event.impl.GameTickEvent;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;

public class FullBright extends Module {
   private double old;

   public FullBright() {
      super("FullBright", Category.RENDER);
      this.setEnabled(true);
   }

   @Override
   public void onEnable() {
      this.old = mc.options.gamma;
   }

   @Override
   public void onDisable() {
      mc.options.gamma = this.old;
   }

   @EventPriority
   public void onTick(GameTickEvent e) {
      if (mc.options.gamma != 10000.0) {
         mc.options.gamma = 10000.0;
      }
   }
}
