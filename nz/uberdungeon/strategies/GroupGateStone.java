package nz.uberdungeon.strategies;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.common.Strategy;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.misc.GameConstants;
import nz.uberdungeon.utils.MyMovement;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/5/11
 * Time: 6:15 PM
 * Package: nz.uberdungeon.strategies;
 */
public class GroupGateStone extends Strategy
{
    /**
     * Instantiates a new MyEquipment items.
     *
     * @param parent instance of main script
     */
    public GroupGateStone(DungeonMain parent) {
        super(parent);
    }

    @Override
    public int execute() {
        GameObject portal = Objects.getNearest(GameConstants.GGSPortal);
        if (portal != null) {
            MyMovement.turnTo(portal);
            int timeout = 0;
            while (Calculations.distanceTo(portal) > 4 && ++timeout <= 15)
                sleep(100);
            if (portal.interact("Enter")) {
                timeout = 0;
                while (MyPlayer.currentRoom().contains(MyPlayer.location()) && ++timeout <= 15)
                    sleep(100);
                if (!MyPlayer.currentRoom().contains(MyPlayer.location()))
                    MyPlayer.setTeleBack(false);
            }
        }
        return random(400, 600);
    }

    @Override
    public boolean isValid() {
        return MyPlayer.currentRoom().contains(MyPlayer.location()) && MyPlayer.teleBack();
    }

    @Override
    public void reset() {
    }

    @Override
    public String getStatus() {
        return "Teleporting back to Group Gate Stone";
    }
}
