package tk.jacobempire.mo_movements.networking.packet;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LayPacket {

    public LayPacket() {

    }

    public LayPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //WE ON DA SERVER
            ServerPlayer player = context.getSender();
            if (!player.getForcedPose().equals(Pose.SLEEPING)) {
                player.setForcedPose(null);
                player.sendSystemMessage(Component.literal("You are now laying down.").withStyle(ChatFormatting.GREEN));
                player.setForcedPose(Pose.SLEEPING);
                player.refreshDimensions();
            }
        });
        return true;
    }
}