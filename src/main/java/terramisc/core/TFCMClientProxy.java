package terramisc.core;

import java.io.File;

import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCOptions;

import codechicken.nei.api.API;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import terramisc.render.CropRender;
import terramisc.render.HalberdRenderer;
import terramisc.render.ItemCrossbowRender;
import terramisc.render.ItemLongbowRender;
import terramisc.render.ItemPolearmRender;
import terramisc.render.TESRBrickOven;
import terramisc.render.TESRFruitPress;
import terramisc.render.TESRSoupKettle;
import terramisc.render.TESRTallowCandle;
import terramisc.render.TESRTallowCandleItem;
import terramisc.render.TESRTallowCandleItemIntial;
import terramisc.tileentities.TEBrickOven;
import terramisc.tileentities.TEFruitPress;
import terramisc.tileentities.TESoupKettle;
import terramisc.tileentities.TETallowCandle;

public class TFCMClientProxy extends TFCMCommonProxy
{
	@Override
	public String getCurrentLanguage()
	{
		return Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
	}

	@Override
	public World getCurrentWorld()
	{
		return Minecraft.getMinecraft().theWorld;
	}

	@Override
	public boolean getGraphicsLevel()
	{
		Minecraft.getMinecraft();
		return Minecraft.isFancyGraphicsEnabled();
	}

	@Override
	public File getMinecraftDirectory()
	{
		return Minecraft.getMinecraft().mcDataDir;
	}

	@Override
	public void hideNEIItems()
	{
		if (Loader.isModLoaded(TFCMDetails.MODID_NEI) && TFCOptions.enableNEIHiding)
		{
			API.hideItem(new ItemStack(TFCMBlocks.blockTallowCandle));

			API.hideItem(new ItemStack(TFCBlocks.pumpkin));
			API.hideItem(new ItemStack(TFCBlocks.litPumpkin));

			API.hideItem(new ItemStack(TFCMBlocks.blockPumpkinLantern));
		}
	}

	@Override
	public boolean isRemote()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void loadOptions()
	{
		//Load our settings from the server
		TFCMOptions.loadSettings();
	}

	@Override
	public void registerGuiHandler()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(terramisc.TerraMisc.instance, new terramisc.handlers.client.TFCMGuiHandler());
	}

	@Override
	public void registerHandlers()
	{
	}

	@Override
	public void registerKeys()
	{
		uploadKeyBindingsToGame();
	}

	@Override
	public void registerKeyBindingHandler()
	{
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerRenderInformation()
	{
		//Other Items
		MinecraftForgeClient.registerItemRenderer(TFCMItems.longBow, new ItemLongbowRender());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.crossBow, new ItemCrossbowRender());
		//Halberds
		MinecraftForgeClient.registerItemRenderer(TFCMItems.halberd_BismuthBronze, new HalberdRenderer());//ItemPolearmRender()
		MinecraftForgeClient.registerItemRenderer(TFCMItems.halberd_BlackBronze, new HalberdRenderer());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.halberd_BlackSteel, new HalberdRenderer());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.halberd_BlueSteel, new HalberdRenderer());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.halberd_Bronze, new HalberdRenderer());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.halberd_Copper, new HalberdRenderer());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.halberd_RedSteel, new HalberdRenderer());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.halberd_Steel, new HalberdRenderer());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.halberd_WroughtIron, new HalberdRenderer());
		//War Hammers
		MinecraftForgeClient.registerItemRenderer(TFCMItems.warHammer_BismuthBronze, new ItemPolearmRender());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.warHammer_BlackBronze, new ItemPolearmRender());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.warHammer_BlackSteel, new ItemPolearmRender());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.warHammer_BlueSteel, new ItemPolearmRender());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.warHammer_Bronze, new ItemPolearmRender());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.warHammer_Copper, new ItemPolearmRender());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.warHammer_RedSteel, new ItemPolearmRender());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.warHammer_Steel, new ItemPolearmRender());
		MinecraftForgeClient.registerItemRenderer(TFCMItems.warHammer_WroughtIron, new ItemPolearmRender());
		//Tallow Candle
			//Render Registering
		TileEntitySpecialRenderer render = new TESRTallowCandle();
		TileEntitySpecialRenderer render2 = new TESRTallowCandleItemIntial();
			//Render to Tile Assigning
		ClientRegistry.bindTileEntitySpecialRenderer(TETallowCandle.class, render);
			//Held Item Rendering
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFCMBlocks.blockTallowCandle), new TESRTallowCandleItem(render2, new TETallowCandle()));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFCMBlocks.blockTallowCandleOff), new TESRTallowCandleItem(render2, new TETallowCandle()));
			//Screen Overlay for custom quiver
		//MinecraftForge.EVENT_BUS.register(new RenderQuiverOverlayHandler());

		RenderingRegistry.registerBlockHandler(TFCMBlocks.cropRenderId = RenderingRegistry.getNextAvailableRenderId(), new CropRender());
	}

	@Override
	public void registerTileEntities(boolean flag)
	{
		super.registerTileEntities(false);

		// TESR registers
		ClientRegistry.registerTileEntity(TETallowCandle.class, "TallowCandleTESR", new TESRTallowCandle());
		ClientRegistry.registerTileEntity(TEFruitPress.class, "FruitPressTESR", new TESRFruitPress());
		ClientRegistry.registerTileEntity(TEBrickOven.class, "BrickOvenTESR", new TESRBrickOven());
		ClientRegistry.registerTileEntity(TESoupKettle.class, "SoupKettleTESR", new TESRSoupKettle());
	}

	@Override
	public void uploadKeyBindingsToGame()
	{
	}
}
