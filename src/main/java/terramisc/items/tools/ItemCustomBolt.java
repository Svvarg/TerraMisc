package terramisc.items.tools;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Enums.EnumAmmo;
import com.bioxx.tfc.api.Enums.EnumDamageType;
import com.bioxx.tfc.api.Enums.EnumItemReach;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.ICausesDamage;
import com.bioxx.tfc.api.Interfaces.IMetallTier;
import com.bioxx.tfc.api.Interfaces.IQuiverAmmo;
import com.bioxx.tfc.api.Interfaces.ISize;
import com.bioxx.tfc.api.Metal;

public class ItemCustomBolt extends Item implements ISize, IQuiverAmmo, IMetallTier, ICausesDamage {

    private Metal metal;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
        ItemTerra.addSizeInformation(is, arraylist);
    }

    public ItemCustomBolt(Metal metal) {
        super();
        this.maxStackSize = 16;
        this.hasSubtypes = false;
        this.setCreativeTab(TFCTabs.TFC_WEAPONS);
        this.metal = metal;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon("tfcm:projectileItems/" + this.getUnlocalizedName().replace("item.", ""));
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.LARGE;
    }

    @Override
    public EnumWeight getWeight(ItemStack is) {
        return EnumWeight.LIGHT;
    }

    @Override
    public EnumItemReach getReach(ItemStack is) {
        return EnumItemReach.SHORT;
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumAmmo getAmmoType() {
        return EnumAmmo.ARROW;
    }

    @Override
    public int getMetallTier() {
        return Metal.getTier(this.metal);
    }

    @Override
    public Metal getMetal() {
        return this.metal == null ? Global.UNKNOWN : this.metal;
    }

    @Override
    public EnumDamageType getDamageType() {
        return EnumDamageType.CRUSHING;
    }
}
