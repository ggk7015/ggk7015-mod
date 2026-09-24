package io.github.ggk7015.ggk7015mod;

import io.github.ggk7015.ggk7015mod.item.GocStrikeItem;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ModItems {

    public static final Item GOC_STRIKE = new GocStrikeItem();

    public static void register() {
        Registry.register(BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath("ggk7015-mod", "goc_strike"),
                GOC_STRIKE);
    }
}
