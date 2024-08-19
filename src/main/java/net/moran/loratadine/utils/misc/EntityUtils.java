package net.moran.loratadine.utils.misc;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.moran.loratadine.Loratadine;
import net.moran.loratadine.modules.impl.misc.Teams;
import net.moran.loratadine.utils.wrapper.Wrapper;

public final class EntityUtils implements Wrapper {
   public static boolean isSelected(
      Entity entity, boolean targetPlayer, boolean targetMobs, boolean targetAnimals, boolean targetDead, boolean targetInvisible, boolean canAttackCheck
   ) {
      if (entity instanceof LivingEntity && (targetDead || entity.isAlive()) && !entity.equals(mc.player) && (targetInvisible || !entity.isInvisible())) {
         if (targetPlayer && entity instanceof Player entityPlayer) {
            if (!canAttackCheck) {
               return true;
            } else if (entityPlayer.isSpectator()) {
               return false;
            } else {
               Teams teams = (Teams)Loratadine.INSTANCE.getModuleManager().getModule(Teams.class);
               return !teams.isEnabled() || !teams.isSameTeam(entityPlayer);
            }
         } else {
            return targetMobs && isMob(entity) || targetAnimals && isAnimal(entity);
         }
      } else {
         return false;
      }
   }

   public static boolean isAnimal(Entity entity) {
      return entity instanceof Animal || entity instanceof Squid || entity instanceof IronGolem || entity instanceof Bat;
   }

   public static boolean isMob(Entity entity) {
      return entity instanceof Mob
         || entity instanceof Villager
         || entity instanceof Slime
         || entity instanceof Ghast
         || entity instanceof EnderDragon
         || entity instanceof Shulker;
   }

   public static int getPing(Player entityPlayer) {
      if (entityPlayer == null) {
         return 0;
      } else {
         ClientPacketListener connection = mc.getConnection();
         if (connection == null) {
            return 0;
         } else {
            PlayerInfo networkPlayerInfo = connection.getPlayerInfo(entityPlayer.getUUID());
            return networkPlayerInfo == null ? 0 : networkPlayerInfo.getLatency();
         }
      }
   }
}
