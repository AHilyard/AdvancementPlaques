package com.anthonyhilyard.advancementplaques;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AdvancementPlaques
{
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

	public static final ResourceLocation TEXTURE_PLAQUES = new ResourceLocation(Loader.MODID, "textures/gui/plaques.png");
	public static final ResourceLocation TEXTURE_PLAQUE_EFFECTS = new ResourceLocation(Loader.MODID, "textures/gui/plaqueeffect.png");

	public static final ResourceLocation TASK_COMPLETE_ID = new ResourceLocation(Loader.MODID, "ui.toast.task_complete");
	public static final ResourceLocation GOAL_COMPLETE_ID = new ResourceLocation(Loader.MODID, "ui.toast.goal_complete");
	public static final SoundEvent TASK_COMPLETE = new SoundEvent(TASK_COMPLETE_ID);
	public static final SoundEvent GOAL_COMPLETE = new SoundEvent(GOAL_COMPLETE_ID);

	public static void onClientSetup()
	{
		AdvancementPlaquesConfig.init();

		ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
			client.toast = new AdvancementPlaquesToastGui(client);
		});
	}
}