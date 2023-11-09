package tk.jacobempire.mo_movements.networking.packet;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SitPacket {
    public SitPacket() {
        // Default constructor
    }

    public SitPacket(FriendlyByteBuf buf) {
        // Read packet data from buffer
    }

    public void toBytes(FriendlyByteBuf buf) {
        // Write packet data to buffer
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // We are on the server
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = player.getLevel();

                // Logic for when the player sits for the first time
                ArmorStand armorStand = new ArmorStand(EntityType.ARMOR_STAND, level){
                    @Override
                    public void tick() {
                        super.tick();
                        if (!isVehicle())
                            this.discard();
                    }
                };
                armorStand.setPos(player.getX(), player.getY() - 1.75, player.getZ());
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
                tag.putBoolean("chair", true);

                level.addFreshEntity(armorStand);
                player.startRiding(armorStand, true);
                player.sendSystemMessage(Component.literal("You have sat down.").withStyle(ChatFormatting.GREEN));
                player.isPassenger();
            }
        });
        return true;
    }
}