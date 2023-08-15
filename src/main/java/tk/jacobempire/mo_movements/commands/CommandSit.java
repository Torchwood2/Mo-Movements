package tk.jacobempire.mo_movements.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.math.Vector3d;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class CommandSit {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("sit").executes(context -> {
            Entity entity = context.getSource().getEntity();
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                MinecraftServer server = context.getSource().getServer();
                World world = player.getEntity().getCommandSenderWorld();
                Vector3d pos = player.position();
                ArmorStandEntity armorStand = new ArmorStandEntity(world, pos.x, pos.y - 1.7, pos.z);
                armorStand.setInvisible(true);
                armorStand.setNoGravity(true);
                armorStand.setInvulnerable(true);
                armorStand.getPersistentData().putBoolean("Marker", true);
                armorStand.getPersistentData().putBoolean("Small", true);
                armorStand.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.AIR));
                armorStand.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.AIR));
                armorStand.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.AIR));
                armorStand.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.AIR));

                // Add "chair" tag to armor stand
                CompoundNBT tags = new CompoundNBT();
                tags.putBoolean("chair", true);
                armorStand.getPersistentData().put("Tags", tags);

                world.addFreshEntity(armorStand);
                player.startRiding(armorStand, true);
                player.sendMessage(new StringTextComponent("You have sat down.").withStyle(TextFormatting.GREEN), player.getUUID());

                // Find and kill unoccupied chairs
                List<ArmorStandEntity> chairs = world.getEntitiesOfClass(ArmorStandEntity.class, armorStand.getBoundingBox().inflate(1.0));
                for (ArmorStandEntity chair : chairs) {
                    if (chair.getPersistentData().contains("Tags")) {
                        CompoundNBT chairTags = chair.getPersistentData().getCompound("Tags");
                        if (chairTags.getBoolean("chair")) {
                            if (chair.getPassengers().isEmpty()) {
                                chair.remove();
                            }
                        }
                    }
                }

                return 1;
            } else {
                context.getSource().sendFailure(new StringTextComponent("Only players can use this command!"));
                return 0;
            }
        }));
    }
}
