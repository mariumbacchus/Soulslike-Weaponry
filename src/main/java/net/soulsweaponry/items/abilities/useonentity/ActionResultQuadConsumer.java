package net.soulsweaponry.items.abilities.useonentity;

import net.minecraft.util.ActionResult;

@FunctionalInterface
public interface ActionResultQuadConsumer<A, B, C, D> {

    ActionResult accept(A a, B b, C c, D d);
}
