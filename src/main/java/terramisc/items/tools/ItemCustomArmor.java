package terramisc.items.tools;

import net.minecraft.util.IIcon;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.bioxx.tfc.Items.ItemTFCArmor;
import com.bioxx.tfc.api.Armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCustomArmor extends ItemTFCArmor //ItemArmor implements ISize, IClothing
{

    public ItemCustomArmor(Armor armor, int renderIndex, int armorSlot, int thermal, int type) {
        super(armor, renderIndex, armorSlot, thermal, type);//armorSlot % 4 inside ItemTFCArmor
        this.armorTypeTFC.metaltype = armor.metaltype;
    }

    public ItemCustomArmor(Armor armor, int renderIndex, int armorSlot, ArmorMaterial mat, int thermal, int type) {
        super(armor, renderIndex, armorSlot, thermal, type);//armorSlot % 4 inside ItemTFCArmor
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Gets an icon index based on an item's damage value and the given render
     * pass
     */
    public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
        return par2 == 1 ? overlayIcon : super.getIconFromDamageForRenderPass(par1, par2);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String name = "tfcm:armor/" + this.getUnlocalizedName().replace("item.", "");
        this.itemIcon = registerer.registerIcon(name);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        String m = this.armorTypeTFC.metaltype;
        return "tfcm:textures/models/armor/" + m + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
    }
}
