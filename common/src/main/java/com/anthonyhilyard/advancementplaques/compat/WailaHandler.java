package com.anthonyhilyard.advancementplaques.compat;

import mcp.mobius.waila.WailaClient;

public class WailaHandler
{
	private static boolean previousState = true;
	private static boolean disabled = false;

	public static void disableWaila()
	{
		boolean currentState = WailaClient.CONFIG.get().getGeneral().isDisplayTooltip();
		if (!disabled || currentState)
		{
			previousState = currentState;
			WailaClient.CONFIG.get().getGeneral().setDisplayTooltip(false);
			disabled = true;
		}
	}

	public static void enableWaila()
	{
		if (disabled)
		{
			WailaClient.CONFIG.get().getGeneral().setDisplayTooltip(previousState);
			disabled = false;
		}
	}
}
