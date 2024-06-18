package terramisc.core;

import java.io.File;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class TFCMOptions
{
	//Redstone
	public static boolean enableCraftingPiston = true;
	public static boolean enableCraftingLogicTiles = true;
	//Archery
	public static boolean enableCraftingLongbow = true;
	public static double LongbowDrawSpeedMult = 1;
	public static boolean enableCraftingCrossbow = true;
	public static boolean enableInfinityEnchantment = false;
	//Crucible Emptying
	public static boolean enableCrucibleEmptying = true;
	//Other
	public static boolean enableCraftingCompassClock = true;
	public static float TallowCandleBurnTime = 975;
	//Crops
	public static int pumpkinID = 100;
	public static int cayenneID = 101;
	public static int coffeeID= 102;

	public static void loadSettings()
	{
		System.out.println("[" + TFCMDetails.ModName + "] Loading options.");

		Configuration config;

		try
		{
			config = new Configuration(new File(terramisc.TerraMisc.instance.getMinecraftDirectory(), TFCMDetails.ConfigFilePath+TFCMDetails.ConfigFileName));
			config.load();
		}
		catch (Exception ex)
		{
			System.out.println("["+TFCMDetails.ModName+"] Error while trying to access settings configuration!");
			config = null;
		}

		/** Start Here **/
			//Redstone
		enableCraftingPiston = getBooleanFor(config, "RedStone", "enableCraftingPiston", true, "Enable Piston Crafting. (Default = true)");
		enableCraftingLogicTiles = getBooleanFor(config, "RedStone", "enableCraftingLogicTiles", true, "Enable Repeater and Comparator Crafting. (Default = true)");
			//Archery
		enableCraftingLongbow = getBooleanFor(config, "Archery", "enableCraftingLongbow", true, "Enable Longbow crafting. (Default = true)");
		LongbowDrawSpeedMult = getDoubleFor(config, "Archery", "LongbowDrawSpeedMult", 1, "Longbow draw speed modifier, higher the number the more time it takes to draw the bow. Must be greater than 0. (Default = 1");
		enableCraftingCrossbow = getBooleanFor(config, "Archery", "enableCraftingCrossbow", true, "Enable Crossbow crafting. (Default = true)");
		enableInfinityEnchantment = getBooleanFor(config, "Archery", "enableInfinityEnchantment", false, "Enable Crossbow and Longbow using of the Infinite ammo enchantmnet (Default = false)");
			//Crucible Emptying
		enableCrucibleEmptying = getBooleanFor(config, "Crucible Emptying", "enableCrucibleEmptying", true, "Enable Crucible Emptying Recipe. (Default = true)");
			//Other
		enableCraftingCompassClock = getBooleanFor(config, "Other", "enableCraftingCompassClock", true, "Enable crafting Clocks and Compasses. (Default = true)");
		TallowCandleBurnTime = getIntFor(config, "Other", "TallowCandleBurnTime", 975, "The number of hours the candle will burn for, torch default is 48, oil lamp default is 2000. (Default = 975");
			//Crops
		pumpkinID = getIntFor(config, "CropIDs", "pumpkinID", 100, "DO NOT CHANGE THIS UNLESS YOU KNOW WHAT YOU ARE DOING! (Default = 100");
		cayenneID = getIntFor(config, "CropIDs", "cayenneID", 101, "DO NOT CHANGE THIS UNLESS YOU KNOW WHAT YOU ARE DOING! (Default = 101");
		coffeeID = getIntFor(config, "CropIDs", "coffeeID", 102, "DO NOT CHANGE THIS UNLESS YOU KNOW WHAT YOU ARE DOING! (Default = 102");
		/** End Here*/
		if (config != null)
			config.save();

		System.out.println("[" + TFCMDetails.ModName + "] Done loading options.");
	}

	public static void loadSettings(TFCMOptions options)
	  {
	    System.out.println("[TerraMisc] Loading options from options class.");
	    	//Redstone
	    enableCraftingPiston = options == null ? true : enableCraftingPiston;
	    enableCraftingLogicTiles = options == null ? true : enableCraftingLogicTiles;
	    	//Archery
	    enableCraftingLongbow = options == null ? true : enableCraftingLongbow;
	    LongbowDrawSpeedMult = options == null ? 1: LongbowDrawSpeedMult;
	    enableCraftingCrossbow = options == null ? true : enableCraftingCrossbow;
	    	//Crucible Emptying
	    enableCrucibleEmptying = options == null ? true : enableCrucibleEmptying;
	    	//Other
	    enableCraftingCompassClock = options == null ? true : enableCraftingCompassClock;
	    TallowCandleBurnTime = options == null ? 1: TallowCandleBurnTime;

	    pumpkinID = options == null ? 1: pumpkinID;
	    cayenneID = options == null ? 1: cayenneID;
	    coffeeID = options == null ? 1: coffeeID;
	    System.out.println("[TerraMisc] Done loading options from options class.");
	  }

	public static boolean getBooleanFor(Configuration config,String heading, String item, boolean value)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			return prop.getBoolean(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + TFCMDetails.ModName + "] Error while trying to add Boolean, config wasn't loaded properly!");
		}
		return value;
	}

	public static boolean getBooleanFor(Configuration config,String heading, String item, boolean value, String comment)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			prop.comment = comment;
			return prop.getBoolean(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + TFCMDetails.ModName + "] Error while trying to add Boolean, config wasn't loaded properly!");
		}
		return value;
	}

	public static int getIntFor(Configuration config, String heading, String item, int value)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			return prop.getInt(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + TFCMDetails.ModName + "] Error while trying to add Integer, config wasn't loaded properly!");
		}
		return value;
	}

	public static int getIntFor(Configuration config,String heading, String item, int value, String comment)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			prop.comment = comment;
			return prop.getInt(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + TFCMDetails.ModName + "] Error while trying to add Integer, config wasn't loaded properly!");
		}
		return value;
	}

	public static double getDoubleFor(Configuration config,String heading, String item, double value)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			return prop.getDouble(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + TFCMDetails.ModName + "] Error while trying to add Double, config wasn't loaded properly!");
		}
		return value;
	}

	public static double getDoubleFor(Configuration config,String heading, String item, double value, String comment)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			prop.comment = comment;
			return prop.getDouble(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + TFCMDetails.ModName + "] Error while trying to add Double, config wasn't loaded properly!");
		}
		return value;
	}

	public static String getStringFor(Configuration config, String heading, String item, String value)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			return prop.getString();
		} catch (Exception e)
		{
			System.out.println("[" + TFCMDetails.ModName + "] Error while trying to add String, config wasn't loaded properly!");
		}
		return value;
	}

	public static String getStringFor(Configuration config, String heading, String item, String value, String comment)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			prop.comment = comment;
			return prop.getString();
		} catch (Exception e)
		{
			System.out.println("[" + TFCMDetails.ModName + "] Error while trying to add String, config wasn't loaded properly!");
		}
		return value;
	}

    // Used by Crossbow and Longbow
    // Check to see if player is in creative mode or has enchantments.
	public static boolean hasInfinityAmmo(EntityPlayer p, ItemStack is)
    {
        if (is == null || p == null) return false;

        return p.capabilities.isCreativeMode || enableInfinityEnchantment &&
            EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, is) > 0;
    }
}
