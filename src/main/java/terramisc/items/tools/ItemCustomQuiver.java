package terramisc.items.tools;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import terramisc.TerraMisc;
import terramisc.core.TFCMItems;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Items.ItemArrow;
import com.bioxx.tfc.Items.ItemQuiver;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.TFCItems;
import net.minecraft.nbt.NBTTagList;
import terramisc.core.TFCMItemsSetup;

/**
 * Коды типа стрел и болтов пишутся в лук, арбалет.
 * Значение кодов - ammoId (tier) находятся здесь, в данном классе, как и урон от разных металлов
 * @author Swarg
 */
public class ItemCustomQuiver extends ItemQuiver
{

    //сила удара стрел и болтов в зависимости от метталла
    //arrow: 0:stone   1:copper ....
    //bolt:  0:default 1:copper 2:bisBronze 3:Broze 4:BlackBronze 5:WhroIron 6:Steel 7:BlackSteel 8:BlueSteel 9:RedSteel
    public static final float[] AMMO_TIER_MULT = {0.55f, 0.60f, 0.65f, 0.70f, 0.75f, 0.80f, 0.85f, 0.90f, 1.00f, 1.00f };
    //                                                0    1       2      3     4      5      6      7      8      9

    //Opens gui and stuff.
    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.openGui(TerraMisc.instance, 2, entityplayer.worldObj, (int) entityplayer.posX, (int) entityplayer.posY, (int) entityplayer.posZ);
        return itemstack;
    }

    //
    /**
     * Gathers information on the amount of arrows inside the quiver.
     * Подсчёт общего количества
     * @param quiver
     * @return
     */
    public int getQuiverMetalArrows(ItemStack quiver) {
        int n = 0;
        NBTTagList nbttaglist = TFC_Core.getItemsList(quiver);
        if (nbttaglist != null && nbttaglist.tagCount() > 0) {
            final int sz = nbttaglist.tagCount();
            for (int i = 0; i < sz; i++) {
                NBTTagCompound slotNbt = nbttaglist.getCompoundTagAt(i);
                if (slotNbt != null && slotNbt.hasKey("id")) {
                    final byte slot = slotNbt.getByte("Slot");
                    if (slot >= 0 && slot < 8) {
                        Item item0 = Item.getItemById(slotNbt.getShort("id"));
                        if (item0 != null && (item0 instanceof ItemArrow || item0 instanceof ItemCustomArrow)) {
                            n += slotNbt.getByte("Count");
                        }
                    }
                }
            }
        }
        //ItemStack[] inventory = loadInventory(item);
        //for (ItemStack i : inventory) {
        //    if (i != null && (i.getItem() instanceof ItemArrow || i.getItem() instanceof ItemCustomArrow)) {
        //        n += i.stackSize;
        //    }
        //}
        return n;
    }

    /**
     * Вернуть код стрелы если в колчане вообще есть стрелы
     * либо -1 если стрел нет
     * Два в одном hasArrowAmmo и getQuiverMetalArrowTier
     * @param quiver
     * @return
     */
    public int getQuiverMetalArrowTierOrZero(ItemStack quiver) {
        NBTTagList nbttaglist = TFC_Core.getItemsList(quiver);
        if (nbttaglist != null && nbttaglist.tagCount() > 0) {
            final int sz = nbttaglist.tagCount();
            for (int i = 0; i < sz; i++) {
                NBTTagCompound slotNbt = nbttaglist.getCompoundTagAt(i);
                if (slotNbt != null && slotNbt.hasKey("id")) {
                    final byte slot = slotNbt.getByte("Slot");
                    if (slot >= 0 && slot < 8) {
                        Item item0 = Item.getItemById(slotNbt.getShort("id"));
                        if (item0 != null && (item0 instanceof ItemArrow || item0 instanceof ItemCustomArrow)) {
                            int count = slotNbt.getByte("Count");
                            if (count > 0) {
                                return getMetalArrowTier(item0);
                            }
                            //else clear slot?
                        }
                    }
                }
            }
        }
        return 0;//нет стрел
    }

    ////Returns boolean for to determine if ammo is present.
    //public ItemStack hasArrowAmmo(ItemStack quiver) {
    //    ItemStack[] inventory = loadInventory(quiver);
    //    for (int i = 0; i < inventory.length; i++) {
    //        if (inventory[i] != null && (inventory[i].getItem() instanceof ItemArrow || inventory[i].getItem() instanceof ItemCustomArrow)) {
    //            ItemStack out = inventory[i].copy();
    //            out.stackSize = 1;
    //
    //            if (inventory[i].stackSize <= 0) {
    //                inventory[i] = null;
    //            }
    //
    //            saveInventory(quiver, inventory);
    //
    //            return out;
    //        }
    //    }
    //    return null;
    //}

    ////Gets ammo tier for longbow to determine damage, and other triggers.
    //public int getQuiverMetalArrowTier(ItemStack item) {
    //    ItemStack[] inventory = loadInventory(item);
    //    for (ItemStack is : inventory) {
    //        Item i = is == null ? null : is.getItem();
    //        return arrowTier = getMetalArrowTier(i);
    //    }
    //    return arrowTier;//???
    //}

    //item - тут скорее ammoId а не metalTier (code-tier)
    public static int getMetalArrowTier(Item item) {
        if (item != null) {
            for (int i = 1; i < TFCMItemsSetup.ARROWS.length; i++) {
                if (TFCMItemsSetup.ARROWS[i] == item) {
                    return i;//ammoId
                }
            }
        }
        return 0;
    }

    //tier-code to Arrow
    public static Item getArrowItemFromTier(int t) {
        if (t > 0 && t < TFCMItems.ARROWS.length) {
            return TFCMItems.ARROWS[t];//ammoId look to TFCMItemsSetup
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    // Storing both Strings and Integers in the same ArrayList
    public List[] getQuiverArrowTypes(ItemStack item) {
        ArrayList[] pair = new ArrayList[2];
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<Integer> listNum = new ArrayList<Integer>();
        ItemStack[] inventory = loadInventory(item);
        for (ItemStack i : inventory) {
            if (i != null && (i.getItem() instanceof ItemArrow || i.getItem() instanceof ItemCustomArrow)) {
                String s = i.getItem().getItemStackDisplayName(i);
                if (!list.contains(s)) {
                    list.add(s);
                }
                int n = list.indexOf(s);
                if (listNum.size() == n) {
                    listNum.add(0);
                }
                listNum.set(n, listNum.get(n) + i.stackSize);
            }
        }
        pair[0] = list;
        pair[1] = listNum;
        return pair;
    }

    //Gathers information on the amount of bolts inside the quiver.
    public int getQuiverMetalBolts(ItemStack quiver) {
        int n = 0;
        NBTTagList nbttaglist = TFC_Core.getItemsList(quiver);
        if (nbttaglist != null && nbttaglist.tagCount() > 0) {
            final int sz = nbttaglist.tagCount();
            for (int i = 0; i < sz; i++) {
                NBTTagCompound slotNbt = nbttaglist.getCompoundTagAt(i);
                if (slotNbt != null && slotNbt.hasKey("id")) {
                    final byte slot = slotNbt.getByte("Slot");
                    if (slot >= 0 && slot < 8) {
                        Item item0 = Item.getItemById(slotNbt.getShort("id"));
                        if (item0 != null && item0 instanceof ItemCustomBolt) {
                            n += slotNbt.getByte("Count");//i.stackSize;
                        }
                    }
                }
            }
        }
        //ItemStack[] inventory = loadInventory(item);
        //for (ItemStack i : inventory) {
        //    if (i != null && (i.getItem() instanceof ItemCustomBolt)) {
        //        n += i.stackSize;
        //    }
        //}
        return n;
    }

    //
    /**
     * hasBoltAmmo & getQuiverMetalBoltTier
     * Перебор с конца а не с начала для того чтобы при дальнейшем изьятии
     * снарядов не двигать массив т.е. вынимать с конца массива
     * @param quiver
     * @return
     */
    public int getQuiverMetalBoltTierOrZero(ItemStack quiver) {
        NBTTagList nbttaglist = TFC_Core.getItemsList(quiver);
        if (nbttaglist != null && nbttaglist.tagCount() > 0) {
            final int sz = nbttaglist.tagCount();
            for (int i = 0; i < sz; i++) {
                NBTTagCompound slotNbt = nbttaglist.getCompoundTagAt(i);
                if (slotNbt != null && slotNbt.hasKey("id")) {
                    final byte slot = slotNbt.getByte("Slot");
                    if (slot >= 0 && slot < 8) {
                        Item item0 = Item.getItemById(slotNbt.getShort("id"));
                        if (item0 != null && item0 instanceof ItemCustomBolt) {
                            int count = slotNbt.getByte("Count");
                            if (count > 0) {
                                return getMetalBoltTier(item0);
                            }
                            //else clear slot?
                        }
                    }
                }
            }
        }
        return 0;//нет болтов
    }

//    //Returns boolean for to determine if ammo is present.
//    public ItemStack hasBoltAmmo(ItemStack quiver) {
//        ItemStack[] inventory = loadInventory(quiver);
//        for (int i = 0; i < inventory.length; i++) {
//            if (inventory[i] != null && (inventory[i].getItem() instanceof ItemCustomBolt)) {
//                ItemStack out = inventory[i].copy();
//                out.stackSize = 1;
//
//                if (inventory[i].stackSize <= 0) {
//                    inventory[i] = null;
//                }
//
//                saveInventory(quiver, inventory);
//
//                return out;
//            }
//        }
//        return null;
//    }
//
//    //Gets ammo tier for crossbow to determine damage, and other triggers.
//    public int getQuiverMetalBoltTier(ItemStack item) {
//        ItemStack[] inventory = loadInventory(item);
//        for (ItemStack is : inventory) {
//            Item i = is == null ? null : is.getItem();
//            return getMetalBoltTier(i);//boltTier
//        }
//        return 0;
//    }

    //это скорее не tier а код металла который пишется в "заряжено-летящий" болт
    public static int getMetalBoltTier(Item item) {
        if (item != null) {
            for (int i = 1; i < TFCMItemsSetup.BOLTS.length; i++) {
                if (TFCMItemsSetup.BOLTS[i] == item) {
                    return i;//ammoId
                }
            }
        }
        return 0;
    }

    public static Item getBoltItemFromTier(int boltId) {
        if (boltId > 0 && boltId < TFCMItems.BOLTS.length) {
            return TFCMItems.BOLTS[boltId];//ammoId look to TFCMItemsSetup
        }
        return null;
    }


    @SuppressWarnings("rawtypes")
    // Storing both Strings and Integers in the same ArrayList
    public List[] getQuiverBoltTypes(ItemStack item) {
        ArrayList[] pair = new ArrayList[2];
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<Integer> listNum = new ArrayList<Integer>();
        ItemStack[] inventory = loadInventory(item);
        for (ItemStack i : inventory) {
            if (i != null && i.getItem() instanceof ItemCustomBolt) {
                String s = i.getItem().getItemStackDisplayName(i);
                if (!list.contains(s)) {
                    list.add(s);
                }
                int n = list.indexOf(s);
                if (listNum.size() == n) {
                    listNum.add(0);
                }
                listNum.set(n, listNum.get(n) + i.stackSize);
            }
        }
        pair[0] = list;
        pair[1] = listNum;
        return pair;
    }

    //
    /**
     * Removes ammo from quiver. (Забрать из колчана снаряд-предмет ammo)
     * В оригинальном классе тфк ItemQuiver здесь возвращается ItemStack
     * снаряда-предмета, при этом он используется только для копий т.к. будет
     * перемещение следующего копья в руку. Для стрел просто проверка на успешное
     * изьятие снаряда из колчана. Поэтому здесь return вместо ItemStack даёт boolean
     * @param quiver
     * @param ammo
     * @param shouldConsume
     * @return
     */
    public boolean/*ItemStack*/ consumeMetalAmmo(ItemStack quiver, Item ammo, boolean shouldConsume) {
        if (shouldConsume) {
            NBTTagList nbttaglist = TFC_Core.getItemsList(quiver);
            if (nbttaglist != null && nbttaglist.tagCount() > 0) {
                final int sz = nbttaglist.tagCount();
                final int ammoItemId = Item.getIdFromItem(ammo);
                
                for (int i = 0; i < sz; i++) {
                    NBTTagCompound slotNbt = nbttaglist.getCompoundTagAt(i);
                    if (slotNbt != null && slotNbt.hasKey("id")) {
                        final byte slot = slotNbt.getByte("Slot");
                        if (slot >= 0 && slot < 8) {
                            //Item item0 = Item.getItemById(slotNbt.getShort("id"));
                            //if (item0 != null && item0 == ammo) {
                            if (ammoItemId == slotNbt.getShort("id")) {
                                int stackSize = slotNbt.getByte("Count");
                                if (stackSize == 1) {
                                    nbttaglist.removeTag(i);
                                    return true;
                                }
                                else if (stackSize > 1) {
                                    slotNbt.setByte("Count", (byte) (stackSize - 1));//saveInventory(
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        //ItemStack[] inventory = loadInventory(quiver);
        //for (int i = 0; i < inventory.length; i++) {
        //    if (inventory[i] != null && inventory[i].getItem() == ammo) {
        //        ItemStack out = inventory[i].copy();
        //        out.stackSize = 1;
        //        if (shouldConsume) {
        //            inventory[i].stackSize--;
        //        }
        //        if (inventory[i].stackSize <= 0) {
        //            inventory[i] = null;
        //        }
        //        saveInventory(quiver, inventory);
        //        return out;
        //    }
        //}
        return false;//null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
        ItemTerra.addSizeInformation(is, arraylist);

        if (TFC_Core.showShiftInformation()) {
            arraylist.add(EnumChatFormatting.WHITE + TFC_Core.translate("gui.Advanced") + ":");

            arraylist.add(EnumChatFormatting.ITALIC + TFC_Core.translate("gui.Bow.Arrows") + ": " + EnumChatFormatting.YELLOW + getQuiverMetalArrows(is));//
            List[] javData1 = getQuiverArrowTypes(is);
            for (int i = 0; i < javData1[0].size(); i++) {
                String s = (String) (javData1[0].get(i));
                int n = (Integer) (javData1[1].get(i));
                arraylist.add(EnumChatFormatting.ITALIC + "  -" + s + ": " + EnumChatFormatting.YELLOW + n);
            }

            arraylist.add(EnumChatFormatting.ITALIC + TFC_Core.translate("gui.crossbow.bolts") + ": " + EnumChatFormatting.YELLOW + getQuiverMetalBolts(is));
            List[] javData2 = getQuiverBoltTypes(is);
            for (int i = 0; i < javData2[0].size(); i++) {
                String s1 = (String) (javData2[0].get(i));
                int n1 = (Integer) (javData2[1].get(i));
                arraylist.add(EnumChatFormatting.ITALIC + "  -" + s1 + ": " + EnumChatFormatting.YELLOW + n1);
            }

            arraylist.add(EnumChatFormatting.ITALIC + TFC_Core.translate("gui.Bow.Javelins") + ": " + EnumChatFormatting.YELLOW + getQuiverJavelins(is));
            List[] javData3 = getQuiverJavelinTypes(is);
            for (int i = 0; i < javData3[0].size(); i++) {
                String s2 = (String) (javData3[0].get(i));
                int n2 = (Integer) (javData3[1].get(i));
                arraylist.add(EnumChatFormatting.ITALIC + "  -" + s2 + ": " + EnumChatFormatting.YELLOW + n2);
            }

            if (is.hasTagCompound()) {
                NBTTagCompound stackTagCompound = is.getTagCompound();
                if (stackTagCompound.hasKey("creator")) {
                    arraylist.add(EnumChatFormatting.ITALIC + TFC_Core.translate("gui.Armor.ForgedBy") + " " + stackTagCompound.getString("creator"));
                }
            }
        }
        else {
            arraylist.add(EnumChatFormatting.DARK_GRAY + TFC_Core.translate("gui.Advanced") + ": (" + TFC_Core.translate("gui.Hold") + " " + EnumChatFormatting.GRAY + TFC_Core.translate("gui.Shift")
                    + EnumChatFormatting.DARK_GRAY + ")");
        }

    }
}
