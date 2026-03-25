//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import java.util.*;
import net.minecraft.client.network.*;
import me.alpha432.oyvey.impl.features.modules.misc.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.alpha432.oyvey.manager.*;
import net.minecraft.scoreboard.*;
import me.alpha432.oyvey.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ GuiPlayerTabOverlay.class })
public class MixinGuiPlayerTabOverlay extends Gui
{
    @Redirect(method = { "renderPlayerlist" }, at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;"))
    public List<NetworkPlayerInfo> subListHook(final List<NetworkPlayerInfo> list, final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, ExtraTab.getINSTANCE().isEnabled() ? Math.min((int)ExtraTab.getINSTANCE().size.getValue(), list.size()) : toIndex);
    }
    
    @Inject(method = { "getPlayerName" }, at = { @At("HEAD") }, cancellable = true)
    public void getPlayerNameHook(final NetworkPlayerInfo networkPlayerInfoIn, final CallbackInfoReturnable<String> info) {
        final String ign = networkPlayerInfoIn.func_178845_a().getName();
        final String ircName = IrcNameManager.getIrcNameForPlayer(ign);
        if (ircName != null) {
            String displayName;
            if (networkPlayerInfoIn.func_178854_k() != null) {
                displayName = networkPlayerInfoIn.func_178854_k().func_150254_d().replace(ign, ircName);
            }
            else {
                displayName = ScorePlayerTeam.func_96667_a((Team)networkPlayerInfoIn.func_178850_i(), ircName);
            }
            if (OyVey.friendManager.isFriend(ign)) {
                info.setReturnValue((Object)("§b" + displayName));
            }
            else {
                info.setReturnValue((Object)displayName);
            }
            return;
        }
        if (ExtraTab.getINSTANCE().isEnabled()) {
            info.setReturnValue((Object)ExtraTab.getPlayerName(networkPlayerInfoIn));
        }
    }
}
