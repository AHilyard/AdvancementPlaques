package com.anthonyhilyard.advancementplaques;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.toasts.AdvancementToast;
import net.minecraft.client.gui.toasts.IToast.Visibility;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.LanguageMap;

public class AdvancementPlaque
{
	private final AdvancementToast toast;
	private long animationTime = -1L;
	private long visibleTime = -1L;
	private boolean hasPlayedSound = false;
	private Visibility visibility = Visibility.SHOW;
	private Minecraft mc;
	private CustomItemRenderer itemRenderer;

	public AdvancementPlaque(AdvancementToast toastIn, Minecraft mcIn, CustomItemRenderer itemRendererIn)
	{
		toast = toastIn;
		mc = mcIn;
		itemRenderer = itemRendererIn;
	}

	public AdvancementToast getToast()
	{
		return toast;
	}

	public int width()
	{
		return 256;
	}

	public int height()
	{
		return 32;
	}

	private float getVisibility(long currentTime)
	{
		float f = MathHelper.clamp((float)(currentTime - animationTime) / 600.0f, 0.0f, 1.0f);
		f = f * f;
		return visibility == Visibility.HIDE ? 1.0f - f : f;
	}

	@SuppressWarnings("deprecation")
	private Visibility drawPlaque(MatrixStack matrixStack, long displayTime)
	{
		DisplayInfo displayInfo = toast.advancement.getDisplay();

		if (displayInfo != null)
		{
			float fadeInTime = 500f, fadeOutTime = 1500f;
			if (displayInfo.getFrame() == FrameType.CHALLENGE)
			{
				fadeInTime = 1250f;
			}

			if (displayTime >= fadeInTime)
			{
				float alpha = 1.0f;
				if (displayTime > 7000)
				{
					alpha = Math.max(0.0f, Math.min(1.0f, 1.0f - ((float)displayTime - 7000) / 1000.0f));
				}
				int alphaMask = (int)(alpha * 255.0f);

				mc.getTextureManager().bindTexture(AdvancementPlaques.TEXTURE_PLAQUES);
				RenderSystem.enableBlend();
				RenderSystem.color4f(1.0f, 1.0f, 1.0f, alpha);
				int frameOffset = 0;
				if (displayInfo.getFrame() == FrameType.GOAL)
				{
					frameOffset = 1;
				}
				else if (displayInfo.getFrame() == FrameType.CHALLENGE)
				{
					frameOffset = 2;
				}
				AdvancementPlaquesToastGui.blit(matrixStack, -1, -1, 0, height() * frameOffset, width(), height(), 256, 256);

				// Only bother drawing text if alpha is greater than 0.1.
				if (alpha > 0.1f)
				{
					alphaMask <<= 24;

					// Text like "Challenge Complete!" at the top of the plaque.
					int typeWidth = mc.fontRenderer.getStringPropertyWidth(displayInfo.getFrame().getTranslatedToast());
					mc.fontRenderer.func_243248_b(matrixStack, displayInfo.getFrame().getTranslatedToast(), (width() - typeWidth) / 2.0f + 15.0f, 5.0f, 0x332200 | alphaMask);

					int titleWidth = mc.fontRenderer.getStringPropertyWidth(displayInfo.getTitle());

					// If the width of the advancement title is less than the full available width, display it normally.
					if (titleWidth <= (220 / 1.5f))
					{
						RenderSystem.pushMatrix();
						RenderSystem.scalef(1.5f, 1.5f, 1.0f);
						mc.fontRenderer.func_238422_b_(matrixStack, LanguageMap.getInstance().func_241870_a(displayInfo.getTitle()), ((width() / 1.5f) - titleWidth) / 2.0f + (15.0f / 1.5f), 9.0f, 0xFFFFFF | alphaMask);
						RenderSystem.popMatrix();
					}
					// Otherwise, display it with a smaller (default) font.
					else
					{
						mc.fontRenderer.func_238422_b_(matrixStack, LanguageMap.getInstance().func_241870_a(displayInfo.getTitle()), (width() - titleWidth) / 2.0f + 15.0f, 15.0f, 0xFFFFFF | alphaMask);
					}
				}

				RenderSystem.pushMatrix();
				RenderSystem.translatef(1.0f, 1.0f, 0.0f);
				RenderSystem.scalef(1.5f, 1.5f, 1.0f);
				itemRenderer.renderItemModelIntoGUIWithAlpha(displayInfo.getIcon(), 1, 1, alpha);
				RenderSystem.popMatrix();

				if (!hasPlayedSound)
				{
					hasPlayedSound = true;
					if (displayInfo.getFrame() == FrameType.CHALLENGE)
					{
						mc.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f));
					}
				}
			}

