package net.soulsweaponry.datagen.advancements;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.AdvancementEntry;

import java.util.function.Consumer;

public class AdvancementsProvider extends FabricAdvancementProvider {

    public AdvancementsProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<AdvancementEntry> consumer) {
        new Advancements().accept(consumer);
    }
}
