package nz.uberdungeon.dungeon.rooms;

import com.rsbuddy.script.wrappers.GroundItem;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.dungeon.doors.Door;
import nz.uberdungeon.utils.RSArea;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/1/11
 * Time: 3:39 PM
 * Package: nz.uberdungeon.dungeon.rooms;
 */
public class PuzzleRoom extends Room
{
    private boolean solved;

    /**
     * Instantiates a new room.
     *
     * @param area   the area
     * @param doors  the doors
     * @param parent instance of main script
     */
    public PuzzleRoom(RSArea area,
                      LinkedList<Door> doors,
                      GroundItem[] groundItems,
                      DungeonMain parent) {
        super(area, doors, PUZZLE, parent);
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
