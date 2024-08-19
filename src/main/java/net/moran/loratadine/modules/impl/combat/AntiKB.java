package net.moran.loratadine.modules.impl.combat;

import java.lang.reflect.Field;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.moran.loratadine.event.annotations.EventPriority;
import net.moran.loratadine.event.impl.LivingUpdateEvent;
import net.moran.loratadine.event.impl.PacketEvent;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.utils.helper.ReflectionHelper;

public class AntiKB extends Module {
   public boolean receivedKnockBack;
   public boolean attacked;

   public AntiKB() {
      super("AntiKB", Category.COMBAT);
      this.setEnabled(true);
   }

   @Override
   protected void onEnable() {
      this.receivedKnockBack = this.attacked = false;
   }

   @EventPriority
   public void onUpdate(LivingUpdateEvent event) {
      if (mc.player.hurtTime == 0) {
         this.receivedKnockBack = this.attacked = false;
      }

      if (this.receivedKnockBack && !this.attacked) {
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

         for (int i = 0; i < 5; i++) {
            mc.getConnection().getConnection().send(ServerboundInteractPacket.createAttackPacket(KillAura.getTarget(), mc.player.isShiftKeyDown()));
            mc.getConnection().getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
         }

         if (!sprinting) {
            mc.getConnection().getConnection().send(new ServerboundPlayerCommandPacket(mc.player, Action.STOP_SPRINTING));
         }

         this.attacked = true;
         Vec3 deltaMovement = mc.player.getDeltaMovement();
         mc.player.setDeltaMovement(deltaMovement.x * 0.07776, deltaMovement.y, deltaMovement.z * 0.07776);
      }
   }

   @EventPriority
   public void onPacket(PacketEvent event) {
      if (mc.player != null
         && event.getPacket() instanceof ClientboundSetEntityMotionPacket packet
         && packet.getId() == mc.player.getId()
         && KillAura.getTarget() != null) {
         this.attacked = false;
         this.receivedKnockBack = true;
      }
   }
}
