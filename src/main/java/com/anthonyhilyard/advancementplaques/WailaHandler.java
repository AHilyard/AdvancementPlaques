package com.anthonyhilyard.advancementplaques;

import mcp.mobius.waila.Waila;

public class WailaHandler
{
	private static boolean previousState = true;
	private static boolean disabled = false;

	public static void disableWaila()
	{
		boolean currentState = Waila.config.get().getGeneral().shouldDisplayTooltip();
		if (!disabled || currentState)
		{
			previousState = currentState;
			Waila.config.get().getGeneral().setDisplayTooltip(false);
			disabled = true;
		}
	}

	public static void enableWaila()
	{
		if (disabled)
		{
			Waila.config.get().getGeneral().setDisplayTooltip(previousState);
			disabled = false;
		}
	}
	
}
