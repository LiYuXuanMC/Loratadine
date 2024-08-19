package net.moran.loratadine.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.player.LocalPlayer;
import net.moran.loratadine.Loratadine;
import net.moran.loratadine.event.Event;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class NetworkUtils implements Wrapper {
   public static void init() {
      Object handler = Loratadine.INSTANCE.getEventHandler();

      try {
         Class<?> clientPacketListenerClass = Class.forName("net.minecraft.client.multiplayer.ClientPacketListener");
         Class<?> connectionClass = Class.forName("net.minecraft.network.Connection");

         label44:
         for (Field field : LocalPlayer.class.getDeclaredFields()) {
            if (field.getType() == clientPacketListenerClass) {
               field.setAccessible(true);
               Object clientPacketListener = field.get(mc.player);
               field.setAccessible(false);

               for (Field innerField : clientPacketListener.getClass().getDeclaredFields()) {
                  if (innerField.getType() == connectionClass) {
                     innerField.setAccessible(true);
                     Object connection = innerField.get(clientPacketListener);
                     innerField.setAccessible(false);

                     for (Field connField : connection.getClass().getDeclaredFields()) {
                        if (connField.getType() == Channel.class) {
                           connField.setAccessible(true);
                           Channel channel = (Channel)connField.get(connection);
                           ChannelPipeline pipeline = channel.pipeline();
                           ChannelDuplexHandler packetHandler = new PacketHandler(handler);
                           pipeline.addBefore("packet_handler", "PacketHandler", packetHandler);
                           connField.setAccessible(false);
                           return;
                        }
                     }
                     break label44;
                  }
               }
               break;
            }
         }
      } catch (Throwable var20) {
         var20.printStackTrace();
      }
   }

   private static class PacketHandler extends ChannelDuplexHandler {
      private final Object handler;

      public PacketHandler(Object handler) {
         this.handler = handler;
      }

      public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
         try {
            Method onPacketMethod = this.handler.getClass().getDeclaredMethod("onPacket", Object.class, Event.Side.class);
            onPacketMethod.setAccessible(true);
            Boolean result = (Boolean)onPacketMethod.invoke(this.handler, msg, Event.Side.PRE);
            if (result) {
               return;
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }

         super.channelRead(ctx, msg);
      }

      public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
         try {
            Method onPacketMethod = this.handler.getClass().getDeclaredMethod("onPacket", Object.class, Event.Side.class);
            onPacketMethod.setAccessible(true);
            Boolean result = (Boolean)onPacketMethod.invoke(this.handler, msg, Event.Side.POST);
            if (result) {
               return;
            }
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         super.write(ctx, msg, promise);
      }
   }
}
