package com.anthonyhilyard.advancementplaques;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

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

	public AdvancementPlaquesConfig(ForgeConfigSpec.Builder build)
	{
		build.comment("Client Configuration").push("client").push("visual_options");

		// Display options for plaque placement.
		onTop = build.comment("If plaques should show on the top of the screen.").define("on_top", true);
		distance = build.comment("The distance from the top or bottom of the screen, in pixels.").defineInRange("distance", 42, 8, 256);
		hideWaila = build.comment("Hide waila/hwyla/jade popups while plaques are showing.").define("hide_waila", false);

		// Display options for advancement types.
		tasks = build.comment("If plaques should show for task advancements (normal advancements).").define("tasks", true);
		goals = build.comment("If plaques should show for goal advancements (medium-difficulty advancements).").define("goals", true);
		challenges = build.comment("If plaques should show for challenge advancements (high-difficulty advancements).").define("challenges", true);

		build.pop().pop();
	}

	@SubscribeEvent
	public static void onLoad(ModConfig.Loading e)
	{
		if (e.getConfig().getModId().equals(Loader.MODID))
		{
			Loader.LOGGER.info("Advancement Plaques config reloaded.");
		}
	}

}