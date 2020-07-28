package com.stuintech.sonicdevices;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static final Identifier sonicSoundID = new Identifier(SonicDevices.MODID, "sonic_sound");
    public static final SoundEvent sonicSound = new SoundEvent(sonicSoundID);

    public static void register() {
        Registry.register(Registry.SOUND_EVENT, sonicSoundID, sonicSound);
    }
}
