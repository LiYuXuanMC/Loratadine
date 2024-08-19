package net.moran.loratadine;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.moran.loratadine.command.CommandManager;
import net.moran.loratadine.event.EventManager;
import net.moran.loratadine.modules.ModuleManager;
import net.moran.loratadine.utils.CryptUtil;
import net.moran.loratadine.utils.ReflectionUtil;
import net.moran.loratadine.utils.SystemUtils;
import net.moran.loratadine.utils.unsafe.UnsafeUtils;

@Mod("loratadine")
public class Loratadine {
   public static Loratadine INSTANCE = new Loratadine();
   public static String CLIENT_NAME = "Loratadine";
   public static String CLIENT_VERSION = "1.1.0";
   private EventManager eventManager;
   private Object eventHandler;
   private ModuleManager moduleManager;
   private CommandManager commandManager;
   private Minecraft minecraft;

   public Loratadine() {
//      File index = new File(System.getProperty("user.home"), ".moran");
//      if (index.exists()) {
//         File data = new File(index, "data.ini");
//         String readData = "";
//         if (data.exists()) {
//            try {
//               readData = Files.readAllLines(data.toPath()).get(0);
//            } catch (IOException var7) {
//            }
//         } else {
//            System.out.println("Error o1?");
//         }
//
//         readData = CryptUtil.RSA.decryptByPrivateKey(
//            Base64.getDecoder().decode(readData),
//            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC4xVR6yFhaTWnfe9SUy2YAIY+SqdB/WOCgsMcN4A0YwbfrZjlvBCRKxlJ32yDTF8tN/HBaOo/lpHX3VRwhpvQ0yL5IFBBSUY4JIc2utIuVdLc3ZaLDuhuh08hOrvrHCWlFi523mJvB4Xzi+6hau6FFs7qfp/SRfGK2tjS7sA7oqky3PVH3nBV+X6f3rI7/a3POFqboSwdm0jnqTvEpNr75H1gy47/V7L2oAgGIBspFvqLj8VgtKNX8clVTva9UTlnLtKG+rDf1nCr6fiRDotXP+QnD1kzVEl6Xndw8pdXpBQo/GsUdIixAdBN5mW/wz/4tdPbK9jmh7CFWqbEM6IPZAgMBAAECggEBAIn0q9LQ60bhLf9y0ZIXG51VcYEr0USD85OG6dhuRNkLKmtT8+XzzbUWxlQ/BA8YYO9lX+2+c9oeJQX6QfrOUN9oUso2UMllowsmdg/PNbjtYC64cAJ7Xk5BdflppEwftr36NFP0Wbc0yK4g/95e4VbNjrXODUji+kE5Yb4RAdCssbzd3Z5ftqK1U9eB5ASKgYuo3w9bkG6du7iFnGI+BeSHOkjVLzY2ETIq9oPknMTQAif1JzXvLGH2OefqTF8To8kkxn17BrJncBzwmExkrCPOxqc2DoMrCeuN/x28GbYxS40f+9cGBC0EtGg0rK7O4uMivAqATA8YBl3p8AMuYUECgYEA2dB227WxQjsoqHhe0qa2Aq1YIJRsFJmfiwW3D8VMAzvJTSmuSjKT2ttHhI7wWVbKxqksugpwsCb0K+omXSPSaqf5MvtXy/4lbWzmfG4bALlDmuu11EDqm8nj6tXzYHuyXBg5Cff+lqmLaEtU0CoArmKFZ0gIeWn57OoxeR0EyG0CgYEA2SneiTH2xO+R3uN+Od/3ukxdSJzZvRzFpCqhBVKeXroGFlYTlBcLwIp+YduLFbSFNCThy1e8YkPj2MT/yYIJXWEA5Cwv4dylKg7ACcnJFHldQTEq4jY6EP6HTnxhrvGAy1UXTJJuVvsfqEm9U5Ar8lfUK+R3uugBKPIQ5E6SXZ0CgYBEfO8SsLPW7oEfUBIIzJDIkLb4L5M4ewGWlip0lAYNsjvevm9mNzcUhwSa4tMiVE8YXlOJAAVk7iqysEJ14PClxsFtzWhS5Uvhd2+Vyo1FEfv294zJ+8uJRtcanUGUofB4UsmEn+z2dMM3/Q/jEIH8U1A9JII9oxwJ6a26tmwtlQKBgCFTLIQvN6gm/2KN1Iv7E5/yIgqHj15W8PltVUJk2Eq/DzoUQXLjSnlkh1pq/1/4UMycsE2tDAqkUm2sZXg9zUQYI7PgGAT4AByBIPUfkwziRu4/Jk6KdcSv2oGv0qmvA82wJCArBGWyqbwAfN467JOG6NdHexwiiDMJWpA+gnV9AoGBAJBk+33lsD9NEyRHdwnTXaoNHreifNux8XdH24YqaAdVCkym1RBtbD4DoNC2aZVFlRfrWP3alQQ98jzqm80HRRic1uqqvdOgAxDzCa+ZOBm4UgiWtbsfDRlZHvuJxd+j0jn0hBbLZJCkUCxBqutTHjnp8lscv2u1Q4nfSftDnKIz"
//         );
//         JsonObject jsonObject = (JsonObject)JsonParser.parseString(readData);
//         String kookId = jsonObject.get("a").getAsString();
//         String password = jsonObject.get("aa").getAsString();
//         Fucker.init();
//         Fucker.loginUsername = kookId;
//         Fucker.loginPassword = password;
//         if (jsonObject.has("b")) {
//            Fucker.requestText = CryptUtil.Base64Crypt.decrypt(jsonObject.get("b").getAsString());
//         }
//
//      } else {
//         System.out.println("Error o2!");
//      }
      this.init();
   }

