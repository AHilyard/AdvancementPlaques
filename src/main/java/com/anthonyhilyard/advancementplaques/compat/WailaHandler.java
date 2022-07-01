package com.anthonyhilyard.advancementplaques.compat;

import com.anthonyhilyard.advancementplaques.Loader;

import mcp.mobius.waila.Waila;

public class WailaHandler
{
	private static boolean previousState = true;
	private static boolean disabled = false;

	private static Object configObject = null;

	static
	{
		boolean objectSet = false;
		try
		{
			Object CONFIG = Waila.class.getDeclaredField("CONFIG").get(null);
			Object get = CONFIG.getClass().getDeclaredMethod("get").invoke(CONFIG);
			configObject = Class.forName("mcp.mobius.waila.config.WailaConfig").getDeclaredMethod("getGeneral").invoke(get);
			objectSet = true;
		}
		catch (Exception e) { }

		if (!objectSet)
		{
			try
			{
				Object CONFIG = Waila.class.getDeclaredField("CONFIG").get(null);
				Object get = CONFIG.getClass().getDeclaredMethod("get").invoke(CONFIG);
				configObject = Class.forName("mcp.mobius.waila.api.config.WailaConfig").getDeclaredMethod("getGeneral").invoke(get);
			}
			catch (Exception e) { }
		}

		if (configObject == null)
		{
			Loader.LOGGER.error("Could not find a valid Waila configuration object, Advancement Plaques will not be able to hide Waila popups.  Maybe they changed their API again, try a different Waila variant.");
		}
	}

	private static boolean shouldDisplayTooltip()
	{
		boolean result = true;
		boolean stateSet = false;
		try
		{
			result = (boolean) configObject.getClass().getDeclaredMethod("isDisplayTooltip").invoke(configObject);
			stateSet = true;
		}
		catch (Exception e) {}

		if (!stateSet)
		{
			try
			{
				result = (boolean) configObject.getClass().getDeclaredMethod("shouldDisplayTooltip").invoke(configObject);
			}
			catch (Exception e) {}
		}
		return result;
	}

	private static void setDisplayTooltip(boolean displayTooltip)
	{
		try
		{
			configObject.getClass().getDeclaredMethod("setDisplayTooltip", boolean.class).invoke(configObject, displayTooltip);

		}
		catch (Exception e) {}
	}

	public static void disableWaila()
	{
		if (configObject == null)
		{
			return;
		}

		boolean currentState = shouldDisplayTooltip();
		if (!disabled || currentState)
		{
			previousState = currentState;
			setDisplayTooltip(false);
			disabled = true;
		}
	}

	public static void enableWaila()
	{
		if (configObject == null)
		{
			return;
		}
		if (disabled)
		{
			setDisplayTooltip(previousState);
			disabled = false;
		}
	}
}
