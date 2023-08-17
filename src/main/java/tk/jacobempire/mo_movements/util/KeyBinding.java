package tk.jacobempire.mo_movements.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_MOMOVEMENTS = "key.momovements.movements";
    public static final String KEY_SIT = "key.momovements.sit";

    public static final KeyMapping SIT_KEY = new KeyMapping(KEY_SIT, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, KEY_CATEGORY_MOMOVEMENTS);


}
