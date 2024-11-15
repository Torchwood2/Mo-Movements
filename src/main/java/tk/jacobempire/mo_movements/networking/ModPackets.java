package tk.jacobempire.mo_movements.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import tk.jacobempire.mo_movements.MoMovements;
import tk.jacobempire.mo_movements.networking.packet.CrawlPacket;
import tk.jacobempire.mo_movements.networking.packet.LayPacket;
import tk.jacobempire.mo_movements.networking.packet.SitPacket;
import tk.jacobempire.mo_movements.networking.packet.UnSitPacket;

public class ModPackets {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(MoMovements.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();


        INSTANCE = net;

        net.messageBuilder(SitPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SitPacket::new)
                .encoder(SitPacket::toBytes)
                .consumerMainThread(SitPacket::handle)
                .add();

        net.messageBuilder(UnSitPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UnSitPacket::new)
                .encoder(UnSitPacket::toBytes)
                .consumerMainThread(UnSitPacket::handle)
                .add();

        net.messageBuilder(CrawlPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CrawlPacket::new)
                .encoder(CrawlPacket::toBytes)
                .consumerMainThread(CrawlPacket::handle)
                .add();

        net.messageBuilder(LayPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(LayPacket::new)
                .encoder(LayPacket::toBytes)
                .consumerMainThread(LayPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
