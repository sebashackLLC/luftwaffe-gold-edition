//Decompiled by Procyon!

package me.alpha432.oyvey.api.features.settings;

import org.lwjgl.input.*;
import com.google.common.base.*;
import com.google.gson.*;

public class Bind
{
    private int key;
    boolean isMouse;
    public static final int MOUSE_4 = 3;
    public static final int MOUSE_5 = 4;
    
    public Bind(final int key) {
        this.isMouse = false;
        this.key = key;
        this.isMouse = isMouseButton(key);
    }
    
    public static boolean isMouseButton(final int key) {
        return key == 3 || key == 4 || (key >= 0 && key <= 2);
    }
    
    public static Bind none() {
        return new Bind(-1);
    }
    
    public void setIsMouse(final boolean mouse) {
        this.isMouse = mouse;
    }
    
    public boolean getIsMouse() {
        return this.isMouse;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
        this.isMouse = isMouseButton(key);
    }
    
    public boolean isEmpty() {
        return this.key < 0;
    }
    
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "None";
        }
        if (this.key == 3) {
            return "Mouse4";
        }
        if (this.key == 4) {
            return "Mouse5";
        }
        if (!this.isMouse) {
            return this.capitalise(Keyboard.getKeyName(this.key));
        }
        switch (this.key) {
            case 0: {
                return "Left";
            }
            case 1: {
                return "Right";
            }
            case 2: {
                return "Middle";
            }
            default: {
                return "Mouse" + this.key;
            }
        }
    }
    
    public boolean isDown() {
        if (this.isEmpty()) {
            return false;
        }
        if (this.key == 3) {
            return Mouse.isButtonDown(3);
        }
        if (this.key == 4) {
            return Mouse.isButtonDown(4);
        }
        return this.isMouse ? Mouse.isButtonDown(this.key) : Keyboard.isKeyDown(this.getKey());
    }
    
    private String capitalise(final String str) {
        if (str.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(str.charAt(0)) + ((str.length() != 1) ? str.substring(1).toLowerCase() : "");
    }
    
    public static class BindConverter extends Converter<Bind, JsonElement>
    {
        public JsonElement doForward(final Bind bind) {
            return (JsonElement)new JsonPrimitive(bind.toString());
        }
        
        public Bind doBackward(final JsonElement jsonElement) {
            final String s = jsonElement.getAsString();
            if (s.equalsIgnoreCase("None")) {
                return Bind.none();
            }
            if (s.equalsIgnoreCase("Mouse4")) {
                final Bind bind = new Bind(3);
                bind.setIsMouse(true);
                return bind;
            }
            if (s.equalsIgnoreCase("Mouse5")) {
                final Bind bind = new Bind(4);
                bind.setIsMouse(true);
                return bind;
            }
            if (s.toLowerCase().startsWith("mouse")) {
                try {
                    if (s.equalsIgnoreCase("MouseLeft")) {
                        return new Bind(0);
                    }
                    if (s.equalsIgnoreCase("MouseRight")) {
                        return new Bind(1);
                    }
                    if (s.equalsIgnoreCase("MouseMiddle")) {
                        return new Bind(2);
                    }
                    final int mouseButton = Integer.parseInt(s.substring(5));
                    final Bind bind2 = new Bind(mouseButton);
                    bind2.setIsMouse(true);
                    return bind2;
                }
                catch (NumberFormatException ex) {}
            }
            int key = -1;
            try {
                key = Keyboard.getKeyIndex(s.toUpperCase());
            }
            catch (Exception ex2) {}
            if (key == 0) {
                return Bind.none();
            }
            return new Bind(key);
        }
    }
}
