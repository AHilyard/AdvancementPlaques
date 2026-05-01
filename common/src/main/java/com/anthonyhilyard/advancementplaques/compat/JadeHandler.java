package com.anthonyhilyard.advancementplaques.compat;

import snownee.jade.Jade;
import snownee.jade.impl.config.WailaConfig;

public class JadeHandler
{
	private static boolean previousState = true;
	private static boolean disabled = false;

	public static void disableJade()
	{
		WailaConfig.General Config = Jade.config().general();
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
		WailaConfig.General Config = Jade.config().general();
		if (disabled)
		{
			Config.setDisplayTooltip(previousState);
			disabled = false;
		}
	}
}