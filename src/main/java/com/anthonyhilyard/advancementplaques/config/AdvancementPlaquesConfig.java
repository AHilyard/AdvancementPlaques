package com.anthonyhilyard.advancementplaques.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.electronwill.nightconfig.core.Config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.LongValue;

public class AdvancementPlaquesConfig
{
	public static final ForgeConfigSpec SPEC;
	public static final AdvancementPlaquesConfig INSTANCE;
	static
	{
		Config.setInsertionOrderPreserved(true);
		Pair<AdvancementPlaquesConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(AdvancementPlaquesConfig::new);
		SPEC = specPair.getRight();
		INSTANCE = specPair.getLeft();
	}

	public final BooleanValue onTop;
	public final IntValue distance;
	public final IntValue horizontalOffset;
	public final BooleanValue hideWaila;

	public final BooleanValue tasks;
	public final BooleanValue goals;
	public final BooleanValue challenges;

	public final LongValue titleColor;
	public final LongValue nameColor;
	
	public final DoubleValue taskEffectFadeInTime;
	public final DoubleValue taskEffectFadeOutTime;
	public final DoubleValue taskDuration;
	
	public final DoubleValue goalEffectFadeInTime;
	public final DoubleValue goalEffectFadeOutTime;
	public final DoubleValue goalDuration;

	public final DoubleValue challengeEffectFadeInTime;
	public final DoubleValue challengeEffectFadeOutTime;
	public final DoubleValue challengeDuration;

	public final ConfigValue<List<? extends String>> whitelist;
	public final ConfigValue<List<? extends String>> blacklist;
	public final BooleanValue muteTasks;
	public final BooleanValue muteGoals;
	public final BooleanValue muteChallenges;

	public AdvancementPlaquesConfig(ForgeConfigSpec.Builder build)
	{
		build.comment("Client Configuration").push("client").push("visual_options");

		onTop = build.comment(" If plaques should show on the top of the screen.").define("on_top", true);
		distance = build.comment(" The distance from the top or bottom of the screen, in pixels.").defineInRange("distance", 16, 8, 256);
		horizontalOffset = build.comment(" The horizontal offset from the center, in pixels.").defineInRange("horizontal_offset", 0, -256, 256);
		hideWaila = build.comment(" Hide waila/hwyla/jade popups while plaques are showing.").define("hide_waila", false);

		tasks = build.comment(" If plaques should show for task advancements (normal advancements).").define("tasks", true);
		goals = build.comment(" If plaques should show for goal advancements (medium-difficulty advancements).").define("goals", true);
		challenges = build.comment(" If plaques should show for challenge advancements (high-difficulty advancements).").define("challenges", true);

		titleColor = build.comment(" Text color to use for plaque titles (like \"Advancement made!\"). Can be entered as an 8-digit hex color code 0xAARRGGBB for convenience.").defineInRange("title_color", 0xFF332200L, 0x00000000L, 0xFFFFFFFFL);
		nameColor = build.comment(" Text color to use for advancement names on plaques. Can be entered as an 8-digit hex color code 0xAARRGGBB for convenience.").defineInRange("name_color", 0xFFFFFFFFL, 0x00000000L, 0xFFFFFFFFL);

		build.pop().push("duration_options");

		taskEffectFadeInTime = build.comment(" Duration of the shiny effect fade in for tasks.").defineInRange("task_effect_fadein", 0.5, 0.1, 3.0);
		taskEffectFadeOutTime = build.comment(" Duration of the shiny effect fade out for tasks.").defineInRange("task_effect_fadeout", 1.5, 0.1, 3.0);
		taskDuration = build.comment(" Duration of the plaques for tasks (minus the effect fade in/out durations).").defineInRange("task_duration", 7.0, 2.0, 10.0);
		
		goalEffectFadeInTime = build.comment(" Duration of the shiny effect fade in for goals.").defineInRange("goal_effect_fadein", 0.5, 0.1, 3.0);
		goalEffectFadeOutTime = build.comment(" Duration of the shiny effect fade out for goals.").defineInRange("goal_effect_fadeout", 1.5, 0.1, 3.0);
		goalDuration = build.comment(" Duration of the plaques for goals (minus the effect fade in/out durations).").defineInRange("goal_duration", 7.0, 2.0, 10.0);

		challengeEffectFadeInTime = build.comment(" Duration of the shiny effect fade in for challenges.").defineInRange("challenge_effect_fadein", 1.25, 0.1, 3.0);
		challengeEffectFadeOutTime = build.comment(" Duration of the shiny effect fade out for challenges.").defineInRange("challenge_effect_fadeout", 1.5, 0.1, 3.0);
		challengeDuration = build.comment(" Duration of the plaques for challenges (minus the effect fade in/out durations).").defineInRange("challenge_duration", 7.0, 2.0, 10.0);

		build.pop().push("functionality_options");

		blacklist = build.comment(" Blacklist of advancements to never show plaques for.  Takes precedence over whitelist if they conflict.\n" +
								  " Options:\n" +
								  "  Advancement ID (eg. \"minecraft:adventure/adventuring_time\")\n" +
								  "  Mod ID (Omit the colon, eg. \"minecraft\")\n" +
								  "  Advancement Category (End with a /, eg. \"minecraft:story/\")").defineListAllowEmpty(Arrays.asList("blacklist"), () -> new ArrayList<String>(), e -> true );
		whitelist = build.comment(" Whitelist of advancements to show plaques for.  Leave empty to display for all.\n" +
								  " Same options available as blacklist.").defineListAllowEmpty(Arrays.asList("whitelist"), () -> new ArrayList<String>(), e -> true );
		muteTasks = build.comment(" If task sounds should be muted.").define("mute_tasks", false);
		muteGoals = build.comment(" If goal sounds should be muted.").define("mute_goals", false);
		muteChallenges = build.comment(" If challenge sounds should be muted.").define("mute_challenges", false);

		build.pop().pop();
	}

	private static boolean advancementEntryMatches(Advancement advancement, String entry)
	{
		ResourceLocation advancementId = advancement.getId();

		// Exact match.
		if (advancementId.toString().equals(entry))
		{
			return true;
		}

		// Mod match.
		if (!entry.contains(":") && advancementId.getNamespace().toString().equals(entry))
		{
			return true;
		}

		// Category match.
		if (entry.endsWith("/") && advancementId.toString().startsWith(entry))
		{
			return true;
		}

		return false;
	}

	public static boolean showPlaqueForAdvancement(Advancement advancement)
	{
		DisplayInfo displayInfo = advancement.getDisplay();

		// First check if the advancement is blacklisted.
		for (String blacklistEntry : AdvancementPlaquesConfig.INSTANCE.blacklist.get())
		{
			if (advancementEntryMatches(advancement, blacklistEntry))
			{
				return false;
			}
		}

		// Now check if the advancement type is filtered out.
		boolean advancementFiltered = !((displayInfo.getFrame() == FrameType.TASK && AdvancementPlaquesConfig.INSTANCE.tasks.get()) ||
										(displayInfo.getFrame() == FrameType.GOAL && AdvancementPlaquesConfig.INSTANCE.goals.get()) ||
										(displayInfo.getFrame() == FrameType.CHALLENGE && AdvancementPlaquesConfig.INSTANCE.challenges.get()));
		if (advancementFiltered)
		{
			// Check the whitelist to see if the advancement should be shown anyways.
			for (String whitelistEntry : AdvancementPlaquesConfig.INSTANCE.whitelist.get())
			{
				if (advancementEntryMatches(advancement, whitelistEntry))
				{
					return true;
				}
			}
		}
		return !advancementFiltered;
	}
}