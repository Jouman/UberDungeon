package nz.uberdungeon.dungeon.doors;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.event.listeners.MessageListener;
import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.misc.GameConstants;
import nz.uberdungeon.utils.MyMovement;
import nz.uberdungeon.utils.MyObjects;
import nz.uberdungeon.utils.util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/1/11
 * Time: 5:43 PM
 * Package: nz.uberdungeon.dungeon.doors;
 */
public class SkillDoor extends Door implements MessageListener
{
    private boolean canOpen = true;

    /**
     * Instantiates a new door.
     *
     * @param door   the door
     * @param parent instance of main script
     */
    public SkillDoor(GameObject door, DungeonMain parent) {
        super(door, SKILL, parent);
    }

    @Override
    public boolean canOpen() {
        return !(open || !canOpen || (util.arrayContains(GameConstants.DARK_SPIRIT, id) && !MyPlayer.prayDoors()));
    }

    @Override
    public void open() {
        if (!util.tileInRoom(util.getNearestNonWallTile(location)))
            return;
        MyPlayer.setLastDoorOpended(this);
        MyMovement.turnTo(location);
        int timeout = 0;
        while (Calculations.distanceTo(location) > 4 && MyPlayer.currentRoom().getItem() == null && ++timeout <= 15)
            sleep(100);
        GameObject object = MyObjects.getTopAt(location, id);
        if (object.interact(getAction(object))) {
            timeout = 0;
            while (Objects.getNearest(id) != null && ++timeout <= 15)
                sleep(100);
            if (object.getModel().getNextPoint().getX() == -1)
                debug(object.getLocation());
            if (Objects.getTopAt(location, Objects.TYPE_INTERACTIVE) == null)
                open = true;
        }
    }

    public void messageReceived(MessageEvent messageEvent) {
        if (messageEvent.getMessage().contains("level of"))
            canOpen = false;
    }
}
