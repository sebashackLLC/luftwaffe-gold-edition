//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.discord;

import com.google.gson.*;

public class RichPresence
{
    private String details;
    private String state;
    private Assets assets;
    private Timestamps timestamps;
    
    public void setDetails(final String details) {
        this.details = details;
    }
    
    public void setState(final String state) {
        this.state = state;
    }
    
    public void setLargeImage(final String key, final String text) {
        if (this.assets == null) {
            this.assets = new Assets();
        }
        this.assets.large_image = key;
        this.assets.large_text = text;
    }
    
    public void setSmallImage(final String key, final String text) {
        if (this.assets == null) {
            this.assets = new Assets();
        }
        this.assets.small_image = key;
        this.assets.small_text = text;
    }
    
    public void setStart(final long time) {
        if (this.timestamps == null) {
            this.timestamps = new Timestamps();
        }
        this.timestamps.start = time;
    }
    
    public void setEnd(final long time) {
        if (this.timestamps == null) {
            this.timestamps = new Timestamps();
        }
        this.timestamps.end = time;
    }
    
    public JsonObject toJson() {
        final JsonObject o = new JsonObject();
        if (this.details != null) {
            o.addProperty("details", this.details);
        }
        if (this.state != null) {
            o.addProperty("state", this.state);
        }
        if (this.assets != null) {
            final JsonObject a = new JsonObject();
            if (this.assets.large_image != null) {
                a.addProperty("large_image", this.assets.large_image);
            }
            if (this.assets.large_text != null) {
                a.addProperty("large_text", this.assets.large_text);
            }
            if (this.assets.small_image != null) {
                a.addProperty("small_image", this.assets.small_image);
            }
            if (this.assets.small_text != null) {
                a.addProperty("small_text", this.assets.small_text);
            }
            o.add("assets", (JsonElement)a);
        }
        if (this.timestamps != null) {
            final JsonObject t = new JsonObject();
            if (this.timestamps.start != null) {
                t.addProperty("start", (Number)this.timestamps.start);
            }
            if (this.timestamps.end != null) {
                t.addProperty("end", (Number)this.timestamps.end);
            }
            o.add("timestamps", (JsonElement)t);
        }
        return o;
    }
    
    public static class Assets
    {
        public String large_image;
        public String large_text;
        public String small_image;
        public String small_text;
    }
    
    public static class Timestamps
    {
        public Long start;
        public Long end;
    }
}
