package mattwamm.creatures.entity;

import mattwamm.creatures.Creatures;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<RacoonEntity> RACOON = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Creatures.MOD_ID, "racoon"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RacoonEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4f,0.3f)).build());
    public static final EntityType<YetiEntity> YETI = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Creatures.MOD_ID, "yeti"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, YetiEntity::new)
                    .dimensions(EntityDimensions.fixed(1.4f,3f)).build());

}
