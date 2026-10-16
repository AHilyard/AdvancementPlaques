package com.anthonyhilyard.advancementplaques.compat;

import java.lang.reflect.Method;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import net.minecraft.network.chat.Component;

/**
 * Calls Advancement Screenshot's Util.takeScreenshot(Component) by reflection, so that a missing or moved class
 * is logged instead of crashing the game.
 */
public class AdvancementScreenshotCompat
{
	private final String[] utilClasses;
	private Method takeScreenshotMethod = null;
	private boolean resolved = false;

	/**
	 * @param utilClasses The fully qualified names of the Util class to try, in order of preference.
	 */
	public AdvancementScreenshotCompat(String... utilClasses)
	{
		this.utilClasses = utilClasses;
	}

	private synchronized Method resolveMethod()
	{
		if (!resolved)
		{
			resolved = true;
			for (String className : utilClasses)
			{
				try
				{
					takeScreenshotMethod = Class.forName(className).getMethod("takeScreenshot", Component.class);
					break;
				}
				catch (ReflectiveOperationException | LinkageError e)
				{
					// Not present in this version of Advancement Screenshot, try the next one.
				}
			}

			if (takeScreenshotMethod == null)
			{
				AdvancementPlaques.LOGGER.warn("Advancement Screenshot is installed, but its screenshot method could not be found. Screenshots will be skipped.");
			}
		}
		return takeScreenshotMethod;
	}

	public void takeScreenshot(Component title)
	{
		Method method = resolveMethod();
		if (method != null)
		{
			try
			{
				method.invoke(null, title);
			}
			catch (ReflectiveOperationException | LinkageError e)
			{
				AdvancementPlaques.LOGGER.error("Failed to take an advancement screenshot.", e);
			}
		}
	}
}
