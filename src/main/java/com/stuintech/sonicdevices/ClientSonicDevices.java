package com.stuintech.sonicdevices;

import com.stuintech.socketwrench.item.ModeWrenchItem;
import com.stuintech.sonicdevices.item.Device;
import com.stuintech.sonicdevices.item.HiddenDevice;
import com.stuintech.sonicdevices.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ClientSonicDevices implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for(Device d : ModItems.ALL_DEVICES) {
            FabricModelPredicateProviderRegistry.register(d, new Identifier("on"),
                    (itemStack, world, entity, seed) -> itemStack.getOrCreateNbt().getInt("on"));
            FabricModelPredicateProviderRegistry.register(d, new Identifier("level"),
                    (itemStack, world, entity, seed) -> (itemStack.getOrCreateNbt().getInt(ModeWrenchItem.MODE) + 1) / 10F);

            if(d instanceof HiddenDevice)
                FabricModelPredicateProviderRegistry.register(d, new Identifier("hidden"),
                        (itemStack, world, entity, seed) -> ((HiddenDevice) d).isHidden(itemStack) ? 1 : 0);

        }
    }
}
