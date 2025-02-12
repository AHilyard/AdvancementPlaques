package com.anthonyhilyard.advancementplaques.forge.compat;

import com.anthonyhilyard.advancementplaques.compat.IAdvancementScreenshotHandler;
import com.natamus.advancementscreenshot_common_forge.util.Util;
public class ForgeAdvancementScreenshotHandler implements IAdvancementScreenshotHandler
{
	@Override
	public void takeScreenshot()
	{
		Util.takeScreenshot();
	}
}
