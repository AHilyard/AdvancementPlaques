package com.anthonyhilyard.advancementplaques.fabric.compat;

import com.anthonyhilyard.advancementplaques.compat.IAdvancementScreenshotHandler;
import com.natamus.advancementscreenshot_common_fabric.util.Util;
import net.minecraft.network.chat.Component;

public class FabricAdvancementScreenshotHandler implements IAdvancementScreenshotHandler
{
	@Override
	public void takeScreenshot(Component title)
	{
		Util.takeScreenshot(title);
	}
}
