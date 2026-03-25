//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.client.gui.*;
import me.alpha432.oyvey.api.util.render.*;

public class ActiveModules extends Module
{
    private static ActiveModules INSTANCE;
    public Setting<Boolean> renderingUp;
    public Setting<RenderingMode> renderingMode;
    public Setting<Boolean> showInfo;
    public Setting<Boolean> sideLine;
    public Setting<Integer> lineWidth;
    public Setting<Integer> animationHorizontalTime;
    public Setting<Integer> animationVerticalTime;
    private int color;
    
    public ActiveModules() {
        super("ActiveModules", "Displays enabled modules on screen", Module.Category.HUD, true, false, false);
        this.renderingUp = (Setting<Boolean>)this.register(new Setting("RenderingUp", (Object)true, "Render from top or bottom"));
        this.renderingMode = (Setting<RenderingMode>)this.register(new Setting("Ordering", (Object)RenderingMode.Length));
        this.showInfo = (Setting<Boolean>)this.register(new Setting("ShowInfo", (Object)true, "Show module info like [target] or [mode]"));
        this.sideLine = (Setting<Boolean>)this.register(new Setting("SideLine", (Object)false, "Draw a line on the right side"));
        this.lineWidth = (Setting<Integer>)this.register(new Setting("LineWidth", (Object)2, (Object)1, (Object)5, v -> (boolean)this.sideLine.getValue()));
        this.animationHorizontalTime = (Setting<Integer>)this.register(new Setting("AnimationHTime", (Object)500, (Object)1, (Object)1000));
        this.animationVerticalTime = (Setting<Integer>)this.register(new Setting("AnimationVTime", (Object)50, (Object)1, (Object)500));
        this.setInstance();
    }
    
    public static ActiveModules getInstance() {
        if (ActiveModules.INSTANCE == null) {
            ActiveModules.INSTANCE = new ActiveModules();
        }
        return ActiveModules.INSTANCE;
    }
    
