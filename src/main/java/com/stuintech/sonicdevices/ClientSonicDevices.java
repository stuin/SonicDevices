package com.stuintech.sonicdevices;

import com.stuintech.sonicdevices.item.Device;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ClientSonicDevices implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricModelPredicateProviderRegistry.register(new Identifier(SonicDevices.MODID, "level"), Device.levelPredicate);
        FabricModelPredicateProviderRegistry.register(new Identifier(SonicDevices.MODID, "on"), Device.onPredicate);
    }
}
