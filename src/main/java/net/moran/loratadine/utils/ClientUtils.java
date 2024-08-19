package net.moran.loratadine.utils;

import net.minecraft.network.chat.TextComponent;
import net.moran.loratadine.Loratadine;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class ClientUtils implements Wrapper {
   public static void mc_debugMessage(String debugMessage) {
      if (mc.level != null && mc.player != null) {
         mc.gui.getChat().addMessage(new TextComponent("§8[§c§l" + Loratadine.CLIENT_NAME + "§8]§c§d" + debugMessage));
      }
   }

   public static void displayIRC(String text) {
      mc_debugMessage("§l[§bLoratadine§r§l] §r" + text);
   }
}
