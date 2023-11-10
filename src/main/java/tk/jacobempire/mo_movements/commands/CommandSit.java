package tk.jacobempire.mo_movements.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.math.Vector3d;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;


public class CommandSit {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sit").executes(context -> {
            Entity entity = context.getSource().getEntity();
            if (entity instanceof ServerPlayer player) {
                ServerLevel level = context.getSource().getLevel();
                ArmorStand armorStand = new ArmorStand(EntityType.ARMOR_STAND, level);
                armorStand.setPos(player.getX(), player.getY() - 1, player.getZ());
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
                tag.putBoolean("Chair", true);

                level.addFreshEntity(armorStand);
                player.startRiding(armorStand, true);
                player.sendSystemMessage(Component.literal("You have sat down.").withStyle(ChatFormatting.GREEN));

                // Find and kill unoccupied chairs
                List<ArmorStand> chairs = level.getEntitiesOfClass(ArmorStand.class, armorStand.getBoundingBox().inflate(1.0));
                for (ArmorStand chair : chairs) {
                        if (tag.getBoolean("Chair")) {
                            if (chair.getPassengers().isEmpty()) {
                                chair.discard();
                            }
                        }
                }

                return 1;
            } else {
                context.getSource().sendFailure(Component.literal("Only players can use this command!"));
                return 0;
            }
        }));
    }
}
