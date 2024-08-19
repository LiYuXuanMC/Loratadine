package net.moran.loratadine.utils.player;

import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.TntBlock;
import net.moran.loratadine.utils.wrapper.Wrapper;

public final class BlockUtils implements Wrapper {
   public static boolean isAirBlock(BlockPos blockPos) {
      Block block = mc.level.getBlockState(blockPos).getBlock();
      return block instanceof AirBlock;
   }

   public static boolean isValidBlock(BlockPos blockPos) {
      return isValidBlock(mc.level.getBlockState(blockPos).getBlock());
   }

   public static boolean isValidBlock(Block block) {
      return !(block instanceof LiquidBlock)
         && !(block instanceof AirBlock)
         && !(block instanceof ChestBlock)
         && !(block instanceof FurnaceBlock)
         && !(block instanceof LadderBlock)
         && !(block instanceof TntBlock);
   }

   public static Block getBlock(BlockPos blockPos) {
      return mc.level.getBlockState(blockPos).getBlock();
   }

   @Generated
   private BlockUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
