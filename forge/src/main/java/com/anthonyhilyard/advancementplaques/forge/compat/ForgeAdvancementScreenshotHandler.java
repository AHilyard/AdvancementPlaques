package com.anthonyhilyard.advancementplaques.forge.compat;

import com.anthonyhilyard.advancementplaques.compat.IAdvancementScreenshotHandler;
import com.natamus.advancementscreenshot_common_forge.util.Util;
import net.minecraft.network.chat.Component;

public class ForgeAdvancementScreenshotHandler implements IAdvancementScreenshotHandler
{
	@Override
	public void takeScreenshot(Component title)
	{
		Util.takeScreenshot(title);
	}
}
