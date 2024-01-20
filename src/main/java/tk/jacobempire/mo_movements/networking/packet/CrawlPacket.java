package tk.jacobempire.mo_movements.networking.packet;


import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static tk.jacobempire.mo_movements.event.ClientEvents.crawling;

public class CrawlPacket {

        public CrawlPacket() {

        }

        public CrawlPacket(FriendlyByteBuf buf) {

        }

        public void toBytes(FriendlyByteBuf buf) {

        }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // WE ON DA SERVER
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            // Check if the player is already riding an entity
            if (player.isPassenger()) {
                player.sendSystemMessage(Component.literal("You cannot crawl while riding an entity.").withStyle(ChatFormatting.RED));
                return;
            }

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
        });
        return true;
    }
}