package terramisc.items.tools;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import terramisc.core.TFCMItems;
import terramisc.core.TFCMOptions;
import terramisc.entities.EntityMetalArrow;

import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Core.Player.InventoryPlayerTFC;
import com.bioxx.tfc.Items.ItemQuiver;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Enums.EnumAmmo;
import com.bioxx.tfc.api.Enums.EnumItemReach;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.ISize;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCustomLongbow extends ItemBow implements ISize {

    private IIcon[] iconArray;

    public ItemCustomLongbow() {
        super();
        this.maxStackSize = 1;
        this.setMaxDamage(800);
        this.setCreativeTab(TFCTabs.TFC_WEAPONS);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (is.stackTagCompound == null) {
            is.stackTagCompound = new NBTTagCompound();
            is.stackTagCompound.setInteger("ammo", 0);
        }

        boolean flag = TFCMOptions.hasInfinityAmmo(player, is);
        //First we check the if there is ammo in the player's quiver
        boolean hasQuiverAmmo = flag || this.writeTagCompoundIfPlayerHasAmmoQuiver(is, player);
        boolean hasAmmo = false;
        //If there was no quiver or ammo, we check the inventory.
        if (!hasQuiverAmmo) {
            hasAmmo = this.writeTagCompoundIfPlayerHasAmmo(is, player);
        }

        if (hasQuiverAmmo || hasAmmo) {
            ArrowNockEvent event = new ArrowNockEvent(player, is);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
                return event.result;
            }

            if (player.capabilities.isCreativeMode || (hasQuiverAmmo || hasAmmo));
            player.setItemInUse(is, this.getMaxItemUseDuration(is));

            return is;
        }

        return is;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack is, World world, EntityPlayer player, int inUseCount) {
        int ammo = is.getTagCompound().getInteger("ammo");

        int j = this.getMaxItemUseDuration(is) - inUseCount;
        double drawSpeed = TFCMOptions.LongbowDrawSpeedMult;

        ArrowLooseEvent event = new ArrowLooseEvent(player, is, j);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return;
        }
        j = event.charge;

        boolean flag = TFCMOptions.hasInfinityAmmo(player, is);
        //First we check the if there is ammo in the player's quiver
        boolean hasQuiverAmmo = flag || this.writeTagCompoundIfPlayerHasAmmoQuiver(is, player);
        boolean hasAmmo = false;
        //If there was no quiver or ammo, we check the inventory.
        if (!hasQuiverAmmo) {
            hasAmmo = this.writeTagCompoundIfPlayerHasAmmo(is, player);
        }

        if (hasQuiverAmmo || hasAmmo) {
            float forceMult = j / getUseSpeed(player);
            float ammoMult = 0.55f;//default - Stone

            if (forceMult < (0.25D * drawSpeed)) {
                return;
            }

            if (forceMult > (1.00F * drawSpeed)) {
                forceMult = (float) (1.00F * drawSpeed);
            }

            EntityMetalArrow entityarrow = new EntityMetalArrow(world, player, (float) ((forceMult / drawSpeed) * 3.0F));

            if (ammo > 0 && ammo - 1 < ItemCustomQuiver.AMMO_TIER_MULT.length) {
                entityarrow.setPickupItem(ItemCustomQuiver.getArrowItemFromTier(ammo));//установка предмета (что поднимать)
                ammoMult = ItemCustomQuiver.AMMO_TIER_MULT[ammo - 1];//! -1 для стрел т.к. у стрел 1 - Stone, а у болтов 1 - Copper (0-дефолтный)
            }

            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, is);

            if (l > 0) {
                entityarrow.setKnockbackStrength(l);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, is) > 0) {
                entityarrow.setFire(100);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, is);

            if (k > 0) {
                entityarrow.setDamage(entityarrow.getDamage() + k * 50D + 25D);
            }

            if (flag) {
                entityarrow.canBePickedUp = 2;
            }

            if (hasAmmo) {
                Item arrowItem = ItemCustomQuiver.getArrowItemFromTier(ammo);
                if (arrowItem != null) {
                    player.inventory.consumeInventoryItem(arrowItem);
                }
            }
            else if (hasQuiverAmmo) {
                this.consumeArrowInQuiver(player, ammo);
            }

            is.damageItem(1, player);
            world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + forceMult * 0.5F);

            entityarrow.setDamage((forceMult * 300.0 * ammoMult));
            if (forceMult == (1.00F * drawSpeed)) {
                entityarrow.setIsCritical(true);
            }

            if (!world.isRemote) {
                world.spawnEntityInWorld(entityarrow);
            }

            is.getTagCompound().setInteger("ammo", 0);
        }
    }

    //Checks to see if player has ammo, and what type of ammo he/she has.
    /**
     * ECO Refactored За один проход по всему инвентарю проверяеются сразу все виды поддерживаемых стрел
     * InventoryPlayerTFC использует mainInventory так же как и в ванильном InventoryPlayer
     * доступ идёт через интерфейс getStackInSlot(i);
     * замена множественных вызовов player.inventory.hasItem(TFCItems.arrow) для каждого типа стрел
     *
     * Для выстрела будут выбираться снаряды по порядку укладки в инвентаре
     * @param is
     * @param player
     * @return
     */
    public boolean writeTagCompoundIfPlayerHasAmmo(ItemStack is, EntityPlayer player) {
        final int sz = player.inventory.getSizeInventory();
        for (int i = 0; i < sz; i++) {
            ItemStack is0 = player.inventory.getStackInSlot(i);
            if (is0 != null && is0.getItem() != null) {
                int ammoId = ItemCustomQuiver.getMetalArrowTier(is0.getItem());

                if (ammoId > 0) {
                    is.stackTagCompound.setInteger("ammo", ammoId);
                    return true;
                }
            }
        }

        return false;
    }

    //Checks the custom quiver for bow ammo.
    //Добавлена поддержка работы с обычным тфк-колчаном
    public boolean writeTagCompoundIfPlayerHasAmmoQuiver(ItemStack is, EntityPlayer player) {
        ItemStack quiver = ((InventoryPlayerTFC) player.inventory).extraEquipInventory[0];

        if (quiver != null && quiver.getItem() instanceof ItemCustomQuiver) {
            final int tier = ((ItemCustomQuiver) quiver.getItem()).getQuiverMetalArrowTierOrZero(quiver);
            if (tier > 0) {//hasArrowAmmo
                is.stackTagCompound.setInteger("ammo", tier);
                return true;
            }
            //if (((ItemCustomQuiver) quiver.getItem()).hasArrowAmmo(quiver) != null) {
            //    int t = ((ItemCustomQuiver) quiver.getItem()).getQuiverMetalArrowTier(quiver);
            //    is.stackTagCompound.setInteger("ammo", t);
            //    return true;
            //}
        }
        //поддержка обычного тфк-колча и обычных каменных-тфк-стрел
        else if (quiver != null && quiver.getItem() instanceof ItemQuiver) {
            ItemStack ammo = ((ItemQuiver) quiver.getItem()).consumeAmmo(quiver, EnumAmmo.ARROW, false);
            if (ammo != null && ammo.getItem() == TFCItems.arrow) {
                is.stackTagCompound.setInteger("ammo", 1);//ItemCustomQuiver.getMetalArrowTier(TFCItems.arrow));
                return true;
            }
        }
        return false;
    }

    //Removes ammo from quiver.
    //Добавлена поддержка работы с обычным тфк-колчаном
    public void consumeArrowInQuiver(EntityPlayer player, int t) {
        ItemStack quiver = ((InventoryPlayerTFC) player.inventory).extraEquipInventory[0];

        if (quiver != null && quiver.getItem() instanceof ItemCustomQuiver) {
            Item ammo = ItemCustomQuiver.getArrowItemFromTier(t);
            ((ItemCustomQuiver) quiver.getItem()).consumeMetalAmmo(quiver, ammo, true);
        }
        //поддержка обычного тфк-колча и обычных каменных-тфк-стрел
        else if (quiver != null && quiver.getItem() instanceof ItemQuiver) {
            ((ItemQuiver) quiver.getItem()).consumeAmmo(quiver, EnumAmmo.ARROW, true);
        }
    }

    public float getUseSpeed(EntityPlayer player) {
        float speed = 60.0f;
        ItemStack[] armor = player.inventory.armorInventory;
        if (armor[0] != null && armor[0].getItem() instanceof ISize) {
            speed += 20.0f / ((ISize) armor[0].getItem()).getWeight(armor[0]).multiplier;
        }
        if (armor[1] != null && armor[1].getItem() instanceof ISize) {
            speed += 20.0f / ((ISize) armor[1].getItem()).getWeight(armor[1]).multiplier;
        }
        if (armor[2] != null && armor[2].getItem() instanceof ISize) {
            speed += 20.0f / ((ISize) armor[2].getItem()).getWeight(armor[2]).multiplier;
        }
        if (armor[3] != null && armor[3].getItem() instanceof ISize) {
            speed += 20.0f / ((ISize) armor[3].getItem()).getWeight(armor[3]).multiplier;
        }

        return speed;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
        ItemTerra.addSizeInformation(is, arraylist);
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.HUGE;
    }

    @Override
    public EnumWeight getWeight(ItemStack is) {
        return EnumWeight.LIGHT;
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumItemReach getReach(ItemStack is) {
        return EnumItemReach.SHORT;
    }

    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon("tfcm:LongBow");
        iconArray = new IIcon[4];

        iconArray[0] = par1IconRegister.registerIcon("tfcm:LongBow_pulling_0");
        iconArray[1] = par1IconRegister.registerIcon("tfcm:LongBow_pulling_1");
        iconArray[2] = par1IconRegister.registerIcon("tfcm:LongBow_pulling_2");
        iconArray[3] = par1IconRegister.registerIcon("tfcm:LongBow_pulling_3");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getItemIconForUseDuration(int par1) {
        return iconArray[par1];
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (usingItem != null && usingItem.getItem() == this) {
            int j = usingItem.getMaxItemUseDuration() - useRemaining;
            double drawSpeed = TFCMOptions.LongbowDrawSpeedMult;
            float force = j / getUseSpeed(player);

            if (force >= (1.00 * drawSpeed)) // Fully drawn
            {
                return getItemIconForUseDuration(3);
            }
            else if (force > 0.50 * drawSpeed) {
                return getItemIconForUseDuration(2);
            }
            else if (force > 0.25 * drawSpeed) // Minimum required force to fire
            {
                return getItemIconForUseDuration(1);
            }
            else if (force > 0) {
                return getItemIconForUseDuration(0);
            }
        }
        return getIcon(stack, renderPass);
    }
}
