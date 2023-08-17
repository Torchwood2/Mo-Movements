package tk.jacobempire.mo_movements.event;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.jacobempire.mo_movements.MoMovements;
import tk.jacobempire.mo_movements.util.KeyBinding;

public class ClientEvents {
    private static boolean sitting = false;
    @Mod.EventBusSubscriber(modid = MoMovements.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event){
            if(KeyBinding.SIT_KEY.isDown()) {
                if (!sitting){
//                Minecraft.getInstance().player.sendSystemMessage(Component.literal("&4This will make the player sit soon, the keybind works, gotta do server stuff"));
                Level world = Minecraft.getInstance().level;
                Vec3 pos = Minecraft.getInstance().player.position();
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
                ;

                // Add "chair" tag to armor stand
                CompoundTag tags = new CompoundTag();
                tags.putBoolean("chair", true);
                armorStand.getPersistentData().put("Tags", tags);

                world.addFreshEntity(armorStand);
                Minecraft.getInstance().player.startRiding(armorStand, true);
                sitting = true;
            }else{
                Minecraft.getInstance().player.stopRiding();
                sitting = false;
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MoMovements.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents{
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBinding.SIT_KEY);
        }
    }
}
