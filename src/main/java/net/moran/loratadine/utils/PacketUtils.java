package net.moran.loratadine.utils;

import java.util.ArrayList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class PacketUtils implements Wrapper {
   private static final ArrayList<Packet<ServerGamePacketListener>> packets = new ArrayList<>();

   public static boolean handleSendPacket(Packet<ServerGamePacketListener> packet) {
      if (packets.contains(packet)) {
         packets.remove(packet);
         return true;
      } else {
         return false;
      }
   }

   public static void sendPacketNoEvent(Packet<ServerGamePacketListener> packet) {
      if (mc.player != null) {
         packets.add(packet);
         mc.player.connection.send(packet);
      }
   }
}
