//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.impl.features.modules.hud.*;
import me.alpha432.oyvey.impl.features.modules.combat.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.impl.features.modules.player.*;
import me.alpha432.oyvey.impl.features.modules.render.*;
import me.alpha432.oyvey.impl.features.modules.misc.*;
import me.alpha432.oyvey.impl.features.modules.movement.*;
import net.minecraftforge.common.*;
import java.util.function.*;
import me.alpha432.oyvey.impl.events.*;
import java.util.*;
import java.util.stream.*;
import org.lwjgl.input.*;
import me.alpha432.oyvey.impl.gui.*;
import me.alpha432.oyvey.api.util.*;
import java.util.concurrent.*;

public class ModuleManager extends Feature
{
    public ArrayList<Module> modules;
    public List<Module> sortedModules;
    public List<String> sortedModulesABC;
    public Animation animationThread;
    
    public ModuleManager() {
        this.modules = new ArrayList<Module>();
        this.sortedModules = new ArrayList<Module>();
        this.sortedModulesABC = new ArrayList<String>();
    }
    
    public void init() {
        this.modules.add((Module)new SomaGui());
        this.modules.add((Module)new GuiMove());
        this.modules.add((Module)new Font());
        this.modules.add((Module)new ExtraTab());
        this.modules.add((Module)new Metrics());
        this.modules.add((Module)new ActiveModules());
        this.modules.add((Module)new Color());
        this.modules.add((Module)new TotemHUD());
        this.modules.add((Module)new Notifications());
        this.modules.add((Module)new Management());
        this.modules.add((Module)new Welcomer());
        this.modules.add((Module)new IRC());
        this.modules.add((Module)new FastBow());
        this.modules.add((Module)new Clip());
        this.modules.add((Module)new ArmorWarner());
        this.modules.add((Module)new Indicators());
        this.modules.add((Module)new TextRadar());
        this.modules.add((Module)new ArmourHUD());
        this.modules.add((Module)new DurabilityAlert());
        this.modules.add((Module)new Safety());
        this.modules.add((Module)new Surround());
        this.modules.add((Module)new BetterChat());
        this.modules.add((Module)new SkeetIndicators());
        this.modules.add((Module)new AntiUnicode());
        this.modules.add((Module)new HoleESP());
        this.modules.add((Module)new DamageNumbers());
        this.modules.add((Module)new MotionBlur());
        this.modules.add((Module)new ModelModifier());
        this.modules.add((Module)new NoRender());
        this.modules.add((Module)new Swing());
        this.modules.add((Module)new ViewModel());
        this.modules.add((Module)new GlintColorizer());
        this.modules.add((Module)new DeathEffect());
        this.modules.add((Module)new TickShift());
        this.modules.add((Module)new ClickPearl());
        this.modules.add((Module)new Acceleration());
        this.modules.add((Module)new SelfFill());
        this.modules.add((Module)new Reach());
        this.modules.add((Module)new FutureCA());
        this.modules.add((Module)new MultiTask());
        this.modules.add((Module)new FakeLag());
        this.modules.add((Module)new AutoReply());
        this.modules.add((Module)new FakePlayer());
        this.modules.add((Module)new Highlight());
        this.modules.add((Module)new AutoRespawn());
        this.modules.add((Module)new VisualRange());
        this.modules.add((Module)new Sync());
        this.modules.add((Module)new FutureSync());
        this.modules.add((Module)new PearlCooldown());
        this.modules.add((Module)new Trails());
        this.modules.add((Module)new AutoSelect());
        this.modules.add((Module)new HoleFill());
        this.modules.add((Module)new AutoMineReset());
        this.modules.add((Module)new Step());
        this.modules.add((Module)new FastProjectile());
        this.modules.add((Module)new CrystalCircles());
        this.modules.add((Module)new AutoMend());
        this.modules.add((Module)new AutoBait());
        this.modules.add((Module)new Announcer());
        this.modules.add((Module)new Criticals());
        this.modules.add((Module)new Nametags());
        this.modules.add((Module)new ToolTips());
        this.modules.add((Module)new PotionAlerts());
        this.modules.add((Module)new HotbarKeys());
        this.modules.add((Module)new SkyBox());
        this.modules.add((Module)new LogESP());
        this.modules.add((Module)new Watermark());
        this.modules.add((Module)new PearlClip());
        this.modules.add((Module)new Media());
        this.modules.add((Module)new HoleSnap());
        this.modules.add((Module)new LarpPacketFly());
        this.modules.add((Module)new PhaseWalk());
        this.modules.add((Module)new Speed());
        this.modules.add((Module)new LongJump());
        this.modules.add((Module)new AutoCrystal());
        this.modules.add((Module)new Killaura());
        this.modules.add((Module)new Quiver());
        this.modules.add((Module)new OffhandSwap());
        this.modules.add((Module)new Step39());
        this.modules.add((Module)new BurrowESP());
        this.modules.add((Module)new ESP());
        this.modules.add((Module)new HitMarkers());
        this.modules.add((Module)new Crosshair());
        this.modules.add((Module)new HitboxDesync());
        this.modules.add((Module)new Luftwaffe());
        this.modules.add((Module)new NoRotate());
        this.modules.add((Module)new CityESP());
        this.modules.add((Module)new AutoLog());
        this.modules.add((Module)new Anchor());
        (this.animationThread = new Animation()).start();
    }
    
