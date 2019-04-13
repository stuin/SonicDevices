package com.stuintech.sonicdevices;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static SoundEvent sonicSound = new SoundEvent(new Identifier(SonicDevices.MODID, "sonic_sound"));

    public static void register() {
        Registry.register(Registry.SOUND_EVENT, sonicSound.getId(), sonicSound);
    }
}
