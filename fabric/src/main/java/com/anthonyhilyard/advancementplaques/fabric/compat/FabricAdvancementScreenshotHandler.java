package com.anthonyhilyard.advancementplaques.fabric.compat;

import com.anthonyhilyard.advancementplaques.compat.IAdvancementScreenshotHandler;
import com.natamus.advancementscreenshot_common_fabric.util.Util;
public class FabricAdvancementScreenshotHandler implements IAdvancementScreenshotHandler
{
	@Override
	public void takeScreenshot()
	{
		Util.takeScreenshot();
	}
}
