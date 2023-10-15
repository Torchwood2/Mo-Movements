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

import java.util.List;
import java.util.function.Supplier;

public class SitPacket{
    public SitPacket() {

    }

    public SitPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //WE ON DA SERVER
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

                ArmorStand armorStand = new ArmorStand(EntityType.ARMOR_STAND, level);
                armorStand.setPos(player.getX(), player.getY() -1.75, player.getZ());
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

                // Find and kill unoccupied chairs
                List<ArmorStand> chairs = level.getEntitiesOfClass(ArmorStand.class, armorStand.getBoundingBox().inflate(1.0));
                for (ArmorStand chair : chairs) {
                    if (tag.getBoolean("chair")) {
                        if (chair.getPassengers().isEmpty()) {
                            chair.discard();
                        }
                    }
                }
        });
        return true;
    }
}
