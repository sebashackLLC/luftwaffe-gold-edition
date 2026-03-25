//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.api.util.math.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.impl.features.modules.hud.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraft.world.*;
import java.util.function.*;
import com.google.common.base.*;
import net.minecraft.network.play.server.*;
import java.util.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraftforge.client.event.*;
import me.alpha432.oyvey.impl.command.*;
import com.mojang.realmsclient.gui.*;

public class EventManager extends Feature
{
    private final Timer logoutTimer;
    
    public EventManager() {
        this.logoutTimer = new Timer();
    }
    
    public void init() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void onUnload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (!fullNullCheck() && event.getEntity().func_130014_f_().field_72995_K && event.getEntityLiving().equals((Object)EventManager.mc.field_71439_g)) {
            OyVey.inventoryManager.update();
            OyVey.moduleManager.onUpdate();
            if (ActiveModules.getInstance().renderingMode.getValue() == ActiveModules.RenderingMode.Length) {
                OyVey.moduleManager.sortModules(true);
            }
            else {
                OyVey.moduleManager.sortModulesABC();
            }
        }
    }
    
    @SubscribeEvent
    public void onClientConnect(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.logoutTimer.reset();
        OyVey.moduleManager.onLogin();
    }
    
    @SubscribeEvent
    public void onClientDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        OyVey.moduleManager.onLogout();
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (fullNullCheck()) {
            return;
        }
        OyVey.moduleManager.onTick();
        for (final EntityPlayer player : EventManager.mc.field_71441_e.field_73010_i) {
            if (player != null) {
                if (player.func_110143_aJ() > 0.0f) {
                    continue;
                }
                MinecraftForge.EVENT_BUS.post((Event)new DeathEvent(player));
                Management.getInstance().onDeath(player);
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            OyVey.speedManager.updateValues();
            OyVey.rotationManager.updateRotations();
            OyVey.positionManager.updatePosition();
        }
        if (event.getStage() == 1) {
            OyVey.rotationManager.restoreRotations();
            OyVey.positionManager.restorePosition();
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() != 0) {
            return;
        }
        OyVey.serverManager.onPacketReceived();
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.func_149160_c() == 35 && packet.func_149161_a((World)EventManager.mc.field_71441_e) instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)packet.func_149161_a((World)EventManager.mc.field_71441_e);
                MinecraftForge.EVENT_BUS.post((Event)new TotemPopEvent(player));
                Management.getInstance().onTotemPop(player);
            }
        }
        if (event.getPacket() instanceof SPacketPlayerListItem && !fullNullCheck() && this.logoutTimer.passedS(1.0)) {
            final SPacketPlayerListItem packet2 = (SPacketPlayerListItem)event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals((Object)packet2.func_179768_b()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals((Object)packet2.func_179768_b())) {
                return;
            }
            final UUID id;
            final SPacketPlayerListItem sPacketPlayerListItem;
            final String name;
            final EntityPlayer entity;
            String logoutName;
            packet2.func_179767_a().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty(data.func_179962_a().getName()) || data.func_179962_a().getId() != null).forEach(data -> {
                id = data.func_179962_a().getId();
                switch (sPacketPlayerListItem.func_179768_b()) {
                    case ADD_PLAYER: {
                        name = data.func_179962_a().getName();
                        MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(0, id, name));
                        break;
                    }
                    case REMOVE_PLAYER: {
                        entity = EventManager.mc.field_71441_e.func_152378_a(id);
                        if (entity != null) {
                            logoutName = entity.func_70005_c_();
                            MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(1, entity, id, logoutName));
                            break;
                        }
                        else {
                            MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(2, id, (String)null));
                            break;
                        }
                        break;
                    }
                }
                return;
            });
        }
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            OyVey.serverManager.update();
        }
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        EventManager.mc.field_71424_I.func_76320_a("oyvey");
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179103_j(7425);
        GlStateManager.func_179097_i();
        GlStateManager.func_187441_d(1.0f);
        final Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        OyVey.moduleManager.onRender3D(render3dEvent);
        GlStateManager.func_187441_d(1.0f);
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
        GlStateManager.func_179089_o();
        GlStateManager.func_179089_o();
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179147_l();
        GlStateManager.func_179126_j();
        EventManager.mc.field_71424_I.func_76319_b();
    }
    
    @SubscribeEvent
    public void renderHUD(final RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            OyVey.textManager.updateResolution();
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderGameOverlayEvent(final RenderGameOverlayEvent.Text event) {
        if (event.getType().equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            final ScaledResolution resolution = new ScaledResolution(EventManager.mc);
            final Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            OyVey.moduleManager.onRender2D(render2DEvent);
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            OyVey.moduleManager.onKeyPressed(Keyboard.getEventKey());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onMouseInput(final InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButton() == -1 || !Mouse.getEventButtonState()) {
            return;
        }
        final int button = Mouse.getEventButton();
        for (final Module module : OyVey.moduleManager.modules) {
            final Bind bind = module.getBind();
            if (bind.getIsMouse()) {
                if (bind.isEmpty()) {
                    continue;
                }
                if (bind.getKey() == button) {
                    module.toggle();
                    break;
                }
                continue;
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(final ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                EventManager.mc.field_71456_v.func_146158_b().func_146239_a(event.getMessage());
                if (event.getMessage().length() > 1) {
                    OyVey.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                }
                else {
                    Command.sendMessage("Please enter a command.");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage(ChatFormatting.RED + "An error occurred while running this command. Check the log!");
            }
        }
    }
}
