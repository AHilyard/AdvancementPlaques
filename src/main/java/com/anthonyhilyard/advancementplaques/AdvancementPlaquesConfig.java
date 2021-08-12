package com.anthonyhilyard.advancementplaques;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;


@Config(name = "advancementplaques")
public class AdvancementPlaquesConfig implements ConfigData
{
	@ConfigEntry.Gui.Excluded
	static AdvancementPlaquesConfig INSTANCE;

	public static void init()
	{
		AutoConfig.register(AdvancementPlaquesConfig.class, JanksonConfigSerializer::new);
		INSTANCE = AutoConfig.getConfigHolder(AdvancementPlaquesConfig.class).getConfig();
	}

	@Comment("If plaques should show on the top of the screen.")
	public boolean onTop = true;
	@Comment("The distance from the top or bottom of the screen, in pixels.")
	public int distance = 16;
	@Comment("Hide waila/hwyla/jade popups while plaques are showing.")
	public boolean hideWaila = false;

	@Comment("If plaques should show for task advancements (normal advancements).")
	public boolean tasks = true;
	@Comment("If plaques should show for goal advancements (medium-difficulty advancements).")
	public boolean goals = true;
	@Comment("If plaques should show for challenge advancements (high-difficulty advancements).")
	public boolean challenges = true;

	@Comment("Duration of the shiny effect fade in for tasks.")
	public double taskEffectFadeInTime = 0.5;
	@Comment("Duration of the shiny effect fade out for tasks.")
	public double taskEffectFadeOutTime = 1.5;
	@Comment("Duration of the plaques for tasks (minus the effect fade in/out durations).")
	public double taskDuration = 7.0;

	@Comment("Duration of the shiny effect fade in for goals.")
	public double goalEffectFadeInTime = 0.5;
	@Comment("Duration of the shiny effect fade out for goals.")
	public double goalEffectFadeOutTime = 1.5;
	@Comment("Duration of the plaques for goals (minus the effect fade in/out durations).")
	public double goalDuration = 7.0;

	@Comment("Duration of the shiny effect fade in for challenges.")
	public double challengeEffectFadeInTime = 1.25;
	@Comment("Duration of the shiny effect fade out for challenges.")
	public double challengeEffectFadeOutTime = 1.5;
	@Comment("Duration of the plaques for challenges (minus the effect fade in/out durations).")
	public double challengeDuration = 7.0;

	@Comment("Whitelist of advancements to show plaques for.  Leave empty to display for all.")
	public List<String> whitelist = new ArrayList<String>();

	@Override
	public void validatePostLoad()
	{
		Loader.LOGGER.info("Whitelisted advancements:");
		for (String str : whitelist)
		{
			Loader.LOGGER.info(str);
		}
	}
}