			if (displayTime < fadeInTime)
			{
				RenderSystem.enableAlphaTest();
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.defaultAlphaFunc();
				RenderSystem.color4f(1.0f, 1.0f, 1.0f, (float)displayTime / fadeInTime);
				matrixStack.push();
				matrixStack.translate(0.0f, 0.0f, 95.0f);
				mc.getTextureManager().bindTexture(AdvancementPlaques.TEXTURE_PLAQUE_EFFECTS);
				if (displayInfo.getFrame() == FrameType.CHALLENGE)
				{
					AdvancementPlaquesToastGui.blit(matrixStack, -16, -16, 0, height() + 32, width() + 32, height() + 32, 512, 512);
				}
				else
				{
					AdvancementPlaquesToastGui.blit(matrixStack, -16, -16, 0, 0, width() + 32, height() + 32, 512, 512);
				}
				matrixStack.pop();
			}
			else if (displayTime < fadeInTime + fadeOutTime)
			{
				RenderSystem.enableAlphaTest();
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.defaultAlphaFunc();
				RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f - ((float)(displayTime - fadeInTime) / fadeOutTime));
				matrixStack.push();
				matrixStack.translate(0.0f, 0.0f, 95.0f);
				mc.getTextureManager().bindTexture(AdvancementPlaques.TEXTURE_PLAQUE_EFFECTS);
				if (displayInfo.getFrame() == FrameType.CHALLENGE)
				{
					AdvancementPlaquesToastGui.blit(matrixStack, -16, -16, 0, height() + 32, width() + 32, height() + 32, 512, 512);
				}
				else
				{
					AdvancementPlaquesToastGui.blit(matrixStack, -16, -16, 0, 0, width() + 32, height() + 32, 512, 512);
				}
				matrixStack.pop();
			}

			return displayTime >= fadeInTime + fadeOutTime + 6000 ? Visibility.HIDE : Visibility.SHOW;
		}
		else
		{
			return Visibility.HIDE;
		}
	}

	@SuppressWarnings("deprecation")
	public boolean render(int screenWidth, int index, MatrixStack matrixStack)
	{
		long currentTime = Util.milliTime();
		if (animationTime == -1L)
		{
			animationTime = currentTime;
			visibility.playSound(mc.getSoundHandler());
		}

		if (visibility == Visibility.SHOW && currentTime - animationTime <= 600L)
		{
			visibleTime = currentTime;
		}
		
		RenderSystem.pushMatrix();
		RenderSystem.disableDepthTest();
		if (AdvancementPlaquesConfig.INSTANCE.onTop.get())
		{
			RenderSystem.translatef((float)(mc.getMainWindow().getScaledWidth() - width()) / 2.0f,
									AdvancementPlaquesConfig.INSTANCE.distance.get(),
									900.0f + index);
		}
		else
		{
			RenderSystem.translatef((float)(mc.getMainWindow().getScaledWidth() - width()) / 2.0f,
									(float)(mc.getMainWindow().getScaledHeight() - (height() + AdvancementPlaquesConfig.INSTANCE.distance.get())),
									900.0f + index);
		}
		Visibility newVisibility = drawPlaque(matrixStack, currentTime - visibleTime);
		RenderSystem.enableDepthTest();
		RenderSystem.popMatrix();

		if (newVisibility != visibility)
		{
			animationTime = currentTime - (long)((int)((1.0f - getVisibility(currentTime)) * 600.0f));
			visibility = newVisibility;
			visibility.playSound(mc.getSoundHandler());
		}

		return visibility == Visibility.HIDE && currentTime - animationTime > 600L;
	}
}