package terramisc.entities;

import terramisc.core.TFCMItems;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;

import com.bioxx.tfc.Entities.EntityProjectileTFC;
import com.bioxx.tfc.api.Enums.EnumDamageType;

public class EntityMetalBolt extends EntityProjectileTFC {

    public EntityMetalBolt(World world) {
        super(world);
        this.pickupItem = TFCMItems.bolt_Copper;
    }

    public EntityMetalBolt(World world, EntityLivingBase shooter, float force) {
        super(world, shooter, force);
        this.pickupItem = TFCMItems.bolt_Copper;
    }

    @Override
    public EnumDamageType getDamageType() {
        return EnumDamageType.CRUSHING;
    }
}
