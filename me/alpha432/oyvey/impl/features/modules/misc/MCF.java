//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import org.lwjgl.input.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.impl.command.*;
import net.minecraft.entity.*;

public class MCF extends Module
{
    private boolean clicked;
    
    public MCF() {
        super("MCF", "Middleclick Friends.", Module.Category.MISC, true, false, false);
        this.clicked = false;
    }
    
    public void onUpdate() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked && MCF.mc.field_71462_r == null) {
                this.onClick();
            }
            this.clicked = true;
        }
        else {
            this.clicked = false;
        }
    }
    
    private void onClick() {
        final RayTraceResult result = MCF.mc.field_71476_x;
        final Entity entity;
        if (result != null && result.field_72313_a == RayTraceResult.Type.ENTITY && (entity = result.field_72308_g) instanceof EntityPlayer) {
            if (OyVey.friendManager.isFriend(entity.func_70005_c_())) {
                OyVey.friendManager.removeFriend(entity.func_70005_c_());
                Command.sendMessage(ChatFormatting.RED + entity.func_70005_c_() + ChatFormatting.RED + " has been unfriended.");
            }
            else {
                OyVey.friendManager.addFriend(entity.func_70005_c_());
                Command.sendMessage(ChatFormatting.AQUA + entity.func_70005_c_() + ChatFormatting.AQUA + " has been friended.");
            }
        }
        this.clicked = true;
    }
}
