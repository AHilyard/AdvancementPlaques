package com.anthonyhilyard.advancementplaques;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.resources.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AdvancementPlaques
{
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

	public static final ResourceLocation TEXTURE_PLAQUES = new ResourceLocation(Loader.MODID, "textures/gui/plaques.png");
	public static final ResourceLocation TEXTURE_PLAQUE_EFFECTS = new ResourceLocation(Loader.MODID, "textures/gui/plaqueeffect.png");

	public static void onClientSetup()
	{
		AdvancementPlaquesConfig.init();

		ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
			client.toast = new AdvancementPlaquesToastGui(client);
		});
	}
}