package com.anthonyhilyard.advancementplaques.client;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.anthonyhilyard.advancementplaques.AdvancementPlaques;
import com.anthonyhilyard.advancementplaques.ui.ToastComponentWrapper;

import net.minecraft.client.Minecraft;

public class AdvancementPlaquesClient
{
	public static void wrapToasts(Minecraft minecraft)
	{
		try
		{
			if (minecraft.toast != null)
			{
				AdvancementPlaques.LOGGER.debug("Installing Advancement Plaques toast component.");
				minecraft.toast = new ToastComponentWrapper(minecraft, minecraft.toast);
			}
			else
			{
				AdvancementPlaques.LOGGER.debug("Unable to update Toast GUI, Advancement Plaques will not function properly. Maybe another mod is interfering?");
			}
		}
		catch (Exception e)
		{
			AdvancementPlaques.LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
}
