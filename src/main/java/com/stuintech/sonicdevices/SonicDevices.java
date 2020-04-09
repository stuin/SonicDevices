package com.stuintech.sonicdevices;

import com.stuintech.sonicdevices.extensions.ILoader;
import com.stuintech.sonicdevices.items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class SonicDevices implements ModInitializer {
	public static final String MODID = "sonicdevices";
	public static final Logger LOGGER = LogManager.getLogger("sonicdevices");

	public static final ItemGroup SONIC_GROUP = FabricItemGroupBuilder.create(
			new Identifier(MODID, "group"))
			.icon(() -> new ItemStack(ModItems.mark7[0]))
			.build();

	//Mod integration
	private static final String[][] loadExtensions = new String[][] {
			{"RebornCore",
					"reborncore.api.ICustomToolHandler", "com.stuintech.sonicdevices.extensions.reborn.RebornCore"}
	};

	@Override
	public void onInitialize() {
		//Register mod resources
		ModItems.register();
		ModSounds.register();

		//Try loading extensions
		for(String[] s : loadExtensions) {
			try {
				Class.forName(s[1]);
				((ILoader) Class.forName(s[2]).newInstance()).onInitialize();
				LOGGER.info(s[0] + " extension successfully initialized");
			} catch (Exception e) {
				LOGGER.debug(s[0] + "extension not found");
			}
		}
	}
}
