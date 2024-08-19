package net.moran.loratadine.modules.impl.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.moran.loratadine.event.annotations.EventPriority;
import net.moran.loratadine.event.impl.Render3DEvent;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.setting.impl.BooleanSetting;
import net.moran.loratadine.utils.misc.EntityUtils;
import net.moran.loratadine.utils.render.ColorUtils;
import net.moran.loratadine.utils.render.RenderUtils;

public final class ESP extends Module {
   private final BooleanSetting playersValue = new BooleanSetting("Players", this, true);
   private final BooleanSetting mobsValue = new BooleanSetting("Mobs", this, false);
   private final BooleanSetting animalsValue = new BooleanSetting("Animals", this, false);
   private final BooleanSetting deadValue = new BooleanSetting("Dead", this, false);
   private final BooleanSetting invisibleValue = new BooleanSetting("Invisible", this, false);

   public ESP() {
      super("ESP", Category.RENDER, -1);
      this.setEnabled(true);
   }

   @EventPriority
   public void onRender3D(Render3DEvent event) {
      if (mc.player != null && mc.level != null) {
         PoseStack poseStack = event.poseStack();

         for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof LivingEntity) {
               LivingEntity livingEntity = (LivingEntity)entity;
               if (EntityUtils.isSelected(
                  entity,
                  this.playersValue.getValue(),
                  this.mobsValue.getValue(),
                  this.animalsValue.getValue(),
                  this.deadValue.getValue(),
                  this.invisibleValue.getValue(),
                  true
               )) {
                  RenderUtils.renderEntityBoundingBox(poseStack, 0, livingEntity, ColorUtils.rainbow(10, 1).getRGB(), true);
               }
            }
         }
      }
   }
}
