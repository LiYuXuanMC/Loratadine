package net.moran.loratadine.modules.impl.misc;

import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;

public class Disabler extends Module {
   public Disabler() {
      super("Disabler", Category.MISC);
      this.setEnabled(false);
   }
}
