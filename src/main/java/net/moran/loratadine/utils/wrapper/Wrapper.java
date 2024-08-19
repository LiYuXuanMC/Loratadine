package net.moran.loratadine.utils.wrapper;

import net.minecraft.client.Minecraft;
import net.moran.loratadine.Loratadine;

public interface Wrapper {
   Minecraft mc = Loratadine.INSTANCE.getMinecraft();
}
