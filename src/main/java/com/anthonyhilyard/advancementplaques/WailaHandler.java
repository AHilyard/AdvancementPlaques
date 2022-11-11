package com.anthonyhilyard.advancementplaques;

import mcjty.theoneprobe.config.Config;
import mcp.mobius.waila.Waila;

public class WailaHandler
{
	private static boolean previousState = true;
	private static boolean disabled = false;

	public static void disableWaila()
	{
		boolean currentState = Waila.CONFIG.get().getGeneral().shouldDisplayTooltip();
		if (!disabled || currentState)
		{
			previousState = currentState;
			Waila.CONFIG.get().getGeneral().setDisplayTooltip(false);
			disabled = true;
		}
	}

	public static void enableWaila()
	{
		if (disabled)
		{
			Waila.CONFIG.get().getGeneral().setDisplayTooltip(previousState);
			disabled = false;
		}
	}

	public static void disableTOP()
	{
		boolean currentState = Config.isVisible.get();
		if (!disabled || currentState)
		{
			previousState = currentState;
			Config.setVisible(false);
			disabled = true;
		}
	}
	public static void enableTOP()
	{
		if (disabled)
		{
			Config.setVisible(previousState);
			disabled = false;
		}
	}
}
