package terramisc.items.tools;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;

import terramisc.entities.EntityMetalBolt;
import terramisc.core.TFCMItems;
import terramisc.core.TFCMOptions;
import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Core.Player.InventoryPlayerTFC;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.Enums.EnumItemReach;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.ISize;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemCustomCrossbow extends ItemBow implements ISize {

    private IIcon[] iconArray;

    public ItemCustomCrossbow() {
        super();
        this.maxStackSize = 1;
        this.setMaxDamage(800);
        this.setCreativeTab(TFCTabs.TFC_WEAPONS);
    }

    //Creates NBT tags when item is crafted and stores them to that item stack.
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.stackTagCompound = new NBTTagCompound();
        itemStack.stackTagCompound.setBoolean("load", false);
        itemStack.stackTagCompound.setInteger("ammo", 0);
        itemStack.stackTagCompound.setFloat("forceMult", 0);

    }

    //Activates when player right clicks with crossbow.
    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        //Sets up data for crossbow on use, should prevent crashing.
        if (is.stackTagCompound == null) {
            is.stackTagCompound = new NBTTagCompound();
            is.stackTagCompound.setBoolean("load", false);
            is.stackTagCompound.setInteger("ammo", 0);
            is.stackTagCompound.setFloat("forceMult", 0);
        }

        //To try to prevent crashing, from cheated in crossbows.
        if (is.stackTagCompound != null) {
            boolean flag = TFCMOptions.hasInfinityAmmo(player, is);
            //First we check the if there is ammo in the player's quiver
            boolean hasQuiverAmmo = flag || this.playerHasAmmoQuiver(player) != 0;
            boolean hasAmmo = false;
            //If there was no quiver or ammo, we check the inventory.
            if (!hasQuiverAmmo) {
                hasAmmo = this.playerHasAmmo(player) != 0;
            }

            //Load the crossbow if its unloaded, and has ammo.
            if ((hasQuiverAmmo || hasAmmo) && is.getTagCompound().getBoolean("load") == false) {
                ArrowNockEvent event = new ArrowNockEvent(player, is);
                MinecraftForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    return event.result;
                }

                player.setItemInUse(is, this.getMaxItemUseDuration(is));
            }

            //If Crossbow is already loaded
            if (is.getTagCompound().getBoolean("load") == true) {
                this.fireCrossbow(is, world, player, is.getTagCompound().getFloat("forceMult"));
            }
        }

        return is;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack is, World world, EntityPlayer player, int inUseCount) {
        boolean load = is.getTagCompound().getBoolean("load");

        int j = this.getMaxItemUseDuration(is) - inUseCount;
        float forceMult = j / getUseSpeed(player);

        boolean flag = TFCMOptions.hasInfinityAmmo(player, is);
        //First we check the if there is ammo in the player's quiver
        boolean hasQuiverAmmo = flag || this.playerHasAmmoQuiver(player) != 0;
        boolean hasAmmo = false;
        //If there was no quiver or ammo, we check the inventory.
        if (!hasQuiverAmmo) {
            hasAmmo = this.playerHasAmmo(player) != 0;
        }

        if (load == false) {
            if (forceMult < 1.80F) {
                return;
            }
            if (forceMult > 1.80F) {
                forceMult = 1.80F;

                if (hasAmmo) {
                    int ammo = playerHasAmmo(player);
                    writeAmmoToCrossbow(is, ammo);
                    Item boltItem = ItemCustomQuiver.getBoltItemFromTier(ammo);
                    if (boltItem != null) {
                        player.inventory.consumeInventoryItem(boltItem);
                    }
                }
                else if (hasQuiverAmmo) {
                    int ammo = playerHasAmmoQuiver(player);
                    writeAmmoToCrossbow(is, ammo);
                    this.consumeBoltInQuiver(player, ammo);
                }

                is.stackTagCompound.setFloat("forceMult", forceMult);
                is.stackTagCompound.setBoolean("load", true);
            }
        }
    }

    public boolean crossbowHasAmmoLoaded(ItemStack is) {
        return is.stackTagCompound.getInteger("ammo") != 0;
    }

    public void writeAmmoToCrossbow(ItemStack is, Integer ammo) {
        is.stackTagCompound.setInteger("ammo", ammo);
    }

    //Checks to see if player has ammo, and what type of ammo he/she has.
    //Selects the lowest tier of ammo first.
    public int playerHasAmmo(EntityPlayer player) {
        final int sz = player.inventory.getSizeInventory();
        for (int i = 0; i < sz; i++) {
            ItemStack is0 = player.inventory.getStackInSlot(i);
            if (is0 != null && is0.getItem() != null) {
                int ammoId = ItemCustomQuiver.getMetalBoltTier(is0.getItem());
                if (ammoId > 0) {
                    return ammoId;
                }
            }
        }

        return 0;//false
    }

    //Checks the custom quiver for crossbow ammo.
    public int playerHasAmmoQuiver(EntityPlayer player) {
        ItemStack quiver = ((InventoryPlayerTFC) player.inventory).extraEquipInventory[0];

        if (quiver != null && quiver.getItem() instanceof ItemCustomQuiver) {
            final int tier = ((ItemCustomQuiver) quiver.getItem()).getQuiverMetalBoltTierOrZero(quiver);
            if (tier > 0) {
                return tier;
            }
            //if (((ItemCustomQuiver) quiver.getItem()).hasBoltAmmo(quiver) != null) {
            //    int t = ((ItemCustomQuiver) quiver.getItem()).getQuiverMetalBoltTier(quiver);
            //    return t;
            //}
        }
        return 0;
    }

    //Removes ammo from quiver.
    public void consumeBoltInQuiver(EntityPlayer player, int boltId) {
        ItemStack quiver = ((InventoryPlayerTFC) player.inventory).extraEquipInventory[0];

        if (quiver != null && quiver.getItem() instanceof ItemCustomQuiver) {
            Item ammo = ItemCustomQuiver.getBoltItemFromTier(boltId);
            ((ItemCustomQuiver) quiver.getItem()).consumeMetalAmmo(quiver, ammo, true);
        }
    }


    public void fireCrossbow(ItemStack is, World world, EntityPlayer player, float forceMult) {
        int ammoId = is.getTagCompound().getInteger("ammo");//ammoId == tier

        float ammoMult = 0.8f;//default сила удара

        EntityMetalBolt bolt = new EntityMetalBolt(world, player, 2);

        if (ammoId > 0 && ammoId < ItemCustomQuiver.AMMO_TIER_MULT.length) {
            bolt.setPickupItem(ItemCustomQuiver.getBoltItemFromTier(ammoId));//установка предмета (что поднимать)
            ammoMult = ItemCustomQuiver.AMMO_TIER_MULT[ammoId];
        }

        bolt.setDamage((400.0) * ammoMult);

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, is);

        if (k > 0) {
            bolt.setDamage(bolt.getDamage() + k * 50D + 25D);
        }

        int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, is);

        if (l > 0) {
            bolt.setKnockbackStrength(l);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, is) > 0) {
            bolt.setFire(100);
        }

        boolean flag = TFCMOptions.hasInfinityAmmo(player, is);

        if (flag) {
            bolt.canBePickedUp = 2;
        }

        is.damageItem(1, player);
        world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + forceMult * 0.5F);
        if (!world.isRemote) {
            world.spawnEntityInWorld(bolt);
        }

        is.stackTagCompound.setInteger("ammo", 0);
        is.stackTagCompound.setFloat("forceMult", 0);
        is.stackTagCompound.setBoolean("load", false);
    }

    public float getUseSpeed(EntityPlayer player) {
        float speed = 60.0f;
        ItemStack[] armor = player.inventory.armorInventory;
        for (int i = 0; i <= 3; i++) {
            ItemStack part = armor[i];
            if (part != null && part.getItem() instanceof ISize) {
                speed += 20.0f / ((ISize) part.getItem()).getWeight(part).multiplier;
            }
        }
        return speed;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
        ItemTerra.addSizeInformation(is, arraylist);

        if (is.stackTagCompound != null) {
            if (is.getTagCompound().getBoolean("load") == true) {
                arraylist.add(TFC_Core.translate("gui.crossbow.loaded") );
                final int boltId = is.getTagCompound().getInteger("ammo");
                Item ammo = ItemCustomQuiver.getBoltItemFromTier(boltId);//ammoi>-1 && ammoi < ammos.length ? ammos[ammoi] : "?";
                if (ammo != null) {
                    arraylist.add(TFC_Core.translate(ammo.getUnlocalizedName()));//TODO translate
                }
            }
            else {
                arraylist.add(TFC_Core.translate("gui.crossbow.unloaded"));
            }
        }
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.LARGE;
    }

    @Override
    public EnumWeight getWeight(ItemStack is) {
        return EnumWeight.HEAVY;
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumItemReach getReach(ItemStack is) {
        return EnumItemReach.SHORT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon("tfcm:CrossBow");
        iconArray = new IIcon[5];

        iconArray[0] = par1IconRegister.registerIcon("tfcm:CrossBow_loading_0");
        iconArray[1] = par1IconRegister.registerIcon("tfcm:CrossBow_loading_1");
        iconArray[2] = par1IconRegister.registerIcon("tfcm:CrossBow_loading_2");
        iconArray[3] = par1IconRegister.registerIcon("tfcm:CrossBow_loaded");
        iconArray[4] = par1IconRegister.registerIcon("tfcm:CrossBow");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getItemIconForUseDuration(int par1) {
        return iconArray[par1];
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (stack.stackTagCompound != null) {
            if (stack.getTagCompound().getBoolean("load") == true) {
                return getItemIconForUseDuration(3); //While loaded
            }
            else if (usingItem != null && usingItem.getItem() == TFCMItems.crossBow) {
                int j = usingItem.getMaxItemUseDuration() - useRemaining;
                float force = j / getUseSpeed(player);

                if (force >= 1.80) // Fully loaded
                {
                    return getItemIconForUseDuration(3);
                }
                else if (force > 0.90) {
                    return getItemIconForUseDuration(2);
                }
                else if (force > 0.30) {
                    return getItemIconForUseDuration(1);
                }
                else if (force > 0) // Beginning to load
                {
                    return getItemIconForUseDuration(0);
                }
            }
        }
        else {
            return getItemIconForUseDuration(4);
        }

        return getIcon(stack, renderPass);
    }
}
