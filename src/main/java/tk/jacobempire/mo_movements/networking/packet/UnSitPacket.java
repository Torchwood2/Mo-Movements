package tk.jacobempire.mo_movements.networking.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class UnSitPacket {
    public UnSitPacket() {
        // Default constructor
    }

    public UnSitPacket(FriendlyByteBuf buf) {
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
                if (player.getVehicle() instanceof ArmorStand armorStand) {
                    CompoundTag tag = armorStand.getPersistentData();
                    if (tag.getBoolean("Chair")) {
                        // Logic for removing unoccupied chairs
                        player.stopRiding();
                        armorStand.discard();
                    }
                }
            }
        });
        return true;
    }
}
