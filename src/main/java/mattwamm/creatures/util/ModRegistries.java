package mattwamm.creatures.util;

import mattwamm.creatures.entity.ModEntities;
import mattwamm.creatures.entity.RacoonEntity;
import mattwamm.creatures.entity.YetiEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModRegistries {

    public static void mainRegister(){
        registerAttributes();
    }


    private static void registerAttributes()
    {
        FabricDefaultAttributeRegistry.register(ModEntities.RACOON, RacoonEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.YETI, YetiEntity.setAttributes());
    }
}
