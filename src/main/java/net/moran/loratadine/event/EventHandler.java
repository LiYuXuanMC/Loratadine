package net.moran.loratadine.event;

import cn.lzq.injection.Fucker;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.moran.loratadine.Loratadine;
import net.moran.loratadine.event.impl.AttackEvent;
import net.moran.loratadine.event.impl.MotionEvent;
import net.moran.loratadine.event.impl.PacketEvent;
import net.moran.loratadine.event.impl.Render2DEvent;
import net.moran.loratadine.event.impl.Render3DEvent;
import net.moran.loratadine.utils.ByteUtil;
import net.moran.loratadine.utils.NetworkUtils;
import net.moran.loratadine.utils.PacketUtils;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class EventHandler implements Wrapper {
   private boolean loaded = false;
   private static long lastPressTime = 0L;
   private static final long DEBOUNCE_TIME = 50L;

   private void call(Event event) {
      Loratadine.INSTANCE.getEventManager().call(event);
   }

   @SubscribeEvent
   public void onRender2D(Text playerEvent) {
      Render2DEvent event = new Render2DEvent(playerEvent.getPartialTicks(), playerEvent.getMatrixStack());
      this.call(event);
   }

   @SubscribeEvent
   public void onRender3D(RenderLevelLastEvent playerEvent) {
      Render3DEvent event = new Render3DEvent(playerEvent.getPartialTick(), playerEvent.getPoseStack());
      this.call(event);
   }

   @SubscribeEvent
   public void onKeyInput(KeyInputEvent event) {
      int key = event.getKey();
      int action = event.getAction();
      long currentTime = System.currentTimeMillis();
      if (action == 1 && currentTime - lastPressTime > 50L) {
         lastPressTime = currentTime;
         Loratadine.INSTANCE.getModuleManager().modules.values().forEach(module -> {
            if (module.getKey() != -1 && module.getKey() == key && mc.screen == null || module.getKey() == 344 && key == -1 && mc.screen == null) {
               module.toggle();
            }
         });
      }
   }

   @SubscribeEvent
   public void onClientTick(ClientTickEvent tickEvent) {
      this.call((Event)this.get_gameTick_event());
      if (tickEvent.side == LogicalSide.CLIENT) {
         MotionEvent event = new MotionEvent(tickEvent.phase == Phase.START ? Event.Side.PRE : Event.Side.POST);
         this.call(event);
         if (tickEvent.phase == Phase.START) {
            this.call((Event)this.get_livingUpdate_event());
         }
      }

      this.loaded();
   }

   @SubscribeEvent
   public void onAttack(AttackEntityEvent attackEntityEvent) {
      AttackEvent event = new AttackEvent(attackEntityEvent.getPlayer(), attackEntityEvent.getTarget());
      this.call(event);
      attackEntityEvent.setCanceled(event.isCancelled());
   }

   public boolean onPacket(Object packet, Event.Side side) {
      if (packet instanceof Packet<?> wrapper) {
         if (wrapper instanceof ServerGamePacketListener && PacketUtils.handleSendPacket((Packet<ServerGamePacketListener>)wrapper)) {
            return false;
         } else {
            PacketEvent event = new PacketEvent(side, wrapper);
            this.call(event);
            return event.isCancelled();
         }
      } else {
         return false;
      }
   }

   private void loaded() {
      if (mc.player == null || mc.level == null) {
         this.loaded = false;
      } else if (!this.loaded) {
         this.loaded = true;
         NetworkUtils.init();
         new ByteUtil(Fucker.channel).online(mc.player.getScoreboardName());
      }
   }

   private Object get_gameTick_event() {
      Object gameTick = null;

      try {
         gameTick = Class.forName("net.moran.loratadine.event.impl.GameTickEvent").getConstructor().newInstance();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

      return gameTick;
   }

   private Object get_livingUpdate_event() {
      Object livingUpdate = null;

      try {
         livingUpdate = Class.forName("net.moran.loratadine.event.impl.LivingUpdateEvent").getConstructor().newInstance();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

      return livingUpdate;
   }
}
