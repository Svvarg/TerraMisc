package terramisc.gui;

import terramisc.tileentities.TEFruitPress;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.bioxx.tfc.Reference;
import com.bioxx.tfc.Containers.ContainerHopper;
import com.bioxx.tfc.GUI.GuiContainerTFC;

public class GuiFruitPress extends GuiContainerTFC
{
	public static ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, Reference.ASSET_PATH_GUI + "gui_hopper.png");
	public GuiFruitPress(InventoryPlayer inventoryplayer, TEFruitPress te, World world, int i, int j, int k)
	{
		super(new ContainerHopper(inventoryplayer, te), 176, 49);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		this.drawGui(texture);
	}
}