    private void setInstance() {
        ActiveModules.INSTANCE = this;
    }
    
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        this.color = Color.getInstance().syncColor();
        final int gray = -5592406;
        final int white = -1;
        final int[] counter1 = { 1 };
        int j = (ActiveModules.mc.field_71462_r instanceof GuiChat && !(boolean)this.renderingUp.getValue()) ? 14 : 0;
        int firstY = -1;
        int lastY = -1;
        final int lineX = width;
        if (this.renderingUp.getValue()) {
            if (this.renderingMode.getValue() == RenderingMode.ABC) {
                for (int k = 0; k < OyVey.moduleManager.sortedModulesABC.size(); ++k) {
                    final String fullStr = OyVey.moduleManager.sortedModulesABC.get(k);
                    final Module module = OyVey.moduleManager.getModuleByDisplayName(fullStr.split(" \\[")[0].replace(ChatFormatting.GRAY.toString(), "").replace(ChatFormatting.WHITE.toString(), "").trim());
                    final float xOffset = (module != null) ? module.arrayListOffset : 0.0f;
                    final int yPos = 2 + j * 10;
                    final int currentColor = this.getColor(counter1[0]);
                    if (this.showInfo.getValue()) {
                        final String moduleName = (module != null) ? module.getDisplayName() : fullStr.split(" \\[")[0].replace(ChatFormatting.GRAY.toString(), "").replace(ChatFormatting.WHITE.toString(), "").trim();
                        final String info = (module != null && module.getDisplayInfo() != null) ? module.getDisplayInfo() : null;
                        final String fullDisplay = (info != null) ? (moduleName + " [" + info + "]") : moduleName;
                        final float startX = width - 2 - this.renderer.getStringWidth(fullDisplay) + xOffset;
                        this.renderer.drawStringWithGradient(moduleName, startX, (float)yPos, true, counter1[0]);
                        if (info != null) {
                            float infoX = startX + this.renderer.getStringWidth(moduleName);
                            this.renderer.drawString(" [", infoX, (float)yPos, gray, true);
                            infoX += this.renderer.getStringWidth(" [");
                            this.renderer.drawString(info, infoX, (float)yPos, white, true);
                            infoX += this.renderer.getStringWidth(info);
                            this.renderer.drawString("]", infoX, (float)yPos, gray, true);
                        }
                    }
                    else {
                        final String moduleName = (module != null) ? module.getDisplayName() : fullStr.split(" \\[")[0].replace(ChatFormatting.GRAY.toString(), "").replace(ChatFormatting.WHITE.toString(), "").trim();
                        this.renderer.drawStringWithGradient(moduleName, width - 2 - this.renderer.getStringWidth(moduleName) + xOffset, (float)yPos, true, counter1[0]);
                    }
                    if (this.sideLine.getValue()) {
                        if (firstY == -1) {
                            firstY = yPos;
                        }
                        lastY = yPos + 9;
                        this.drawSideLine(width, yPos, currentColor);
                    }
                    ++j;
                    final int[] array = counter1;
                    final int n = 0;
                    ++array[n];
                }
            }
            else {
                for (int k = 0; k < OyVey.moduleManager.sortedModules.size(); ++k) {
                    final Module module2 = OyVey.moduleManager.sortedModules.get(k);
                    final float xOffset2 = module2.arrayListOffset;
                    final int yPos2 = 2 + j * 10;
                    final int currentColor2 = this.getColor(counter1[0]);
                    if (this.showInfo.getValue()) {
                        final String moduleName2 = module2.getDisplayName();
                        final String info2 = module2.getDisplayInfo();
                        final String fullDisplay2 = (info2 != null) ? (moduleName2 + " [" + info2 + "]") : moduleName2;
                        final float startX2 = width - 2 - this.renderer.getStringWidth(fullDisplay2) + xOffset2;
                        this.renderer.drawStringWithGradient(moduleName2, startX2, (float)yPos2, true, counter1[0]);
                        if (info2 != null) {
                            float infoX2 = startX2 + this.renderer.getStringWidth(moduleName2);
                            this.renderer.drawString(" [", infoX2, (float)yPos2, gray, true);
                            infoX2 += this.renderer.getStringWidth(" [");
                            this.renderer.drawString(info2, infoX2, (float)yPos2, white, true);
                            infoX2 += this.renderer.getStringWidth(info2);
                            this.renderer.drawString("]", infoX2, (float)yPos2, gray, true);
                        }
                    }
                    else {
                        this.renderer.drawStringWithGradient(module2.getDisplayName(), width - 2 - this.renderer.getStringWidth(module2.getDisplayName()) + xOffset2, (float)yPos2, true, counter1[0]);
                    }
                    if (this.sideLine.getValue()) {
                        if (firstY == -1) {
                            firstY = yPos2;
                        }
                        lastY = yPos2 + 9;
                        this.drawSideLine(width, yPos2, currentColor2);
                    }
                    ++j;
                    final int[] array2 = counter1;
                    final int n2 = 0;
                    ++array2[n2];
                }
            }
        }
        else if (this.renderingMode.getValue() == RenderingMode.ABC) {
            for (int k = 0; k < OyVey.moduleManager.sortedModulesABC.size(); ++k) {
                final String fullStr = OyVey.moduleManager.sortedModulesABC.get(k);
                final Module module = OyVey.moduleManager.getModuleByDisplayName(fullStr.split(" \\[")[0].replace(ChatFormatting.GRAY.toString(), "").replace(ChatFormatting.WHITE.toString(), "").trim());
                final float xOffset = (module != null) ? module.arrayListOffset : 0.0f;
                j += 10;
                final int yPos = height - j;
                final int currentColor = this.getColor(counter1[0]);
                if (this.showInfo.getValue()) {
                    final String moduleName = (module != null) ? module.getDisplayName() : fullStr.split(" \\[")[0].replace(ChatFormatting.GRAY.toString(), "").replace(ChatFormatting.WHITE.toString(), "").trim();
                    final String info = (module != null && module.getDisplayInfo() != null) ? module.getDisplayInfo() : null;
                    final String fullDisplay = (info != null) ? (moduleName + " [" + info + "]") : moduleName;
                    final float startX = width - 2 - this.renderer.getStringWidth(fullDisplay) + xOffset;
                    this.renderer.drawStringWithGradient(moduleName, startX, (float)yPos, true, counter1[0]);
                    if (info != null) {
                        float infoX = startX + this.renderer.getStringWidth(moduleName);
                        this.renderer.drawString(" [", infoX, (float)yPos, gray, true);
                        infoX += this.renderer.getStringWidth(" [");
                        this.renderer.drawString(info, infoX, (float)yPos, white, true);
                        infoX += this.renderer.getStringWidth(info);
                        this.renderer.drawString("]", infoX, (float)yPos, gray, true);
                    }
                }
                else {
                    final String moduleName = (module != null) ? module.getDisplayName() : fullStr.split(" \\[")[0].replace(ChatFormatting.GRAY.toString(), "").replace(ChatFormatting.WHITE.toString(), "").trim();
                    this.renderer.drawStringWithGradient(moduleName, width - 2 - this.renderer.getStringWidth(moduleName) + xOffset, (float)yPos, true, counter1[0]);
                }
                if (this.sideLine.getValue()) {
                    if (firstY == -1) {
                        firstY = yPos + 9;
                    }
                    lastY = yPos;
                    this.drawSideLine(width, yPos, currentColor);
                }
                final int[] array3 = counter1;
                final int n3 = 0;
                ++array3[n3];
            }
        }
        else {
            for (int k = 0; k < OyVey.moduleManager.sortedModules.size(); ++k) {
                final Module module2 = OyVey.moduleManager.sortedModules.get(k);
                final float xOffset2 = module2.arrayListOffset;
                j += 10;
                final int yPos2 = height - j;
                final int currentColor2 = this.getColor(counter1[0]);
                if (this.showInfo.getValue()) {
                    final String moduleName2 = module2.getDisplayName();
                    final String info2 = module2.getDisplayInfo();
                    final String fullDisplay2 = (info2 != null) ? (moduleName2 + " [" + info2 + "]") : moduleName2;
                    final float startX2 = width - 2 - this.renderer.getStringWidth(fullDisplay2) + xOffset2;
                    this.renderer.drawStringWithGradient(moduleName2, startX2, (float)yPos2, true, counter1[0]);
                    if (info2 != null) {
                        float infoX2 = startX2 + this.renderer.getStringWidth(moduleName2);
                        this.renderer.drawString(" [", infoX2, (float)yPos2, gray, true);
                        infoX2 += this.renderer.getStringWidth(" [");
                        this.renderer.drawString(info2, infoX2, (float)yPos2, white, true);
                        infoX2 += this.renderer.getStringWidth(info2);
                        this.renderer.drawString("]", infoX2, (float)yPos2, gray, true);
                    }
                }
                else {
                    this.renderer.drawStringWithGradient(module2.getDisplayName(), width - 2 - this.renderer.getStringWidth(module2.getDisplayName()) + xOffset2, (float)yPos2, true, counter1[0]);
                }
                if (this.sideLine.getValue()) {
                    if (firstY == -1) {
                        firstY = yPos2 + 9;
                    }
                    lastY = yPos2;
                    this.drawSideLine(width, yPos2, currentColor2);
                }
                final int[] array4 = counter1;
                final int n4 = 0;
                ++array4[n4];
            }
        }
    }
    
    private void drawSideLine(final int screenWidth, final int yPos, final int color) {
        final int lw = (int)this.lineWidth.getValue();
        Gui.func_73734_a(screenWidth - lw, yPos, screenWidth, yPos + 10, color);
    }
    
    private int getColor(final int counter) {
        if (Color.getInstance().rainbow.getValue()) {
            if (Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) {
                return ColorUtil.rainbow(counter * (int)Color.getInstance().rainbowHue.getValue()).getRGB();
            }
            return ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB();
        }
        else {
            if (Color.getInstance().gradient.getValue()) {
                return Color.getInstance().syncColor(counter);
            }
            return this.color;
        }
    }
    
    static {
        ActiveModules.INSTANCE = new ActiveModules();
    }
    
    public enum RenderingMode
    {
        Length, 
        ABC;
    }
}
