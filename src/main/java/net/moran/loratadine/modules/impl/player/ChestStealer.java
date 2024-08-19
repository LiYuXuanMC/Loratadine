package net.moran.loratadine.modules.impl.player;


import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SwordItem;
import net.moran.loratadine.event.Event;
import net.moran.loratadine.event.annotations.EventPriority;
import net.moran.loratadine.event.impl.MotionEvent;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.setting.impl.BooleanSetting;
import net.moran.loratadine.setting.impl.NumberSetting;
import net.moran.loratadine.utils.TimerUtils;
import net.moran.loratadine.utils.item.InventoryUtils;
import net.moran.loratadine.utils.math.MathUtils;

public class ChestStealer extends Module {
   private final BooleanSetting noDelay = new BooleanSetting("No Delay", this, false);
   private final BooleanSetting silent = new BooleanSetting("Silent", this, false);
   private final TimerUtils stopwatch2 = new TimerUtils();
   private final TimerUtils stopwatch = new TimerUtils();
   private long nextClick;
   private int lastClick;
   private int lastSteal;
   private final NumberSetting delay = new NumberSetting("StealDelay", this, 100, 0, 1000, 10);
   private final BooleanSetting trash = new BooleanSetting("PickTrash", this, false);

   public ChestStealer() {
      super("ChestStealer", Category.PLAYER, 66);
   }

   @EventPriority
   public void onMotion(MotionEvent event) {
      if (event.getSide() == Event.Side.PRE) {
         if (mc.player == null || mc.player.containerMenu == null || !this.stopwatch.hasTimeElapsed(this.nextClick)) {
            return;
         }

         if (mc.player.containerMenu instanceof ChestMenu container) {
            this.lastSteal++;
            if (this.isChestEmpty(container) && this.stopwatch2.hasTimeElapsed(100L)) {
               mc.player.closeContainer();
               return;
            }

            for (int i = 0; i < container.getContainer().getContainerSize(); i++) {
               if (!container.getContainer().getItem(i).isEmpty() && this.lastSteal > 1 && (this.isItemUseful(container, i) || this.trash.getValue())) {
                  this.nextClick = (long)Math.round(
                     MathUtils.getRandomFloat((float)this.delay.getValue().intValue(), (float)(this.delay.getValue().intValue() + 5))
                  );
                  mc.gameMode.handleInventoryMouseClick(container.containerId, i, 0, ClickType.QUICK_MOVE, mc.player);
                  this.stopwatch.reset();
                  this.stopwatch2.reset();
                  this.lastClick = 0;
                  if (this.nextClick > 0L) {
                     return;
                  }
               }
            }
         }
      }
   }

   private boolean isChestEmpty(ChestMenu c) {
      for (int i = 0; i < c.getContainer().getMaxStackSize(); i++) {
         if (!c.getContainer().getItem(i).isEmpty() && (this.isItemUseful(c, i) || this.trash.getValue())) {
            return false;
         }
      }

      return true;
   }

   private boolean isItemUseful(ChestMenu c, int i) {
      ItemStack itemStack = c.getSlot(i).getItem();
      Item item = itemStack.getItem();
      if (item instanceof AxeItem || item instanceof PickaxeItem) {
         return true;
      } else if (itemStack.getItem().getFoodProperties() != null) {
         return true;
      } else if (item instanceof BowItem || item == Items.ARROW) {
         return true;
      } else if (item instanceof PotionItem) {
         return true;
      } else if (item instanceof SwordItem && InventoryUtils.isBestSword(c, itemStack)) {
         return true;
      } else if (item instanceof ArmorItem && InventoryUtils.isBestArmor(c, itemStack)) {
         return true;
      } else if (item instanceof BlockItem) {
         return true;
      } else if (item == Items.SLIME_BALL) {
         return true;
      } else if (item instanceof CrossbowItem) {
         return true;
      } else if (item == Items.WATER_BUCKET) {
         return true;
      } else if (item == Items.TOTEM_OF_UNDYING) {
         return true;
      } else {
         return item == Items.FIRE_CHARGE ? true : item == Items.ENDER_PEARL;
      }
   }


   public BooleanSetting getNoDelay() {
      return this.noDelay;
   }


   public BooleanSetting getSilent() {
      return this.silent;
   }


   public TimerUtils getStopwatch2() {
      return this.stopwatch2;
   }


   public TimerUtils getStopwatch() {
      return this.stopwatch;
   }


   public long getNextClick() {
      return this.nextClick;
   }


   public int getLastClick() {
      return this.lastClick;
   }


   public int getLastSteal() {
      return this.lastSteal;
   }


   public NumberSetting getDelay() {
      return this.delay;
   }


   public BooleanSetting getTrash() {
      return this.trash;
   }
}
