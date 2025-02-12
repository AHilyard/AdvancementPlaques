package com.anthonyhilyard.advancementplaques.neoforge.compat;

import com.anthonyhilyard.advancementplaques.compat.IAdvancementScreenshotHandler;
import com.natamus.advancementscreenshot_common_neoforge.util.Util;
public class NeoForgeAdvancementScreenshotHandler implements IAdvancementScreenshotHandler
{
	@Override
	public void takeScreenshot()
	{
		Util.takeScreenshot();
	}
}
