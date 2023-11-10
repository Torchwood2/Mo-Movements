package tk.jacobempire.mo_movements.networking.packet;


import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StandPacket {

        public StandPacket() {

        }

        public StandPacket(FriendlyByteBuf buf) {

        }

        public void toBytes(FriendlyByteBuf buf) {

        }

        public boolean handle(Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> {
                //WE ON DA SERVER
                ServerPlayer player = context.getSender();
                if (player != null) {
                        if (player.getForcedPose() != null) {
                            player.sendSystemMessage(Component.literal("You are now standing.").withStyle(ChatFormatting.GREEN));
                            player.setForcedPose(null);
                            player.refreshDimensions();
                        }
                    }
            });
        return true;
        }
}