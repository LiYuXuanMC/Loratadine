package net.moran.loratadine.modules.impl.movement;

import net.minecraft.client.KeyMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.moran.loratadine.event.annotations.EventPriority;
import net.moran.loratadine.event.impl.LivingUpdateEvent;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.setting.impl.BooleanSetting;

public class Eagle extends Module {
   private final BooleanSetting autoBuild = new BooleanSetting("AutoBuild", this, true);

   public Eagle() {
      super("Eagle", Category.MOVEMENT, 71);
   }

   public static Block getBlock(BlockPos pos) {
      return mc.level.getBlockState(pos).getBlock();
   }

   public static Block getBlockUnderPlayer(Player player) {
      return getBlock(new BlockPos(player.getX(), player.getY() - 1.0, player.getZ()));
   }

   @EventPriority
   public void onUpdate(LivingUpdateEvent event) {
      if (getBlockUnderPlayer(mc.player) instanceof AirBlock) {
         if (mc.player.isOnGround()) {
            KeyMapping.set(mc.options.keyShift.getKey(), true);
            if (mc.player.getMainHandItem().getItem() instanceof BlockItem
               || mc.player.getOffhandItem().getItem() instanceof BlockItem && this.autoBuild.getValue()) {
               BlockHitResult hitResult = (BlockHitResult)mc.hitResult;
               if (hitResult != null && hitResult.getType() == Type.BLOCK) {
                  BlockPos blockPos = hitResult.getBlockPos().relative(hitResult.getDirection());
                  BlockState state = mc.level.getBlockState(blockPos);
                  if (state.getBlock() instanceof AirBlock) {
                     KeyMapping.set(mc.options.keyUse.getKey(), true);
                  }
               }
            }
         }
      } else if (mc.player.isOnGround()) {
         KeyMapping.set(mc.options.keyShift.getKey(), false);
      }
   }

   @Override
   public void onEnable() {
      if (mc.player != null) {
         mc.player.setShiftKeyDown(false);
         KeyMapping.set(mc.options.keyUse.getKey(), false);
      }
   }

   @Override
   public void onDisable() {
      KeyMapping.set(mc.options.keyShift.getKey(), false);
      KeyMapping.set(mc.options.keyUse.getKey(), false);
   }
}
