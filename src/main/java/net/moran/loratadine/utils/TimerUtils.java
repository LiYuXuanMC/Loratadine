package net.moran.loratadine.utils;

public class TimerUtils {
   public long lastMS = System.currentTimeMillis();

   public TimerUtils() {
      this.reset();
   }

   public void reset() {
      this.lastMS = System.currentTimeMillis();
   }

   public void resetMS() {
      this.lastMS = System.nanoTime();
   }

   public boolean hasTimeElapsed(long time, boolean reset) {
      if (System.currentTimeMillis() - this.lastMS > time) {
         if (reset) {
            this.reset();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean delay(long time) {
      return System.currentTimeMillis() - this.lastMS >= time;
   }

   public boolean passedS(double s) {
      return this.getMs(System.nanoTime() - this.lastMS) >= (long)(s * 1000.0);
   }

   public boolean passedMs(long ms) {
      return this.getMs(System.nanoTime() - this.lastMS) >= ms;
   }

   public boolean hasTimeElapsed(long time) {
      return System.currentTimeMillis() - this.lastMS > time;
   }

   public boolean hasTimeElapsed(double time) {
      return this.hasTimeElapsed((long)time);
   }

   public long getTime() {
      return System.currentTimeMillis() - this.lastMS;
   }

   public void setTime(long time) {
      this.lastMS = time;
   }

   public long getPassedTimeMs() {
      return this.getMs(System.nanoTime() - this.lastMS);
   }

   public long getMs(long time) {
      return time / 1000000L;
   }
}
