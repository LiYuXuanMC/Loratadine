package net.moran.loratadine.modules.impl.player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.moran.loratadine.event.annotations.EventPriority;
import net.moran.loratadine.event.impl.Render2DEvent;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;

public class AutoTool extends Module {
   private int prevItem = 0;
   private boolean mining = false;

   public AutoTool() {
      super("AutoTool", Category.PLAYER);
      this.setEnabled(true);
   }

   @EventPriority
   public void onRender2D(Render2DEvent event) {
      if (!mc.options.keyUse.isDown() && mc.options.keyAttack.isDown() && mc.hitResult != null && mc.hitResult.getType() == Type.BLOCK) {
         if (!this.mining) {
            this.prevItem = mc.player.getInventory().selected;
         }

         this.switchSlot();
         this.mining = true;
      } else if (this.mining) {
         this.restore();
         this.mining = false;
      } else {
         this.prevItem = mc.player.getInventory().selected;
      }
   }

   public void switchSlot() {
      float bestSpeed = 1.0F;
      int bestSlot = -1;
      if (mc.hitResult != null && !mc.level.isEmptyBlock(((BlockHitResult)mc.hitResult).getBlockPos())) {
         BlockState blockState = mc.level.getBlockState(((BlockHitResult)mc.hitResult).getBlockPos());

         for (int i = 0; i <= 8; i++) {
            ItemStack item = mc.player.getInventory().getItem(i);
            if (!item.isEmpty()) {
               float speed = item.getDestroySpeed(blockState);
               if (speed > bestSpeed) {
                  bestSpeed = speed;
                  bestSlot = i;
               }
            }
         }

         if (bestSlot != -1) {
            mc.player.getInventory().selected = bestSlot;
         }
      }
   }

   public void restore() {
      for (int i = 0; i <= 8; i++) {
         if (i == this.prevItem) {
            mc.player.getInventory().selected = i;
            mc.gameMode.tick();
         }
      }
   }

   @Override
   public void onEnable() {
      this.prevItem = 0;
   }
}
