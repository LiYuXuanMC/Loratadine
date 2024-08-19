package net.moran.loratadine.modules;

public enum Category {
   COMBAT("Combat"),
   MOVEMENT("Movement"),
   PLAYER("Player"),
   RENDER("Render"),
   WORLD("World"),
   MISC("Misc");

   public final String name;

   private Category(String name) {
      this.name = name;
   }
}
