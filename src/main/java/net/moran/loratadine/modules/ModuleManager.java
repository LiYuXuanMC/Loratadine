package net.moran.loratadine.modules;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.moran.loratadine.modules.impl.combat.AntiKB;
import net.moran.loratadine.modules.impl.combat.KillAura;
import net.moran.loratadine.modules.impl.combat.SuperKnockBack;
import net.moran.loratadine.modules.impl.misc.Disabler;
import net.moran.loratadine.modules.impl.misc.Teams;
import net.moran.loratadine.modules.impl.movement.Eagle;
import net.moran.loratadine.modules.impl.movement.NoSlow;
import net.moran.loratadine.modules.impl.movement.Sprint;
import net.moran.loratadine.modules.impl.player.AutoTool;
import net.moran.loratadine.modules.impl.player.ChestStealer;
import net.moran.loratadine.modules.impl.player.InvCleaner;
import net.moran.loratadine.modules.impl.render.ClickGui;
import net.moran.loratadine.modules.impl.render.ESP;
import net.moran.loratadine.modules.impl.render.FullBright;
import net.moran.loratadine.modules.impl.render.HUD;

public class ModuleManager {
   public final Map<Class<? extends Module>, Module> modules = new HashMap<>();
   private final Map<Category, List<Module>> categoryModules = new HashMap<>();

   public void init() {
      this.registerModules(
         new Sprint(),
         new HUD(),
         new Eagle(),
         new Disabler(),
         new ClickGui(),
         new KillAura(),
         new AntiKB(),
         new Teams(),
         new SuperKnockBack(),
         new NoSlow(),
         new AutoTool(),
         new ChestStealer(),
         new InvCleaner(),
         new ESP(),
         new FullBright()
      );
   }

   private void registerModules(Module... modules) {
      List.of(modules).forEach(this::registerModule);
   }

   private void registerModule(Module module) {
      this.modules.put((Class<? extends Module>)module.getClass(), module);
   }

   public Module findModule(String name) {
      for (Module module : this.getModules()) {
         if (module.getName().replace(" ", "").equalsIgnoreCase(name)) {
            return module;
         }
      }

      return null;
   }

   public Module getModule(Class<? extends Module> moduleClazz) {
      return this.modules.get(moduleClazz);
   }

   public List<Module> getModule(Category category) {
      return this.modules.values().stream().filter(module -> module.getCategory().equals(category)).collect(Collectors.toList());
   }

   public Collection<Module> getModules() {
      return this.modules.values();
   }

   public List<Module> getModulesByCategory(Category category) {
      List<Module> tryGet = this.categoryModules.get(category);
      if (tryGet == null) {
         List<Module> modules1 = this.modules.values().stream().filter(module -> module.getCategory() == category).toList();
         this.categoryModules.put(category, modules1);
         return modules1;
      } else {
         return tryGet;
      }
   }
}
