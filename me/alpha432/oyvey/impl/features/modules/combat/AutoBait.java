//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.server.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.api.util.math.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AutoBait extends Module
{
    public static AutoBait INSTANCE;
    public Setting<String> targetPlayer;
    
    public AutoBait() {
        super("AutoBait", "Automatically responds to targeted player", Module.Category.COMBAT, true, false, false);
        this.targetPlayer = (Setting<String>)this.register(new Setting("Target", (Object)"Battnet", "bait niggas"));
        AutoBait.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = (SPacketChat)event.getPacket();
            final String rawMessage = packet.func_148915_c().func_150260_c();
            final String Msg = ChatFormatting.stripFormatting(rawMessage).toLowerCase();
            if (Msg.contains("<" + ((String)this.targetPlayer.getValue()).toLowerCase() + ">")) {
                Command.sendSilentMessage("Baiting target player...");
                if (Msg.contains("i'm not") || Msg.contains("im not")) {
                    this.sendMsg("no?", "yes?", "nah LOLOLOLOL", "yes", "no", "COPE!!!!", "you are");
                }
                else if (Msg.contains(" nn")) {
                    this.sendMsg("who are you again?", "yep nn", "LELEL KEEP TALKING NN DOG", "ur a nn urself", "noone knows you bro");
                }
                else if (Msg.contains(" iq?") || Msg.contains("what is your iq") || Msg.contains("what is ur iq")) {
                    AutoBait.mc.field_71439_g.func_71165_d(String.valueOf(MathUtil.random(200.0, 250.0)));
                }
                else if (Msg.contains("your iq") || Msg.contains("ur iq")) {
                    AutoBait.mc.field_71439_g.func_71165_d("you're*");
                }
                else if (Msg.contains(AutoBait.mc.field_71439_g.func_70005_c_())) {
                    this.sendMsg("who me?", "crystaldoickum?");
                }
                else if (Msg.contains("you are") || Msg.contains("youre") || Msg.contains("you're")) {
                    this.sendMsg("who me?", "opium? or me");
                }
                else if (Msg.contains("you ")) {
                    this.sendMsg("who me?", "doickopium?");
                }
                else if (Msg.contains("main?") || Msg.contains("main")) {
                    this.sendMsg("2 NEW 2 KNOW MY MAIN", AutoBait.mc.field_71439_g.func_70005_c_(), "my main is doickopium", "LOOOOOOOOL im more known than jqq");
                }
                else if (Msg.contains("im the")) {
                    AutoBait.mc.field_71439_g.func_71165_d("no?");
                }
                else if (Msg.contains("unfunny") || Msg.contains("who laughed")) {
                    AutoBait.mc.field_71439_g.func_71165_d("i laughed");
                }
                else if (Msg.contains("opium")) {
                    AutoBait.mc.field_71439_g.func_71165_d("opium is so good we love opium");
                }
                else if (Msg.contains("i win")) {
                    this.sendMsg("no lol", "my palms are on my forhead", "I got second hand embarrassment from that", "nice cope haha", "im white, i take care of my self, i go to work & im NOT fat, whos the real winner?", "cope nn", "i win YOU LOSE.", "ai generated", "when? LOL");
                }
                else {
                    this.sendMsg("yep", "LELEL KEEP TALKING NN DOG", "erm, no?", "noone knows you bro ur unknown as shit LOL!", "ARENT YOU LIKE BINNED ASF?", "totemfull dog talking LELEL", "crystalopium doesnt know you tho LOL!!!", "XDXD ur bad bro wtff", "LOL LOL SAY THAT WHEN YOU D!E IN A SW4T", "DOICKSWAG IS SWATTING YOU IN THE VC RN XD", "ok benjamin", "uhh ok?", "STFU NN", "who are you again?", "LELELE soma doesnt know this guy btw", "Didnt you get ratted by us? LOLLLLLLL", "lets go rat 4 rat", "bark rn like a dog", "LOOOL IQ?", "YEAH UR GETTING SW4TT3D TN", "watch out for the sw4t", "LOOOL HARMLESS DOG PIPING UP 2 THE KING OF CRYSTAL PVP", "the thing is that you are still unknown asf", "XDXDXD Who are you again?", "bro is not him....", "NOONE KNOWS THIS NN XD", "C:\\Users\\chern\\Future\\spammer\\spammer.txt", "!battnettt " + this.targetPlayer, "LEL IM THE #1 CRYSTALPVPER XD", "LETS B4B IN PK VC LOL", "THATFROGKERMIT DUNNO YOU LOL", "EXODUS2B2T TOLD ME YOU ARE PEDO", "HOLDEN TOLD ME YOU ARE TRIED TO GET SN0W AT 3 AM LOL", "KLEMEN TOLD ME YOU ARE IN BATTNET LOL", "FUZSE TOLD ME YOU ARE GOT RATTED BY ELEMENTARS LOL", "3958 TOLD ME YOU ARE GOT RATTED BY POLLOSHOOK TEXTUTIL LOL", "PKHAZEL TOLD ME YOU ARE ILLUMINATI LOL", "CHACHOOX TOLD ME YOU ARE ASKED FOR POYOS LOL", "ALONZO TOLD ME YOU ARE GETTING DDOS IN NEXT FIGHT", "SPIKE TOLD ME YOUR MC ACCOUNT IS STOLEN", "JUMPY/XDOLF TOLD ME YOU ARE WORKING AT MCDONALDS LOL", "FUZSE TOLD ME YOU ARE GROOMING KIDS IN VRCHAT", "USEDTOOWN TOLD ME UR GROOMING KINDS IN ROBLOX", "YAUK TOLD ME YOUR PENIS IS UGLY LOL", "DALLAS TOLD ME YOUR LEGS IS BROKE", "CLAIR TOLD ME UR BEGGING HIM 4 MONEY LOL", "ASPHYXIA1337 TOLD ME HES BANNING YOU FROM MIO", "PRASHU TOLD ME YOU ARE GOT RATTED BY LITHIUM", "PANTHERASBF TOLD ME YOU ARE RATTED TO THE BONE", "BOMELOM TOLD ME HES LEAKING UR SHIT RN", "COLT TOLD ME YOU ARE SELLING DATA LOL", "THR0WING GETTING _CPV IN VC LOL", "MAJINRUSSIA TOLD ME YOU ARE UNKOWN WITH SN0W IRC", "HOODJUSTICE TOLD ME YOU ARE SHITTER WITH SN0W IRC", "LOL UR BITCOINLESS NN THATS PIPING UP 2 THE KING (ME)", "LOOOOL THIS NIGGA IS IN BATTNET", "LOL I JS BOUGHT BATTNET UR WIFI FINNA BE FRIED", "XDDDDDDDDD UR NOT FROM OBLOCK VON COMING BACK FROM THE DEAD JUST TO SWAT U", "LEL U FROM 63RD SOSA DDOSING U RIGHT AS WE SPEAK");
                }
            }
        }
    }
    
    private void sendMsg(final String... responses) {
        if (responses.length == 0) {
            return;
        }
        final String response = responses[(int)MathUtil.random(0.0, (double)(responses.length - 1))];
        AutoBait.mc.field_71439_g.func_71165_d(response);
    }
    
    public String getDisplayInfo() {
        return (String)this.targetPlayer.getValue();
    }
}
