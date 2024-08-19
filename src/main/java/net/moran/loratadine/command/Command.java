package net.moran.loratadine.command;

public abstract class Command {
   private final String[] name;

   public Command(String... name) {
      this.name = name;
   }

   public String[] getName() {
      return this.name;
   }

   public abstract void execute(String[] var1);
}
