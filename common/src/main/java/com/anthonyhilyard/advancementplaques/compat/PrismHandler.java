package com.anthonyhilyard.advancementplaques.compat;

import com.anthonyhilyard.prism.util.ConfigHelper;

import net.minecraft.network.chat.TextColor;

public class PrismHandler
{
	public static TextColor getColor(Object value)
	{
		return (TextColor)(Object)ConfigHelper.parseColor(value);
	}
}