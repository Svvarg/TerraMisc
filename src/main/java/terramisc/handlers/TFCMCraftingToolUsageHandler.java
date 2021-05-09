package terramisc.handlers;

import com.bioxx.tfc.Core.Player.SkillStats;
import com.bioxx.tfc.Core.Recipes;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Crafting.AnvilManager;
import terramisc.core.TFCMItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import terramisc.items.tools.ItemCustomArmor;

public class TFCMCraftingToolUsageHandler {


    /**
     * Если в крафте есть хотябы один предмет ItemCustomArmor считать это починкой
     * @param craftMatrix
     * @return
     */
    public boolean isRepairChainArmor(IInventory craftMatrix) {
        if (craftMatrix != null) {
            final int sz = craftMatrix.getSizeInventory();
            for (int i = 0; i < sz; i++) {
                ItemStack is = craftMatrix.getStackInSlot(i);
                if (is != null && is.getItem() instanceof ItemCustomArmor) {
                    return true;
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent e) {
        Item item = e.crafting.getItem();
        IInventory craftMatrix = e.craftMatrix;

        if (craftMatrix != null) {
            //перенос бонуса кузнеца при создании кольчуги ( важно! можно получить из двух разных шлемов один целый как ремонт обычной кожанки)
            if (item instanceof ItemCustomArmor) {
                //не переносить бонус кузнеца для починке например двух одинаковых частей брони
                if (!isRepairChainArmor(craftMatrix)) {
                    SkillStats sk = TFC_Core.getSkillStats(e.player);
                    //в самом тфк это можно найти в TEAnvil  recipe.getSkillMult(lastWorker);
                    float durabuff = (sk.getSkillMultiplier(Global.SKILL_ARMORSMITH) + sk.getSkillMultiplier(Global.SKILL_GENERAL_SMITHING))/2 ;//skill.armorsmith
                    if (durabuff > 0) {
                        AnvilManager.setDurabilityBuff(e.crafting, durabuff);
                    }
                }
            }
            else if (item == TFCMItems.bowLimb) {
                if (e.player.capabilities.isCreativeMode) {
                    e.crafting.setItemDamage(0);
                }

                for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
                    if (craftMatrix.getStackInSlot(i) == null) {
                        continue;
                    }

                    for (int j = 0; j < Recipes.knives.length; j++) {
                        if (craftMatrix.getStackInSlot(i).getItem() == Recipes.knives[j]) {
                            ItemStack tfcKnife = craftMatrix.getStackInSlot(i).copy();
                            if (tfcKnife != null) {
                                tfcKnife.damageItem(1, e.player);
                                if (tfcKnife.getItemDamage() != 0 || e.player.capabilities.isCreativeMode) {
                                    craftMatrix.setInventorySlotContents(i, tfcKnife);
                                    craftMatrix.getStackInSlot(i).stackSize = 2;
                                }
                            }
                        }
                    }
                }
            }
            else if (item == TFCMItems.sinewFiber) {
                if (e.player.capabilities.isCreativeMode) {
                    e.crafting.setItemDamage(0);
                }

                for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
                    if (craftMatrix.getStackInSlot(i) == null) {
                        continue;
                    }

                    for (int j = 0; j < Recipes.hammers.length; j++) {
                        if (craftMatrix.getStackInSlot(i).getItem() == Recipes.hammers[j]) {
                            ItemStack tfcHammer = craftMatrix.getStackInSlot(i).copy();
                            if (tfcHammer != null) {
                                tfcHammer.damageItem(1, e.player);
                                if (tfcHammer.getItemDamage() != 0 || e.player.capabilities.isCreativeMode) {
                                    craftMatrix.setInventorySlotContents(i, tfcHammer);
                                    craftMatrix.getStackInSlot(i).stackSize = 2;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
