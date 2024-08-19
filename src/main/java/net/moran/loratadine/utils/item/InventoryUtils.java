package net.moran.loratadine.utils.item;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.moran.loratadine.utils.wrapper.Wrapper;

public final class InventoryUtils implements Wrapper {
   public static final int INCLUDE_ARMOR_BEGIN = 5;
   public static final int EXCLUDE_ARMOR_BEGIN = 9;
   public static final int ONLY_HOT_BAR_BEGIN = 36;
   public static final int END = 45;
   private static final Set<MobEffect> GOOD_STATUS_EFFECTS = new HashSet<>(
      Set.of(
         MobEffects.MOVEMENT_SPEED,
         MobEffects.HEAL,
         MobEffects.DAMAGE_BOOST,
         MobEffects.JUMP,
         MobEffects.REGENERATION,
         MobEffects.DAMAGE_RESISTANCE,
         MobEffects.FIRE_RESISTANCE,
         MobEffects.WATER_BREATHING,
         MobEffects.NIGHT_VISION,
         MobEffects.HEALTH_BOOST,
         MobEffects.ABSORPTION,
         MobEffects.SATURATION,
         MobEffects.LUCK,
         MobEffects.SLOW_FALLING,
         MobEffects.CONDUIT_POWER,
         MobEffects.DOLPHINS_GRACE,
         MobEffects.HERO_OF_THE_VILLAGE,
         MobEffects.INVISIBILITY
      )
   );

   public static boolean isBestCrossBow(InventoryMenu handler, ItemStack itemStack) {
      double bestBowDmg = -1.0;
      ItemStack bestBow = ItemStack.EMPTY;

      for (int i = 9; i < 45; i++) {
         ItemStack stack = handler.getSlot(i).getItem();
         if (!stack.isEmpty() && stack.getItem() instanceof CrossbowItem) {
            double damage = getCrossBowDamage(stack);
            if (damage > bestBowDmg) {
               bestBow = stack;
               bestBowDmg = damage;
            }
         }
      }

      return itemStack.equals(bestBow) || getBowDamage(itemStack) > bestBowDmg;
   }

   public static boolean isBestBow(AbstractContainerMenu handler, ItemStack itemStack) {
      double bestBowDmg = -1.0;
      ItemStack bestBow = ItemStack.EMPTY;

      for (int i = 9; i < 45; i++) {
         ItemStack stack = handler.getSlot(i).getItem();
         if (!stack.isEmpty() && stack.getItem() instanceof BowItem) {
            double damage = getBowDamage(stack);
            if (damage > bestBowDmg) {
               bestBow = stack;
               bestBowDmg = damage;
            }
         }
      }

      return itemStack.equals(bestBow) || getBowDamage(itemStack) > bestBowDmg;
   }

   public static double getCrossBowDamage(ItemStack stack) {
      double damage = 0.0;
      if (stack.getItem() instanceof CrossbowItem && stack.hasFoil()) {
         damage += (double)getLevel(Enchantments.POWER_ARROWS, stack);
      }

      return damage;
   }

   public static double getBowDamage(ItemStack stack) {
      double damage = 0.0;
      if (stack.getItem() instanceof BowItem && stack.hasFoil()) {
         damage += (double)getLevel(Enchantments.POWER_ARROWS, stack);
      }

      return damage;
   }

   public static boolean isBuffPotion(ItemStack stack) {
      for (MobEffectInstance effect : PotionUtils.getMobEffects(stack)) {
         if (!effect.getEffect().isBeneficial()) {
            return false;
         }
      }

      return true;
   }

   public static boolean isBestTool(AbstractContainerMenu handler, ItemStack itemStack) {
      int type = getToolType(itemStack);
      Tool bestTool = new Tool(-1, -1.0, ItemStack.EMPTY);

      for (int i = 9; i < 45; i++) {
         ItemStack stack = handler.getSlot(i).getItem();
         if (!stack.isEmpty() && stack.getItem() instanceof DiggerItem && type == getToolType(stack)) {
            double efficiency = (double)getToolScore(stack);
            if (efficiency > (double)getToolScore(bestTool.getItem())) {
               bestTool = new Tool(i, efficiency, stack);
            }
         }
      }

      return bestTool.getItem().equals(itemStack) || getToolScore(itemStack) > getToolScore(bestTool.getItem());
   }

   public static float getToolScore(ItemStack stack) {
      float score = 0.0F;
      Item item = stack.getItem();
      if (item instanceof DiggerItem tool) {
         if (item instanceof PickaxeItem) {
            score = tool.getDestroySpeed(stack, Blocks.STONE.defaultBlockState()) - 0.0F;
         } else {
            if (!(item instanceof AxeItem)) {
               return 1.0F;
            }

            score = tool.getDestroySpeed(stack, Blocks.DARK_OAK_LOG.defaultBlockState());
         }

         score += (float)getLevel(Enchantments.BLOCK_EFFICIENCY, stack) * 0.0075F;
         score += (float)getLevel(Enchantments.BLOCK_EFFICIENCY, stack) / 100.0F;
         score += (float)getLevel(Enchantments.SHARPNESS, stack) * 1.0F;
      }

      return score;
   }

   public static float getToolEfficiency(ItemStack itemStack) {
      DiggerItem tool = (DiggerItem)itemStack.getItem();
      float efficiency = tool.getTier().getSpeed();
      int lvl = getLevel(Enchantments.BLOCK_EFFICIENCY, itemStack);
      if (efficiency > 1.0F && lvl > 0) {
         efficiency += (float)(lvl * lvl + 1);
      }

      return efficiency;
   }

