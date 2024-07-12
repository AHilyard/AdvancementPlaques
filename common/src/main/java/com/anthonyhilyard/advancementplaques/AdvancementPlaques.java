package com.anthonyhilyard.advancementplaques;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.anthonyhilyard.advancementplaques.config.AdvancementPlaquesConfig;

public class AdvancementPlaques
{
	public static final String MODID = "advancementplaques";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final ResourceLocation TEXTURE_PLAQUES = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/plaques.png");
	public static final ResourceLocation TEXTURE_PLAQUE_EFFECTS = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/plaqueeffect.png");

	public static final ResourceLocation TASK_COMPLETE_ID = ResourceLocation.fromNamespaceAndPath(MODID, "ui.toast.task_complete");
	public static final ResourceLocation GOAL_COMPLETE_ID = ResourceLocation.fromNamespaceAndPath(MODID, "ui.toast.goal_complete");
	public static final SoundEvent TASK_COMPLETE = SoundEvent.createVariableRangeEvent(TASK_COMPLETE_ID);
	public static final SoundEvent GOAL_COMPLETE = SoundEvent.createVariableRangeEvent(GOAL_COMPLETE_ID);
	public static void init()
	{
		AdvancementPlaquesConfig.register(AdvancementPlaquesConfig.class, MODID);
	}
}