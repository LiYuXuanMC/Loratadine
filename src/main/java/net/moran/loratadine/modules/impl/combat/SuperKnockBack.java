package net.moran.loratadine.modules.impl.combat;

import java.lang.reflect.Field;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.moran.loratadine.event.annotations.EventPriority;
import net.moran.loratadine.event.impl.LivingUpdateEvent;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.utils.helper.ReflectionHelper;

public class SuperKnockBack extends Module {
   public SuperKnockBack() {
      super("SuperKnockBack", Category.COMBAT);
      this.setEnabled(true);
   }

   @EventPriority
   public void onUpdate(LivingUpdateEvent event) {
      this.setSuffix("Legit");
      if (mc.level != null && mc.player != null && KillAura.getTarget() != null) {
         boolean sprinting = false;
         Field sprintingField = ReflectionHelper.findField(mc.player.getClass(), "f_108603_", "wasSprinting");

         try {
            sprinting = sprintingField.getBoolean(mc.player);
         } catch (Exception var5) {
            var5.printStackTrace();
         }

         if (!sprinting) {
            mc.getConnection().getConnection().send(new ServerboundPlayerCommandPacket(mc.player, Action.START_SPRINTING));
         }

         if (!sprinting) {
            mc.getConnection().getConnection().send(new ServerboundPlayerCommandPacket(mc.player, Action.STOP_SPRINTING));
         }
      }
   }
}
