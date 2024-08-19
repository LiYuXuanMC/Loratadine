package net.moran.loratadine.event.impl;

import lombok.Generated;
import net.moran.loratadine.event.Event;

public class MotionEvent implements Event {
   private Event.Side side;

   @Generated
   public Event.Side getSide() {
      return this.side;
   }

   @Generated
   public void setSide(Event.Side side) {
      this.side = side;
   }

   @Generated
   public MotionEvent(Event.Side side) {
      this.side = side;
   }
}
