package nz.uberdungeon.puzzles;

import com.rsbuddy.script.methods.Camera;
import com.rsbuddy.script.methods.Npcs;
import nz.uberdungeon.common.Plugin;
import nz.uberdungeon.utils.MyCamera;
import nz.uberdungeon.utils.util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/22/11
 * Time: 7:11 AM
 * Package: nz.uberdungeon.puzzles;
 */
public class PondSkater extends Plugin
{
    static final int SKATER = 12089;

    public boolean isValid() {
        return Npcs.getNearest(SKATER) != null && util.tileInRoom(Npcs.getNearest(SKATER).getLocation());
    }

    public String getStatus() {
        return "Solving: Pondskaters";
    }

    public String getAuthor() {
        return "Zippy[Taw]";
    }

    public String getName() {
        return "Pondskaters";
    }

    public int loop() {
        Camera.setPitch(20);
        MyCamera.turnTo(Npcs.getNearest(SKATER));
        Npcs.getNearest(SKATER).interact("Catch");
        return 5000;
    }
}
