//package tk.jacobempire.mo_movements.event;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.decoration.ArmorStand;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.InputEvent;
//import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import tk.jacobempire.mo_movements.MoMovements;
//import tk.jacobempire.mo_movements.util.KeyBinding;
//
//public class ClientEvents {
//    private static boolean sitting = false;
//    @Mod.EventBusSubscriber(modid = MoMovements.MODID, value = Dist.CLIENT)
//    public static class ClientForgeEvents {
//
//        @SubscribeEvent
//        public static void onKeyInput(InputEvent.Key event){
//            if(KeyBinding.SIT_KEY.isDown()) {
//                if (!sitting){
////                Minecraft.getInstance().player.sendSystemMessage(Component.literal("&4This will make the player sit soon, the keybind works, gotta do server stuff"));
//                Level world = Minecraft.getInstance().level;
//                Vec3 pos = Minecraft.getInstance().player.position();
//                ArmorStand armorStand = new ArmorStand(world, pos.x, pos.y -1.7, pos.z);
//                armorStand.setInvisible(true);
//                armorStand.setNoGravity(true);
//                armorStand.setInvulnerable(true);
//                armorStand.getPersistentData().putBoolean("Marker", true);
//                armorStand.getPersistentData().putBoolean("Small", true);
//                armorStand.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.AIR));
//                armorStand.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.AIR));
//                armorStand.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.AIR));
//                armorStand.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.AIR));
//                ;
//
//                // Add "chair" tag to armor stand
//                CompoundTag tags = new CompoundTag();
//                tags.putBoolean("chair", true);
//                armorStand.getPersistentData().put("Tags", tags);
//
//                world.addFreshEntity(armorStand);
//                Minecraft.getInstance().player.startRiding(armorStand, true);
//                sitting = true;
//            }else{
//                Minecraft.getInstance().player.stopRiding();
//                sitting = false;
//                }
//            }
//        }
//    }
//
//    @Mod.EventBusSubscriber(modid = MoMovements.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class ClientModBusEvents{
//        @SubscribeEvent
//        public static void onKeyRegister(RegisterKeyMappingsEvent event){
//            event.register(KeyBinding.SIT_KEY);
//        }
//    }
//}

package tk.jacobempire.mo_movements.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.jacobempire.mo_movements.MoMovements;
import tk.jacobempire.mo_movements.util.KeyBinding;

import static net.minecraft.commands.Commands.literal;

@Mod.EventBusSubscriber(modid = MoMovements.MODID, value = Dist.CLIENT)
public class ClientEvents {
    private static boolean sitting = false;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyBinding.SIT_KEY.isDown()) {
            Minecraft mc = Minecraft.getInstance();
            if (!sitting) {
                mc.player.sendSystemMessage(Component.literal("You have sat down."));
                sitPlayer();
            } else {
                mc.player.sendSystemMessage(Component.literal("You have stood up."));
                standPlayer();
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralArgumentBuilder<CommandSourceStack> sitCommand = literal("sit")
                .executes(context -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (!sitting) {
                        mc.player.sendSystemMessage(Component.literal("You have sat down."));
                        sitPlayer();
                    } else {
                        mc.player.sendSystemMessage(Component.literal("You have stood up."));
                        standPlayer();
                    }
                    return 1;
                });

        dispatcher.register(sitCommand);
    }

    private static void sitPlayer() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level world = mc.level;

        double yOffset = -0.35; // Adjust as needed to match the sitting position
        double armorStandYOffset = -0.5; // Adjust this value to match the height of the armor stand

        Vec3 playerPos = player.position();
        ArmorStand armorStand = new ArmorStand(world, playerPos.x, playerPos.y + yOffset + armorStandYOffset, playerPos.z);

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
        sitting = true;
    }




    private static void standPlayer() {
        Minecraft mc = Minecraft.getInstance();
        mc.player.stopRiding();
        sitting = false;
    }
}
