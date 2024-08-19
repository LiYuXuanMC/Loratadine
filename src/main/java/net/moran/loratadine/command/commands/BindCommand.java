package net.moran.loratadine.command.commands;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import net.moran.loratadine.Loratadine;
import net.moran.loratadine.command.Command;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.utils.ClientUtils;
import org.lwjgl.glfw.GLFW;

public class BindCommand extends Command {
   private Map<String, Integer> bindMap = new HashMap<>();

   public BindCommand() {
      super("bind", "b");
      this.bindMap.put("none", 0);

      for (Field field : GLFW.class.getFields()) {
         if (Modifier.isStatic(field.getModifiers()) && field.getName().startsWith("GLFW_KEY_")) {
            field.setAccessible(true);

            try {
               this.bindMap.put(field.getName().substring(9).toLowerCase(), (Integer)field.get(null));
            } catch (IllegalAccessException var6) {
               var6.printStackTrace();
            }
         }
      }
   }

   @Override
   public void execute(String[] params) {
      if (params.length == 2) {
         Module module = Loratadine.INSTANCE.getModuleManager().findModule(params[0]);
         if (module != null) {
            Integer key = this.bindMap.get(params[1]);
            if (key != null) {
               module.setKey(key);
               ClientUtils.mc_debugMessage("Bound module " + module.getName() + " to " + params[1] + ".");
            } else {
               ClientUtils.mc_debugMessage("Error: Invalid key");
            }
         } else {
            ClientUtils.mc_debugMessage("Error: " + params[0] + " not found");
         }
      } else {
         ClientUtils.mc_debugMessage("Usage: .bind <module> <key>");
      }
   }
}
