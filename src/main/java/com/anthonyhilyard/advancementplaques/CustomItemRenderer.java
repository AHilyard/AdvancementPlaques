package com.anthonyhilyard.advancementplaques;

import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraftforge.client.ForgeHooksClient;

public class CustomItemRenderer extends ItemRenderer
{
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();
	
	public CustomItemRenderer(TextureManager textureManagerIn, ModelManager modelManagerIn, ItemColors itemColorsIn)
	{
		super(textureManagerIn, modelManagerIn, itemColorsIn);
	}

	public void renderQuads(MatrixStack matrixStackIn, IVertexBuilder bufferIn, List<BakedQuad> quadsIn, ItemStack itemStackIn, int combinedLightIn, int combinedOverlayIn, float alpha)
	{
		boolean flag = !itemStackIn.isEmpty();
		MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
  
		for (BakedQuad bakedquad : quadsIn)
		{
			int i = -1;
			if (flag && bakedquad.hasTintIndex())
			{
				i = this.itemColors.getColor(itemStackIn, bakedquad.getTintIndex());
			}

			float f = (float)(i >> 16 & 255) / 255.0F;
			float f1 = (float)(i >> 8 & 255) / 255.0F;
			float f2 = (float)(i & 255) / 255.0F;
			bufferIn.addVertexData(matrixstack$entry, bakedquad, f, f1, f2, alpha, combinedLightIn, combinedOverlayIn, true);
		}
	}

	@SuppressWarnings("deprecation")
	public void renderModel(IBakedModel modelIn, ItemStack stack, int combinedLightIn, int combinedOverlayIn, MatrixStack matrixStackIn, IVertexBuilder bufferIn, float alpha)
	{
		Random random = new Random();

		for (Direction direction : Direction.values())
		{
			random.setSeed(42L);
			this.renderQuads(matrixStackIn, bufferIn, modelIn.getQuads((BlockState)null, direction, random), stack, combinedLightIn, combinedOverlayIn, alpha);
		}

		random.setSeed(42L);
		this.renderQuads(matrixStackIn, bufferIn, modelIn.getQuads((BlockState)null, (Direction)null, random), stack, combinedLightIn, combinedOverlayIn, alpha);
	}

	public void drawItemLayered(IBakedModel modelIn, ItemStack itemStackIn, MatrixStack matrixStackIn,
								IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, boolean fabulous, float alpha)
	{
		for (Pair<IBakedModel, RenderType> layerModel : modelIn.getLayerModels(itemStackIn, fabulous))
		{
			IBakedModel layer = layerModel.getFirst();
			RenderType rendertype = layerModel.getSecond();
			ForgeHooksClient.setRenderLayer(rendertype);
			IVertexBuilder ivertexbuilder;
			if (fabulous)
			{
				ivertexbuilder = ItemRenderer.getEntityGlintVertexBuilder(bufferIn, rendertype, true, itemStackIn.hasEffect());
			}
			else
			{
				ivertexbuilder = ItemRenderer.getBuffer(bufferIn, rendertype, true, itemStackIn.hasEffect());
			}
			renderModel(layer, itemStackIn, combinedLightIn, combinedOverlayIn, matrixStackIn, ivertexbuilder, alpha);
		}
		ForgeHooksClient.setRenderLayer(null);
	}

	@SuppressWarnings("resource")
	protected static final RenderState.TargetState TRANSLUCENT_TARGET = new RenderState.TargetState("translucent_target", () -> {
		if (Minecraft.isFabulousGraphicsEnabled())
		{
			Minecraft.getInstance().worldRenderer.func_239228_q_().bindFramebuffer(false);
		}
	 }, () -> {
		if (Minecraft.isFabulousGraphicsEnabled())
		{
			Minecraft.getInstance().getFramebuffer().bindFramebuffer(false);
		}
	 });

	public void renderItem(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn, float alpha)
	{
		if (!itemStackIn.isEmpty())
		{
			matrixStackIn.push();

			if (itemStackIn.getItem() == Items.TRIDENT)
			{
				modelIn = this.itemModelMesher.getModelManager().getModel(new ModelResourceLocation("minecraft:trident#inventory"));
			}

			modelIn = ForgeHooksClient.handleCameraTransforms(matrixStackIn, modelIn, ItemCameraTransforms.TransformType.GUI, false);
			matrixStackIn.translate(-0.5D, -0.5D, -0.5D);

			if (!modelIn.isBuiltInRenderer())
			{
				if (modelIn.isLayered())
				{
					drawItemLayered(modelIn, itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, true, alpha);
				}
				else
				{
					RenderType rendertype = RenderTypeLookup.func_239219_a_(itemStackIn, true);
					IVertexBuilder ivertexbuilder;
					if (itemStackIn.getItem() == Items.COMPASS && itemStackIn.hasEffect())
					{
						matrixStackIn.push();
						MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
						matrixstack$entry.getMatrix().mul(0.5F);

						ivertexbuilder = getDirectGlintVertexBuilder(bufferIn, rendertype, matrixstack$entry);

						matrixStackIn.pop();
					}
					else
					{
						ivertexbuilder = bufferIn.getBuffer(Atlases.getItemEntityTranslucentCullType());
					}
					this.renderModel(modelIn, itemStackIn, combinedLightIn, combinedOverlayIn, matrixStackIn, ivertexbuilder, alpha);
				}
			}
			else
			{
				itemStackIn.getItem().getItemStackTileEntityRenderer().func_239207_a_(itemStackIn, ItemCameraTransforms.TransformType.GUI, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
			}

			matrixStackIn.pop();
		}
	}

	@SuppressWarnings("deprecation")
	public void renderItemModelIntoGUIWithAlpha(ItemStack stack, int x, int y, float alpha, Minecraft mc)
	{
		IBakedModel bakedmodel = mc.getItemRenderer().getItemModelWithOverrides(stack, null, null);
		RenderSystem.pushMatrix();
		mc.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		mc.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
		RenderSystem.enableRescaleNormal();
		RenderSystem.enableAlphaTest();
		RenderSystem.defaultAlphaFunc();
		RenderSystem.enableBlend();
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, alpha);
		RenderSystem.translatef((float)x, (float)y, 150.0f);
		RenderSystem.translatef(8.0f, 8.0f, 0.0f);
		RenderSystem.scalef(1.0f, -1.0f, 1.0f);
		RenderSystem.scalef(16.0f, 16.0f, 16.0f);
		MatrixStack matrixstack = new MatrixStack();
		IRenderTypeBuffer.Impl renderBuffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		boolean flag = !bakedmodel.isSideLit();
		if (flag)
		{
			RenderHelper.setupGuiFlatDiffuseLighting();
		}
		
		renderItem(stack, matrixstack, renderBuffer, 0xF000F0, OverlayTexture.NO_OVERLAY, bakedmodel, alpha);
		renderBuffer.finish();
		RenderSystem.enableDepthTest();
		if (flag)
		{
			RenderHelper.setupGui3DDiffuseLighting();
		}

		RenderSystem.disableAlphaTest();
		RenderSystem.disableRescaleNormal();
		RenderSystem.popMatrix();
	}
}
