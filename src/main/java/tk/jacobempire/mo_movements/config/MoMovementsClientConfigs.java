package tk.jacobempire.mo_movements.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MoMovementsClientConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Configs for Mo' Movements");

        //Define the Configs here


        BUILDER.pop();
        SPEC = BUILDER.build();

    }
}
