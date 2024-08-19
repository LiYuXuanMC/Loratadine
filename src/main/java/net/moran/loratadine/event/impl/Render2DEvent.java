package net.moran.loratadine.event.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import net.moran.loratadine.event.Event;

public record Render2DEvent(float partialTick, PoseStack poseStack) implements Event {
}
