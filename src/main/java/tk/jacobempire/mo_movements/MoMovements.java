package tk.jacobempire.mo_movements;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import tk.jacobempire.mo_movements.commands.CommandSit;
import tk.jacobempire.mo_movements.config.MoMovementsClientConfigs;
import tk.jacobempire.mo_movements.event.ClientEvents;
import tk.jacobempire.mo_movements.networking.ModPackets;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MoMovements.MODID)
public class MoMovements
{

    public static final String MODID = "momovements";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MoMovements()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);


        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MoMovementsClientConfigs.SPEC, "mo-movements-client.toml");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {

        });

        ModPackets.register();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandSit.register(dispatcher);

    }

    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        this.registerCommands(commandDispatcher);
    }
    @SubscribeEvent
    public void playerEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player != null && player.getForcedPose() != null) {
            if (player.getForcedPose().equals(Pose.SLEEPING) || player.getForcedPose().equals(Pose.SWIMMING)) {
                if (player.isUnderWater()) {
                    player.setForcedPose(null);
                    ClientEvents.laying = false;
                    ClientEvents.crawling = false;
                }
            }
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
