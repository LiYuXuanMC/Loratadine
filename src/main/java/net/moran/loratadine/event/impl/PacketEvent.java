package net.moran.loratadine.event.impl;


import net.minecraft.network.protocol.Packet;
import net.moran.loratadine.event.CancellableEvent;
import net.moran.loratadine.event.Event;

public class PacketEvent extends CancellableEvent {
    private final Side side;
    private final Packet<?> packet;


    public PacketEvent(Side side, Packet<?> packet) {
        this.side = side;
        this.packet = packet;
    }


    public Event.Side getSide() {
        return this.side;
    }


    public Packet<?> getPacket() {
      return this.packet;
   }
}
