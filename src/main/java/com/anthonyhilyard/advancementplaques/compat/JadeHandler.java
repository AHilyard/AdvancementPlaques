package com.anthonyhilyard.advancementplaques.compat;

import snownee.jade.Jade;
import snownee.jade.impl.config.WailaConfig.ConfigGeneral;

public class JadeHandler
{
	private static boolean previousState = true;
	private static boolean disabled = false;

	public static void disableJade()
	{
		ConfigGeneral Config = Jade.CONFIG.get().getGeneral();
		boolean currentState = Config.shouldDisplayTooltip();
		if (!disabled || currentState)
		{
			previousState = currentState;
			Config.setDisplayTooltip(false);
			disabled = true;
		}
	}

	public static void enableJade()
	{
		ConfigGeneral Config = Jade.CONFIG.get().getGeneral();
		if (disabled)
		{
			Config.setDisplayTooltip(previousState);
			disabled = false;
		}
	}
}
