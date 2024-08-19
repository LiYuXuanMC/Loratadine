package net.moran.loratadine.modules.impl.misc;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.setting.impl.BooleanSetting;

public class Teams extends Module {
   private final BooleanSetting armorColor = new BooleanSetting("ArmorColor", this, true);

   public Teams() {
      super("Teams", Category.MISC);
      this.setEnabled(true);
   }

   public boolean isSameTeam(LivingEntity entity) {
      if (this.armorColor.getValue() && entity instanceof Player entityPlayer) {
         ItemStack myHead = (ItemStack)mc.player.getInventory().armor.get(3);
         ItemStack entityHead = (ItemStack)entityPlayer.getInventory().armor.get(3);
         if (!myHead.isEmpty() && !entityHead.isEmpty() && myHead.getItem() instanceof ArmorItem && entityHead.getItem() instanceof ArmorItem) {
            return this.getArmorColor(myHead) == this.getArmorColor(entityHead);
         }
      }

      return false;
   }

   private int getArmorColor(ItemStack stack) {
      return stack.getItem() instanceof DyeableLeatherItem ? ((DyeableLeatherItem)stack.getItem()).getColor(stack) : -1;
   }
}
