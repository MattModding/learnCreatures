package mattwamm.creatures;

import mattwamm.creatures.util.ModRegistries;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import software.bernie.geckolib3.GeckoLib;

public class Creatures implements ModInitializer {

    public static final String MOD_ID = "creatures";


    @Override
    public void onInitialize() {

        ModRegistries.mainRegister();

        GeckoLib.initialize();
    }
}
