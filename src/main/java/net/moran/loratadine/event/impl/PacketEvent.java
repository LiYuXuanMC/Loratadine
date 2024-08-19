package net.moran.loratadine.event.impl;

import lombok.Generated;
import net.minecraft.network.protocol.Packet;
import net.moran.loratadine.event.CancellableEvent;
import net.moran.loratadine.event.Event;

public class PacketEvent extends CancellableEvent {
   private final Event.Side side;
   private final Packet<?> packet;

   @Generated
   public PacketEvent(Event.Side side, Packet<?> packet) {
      this.side = side;
      this.packet = packet;
   }

   @Generated
   public Event.Side getSide() {
      return this.side;
   }

   @Generated
   public Packet<?> getPacket() {
      return this.packet;
   }
}
