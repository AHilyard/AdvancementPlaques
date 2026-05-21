package com.anthonyhilyard.advancementplaques;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.anthonyhilyard.advancementplaques.config.AdvancementPlaquesConfig;

public class AdvancementPlaques
{
	public static final String MODID = "advancementplaques";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final Identifier TEXTURE_PLAQUES = Identifier.fromNamespaceAndPath(MODID, "textures/gui/plaques.png");
	public static final Identifier TEXTURE_PLAQUE_EFFECTS = Identifier.fromNamespaceAndPath(MODID, "textures/gui/plaqueeffect.png");

	public static final Identifier TASK_COMPLETE_ID = Identifier.fromNamespaceAndPath(MODID, "ui.toast.task_complete");
	public static final Identifier GOAL_COMPLETE_ID = Identifier.fromNamespaceAndPath(MODID, "ui.toast.goal_complete");
	public static final SoundEvent TASK_COMPLETE = SoundEvent.createVariableRangeEvent(TASK_COMPLETE_ID);
	public static final SoundEvent GOAL_COMPLETE = SoundEvent.createVariableRangeEvent(GOAL_COMPLETE_ID);
	public static void init()
	{
		AdvancementPlaquesConfig.register(AdvancementPlaquesConfig.class, MODID);
	}
}