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
	@Comment("The horizontal offset from the center, in pixels.")
	public int horizontalOffset = 0;
	@Comment("Hide waila/hwyla/jade/wthit popups while plaques are showing.")
	public boolean hideWaila = false;

	@Comment("If plaques should show for task advancements (normal advancements).")
	public boolean tasks = true;
	@Comment("If plaques should show for goal advancements (medium-difficulty advancements).")
	public boolean goals = true;
	@Comment("If plaques should show for challenge advancements (high-difficulty advancements).")
	public boolean challenges = true;

	@Comment("Text color to use for plaque titles (like \"Advancement made!\"). Can be entered as an 8-digit LOWERCASE hex color code 0xaarrggbb for convenience.")
	public long titleColor = 0xFF332200L;
	@Comment("Text color to use for advancement names on plaques. Can be entered as an 8-digit LOWERCASE hex color code 0xaarrggbb for convenience.")
	public long nameColor = 0xFFFFFFFFL;

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
	@Comment("If task sounds should be muted.")
	public boolean muteTasks = false;
	@Comment("If goal sounds should be muted.")
	public boolean muteGoals = false;
	@Comment("If challenge sounds should be muted.")
	public boolean muteChallenges = false;
}