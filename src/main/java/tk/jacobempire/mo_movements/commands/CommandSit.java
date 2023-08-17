package tk.jacobempire.mo_movements.commands;


import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
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
            if (entity instanceof Player) {
                Player player = (Player) entity;
                MinecraftServer server = context.getSource().getServer();
                Level world = player.getCommandSenderWorld();
                Vec3 pos = player.position();
                ArmorStand armorStand = new ArmorStand(world, pos.x, pos.y - 1.7, pos.z);
                armorStand.setInvisible(true);
                armorStand.setNoGravity(true);
                armorStand.setInvulnerable(true);
                armorStand.getPersistentData().putBoolean("Marker", true);
                armorStand.getPersistentData().putBoolean("Small", true);
                armorStand.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.AIR));
                armorStand.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.AIR));
                armorStand.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.AIR));
                armorStand.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.AIR));

                // Add "chair" tag to armor stand
                CompoundTag tags = new CompoundTag();
                tags.putBoolean("chair", true);
                armorStand.getPersistentData().put("Tags", tags);

                world.addFreshEntity(armorStand);
                player.startRiding(armorStand, true);
                player.sendSystemMessage(Component.literal("You have sat down.").withStyle(ChatFormatting.GREEN));

                // Find and kill unoccupied chairs
                List<ArmorStand> chairs = world.getEntitiesOfClass(ArmorStand.class, armorStand.getBoundingBox().inflate(1.0));
                for (ArmorStand chair : chairs) {
                    if (chair.getPersistentData().contains("Tags")) {
                        CompoundTag chairTags = chair.getPersistentData().getCompound("Tags");
                        if (chairTags.getBoolean("chair")) {
                            if (chair.getPassengers().isEmpty()) {
                                chair.discard();
                            }
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
