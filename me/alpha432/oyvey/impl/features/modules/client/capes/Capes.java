//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client.capes;

public enum Capes
{
    NONE(""), 
    CRYSTAL("assets/capes/crystal.png"), 
    DOICK("assets/capes/doick.png"), 
    GRETTEN("assets/capes/gretten.png"), 
    SOMA("assets/capes/soma.png"), 
    FUZSE("assets/capes/fuzse.png");
    
    private final String source;
    
    private Capes(final String source) {
        this.source = source;
    }
    
    public String getSource() {
        return this.source;
    }
}
