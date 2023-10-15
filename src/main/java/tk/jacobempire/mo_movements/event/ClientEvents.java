package tk.jacobempire.mo_movements.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.jacobempire.mo_movements.MoMovements;
import tk.jacobempire.mo_movements.util.KeyBinding;

import java.util.List;

import static net.minecraft.commands.Commands.literal;

@Mod.EventBusSubscriber(modid = MoMovements.MODID, value = Dist.CLIENT)
public class ClientEvents {
    public static boolean crawling = false;
    public static boolean laying = false;
    private static final String TAG_CHAIR = "Chair";

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null) {
            Level level = player.getLevel();
            if (player.getServer() != null) {
                ServerLevel serverLevel = player.getServer().getLevel(level.dimension());
                ServerPlayer serverPlayer = (ServerPlayer) serverLevel.getPlayerByUUID(player.getUUID());
                if (KeyBinding.CRAWL_KEY.isDown()) {

                    if (!crawling) {
                        player.sendSystemMessage(Component.literal("You are now crawling.").withStyle(ChatFormatting.GREEN));
                        player.setForcedPose(Pose.SWIMMING);
                        player.refreshDimensions();
                        crawling = true;
                    } else {
                        player.sendSystemMessage(Component.literal("You are no longer crawling.").withStyle(ChatFormatting.GREEN));
                        player.setForcedPose(null);
                        player.refreshDimensions();
                        crawling = false;
                    }
                }
                if (KeyBinding.LAY_KEY.isDown()) {
                    if (!laying) {
                        player.sendSystemMessage(Component.literal("You are now laying down.").withStyle(ChatFormatting.GREEN));
                        player.setForcedPose(Pose.SLEEPING);
                        player.refreshDimensions();
                        laying = true;
                    } else {
                        player.sendSystemMessage(Component.literal("You are no longer laying down.").withStyle(ChatFormatting.GREEN));
                        player.setForcedPose(null);
                        player.refreshDimensions();
                        laying = false;
                    }
                }

                if (KeyBinding.SIT_KEY.isDown()) {

                    sit(serverPlayer, serverLevel);
                    player.refreshDimensions();
                }
            }
            if (mc.hasSingleplayerServer()) {
                ServerLevel serverLevel = mc.getSingleplayerServer().getLevel(level.dimension());
                ServerPlayer serverPlayer = (ServerPlayer) serverLevel.getPlayerByUUID(player.getUUID());
                if (KeyBinding.CRAWL_KEY.isDown()) {

                    if (!crawling) {
                        player.sendSystemMessage(Component.literal("You are now crawling.").withStyle(ChatFormatting.GREEN));
                        player.setForcedPose(Pose.SWIMMING);
                        player.refreshDimensions();
                        crawling = true;
                    } else {
                        player.sendSystemMessage(Component.literal("You are no longer crawling.").withStyle(ChatFormatting.GREEN));
                        player.setForcedPose(null);
                        player.refreshDimensions();
                        crawling = false;
                    }
                }
                if (KeyBinding.LAY_KEY.isDown()) {
                    if (!laying) {
                        player.sendSystemMessage(Component.literal("You are now laying down.").withStyle(ChatFormatting.GREEN));
                        player.setForcedPose(Pose.SLEEPING);
                        player.refreshDimensions();
                        laying = true;
                    } else {
                        player.sendSystemMessage(Component.literal("You are no longer laying down.").withStyle(ChatFormatting.GREEN));
                        player.setForcedPose(null);
                        player.refreshDimensions();
                        laying = false;
                    }
                }

                if (KeyBinding.SIT_KEY.isDown()) {
                    sit(serverPlayer, serverLevel);
                    player.refreshDimensions();
                }
            } else
                player.sendSystemMessage(Component.literal("Error: Code executed on client side!").withStyle(ChatFormatting.DARK_RED));
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralArgumentBuilder<CommandSourceStack> sitCommand = literal("sit")
                .executes(context -> {
                    Entity entity = context.getSource().getEntity();
                    if (entity instanceof ServerPlayer player) {
                        sit(context.getSource().getPlayer(), context.getSource().getLevel());
                        return 1;
                    } else {
                        context.getSource().sendFailure(Component.literal("Only players can use this command!").withStyle(ChatFormatting.DARK_RED));
                        return 0;
                    }
                });

        dispatcher.register(sitCommand);
    }

    private static void sit(Entity entity, Level level) {
        if (entity instanceof ServerPlayer player) {
            ArmorStand armorStand = new ArmorStand(EntityType.ARMOR_STAND, level);
            armorStand.setPos(player.getX(), player.getY() - 1.7, player.getZ());
            armorStand.getOnPos();
            CompoundTag tag = armorStand.getPersistentData();
            armorStand.setInvisible(true);
            armorStand.setNoGravity(true);
            armorStand.setInvulnerable(true);
            tag.putBoolean("Marker", true);
            tag.putBoolean("Small", true);
            armorStand.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.AIR));
            armorStand.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.AIR));
            armorStand.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.AIR));
            armorStand.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.AIR));

            // Add "chair" tag to armor stand
            tag.putBoolean(TAG_CHAIR, true);

            level.addFreshEntity(armorStand);
            player.startRiding(armorStand, true);
            player.sendSystemMessage(Component.literal("You have sat down.").withStyle(ChatFormatting.GREEN));

            // Find and kill unoccupied chairs
            List<ArmorStand> chairs = level.getEntitiesOfClass(ArmorStand.class, armorStand.getBoundingBox().inflate(1.0));
            for (ArmorStand chair : chairs) {
                if (chair.getTags().contains(TAG_CHAIR)) {
                    if (chair.getPassengers().isEmpty()) {
                        chair.discard();
                    }
                } else return;
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MoMovements.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.SIT_KEY);
            event.register(KeyBinding.CRAWL_KEY);
        }
    }
}