   public static boolean isGoodFood(ItemStack stack) {
      if (stack.getItem() == Items.GOLDEN_APPLE) {
         return true;
      } else {
         FoodProperties component = stack.getItem().getFoodProperties();
         return component.getNutrition() >= 4 && component.getSaturationModifier() >= 0.3F;
      }
   }

   public static boolean isGoodItem(ItemStack stack) {
      Item item = stack.getItem();
      return item == Items.FIRE_CHARGE
         || item == Items.ENDER_PEARL
         || item == Items.ARROW
         || item == Items.WATER_BUCKET
         || item == Items.SLIME_BALL
         || item == Items.TNT
         || item instanceof CrossbowItem && isBestCrossBow(mc.player.inventoryMenu, stack);
   }

   public static boolean isBestSword(AbstractContainerMenu c, ItemStack itemStack) {
      double damage = 0.0;
      ItemStack bestStack = ItemStack.EMPTY;

      for (int i = 9; i < 45; i++) {
         ItemStack stack = c.getSlot(i).getItem();
         if (!stack.isEmpty() && stack.getItem() instanceof SwordItem) {
            double newDamage = getItemDamage(stack);
            if (newDamage > damage) {
               damage = newDamage;
               bestStack = stack;
            }
         }
      }

      return bestStack.equals(itemStack) || getItemDamage(itemStack) > damage;
   }

   public static boolean isBestArmor(AbstractContainerMenu c, ItemStack itemStack) {
      ArmorItem itemArmor = (ArmorItem)itemStack.getItem();
      double reduction = 0.0;
      ItemStack bestStack = ItemStack.EMPTY;

      for (int i = 5; i < 45; i++) {
         ItemStack stack = c.getSlot(i).getItem();
         if (!stack.isEmpty()) {
            Item newReduction = stack.getItem();
            if (newReduction instanceof ArmorItem) {
               ArmorItem stackArmor = (ArmorItem)newReduction;
               if (stackArmor.getSlot().getFilterFlag() - 1 == itemArmor.getSlot().getFilterFlag() - 1) {
                  double newReductionx = getDamageReduction(stack);
                  if (newReductionx > reduction) {
                     reduction = newReductionx;
                     bestStack = stack;
                  }
               }
            }
         }
      }

      return bestStack.equals(itemStack) || getDamageReduction(itemStack) > reduction;
   }

   public static double getDamageReduction(ItemStack stack) {
      double reduction = 0.0;
      if (!(stack.getItem() instanceof ArmorItem)) {
         return 0.0;
      } else {
         ArmorItem armor = (ArmorItem)stack.getItem();
         reduction += (double)armor.getDefense();
         if (stack.hasFoil()) {
            reduction += (double)getLevel(Enchantments.ALL_DAMAGE_PROTECTION, stack) * 0.25;
         }

         return reduction;
      }
   }

   public static double getItemDamage(ItemStack stack) {
      double damage = 0.0;
      Multimap<Attribute, AttributeModifier> attributeModifierMap = stack.getAttributeModifiers(EquipmentSlot.MAINHAND);

      for (Attribute attributeName : attributeModifierMap.keySet()) {
         if (attributeName.getDescriptionId().equals("attribute.name.generic.attack_damage")) {
            Iterator<AttributeModifier> attributeModifiers = attributeModifierMap.get(attributeName).iterator();
            if (attributeModifiers.hasNext()) {
               damage += attributeModifiers.next().getAmount();
            }
            break;
         }
      }

      if (stack.hasFoil()) {
         damage += (double)EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
         damage += (double)EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, stack) * 1.25;
      }

      return damage;
   }

   private static int getLevel(Enchantment registryKey, ItemStack itemStack) {
      return EnchantmentHelper.getItemEnchantmentLevel(registryKey, itemStack);
   }

   public static int getToolType(ItemStack stack) {
      DiggerItem tool = (DiggerItem)stack.getItem();
      if (tool instanceof PickaxeItem) {
         return 0;
      } else {
         return tool instanceof AxeItem ? 1 : -1;
      }
   }

   public static List<ItemStack> getItemStacks(Player player) {
      List<ItemStack> result = new ArrayList<>();

      for (Slot slot : player.inventoryMenu.slots) {
         if (!slot.getItem().isEmpty()) {
            result.add(slot.getItem());
         }
      }

      return result;
   }

   public static float getPlayerArmorScore(Player player) {
      float score = 0.0F;

      for (int armorSlot = 5; armorSlot < 9; armorSlot++) {
         ItemStack stack = player.inventoryMenu.getSlot(armorSlot).getItem();
         if (stack != null) {
            score += (float)getDamageReduction(stack);
         }
      }

      return score;
   }

   public static boolean isArmorBetter(Player player) {
      return getPlayerArmorScore(player) < getPlayerArmorScore(mc.player);
   }


   private InventoryUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static enum BlockAction {
      PLACE,
      REPLACE,
      PLACE_ON;
   }

   private static class Tool {
      private final int slot;
      private final double efficiency;
      private final ItemStack stack;

      public Tool(int slot, double efficiency, ItemStack stack) {
         this.slot = slot;
         this.efficiency = efficiency;
         this.stack = stack;
      }

      public int getSlot() {
         return this.slot;
      }

      public double getEfficiency() {
         return this.efficiency;
      }

      public ItemStack getItem() {
         return this.stack;
      }
   }
}
