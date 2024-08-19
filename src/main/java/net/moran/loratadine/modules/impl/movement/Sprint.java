package net.moran.loratadine.modules.impl.movement;

import net.moran.loratadine.event.annotations.EventPriority;
import net.moran.loratadine.event.impl.LivingUpdateEvent;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.setting.impl.BooleanSetting;

public class Sprint extends Module {
   private final BooleanSetting usingSprint = new BooleanSetting("UsingSprint", this, true);

   public Sprint() {
      super("Sprint", Category.MOVEMENT, 86);
      this.setEnabled(true);
   }

   @EventPriority
   public void onUpdate(LivingUpdateEvent event) {
      if (mc.level != null && mc.player != null) {
         mc.player
            .setSprinting(
               mc.player.getFoodData().getFoodLevel() > 6
                     && !mc.player.horizontalCollision
                     && mc.player.input.forwardImpulse > 0.0F
                     && !mc.player.isShiftKeyDown()
                  || !mc.player.isUsingItem() && !this.usingSprint.getValue()
            );
      }
   }
}
