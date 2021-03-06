package nz.uberdungeon.puzzles;

import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Players;
import com.rsbuddy.script.wrappers.Npc;
import nz.uberdungeon.common.Plugin;
import nz.uberdungeon.utils.MyCamera;
import nz.uberdungeon.utils.util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/23/11
 * Time: 8:41 PM
 * Package: nz.uberdungeon.puzzles;
 */
public class Ghosts extends Plugin
{
    final static int GHOST = 10985;

    public boolean isValid() {
        return Npcs.getNearest(GHOST) != null && util.tileInRoom(Npcs.getNearest(GHOST).getLocation());
    }

    public String getStatus() {
        return "Solving: Ghosts";
    }

    public String getAuthor() {
        return "Zippy[Taw]";
    }

    public String getName() {
        return "Ghosts";
    }

    public int loop() {
        try {
            if (Players.getLocal().getInteracting() == null
                || ((Npc) Players.getLocal().getInteracting()).getId() != GHOST) {
                Npc n = Npcs.getNearest(GHOST);
                MyCamera.turnTo(n);
                n.interact("Attack");
            }
        } catch (Exception e) {
        }
        return 300;
    }
}
