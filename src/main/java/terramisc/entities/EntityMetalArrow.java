package terramisc.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import terramisc.core.TFCMItems;

import com.bioxx.tfc.Entities.EntityProjectileTFC;

public class EntityMetalArrow extends EntityProjectileTFC {

    public EntityMetalArrow(World par1World) {
        super(par1World);
        this.pickupItem = TFCMItems.arrow_Copper;
    }

    public EntityMetalArrow(World par1World, EntityLivingBase shooter, float force) {
        super(par1World, shooter, force);
        this.pickupItem = TFCMItems.arrow_Copper;
    }
}
