package net.moran.loratadine.utils.player;

import lombok.Generated;
import net.moran.loratadine.utils.wrapper.Wrapper;

public final class MoveUtils implements Wrapper {
   public static boolean isMoving() {
      return mc.player != null
         && mc.level != null
         && mc.player.input != null
         && ((double)mc.player.input.forwardImpulse != 0.0 || (double)mc.player.input.leftImpulse != 0.0);
   }

   public static double direction(float rotationYaw, double moveForward, double moveStrafing) {
      if (moveForward < 0.0) {
         rotationYaw += 180.0F;
      }

      float forward = 1.0F;
      if (moveForward < 0.0) {
         forward = -0.5F;
      } else if (moveForward > 0.0) {
         forward = 0.5F;
      }

      if (moveStrafing > 0.0) {
         rotationYaw -= 90.0F * forward;
      }

      if (moveStrafing < 0.0) {
         rotationYaw += 90.0F * forward;
      }

      return Math.toRadians((double)rotationYaw);
   }

   @Generated
   private MoveUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
