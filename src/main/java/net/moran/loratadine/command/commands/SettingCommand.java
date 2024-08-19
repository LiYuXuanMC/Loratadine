package net.moran.loratadine.command.commands;

import net.moran.loratadine.Loratadine;
import net.moran.loratadine.command.Command;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.setting.Setting;
import net.moran.loratadine.setting.impl.BooleanSetting;
import net.moran.loratadine.setting.impl.ModeSetting;
import net.moran.loratadine.utils.ClientUtils;

public class SettingCommand extends Command {
   public SettingCommand() {
      super("value", "setting");
   }

   @Override
   public void execute(String[] params) {
      if (params.length == 3) {
         Module module = Loratadine.INSTANCE.getModuleManager().findModule(params[0]);
         if (module != null) {
            Setting<?> setting = module.findSetting(params[1]);
            if (setting != null) {
               if (setting instanceof BooleanSetting wrapper) {
                  wrapper.setValue(Boolean.valueOf(Boolean.parseBoolean(params[2])));
               }

               if (setting instanceof ModeSetting wrapper) {
                  wrapper.setValue(String.valueOf(params[2]));
               }

               ClientUtils.mc_debugMessage("Set value " + setting.getName() + " to " + setting.getValue().toString());
            }
         } else {
            ClientUtils.mc_debugMessage("Error: " + params[1] + " in " + module.getName() + " not found.");
         }
      } else {
         ClientUtils.mc_debugMessage("Usage: .setting/value <module> <value>");
      }
   }
}
