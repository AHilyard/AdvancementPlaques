package com.anthonyhilyard.advancementplaques.config;

import java.util.List;
import java.util.function.Supplier;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.iceberg.config.IcebergConfig;
import com.anthonyhilyard.iceberg.services.IIcebergConfigSpecBuilder;
import com.anthonyhilyard.iceberg.services.Services;
import com.google.common.collect.Lists;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;

public final class AdvancementPlaquesConfig extends IcebergConfig<AdvancementPlaquesConfig>
{
	public static AdvancementPlaquesConfig getInstance() { return (AdvancementPlaquesConfig)configInstances.get(AdvancementPlaques.MODID); }

	public final Supplier<Boolean> onTop;
	public final Supplier<Integer> distance;
	public final Supplier<Integer> horizontalOffset;
	public final Supplier<Boolean> hideWaila;

	public final Supplier<Boolean> tasks;
	public final Supplier<Boolean> goals;
	public final Supplier<Boolean> challenges;
	
	public final Supplier<Double> taskEffectFadeInTime;
	public final Supplier<Double> taskEffectFadeOutTime;
	public final Supplier<Double> taskDuration;
	
	public final Supplier<Double> goalEffectFadeInTime;
	public final Supplier<Double> goalEffectFadeOutTime;
	public final Supplier<Double> goalDuration;

	public final Supplier<Double> challengeEffectFadeInTime;
	public final Supplier<Double> challengeEffectFadeOutTime;
	public final Supplier<Double> challengeDuration;

	public final Supplier<List<? extends String>> whitelist;
	public final Supplier<List<? extends String>> blacklist;

	public final Supplier<Double> taskVolume;
	public final Supplier<Double> goalVolume;
	public final Supplier<Double> challengeVolume;

	private final Supplier<Supplier<?>> titleSupplier;
	private final Supplier<Supplier<?>> nameSupplier;

	private TextColor titleColor = null;
	private TextColor nameColor = null;

	protected AdvancementPlaquesConfig(IIcebergConfigSpecBuilder build)
	{
		build.comment("Client Configuration").push("client").push("visual_options");

		onTop = build.comment(" If plaques should show on the top of the screen.").add("on_top", true);
		distance = build.comment(" The distance from the top or bottom of the screen, in pixels.").addInRange("distance", 16, 8, 256);
		horizontalOffset = build.comment(" The horizontal offset from the center, in pixels.").addInRange("horizontal_offset", 0, -256, 256);
		hideWaila = build.comment(" Hide waila/hwyla/jade popups while plaques are showing.").add("hide_waila", false);

		tasks = build.comment(" If plaques should show for task advancements (normal advancements).").add("tasks", true);
		goals = build.comment(" If plaques should show for goal advancements (medium-difficulty advancements).").add("goals", true);
		challenges = build.comment(" If plaques should show for challenge advancements (high-difficulty advancements).").add("challenges", true);

		// Parse the color values.
		Supplier<?> titleColorValue = build.comment(" Text color to use for plaque titles (like \"Advancement made!\"). Can be entered as an 8-digit hex color code #AARRGGBB for convenience. If Prism library is installed, any Prism color definition is supported.").add("title_color", "#FF332200", v -> validateColor(v));
		Supplier<?> nameColorValue =  build.comment(" Text color to use for advancement names on plaques. Can be entered as an 8-digit hex color code #AARRGGBB for convenience. If Prism library is installed, any Prism color definition is supported.").add("name_color", "#FFFFFFFF", v -> validateColor(v));

		titleSupplier = () -> titleColorValue;
		nameSupplier = () -> nameColorValue;

		build.pop().push("duration_options");

		taskEffectFadeInTime = build.comment(" Duration of the shiny effect fade in for tasks.").addInRange("task_effect_fadein", 0.5, 0.1, 3.0);
		taskEffectFadeOutTime = build.comment(" Duration of the shiny effect fade out for tasks.").addInRange("task_effect_fadeout", 1.5, 0.1, 3.0);
		taskDuration = build.comment(" Duration of the plaques for tasks (minus the effect fade in/out durations).").addInRange("task_duration", 7.0, 2.0, 10.0);
		
		goalEffectFadeInTime = build.comment(" Duration of the shiny effect fade in for goals.").addInRange("goal_effect_fadein", 0.5, 0.1, 3.0);
		goalEffectFadeOutTime = build.comment(" Duration of the shiny effect fade out for goals.").addInRange("goal_effect_fadeout", 1.5, 0.1, 3.0);
		goalDuration = build.comment(" Duration of the plaques for goals (minus the effect fade in/out durations).").addInRange("goal_duration", 7.0, 2.0, 10.0);

		challengeEffectFadeInTime = build.comment(" Duration of the shiny effect fade in for challenges.").addInRange("challenge_effect_fadein", 1.25, 0.1, 3.0);
		challengeEffectFadeOutTime = build.comment(" Duration of the shiny effect fade out for challenges.").addInRange("challenge_effect_fadeout", 1.5, 0.1, 3.0);
		challengeDuration = build.comment(" Duration of the plaques for challenges (minus the effect fade in/out durations).").addInRange("challenge_duration", 7.0, 2.0, 10.0);

		build.pop().push("functionality_options");

		blacklist = build.comment(" Blacklist of advancements to never show plaques for.  Takes precedence over whitelist if they conflict.\n" +
								  " Options:\n" +
								  "  Advancement ID (eg. \"minecraft:adventure/adventuring_time\")\n" +
								  "  Mod ID (Omit the colon, eg. \"minecraft\")\n" +
								  "  Advancement Category (End with a /, eg. \"minecraft:story/\")").addListAllowEmpty("blacklist", Lists.newArrayList(), e -> true );
		whitelist = build.comment(" Whitelist of advancements to show plaques for.  Leave empty to display for all.\n" +
								  " Same options available as blacklist.").addListAllowEmpty("whitelist", Lists.newArrayList(), e -> true );
		taskVolume = build.comment(" Volume of task sounds.  Set to 0 to mute.").addInRange("task_volume", 1.0, 0.0, 1.0);
		goalVolume = build.comment(" Volume of goal sounds.  Set to 0 to mute.").addInRange("goal_volume", 1.0, 0.0, 1.0);
		challengeVolume = build.comment(" Volume of challenge sounds.  Set to 0 to mute.").addInRange("challenge_volume", 1.0, 0.0, 1.0);

		build.pop().pop();
	}

