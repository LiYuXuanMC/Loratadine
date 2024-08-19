package net.moran.loratadine.modules.impl.movement;

import dev.annotations.JNICInclude;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.player.Input;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.moran.loratadine.event.Event;
import net.moran.loratadine.event.annotations.EventPriority;
import net.moran.loratadine.event.impl.GameTickEvent;
import net.moran.loratadine.event.impl.MotionEvent;
import net.moran.loratadine.event.impl.PacketEvent;
import net.moran.loratadine.event.impl.helper.CustomKeyboardInput;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;

@JNICInclude
public class NoSlow extends Module {
   Input old;
   boolean shouldSlow;

   public NoSlow() {
      super("NoSlow", Category.MOVEMENT);
      this.setEnabled(true);
   }

   @Override
   public void onEnable() {
      if (mc.player != null) {
         this.old = mc.player.input;
         if (!(mc.player.input instanceof CustomKeyboardInput)) {
            mc.player.input = new CustomKeyboardInput(mc.options);
         }
      }
   }

   @Override
   public void onDisable() {
      mc.player.input = this.old;
   }

   private boolean isUsable(ItemStack itemStack) {
      if (itemStack != null && !itemStack.isEmpty()) {
         Item item = itemStack.getItem();
         boolean isFood = item.getFoodProperties() != null;
         boolean isShield = item == Items.SHIELD;
         boolean isBow = item instanceof BowItem;
         boolean isCrossBow = item instanceof CrossbowItem;
         return isFood || isShield || isBow || isCrossBow;
      } else {
         return false;
      }
   }

   @EventPriority
   public void onMotion(MotionEvent event) {
      this.setSuffix("Heypixel");
      if (event.getSide() != Event.Side.POST) {
         if (mc.player.isUsingItem() && this.isUsable(mc.player.getMainHandItem()) || this.isUsable(mc.player.getOffhandItem())) {
            this.send();
         }
      }
   }

   @EventPriority
   public void onPacket(PacketEvent event) {
      Packet<?> packet = event.getPacket();
      if (event.getSide() == Event.Side.PRE) {
         Block block = null;
         HitResult hitResult = mc.hitResult;
         if (hitResult != null && hitResult.getType() == Type.BLOCK) {
            block = mc.level.getBlockState(((BlockHitResult)hitResult).getBlockPos()).getBlock();
         }

         if (this.isUsable(mc.player.getMainHandItem())
            || this.isUsable(mc.player.getOffhandItem()) && mc.player.isUsingItem() && (block == null || block != Blocks.CHEST)) {
            if (packet instanceof ClientboundContainerSetContentPacket) {
               event.setCancelled(true);
               this.noCancel();
               this.shouldSlow = false;
            }

            if (packet instanceof ClientboundContainerSetSlotPacket) {
               event.setCancelled(true);
            }
         }
      }

      if (event.getSide() == Event.Side.POST && (this.isUsable(mc.player.getMainHandItem()) || this.isUsable(mc.player.getOffhandItem()))) {
         if (packet instanceof ServerboundUseItemPacket) {
            this.noCancel();
            this.shouldSlow = true;
            this.send();
         }

         if (packet instanceof ServerboundPlayerActionPacket && ((ServerboundPlayerActionPacket)packet).getAction() == Action.RELEASE_USE_ITEM) {
            this.noCancel();
            this.shouldSlow = true;
         }
      }
   }

   private void send() {
      if (mc.player.getUseItemRemainingTicks()
            % (
               !(mc.player.getMainHandItem().getItem() instanceof BowItem)
                     && !(mc.player.getOffhandItem().getItem() instanceof BowItem)
                     && !(mc.player.getMainHandItem().getItem() instanceof CrossbowItem)
                     && !(mc.player.getOffhandItem().getItem() instanceof CrossbowItem)
                  ? 6
                  : 8
            )
         == 0) {
         Int2ObjectMap<ItemStack> modifiedStacks = new Int2ObjectOpenHashMap();
         modifiedStacks.put(36, new ItemStack(Items.BARRIER));
         mc.player.connection.send(new ServerboundContainerClickPacket(0, 0, 36, 0, ClickType.SWAP, new ItemStack(Blocks.BARRIER), modifiedStacks));
      }
   }

   private void noCancel() {
      if (!(mc.player.input instanceof CustomKeyboardInput)) {
         this.old = mc.player.input;
         mc.player.input = new CustomKeyboardInput(mc.options);
      }

      CustomKeyboardInput move = (CustomKeyboardInput)mc.player.input;
      move.setCancel(false);
   }

   @EventPriority
   public void onSlowDown(GameTickEvent event) {
      if (!(mc.player.input instanceof CustomKeyboardInput)) {
         this.old = mc.player.input;
         mc.player.input = new CustomKeyboardInput(mc.options);
      }

      CustomKeyboardInput move = (CustomKeyboardInput)mc.player.input;
      if (mc.player.isUsingItem()) {
         move.setCancel(!this.shouldSlow);
      } else {
         move.setCancel(false);
      }
   }
}
