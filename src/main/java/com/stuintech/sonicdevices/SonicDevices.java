package com.stuintech.sonicdevices;

import com.stuintech.sonicdevices.block.ModBlocks;
import com.stuintech.sonicdevicesapi.ILoader;
import com.stuintech.sonicdevices.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class SonicDevices implements ModInitializer {
	public static final String MODID = "sonicdevices";
	public static final Logger LOGGER = LogManager.getLogger("SonicDevices");

	public static final ItemGroup SONIC_GROUP = FabricItemGroupBuilder.create(
			new Identifier(MODID, "group"))
			.icon(() -> new ItemStack(ModItems.mark7[0]))
			.build();

	//Mod integration
	private static final String[][] loadExtensions = new String[][] {
			{
					"Simple Drawers",
					"me.benfah.simpledrawers.api.drawer.holder.ItemHolder",
					"com.stuintech.sonicdevices.integration.SimpleDrawers"
			},
			{
					"EZPaS",
					"net.kqp.ezpas.block.entity.PullerPipeBlockEntity",
					"com.stuintech.sonicdevices.integration.EZPAS"
			}
	};

	@Override
	public void onInitialize() {
		//Register mod resources
		ModItems.register();
		ModSounds.register();
		ModBlocks.register();

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

		//Check for additional entrypoints
		List<ILoader> entries = FabricLoader.getInstance().getEntrypoints(MODID, ILoader.class);
		for(ILoader loader : entries)
			loader.onInitialize();

	}
}
