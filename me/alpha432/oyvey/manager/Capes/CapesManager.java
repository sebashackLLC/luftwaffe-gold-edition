//Decompiled by Procyon!

package me.alpha432.oyvey.manager.Capes;

import net.minecraft.util.*;
import java.util.*;

public class CapesManager
{
    private final List<CapeUser> capedUsers;
    private final CapeUser CRYSTAL;
    private final CapeUser DOICK;
    private final CapeUser SOMA;
    private final CapeUser GRETTEN;
    private final CapeUser FUZSE;
    
    public CapesManager() {
        this.capedUsers = new ArrayList<CapeUser>();
        this.CRYSTAL = new CapeUser(new ResourceLocation("keqing/textures/capes/spctrzzzcape.png"), new String[] { "6cade50c-f2d0-4e4c-8435-c20d62aad797" });
        this.DOICK = new CapeUser(new ResourceLocation("keqing/textures/capes/tudoucape.png"), new String[] { "7237d3c0-1a0c-4322-bb10-1730ea893156", "de19465e-546e-4e76-a413-aa7141f08834", "eb96a9ca-adba-4fff-b66d-37217faa7f51", "885e003a-3b4b-4d86-8c41-eb35407c44ba" });
        this.SOMA = new CapeUser(new ResourceLocation("keqing/textures/capes/viewmodelcape.png"), new String[] { "4675c91c-fbfa-4db3-a8ec-cbf36bc19110", "d79bd5f9-15a4-4c0c-bdc1-9f6ca917829e", "7d292a53-428e-4ac2-9e01-5dbf1374a4e1" });
        this.GRETTEN = new CapeUser(new ResourceLocation("keqing/textures/capes/goghopcape.png"), new String[] { "3ff6aa84-96a6-44a5-97d2-933584d4d1d3", "7b45a5b1-9e10-4d13-9c28-2d91f66e99d9" });
        this.FUZSE = new CapeUser(new ResourceLocation("keqing/textures/capes/cpvcape.png"), new String[] { "75aa6787-f7de-456b-9074-8dce5a611100", "b8e7bc40-0982-477b-8368-6b619963eb04", "eb811b68-28e3-432b-ba77-54acb980b103", "c0a28c2d-6fbb-4213-93a5-6cec6f9b8a78", "2c83d964-298e-4559-b1bf-314f9ad63f7b" });
    }
    
    public void init() {
        final List<CapeUser> slimes = Arrays.asList(this.CRYSTAL, this.DOICK, this.SOMA, this.GRETTEN, this.FUZSE);
        this.capedUsers.addAll(slimes);
    }
}
