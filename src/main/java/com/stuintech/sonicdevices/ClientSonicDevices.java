package com.stuintech.sonicdevices;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ClientSonicDevices implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricModelPredicateProviderRegistry.register(new Identifier(SonicDevices.MODID, "level"),
                (itemStack, world, entity, seed) -> itemStack.getOrCreateTag().getInt("level") + 1);
        FabricModelPredicateProviderRegistry.register(new Identifier(SonicDevices.MODID, "on"),
                (itemStack, world, entity, seed) -> itemStack.getOrCreateTag().getInt("on"));
    }
}
