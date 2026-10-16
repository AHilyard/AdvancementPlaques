package com.anthonyhilyard.advancementplaques.fabric.compat;

import com.anthonyhilyard.advancementplaques.compat.AdvancementScreenshotCompat;
import com.anthonyhilyard.advancementplaques.compat.IAdvancementScreenshotHandler;
import net.minecraft.network.chat.Component;

public class FabricAdvancementScreenshotHandler implements IAdvancementScreenshotHandler
{
	// Advancement Screenshot 5.3 moved its classes from com.natamus to com.serilum, so try the new package first and fall back to the old one for earlier versions.
	private final AdvancementScreenshotCompat compat = new AdvancementScreenshotCompat(
		"com.serilum.advancementscreenshot_common_fabric.util.Util",
		"com.natamus.advancementscreenshot_common_fabric.util.Util");

	@Override
	public void takeScreenshot(Component title)
	{
		compat.takeScreenshot(title);
	}
}
