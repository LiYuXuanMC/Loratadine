package net.moran.loratadine.event;

public abstract class CancellableEvent implements Event, Cancellable {
   private boolean cancelled;

   @Override
   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   @Override
   public boolean isCancelled() {
      return this.cancelled;
   }
}