    public Module getModuleByName(final String name) {
        for (final Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public <T extends Module> T getModuleByClass(final Class<T> clazz) {
        for (final Module module : this.modules) {
            if (!clazz.isInstance(module)) {
                continue;
            }
            return (T)module;
        }
        return null;
    }
    
    public void enableModule(final Class<Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final Class<Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }
    
    public void enableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }
    
    public boolean isModuleEnabled(final String name) {
        final Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }
    
    public boolean isModuleEnabled(final Class<Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }
    
    public Module getModuleByDisplayName(final String displayName) {
        for (final Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public ArrayList<Module> getEnabledModules() {
        final ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (final Module module : this.modules) {
            if (!module.isEnabled()) {
                continue;
            }
            enabledModules.add(module);
        }
        return enabledModules;
    }
    
    public ArrayList<String> getEnabledModulesName() {
        final ArrayList<String> enabledModules = new ArrayList<String>();
        for (final Module module : this.modules) {
            if (module.isEnabled()) {
                if (!module.isDrawn()) {
                    continue;
                }
                enabledModules.add(module.getFullArrayString());
            }
        }
        return enabledModules;
    }
    
    public ArrayList<Module> getModulesByCategory(final Module.Category category) {
        final ArrayList<Module> modulesCategory = new ArrayList<Module>();
        final ArrayList<Module> list;
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                list.add(module);
            }
            return;
        });
        return modulesCategory;
    }
    
    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }
    
    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(MinecraftForge.EVENT_BUS::register);
        this.modules.forEach(Module::onLoad);
    }
    
    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }
    
    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }
    
    public void onRender2D(final Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }
    
    public void onRender3D(final Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }
    
    public void sortModules(final boolean reverse) {
        final boolean showInfo = (boolean)ActiveModules.getInstance().showInfo.getValue();
        final String text;
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparingInt(module -> {
            text = (showInfo ? module.getFullArrayString() : module.getDisplayName());
            return this.renderer.getStringWidth(text);
        }).reversed()).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
    }
    
    public void sortModulesABC() {
        final boolean showInfo = (boolean)ActiveModules.getInstance().showInfo.getValue();
        this.sortedModulesABC = new ArrayList<String>();
        for (final Module module : this.modules) {
            if (module.isEnabled()) {
                if (!module.isDrawn()) {
                    continue;
                }
                final String str = showInfo ? module.getFullArrayString() : module.getDisplayName();
                this.sortedModulesABC.add(str);
            }
        }
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }
    
    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }
    
    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }
    
    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }
    
    public void onUnloadPost() {
        for (final Module module : this.modules) {
            module.enabled.setValue((Object)false);
        }
    }
    
    public void onKeyPressed(final int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.field_71462_r instanceof OyVeyGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }
    
    private class Animation extends Thread
    {
        ScheduledExecutorService service;
        
        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }
        
        @Override
        public void run() {
            try {
                if (Util.mc.field_71441_e == null) {
                    return;
                }
                for (final Module module : ModuleManager.this.modules) {
                    final String text = module.getFullArrayString();
                    final float targetWidth = (float)ModuleManager.this.renderer.getStringWidth(text);
                    final float animSpeed = targetWidth / Math.max(1.0f, (float)ActiveModules.getInstance().animationHorizontalTime.getValue());
                    if (module.isEnabled() && module.isDrawn()) {
                        if (module.arrayListOffset > 0.0f) {
                            module.arrayListOffset = Math.max(0.0f, module.arrayListOffset - animSpeed);
                            module.sliding = true;
                        }
                        else {
                            module.arrayListOffset = 0.0f;
                            module.sliding = false;
                        }
                    }
                    else if (module.arrayListOffset < targetWidth) {
                        module.arrayListOffset = Math.min(targetWidth, module.arrayListOffset + animSpeed);
                        module.sliding = true;
                    }
                    else {
                        module.sliding = false;
                    }
                }
            }
            catch (Exception ex) {}
        }
        
        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
    
    public interface TimerService
    {
        float getTimerSpeed();
        
        boolean isEnabled();
    }
}
