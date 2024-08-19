package net.moran.loratadine.modules.impl.render;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import net.moran.loratadine.Loratadine;
import net.moran.loratadine.event.annotations.EventPriority;
import net.moran.loratadine.event.impl.GameTickEvent;
import net.moran.loratadine.event.impl.Render2DEvent;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.utils.helper.ReflectionHelper;
import net.moran.loratadine.utils.render.ColorUtils;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class HUD extends Module {
   private int fps;
   private List<Module> enabledModules = new ArrayList<>();

   public HUD() {
      super("HUD", Category.RENDER);
      this.setEnabled(true);
   }

   @EventPriority
   public void onTick(GameTickEvent event) {
      this.enabledModules = new ArrayList<>();
      Collection<Module> modules = Loratadine.INSTANCE.getModuleManager().getModules();
      if (!modules.isEmpty()) {
         for (Module module : modules) {
            if (module.isEnabled()) {
               this.enabledModules.add(module);
            }
         }

         this.enabledModules.sort(new Comparator<Module>() {
            public int compare(Module m1, Module m2) {
               int width1 = Wrapper.mc.font.width(m1.getName());
               int width2 = Wrapper.mc.font.width(m2.getName());
               return Mth.ceil((float)(width2 - width1));
            }
         });
      }
   }

   @EventPriority
   public void onRender2D(Render2DEvent event) {
      mc.font.drawShadow(event.poseStack(), Loratadine.CLIENT_NAME.substring(0, 1), 4.0F, 4.0F, ColorUtils.rainbow(10, 1).getRGB());
      Field fps_field = ReflectionHelper.findField(mc.getClass(), "f_91021_", "fps");

      try {
         this.fps = fps_field.getInt(mc);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      mc.font
         .drawShadow(
            event.poseStack(),
            Loratadine.CLIENT_NAME.substring(1) + " " + Loratadine.CLIENT_VERSION + " [" + this.fps + "FPS]",
            (float)(4 + mc.font.width(Loratadine.CLIENT_NAME.substring(0, 1))),
            4.0F,
            -1
         );
      float y = 0.0F;

      for (Module module : this.enabledModules) {
         mc.font
            .drawShadow(
               event.poseStack(),
               module.getName() + (module.getSuffix() == null ? "" : ChatFormatting.GRAY + " [" + module.getSuffix() + "]"),
               4.0F,
               16.0F + y,
               -1
            );
         y += (float)(9 + 2);
      }
   }
}
