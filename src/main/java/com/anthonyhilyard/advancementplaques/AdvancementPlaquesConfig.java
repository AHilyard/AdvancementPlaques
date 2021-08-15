package com.anthonyhilyard.advancementplaques;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.IConfigEvent;

public class AdvancementPlaquesConfig
{
	public static final ForgeConfigSpec SPEC;
	public static final AdvancementPlaquesConfig INSTANCE;
	static
	{
		Pair<AdvancementPlaquesConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(AdvancementPlaquesConfig::new);
		SPEC = specPair.getRight();
		INSTANCE = specPair.getLeft();
	}

	public final BooleanValue onTop;
	public final IntValue distance;
	public final BooleanValue hideWaila;

	public final BooleanValue tasks;
	public final BooleanValue goals;
	public final BooleanValue challenges;

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
	public final BooleanValue muteTasks;
	public final BooleanValue muteGoals;
	public final BooleanValue muteChallenges;

	public AdvancementPlaquesConfig(ForgeConfigSpec.Builder build)
	{
		build.comment("Client Configuration").push("client").push("visual_options");

		onTop = build.comment("If plaques should show on the top of the screen.").define("on_top", true);
		distance = build.comment("The distance from the top or bottom of the screen, in pixels.").defineInRange("distance", 16, 8, 256);
		hideWaila = build.comment("Hide waila/hwyla/jade popups while plaques are showing.").define("hide_waila", false);

		tasks = build.comment("If plaques should show for task advancements (normal advancements).").define("tasks", true);
		goals = build.comment("If plaques should show for goal advancements (medium-difficulty advancements).").define("goals", true);
		challenges = build.comment("If plaques should show for challenge advancements (high-difficulty advancements).").define("challenges", true);

		build.pop().push("duration_options");

		taskEffectFadeInTime = build.comment("Duration of the shiny effect fade in for tasks.").defineInRange("task_effect_fadein", 0.5, 0.1, 3.0);
		taskEffectFadeOutTime = build.comment("Duration of the shiny effect fade out for tasks.").defineInRange("task_effect_fadeout", 1.5, 0.1, 3.0);
		taskDuration = build.comment("Duration of the plaques for tasks (minus the effect fade in/out durations).").defineInRange("task_duration", 7.0, 2.0, 10.0);
		
		goalEffectFadeInTime = build.comment("Duration of the shiny effect fade in for goals.").defineInRange("goal_effect_fadein", 0.5, 0.1, 3.0);
		goalEffectFadeOutTime = build.comment("Duration of the shiny effect fade out for goals.").defineInRange("goal_effect_fadeout", 1.5, 0.1, 3.0);
		goalDuration = build.comment("Duration of the plaques for goals (minus the effect fade in/out durations).").defineInRange("goal_duration", 7.0, 2.0, 10.0);

		challengeEffectFadeInTime = build.comment("Duration of the shiny effect fade in for challenges.").defineInRange("challenge_effect_fadein", 1.25, 0.1, 3.0);
		challengeEffectFadeOutTime = build.comment("Duration of the shiny effect fade out for challenges.").defineInRange("challenge_effect_fadeout", 1.5, 0.1, 3.0);
		challengeDuration = build.comment("Duration of the plaques for challenges (minus the effect fade in/out durations).").defineInRange("challenge_duration", 7.0, 2.0, 10.0);

		build.pop().push("functionality_options");

		whitelist = build.comment("Whitelist of advancements to show plaques for.  Leave empty to display for all.").defineListAllowEmpty(Arrays.asList("whitelist"), () -> new ArrayList<String>(), e -> ResourceLocation.isValidResourceLocation((String)e) );
		muteTasks = build.comment("If task sounds should be muted.").define("mute_tasks", false);
		muteGoals = build.comment("If goal sounds should be muted.").define("mute_goals", false);
		muteChallenges = build.comment("If challenge sounds should be muted.").define("mute_challenges", false);

		build.pop().pop();
	}

	@SubscribeEvent
	public static void onLoad(IConfigEvent e)
	{
		if (e.getConfig().getModId().equals(Loader.MODID))
		{
			Loader.LOGGER.info("Advancement Plaques config reloaded.");
		}
	}

}