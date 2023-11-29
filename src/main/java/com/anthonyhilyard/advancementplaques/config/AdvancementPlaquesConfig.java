package com.anthonyhilyard.advancementplaques.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.anthonyhilyard.advancementplaques.Loader;
import com.electronwill.nightconfig.core.Config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = Loader.MODID, bus = Bus.MOD)
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

	public final DoubleValue taskVolume;
	public final DoubleValue goalVolume;
	public final DoubleValue challengeVolume;

	private final Supplier<ConfigValue<?>> titleSupplier;
	private final Supplier<ConfigValue<?>> nameSupplier;

	private TextColor titleColor = null;
	private TextColor nameColor = null;

	public AdvancementPlaquesConfig(ForgeConfigSpec.Builder build)
	{
		build.comment("Client Configuration").push("client").push("visual_options");

		onTop = build.comment(" If plaques should show on the top of the screen.").define("on_top", true);
		distance = build.comment(" The distance from the top or bottom of the screen, in pixels.").defineInRange("distance", 16, 8, 256);
		hideWaila = build.comment(" Hide waila/hwyla/jade popups while plaques are showing.").define("hide_waila", false);

		tasks = build.comment(" If plaques should show for task advancements (normal advancements).").define("tasks", true);
		goals = build.comment(" If plaques should show for goal advancements (medium-difficulty advancements).").define("goals", true);
		challenges = build.comment(" If plaques should show for challenge advancements (high-difficulty advancements).").define("challenges", true);

		// Parse the color values.
		ConfigValue<?> titleColorValue = build.comment(" Text color to use for plaque titles (like \"Advancement made!\"). Can be entered as an 8-digit hex color code #AARRGGBB for convenience. If Prism library is installed, any Prism color definition is supported.").define("title_color", "#FF332200", v -> validateColor(v));
		ConfigValue<?> nameColorValue =  build.comment(" Text color to use for advancement names on plaques. Can be entered as an 8-digit hex color code #AARRGGBB for convenience. If Prism library is installed, any Prism color definition is supported.").define("name_color", "#FFFFFFFF", v -> validateColor(v));
		
		titleSupplier = () -> titleColorValue;
		nameSupplier = () -> nameColorValue;

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

		whitelist = build.comment(" Whitelist of advancements to show plaques for.  Leave empty to display for all.").defineListAllowEmpty(Arrays.asList("whitelist"), () -> new ArrayList<String>(), e -> ResourceLocation.isValidResourceLocation((String)e) );
		taskVolume = build.comment(" Volume of task sounds.  Set to 0 to mute.").defineInRange("task_volume", 1.0, 0.0, 1.0);
		goalVolume = build.comment(" Volume of goal sounds.  Set to 0 to mute.").defineInRange("goal_volume", 1.0, 0.0, 1.0);
		challengeVolume = build.comment(" Volume of challenge sounds.  Set to 0 to mute.").defineInRange("challenge_volume", 1.0, 0.0, 1.0);

		build.pop().pop();
	}

	public TextColor getTitleColor(float alpha)
	{
		// If the title color hasn't been resolved, do it now.
		if (titleColor == null)
		{
			resolveColors();
		}

		return applyAlpha(titleColor, alpha);
	}

	public TextColor getNameColor(float alpha)
	{
		// If the name color hasn't been resolved, do it now.
		if (nameColor == null)
		{
			resolveColors();
		}

		return applyAlpha(nameColor, alpha);
	}

	private TextColor applyAlpha(TextColor color, float alpha)
	{
		int tempColor = color.getValue();
		int tempAlpha = (int)(((tempColor >> 24) & 0xFF) * alpha);
		return TextColor.fromRgb((tempColor & 0xFFFFFF) | (tempAlpha << 24));
	}

	private static void resolveColors()
	{
		INSTANCE.titleColor = getColor(INSTANCE.titleSupplier.get().get(), TextColor.fromRgb(0xFF332200));
		INSTANCE.nameColor = getColor(INSTANCE.nameSupplier.get().get(), TextColor.fromRgb(0xFFFFFFFF));
	}

	private static boolean validateColor(Object value)
	{
		return getColor(value, null) != null;
	}

	private static TextColor getColor(Object value, TextColor defaultColor)
	{
		// If Prism is available, let it parse the value.
		if (ModList.get().isLoaded("prism"))
		{
			try
			{
				return (TextColor)Class.forName("com.anthonyhilyard.advancementplaques.compat.PrismHandler").getMethod("getColor", Object.class).invoke(null, value);
			}
			catch (Exception e)
			{
				// Something went wrong, oops.
			}
		}

		// Otherwise, parse the value as hex.
		if (value instanceof String string)
		{
			TextColor parsedColor = TextColor.parseColor(string);
			if (parsedColor == null)
			{
				string = "#" + string.replace("0x", "").replace("#", "");
				parsedColor = TextColor.parseColor(string);
			}

			if (parsedColor != null)
			{
				return parsedColor;
			}
		}
		else if (value instanceof Number number)
		{
			return TextColor.fromRgb(number.intValue());
		}
		return defaultColor;
	}

	@SubscribeEvent
	public static void onReload(ModConfigEvent.Reloading e)
	{
		if (e.getConfig().getModId().equals(Loader.MODID))
		{
			// Also resolve the colors again.
			resolveColors();
		}
	}
}