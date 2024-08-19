package net.moran.loratadine.event.impl.helper;

import lombok.Generated;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomKeyboardInput extends Input {
   private final Options options;
   private boolean cancel;

   public CustomKeyboardInput(Options p_108580_) {
      this.options = p_108580_;
      this.cancel = false;
   }

   public void tick(boolean p_108582_) {
      this.up = this.options.keyUp.isDown();
      this.down = this.options.keyDown.isDown();
      this.left = this.options.keyLeft.isDown();
      this.right = this.options.keyRight.isDown();
      this.forwardImpulse = this.up == this.down ? 0.0F : (this.up ? 1.0F : -1.0F);
      this.leftImpulse = this.left == this.right ? 0.0F : (this.left ? 1.0F : -1.0F);
      this.jumping = this.options.keyJump.isDown();
      this.shiftKeyDown = this.options.keyShift.isDown();
      if (p_108582_) {
         this.leftImpulse = (float)((double)this.leftImpulse * 0.3);
         this.forwardImpulse = (float)((double)this.forwardImpulse * 0.3);
      }

      if (this.cancel) {
         super.leftImpulse *= 5.0F;
         super.forwardImpulse *= 5.0F;
      }
   }

   @Generated
   public void setCancel(boolean cancel) {
      this.cancel = cancel;
   }
}
