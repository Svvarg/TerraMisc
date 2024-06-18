package terramisc.handlers;

import java.util.Random;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.Entities.Mobs.EntityBear;
import com.bioxx.tfc.Entities.Mobs.EntityCowTFC;
import com.bioxx.tfc.Entities.Mobs.EntityDeer;
import com.bioxx.tfc.Entities.Mobs.EntityHorseTFC;
import com.bioxx.tfc.Entities.Mobs.EntitySheepTFC;
import com.bioxx.tfc.api.Entities.IAnimal;
import com.bioxx.tfc.api.Util.Helper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import terramisc.core.TFCMItems;

public class TFCMMobDropHandler
{
	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event)
	{
		if(event.entityLiving instanceof EntityDeer)
		{
			float birth = ((IAnimal) event.entityLiving).getBirthDay();
			float time = TFC_Time.getTotalDays();
			float ageMod = (time - birth) / ((IAnimal) event.entityLiving).getNumberOfDaysToAdult();

			event.entityLiving.entityDropItem(new ItemStack(TFCMItems.deerTendon, (int) (2 * ageMod)), 0);
		}

		if(event.entityLiving instanceof EntityHorseTFC)
		{
			float birth = ((IAnimal) event.entityLiving).getBirthDay();
			float time = TFC_Time.getTotalDays();
			float ageMod = (time - birth) / ((IAnimal) event.entityLiving).getNumberOfDaysToAdult();

			event.entityLiving.entityDropItem(new ItemStack(TFCMItems.deerTendon, (int) (2 * ageMod)), 0);
		}

		if(event.entityLiving instanceof EntityCowTFC)
		{
			float birth = ((IAnimal) event.entityLiving).getBirthDay();
			float time = TFC_Time.getTotalDays();
			float ageMod = (time - birth) / ((IAnimal) event.entityLiving).getNumberOfDaysToAdult();

			event.entityLiving.entityDropItem(new ItemStack(TFCMItems.suet, (int) (4 * ageMod)), 0);
		}

		if(event.entityLiving instanceof EntitySheepTFC)
		{
			float birth = ((IAnimal) event.entityLiving).getBirthDay();
			float time = TFC_Time.getTotalDays();
			float ageMod = (time - birth) / ((IAnimal) event.entityLiving).getNumberOfDaysToAdult();

			event.entityLiving.entityDropItem(new ItemStack(TFCMItems.suet, (int) (3 * ageMod)), 0);
		}

		if(event.entityLiving instanceof EntityBear)
		{
			EntityBear bear = (EntityBear) ((IAnimal) event.entityLiving);
			float birth = ((IAnimal) event.entityLiving).getBirthDay();
			float time = TFC_Time.getTotalDays();
			float ageMod = (time - birth) / ((IAnimal) event.entityLiving).getNumberOfDaysToAdult();

			Random r = new Random();
			float foodWeight = 120 + (120 * (10 * r.nextFloat() - 5) / 100) * ageMod;
			foodWeight = Helper.roundNumber(foodWeight, 10);

			event.entityLiving.entityDropItem(new ItemStack(TFCMItems.suet, (int) (6 * ageMod)), 0);
			TFC_Core.animalDropMeat(bear, TFCMItems.bearRaw, foodWeight);

		}

	}
}

