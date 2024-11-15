package com.anthonyhilyard.advancementplaques.compat;

import com.aetherteam.aether.api.AetherAdvancementSoundOverrides;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.sounds.SoundEvent;

public class AetherHandler
{
	public static SoundEvent getSoundOverride(AdvancementHolder advancement)
	{
		return AetherAdvancementSoundOverrides.retrieveOverride(advancement);
	}
}