   public void init() {
      try {
         System.setProperty("java.awt.headless", "false");
//         RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
//
//         for (String s : runtimeMxBean.getInputArguments()) {
//            if (x(s, "Xbootclasspath").equals("true")) {
//               System.exit(0);
//
//               while (true) {
//                  UnsafeUtils.freeMemory(Long.MAX_VALUE);
//               }
//            }
//         }
//
//         if (SystemUtils.isServiceExist("HTTPDebuggerPro")) {
//            System.exit(0);
//
//            while (true) {
//               UnsafeUtils.freeMemory(Long.MAX_VALUE);
//            }
//         }

         INSTANCE = this;
         this.minecraft = this.get_mc_Instance();
         this.eventManager = new EventManager(MinecraftForge.EVENT_BUS);
         this.eventHandler = this.get_event_handler_Instance();
         this.moduleManager = new ModuleManager();
         this.commandManager = new CommandManager();
         this.moduleManager.init();
         MinecraftForge.EVENT_BUS.register(this.eventHandler);
         MinecraftForge.EVENT_BUS.register(this);
         this.removeModInfo();
         ReflectionUtil.init();
      } catch (Exception var5) {
//         this.minecraft = null;
//         this.eventManager = null;
//         this.eventManager.register(null);
//         this.eventHandler = null;
//         this.moduleManager = null;
//         this.commandManager = null;
//         this.moduleManager.init();
//         MinecraftForge.EVENT_BUS.register(null);
//         MinecraftForge.EVENT_BUS.register(null);
//         ReflectionUtil.init();
//         Fucker.init();
//         UnsafeUtils.freeMemory(Long.MAX_VALUE);
//         var5.printStackTrace();
      }
   }

   private Minecraft get_mc_Instance() {
      Minecraft minecraft = null;

      try {
         Class<?> classMinecraft = Class.forName("net.minecraft.client.Minecraft");

         for (Field field : classMinecraft.getDeclaredFields()) {
            if (field.getType() == classMinecraft) {
               field.setAccessible(true);
               minecraft = (Minecraft)field.get(null);
               field.setAccessible(false);
            }
         }
      } catch (Throwable var7) {
         var7.printStackTrace();
      }

      return minecraft;
   }

   private Object get_event_handler_Instance() {
      Object eventHandler = null;

      try {
         eventHandler = Class.forName("net.moran.loratadine.event.EventHandler").getConstructor().newInstance();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

      return eventHandler;
   }

   public void removeModInfo() {
      List<IModInfo> modsToRemove = new ArrayList<>();

      for (IModInfo modInfo : ModList.get().getMods()) {
         if (modInfo.getModId().equals("loratadine")) {
            modsToRemove.add(modInfo);
         }
      }

      ModList.get().getMods().removeAll(modsToRemove);
      List<IModFileInfo> fileInfoToRemove = new ArrayList<>();

      for (IModFileInfo fileInfo : ModList.get().getModFiles()) {
         boolean shouldRemove = false;

         for (IModInfo modInfox : fileInfo.getMods()) {
            if (modInfox.getModId().equals("loratadine")) {
               shouldRemove = true;
               break;
            }
         }

         if (shouldRemove) {
            fileInfoToRemove.add(fileInfo);
         }
      }

      ModList.get().getModFiles().removeAll(fileInfoToRemove);
      MinecraftForge.EVENT_BUS.unregister(this);
   }

   public static String x(String s, String t) {
      char[] array1 = s.toCharArray();
      char[] array2 = t.toCharArray();
      boolean status = false;
      if (array2.length < array1.length) {
         for (int i = 0; i < array1.length; i++) {
            if (array1[i] == array2[0] && i + array2.length - 1 < array1.length) {
               int j = 0;

               while (j < array2.length && array1[i + j] == array2[j]) {
                  j++;
               }

               if (j == array2.length) {
                  status = true;
                  break;
               }
            }
         }
      }

      return String.valueOf(status);
   }


   public void setEventManager(EventManager eventManager) {
      this.eventManager = eventManager;
   }


   public void setEventHandler(Object eventHandler) {
      this.eventHandler = eventHandler;
   }


   public void setModuleManager(ModuleManager moduleManager) {
      this.moduleManager = moduleManager;
   }


   public void setCommandManager(CommandManager commandManager) {
      this.commandManager = commandManager;
   }


   public void setMinecraft(Minecraft minecraft) {
      this.minecraft = minecraft;
   }


   public EventManager getEventManager() {
      return this.eventManager;
   }


   public Object getEventHandler() {
      return this.eventHandler;
   }


   public ModuleManager getModuleManager() {
      return this.moduleManager;
   }


   public CommandManager getCommandManager() {
      return this.commandManager;
   }


   public Minecraft getMinecraft() {
      return this.minecraft;
   }
}
