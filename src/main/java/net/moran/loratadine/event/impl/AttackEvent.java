package net.moran.loratadine.event.impl;

import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.moran.loratadine.event.CancellableEvent;

public class AttackEvent extends CancellableEvent {
   private final Player player;
   private final Entity target;

   public AttackEvent(Player player, Entity target) {
      this.player = player;
      this.target = target;
   }

   @Generated
   public Player getPlayer() {
      return this.player;
   }

   @Generated
   public Entity getTarget() {
      return this.target;
   }
}
