package net.moran.loratadine.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.moran.loratadine.Loratadine;
import net.moran.loratadine.command.commands.BindCommand;
import net.moran.loratadine.command.commands.SettingCommand;
import net.moran.loratadine.utils.ClientUtils;

public class CommandManager {
   private final Map<String, Command> commands = new HashMap<>();

   public CommandManager() {
      Loratadine.INSTANCE.getEventManager().register(this);
      this.registerCommand(new SettingCommand());
      this.registerCommand(new BindCommand());
   }

   private void registerCommand(Command command) {
      for (String name : command.getName()) {
         this.commands.put(name.toLowerCase(), command);
      }
   }

   @SubscribeEvent
   public void onChat(ClientChatEvent event) {
      if (event.getMessage().startsWith(".")) {
         String[] ars = event.getMessage().substring(1).split(" ");
         String name = ars[0];
         Command command = this.commands.get(name.toLowerCase());
         if (command == null) {
            ClientUtils.mc_debugMessage("Error: " + name + " is not a command.");
         } else {
            command.execute(Arrays.copyOfRange(ars, 1, ars.length));
         }

         event.setCanceled(true);
      }
   }
}
