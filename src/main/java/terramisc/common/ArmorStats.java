package terramisc.common;

import com.bioxx.tfc.api.Armor;
import com.bioxx.tfc.api.Constant.Global;

public class ArmorStats {

    public static Armor CopperChain;
    public static Armor BismuthBronzeChain;
    public static Armor BlackBronzeChain;
    public static Armor BronzeChain;
    public static Armor WroughtIronChain;
    public static Armor SteelChain;
    public static Armor BlackSteelChain;
    public static Armor BlueSteelChain;
    public static Armor RedSteelChain;

    public static void setupTFCMArmorTypes() {                                                       //Old    Actual
        CopperChain        = new Armor( 480,  480, 100, Global.COPPER,        "Copper_Chain");       //Tier 0  1
        BismuthBronzeChain = new Armor( 720,  480, 132, Global.BISMUTHBRONZE, "BismuthBronze_Chain");//Tier 1  2
        BlackBronzeChain   = new Armor( 480,  720, 132, Global.BLACKBRONZE,   "BlackBronze_Chain");  //Tier 1  2
        BronzeChain        = new Armor( 600,  600, 132, Global.BRONZE,        "Bronze_Chain");       //Tier 1  2
        WroughtIronChain   = new Armor( 960,  960, 212, Global.WROUGHTIRON,   "WroughtIron_Chain");  //Tier 2  3
        SteelChain         = new Armor(1200, 1440, 264, Global.STEEL,         "Steel_Chain");        //Tier 3  4
        BlackSteelChain    = new Armor(2400, 2160, 528, Global.BLACKSTEEL,    "BlackSteel_Chain");   //Tier 4  5
        BlueSteelChain     = new Armor(3000, 2400, 800, Global.BLUESTEEL,     "BlueSteel_Chain");    //Tier 5  6
        RedSteelChain      = new Armor(2400, 3000, 800, Global.REDSTEEL,      "RedSteel_Chain");     //Tier 5  6
    }

    //public static Armor CopperChainPlate        = new Armor( 440,  440,  175, Global.COPPER,        "Copper_ChainPlate");//Tier 0
    //public static Armor BismuthBronzeChainPlate = new Armor( 660,  440,  231, Global.BISMUTHBRONZE, "BismuthBronze_ChainPlate");//Tier 1
    //public static Armor BlackBronzeChainPlate   = new Armor( 440,  660,  231, Global.BLACKBRONZE,   "BlackBronze_ChainPlate");//Tier 1
    //public static Armor BronzeChainPlate        = new Armor( 550,  550,  231, Global.BRONZE,        "Bronze_ChainPlate");//Tier 1
    //public static Armor WroughtIronChainPlate   = new Armor( 880,  880,  370, Global.WROUGHTIRON,   "WroughtIron_ChainPlate");//Tier 2
    //public static Armor SteelChainPlate         = new Armor(1100, 1320,  462, Global.STEEL,         "Steel_ChainPlate");//Tier 3
    //public static Armor BlackSteelChainPlate    = new Armor(2200, 1980,  924, Global.BLACKSTEEL,    "BlackSteel_ChainPlate");//Tier 4
    //public static Armor BlueSteelChainPlate     = new Armor(2750, 2200, 1400, Global.BLUESTEEL,     "BlueSteel_ChainPlate");//Tier 5
    //public static Armor RedSteelChainPlate      = new Armor(2200, 2750, 1400, Global.REDSTEEL,      "RedSteel_ChainPlate");//Tier 5
}