	private static boolean advancementEntryMatches(AdvancementHolder advancementHolder, String entry)
	{
		Identifier advancementId = advancementHolder.id();

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

	public static boolean showPlaqueForAdvancement(AdvancementHolder advancementHolder)
	{
		// First check if the advancement is blacklisted.
		for (String blacklistEntry : AdvancementPlaquesConfig.getInstance().blacklist.get())
		{
			if (advancementEntryMatches(advancementHolder, blacklistEntry))
			{
				return false;
			}
		}

		DisplayInfo displayInfo = advancementHolder.value().display().orElse(null);

		// If this advancement doesn't have any display info for some reason, we can't show a plaque anyways.
		if (displayInfo == null)
		{
			return false;
		}

		// Now check if the advancement type is filtered out.
		boolean advancementFiltered = !((displayInfo.getType() == AdvancementType.TASK && AdvancementPlaquesConfig.getInstance().tasks.get()) ||
										(displayInfo.getType() == AdvancementType.GOAL && AdvancementPlaquesConfig.getInstance().goals.get()) ||
										(displayInfo.getType() == AdvancementType.CHALLENGE && AdvancementPlaquesConfig.getInstance().challenges.get()));
		if (advancementFiltered)
		{
			// Check the whitelist to see if the advancement should be shown anyways.
			for (String whitelistEntry : AdvancementPlaquesConfig.getInstance().whitelist.get())
			{
				if (advancementEntryMatches(advancementHolder, whitelistEntry))
				{
					return true;
				}
			}
		}
		return !advancementFiltered;
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
		getInstance().titleColor = getColor(getInstance().titleSupplier.get().get(), TextColor.fromRgb(0xFF332200));
		getInstance().nameColor = getColor(getInstance().nameSupplier.get().get(), TextColor.fromRgb(0xFFFFFFFF));
	}

	private static boolean validateColor(Object value)
	{
		return getColor(value, null) != null;
	}

	private static TextColor getColor(Object value, TextColor defaultColor)
	{
		// If Prism is available, let it parse the value.
		if (Services.getPlatformHelper().isModLoaded("prism"))
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
			TextColor parsedColor = TextColor.parseColor(string).result().orElse(null);
			if (parsedColor == null)
			{
				string = "#" + string.replace("0x", "").replace("#", "");
				parsedColor = TextColor.parseColor(string).result().orElse(null);
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

	@Override
	protected void onReload()
	{
		// Also resolve the colors again.
		resolveColors();
	}
}
