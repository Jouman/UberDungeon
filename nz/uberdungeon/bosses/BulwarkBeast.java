package nz.uberdungeon.bosses;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.event.listeners.MessageListener;
import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import nz.uberdungeon.common.Plugin;
import nz.uberdungeon.dungeon.Enemy;
import nz.uberdungeon.dungeon.EnemyDef;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.misc.GameConstants;
import nz.uberdungeon.utils.MyCombat;
import nz.uberdungeon.utils.MyEquipment;
import nz.uberdungeon.utils.MyMovement;
import nz.uberdungeon.utils.util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/24/11
 * Time: 3:04 PM
 * Package: nz.uberdungeon.bosses;
 */
public class BulwarkBeast extends Plugin
{
    private static boolean destroyedArmour = false;
    private static String weapon;

    public void messageReceived(MessageEvent messageEvent) {
        if (messageEvent.getMessage().toLowerCase().contains("the heavy"))
            destroyedArmour = true;
    }

    private static enum State
    {
        DESTROY_ARMOUR("Destroying Armour"), TAKE_AXE("Taking pickaxe"), KILL_BOSS("Killing beast"), EAT_FOOD(
            "Eating food"), CHANGE_WEAPONS("Changing weapons");

        private State(String name) {
            get = name;
        }

        private final String get;

        public String toString() {
            return get;
        }
    }

    @Override
    public boolean isValid() {
        return MyPlayer.currentRoom().getNearestNpc(".*wark beast") != null;
    }

    @Override
    public String getStatus() {
        return "Killing the Bulwark Beast: " + getState().toString();
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Bulwark Beast";
    }

    private State getState() {
        if (MyPlayer.needToEat() && MyPlayer.hasFood())
            return State.EAT_FOOD;
        else if (!havePickAxe())
            return State.TAKE_AXE;
        else if (util.equipmentContains("pickaxe", true) &&
                 destroyedArmour ||
                 !destroyedArmour && !util.equipmentContains("pickaxe", true))
            return State.CHANGE_WEAPONS;
        else if (!destroyedArmour)
            return State.DESTROY_ARMOUR;
        else
            return State.KILL_BOSS;
    }

    @Override
    public int loop() {
        try {
            MyCombat.doPrayerFor(new EnemyDef(MyPlayer.currentRoom().getNearestNpc(".*wark beast.*")));
            switch (getState()) {
                case TAKE_AXE:
                    takeAxe();
                    break;
                case EAT_FOOD:
                    MyPlayer.eat();
                    break;
                case KILL_BOSS:
                    killBoss();
                    break;
                case DESTROY_ARMOUR:
                    destroyArmour();
                    break;
                case CHANGE_WEAPONS:
                    changeWeapons();
                    break;
            }
        } catch (Exception ignored) {
        }
        return Random.nextInt(400, 600);
    }

    private void changeWeapons() {
        if (!destroyedArmour && !util.equipmentContains("pickaxe", true)) {
            weapon = MyEquipment.getItem(MyEquipment.WEAPON).getName();
            util.clickItem(util.getInventoryItem("pickaxe"));
            Game.openTab(Game.TAB_EQUIPMENT);
            sleep(Random.nextInt(500, 600));
            Game.openTab(Game.TAB_INVENTORY);
        }
        else if (util.getInventoryItem(weapon) != null) {
            util.clickItem(util.getInventoryItem(weapon));
            Game.openTab(Game.TAB_EQUIPMENT);
            sleep(Random.nextInt(500, 600));
            Game.openTab(Game.TAB_INVENTORY);
        }
        else
            killBoss();
    }

    private void destroyArmour() {
        if (util.inventoryContains("pickaxe") && !util.equipmentContains("pickaxe"))
            util.getInventoryItem("pickaxe").click(true);
        else if (MyPlayer.interacting() == null && !MyPlayer.inCombat()) {
            Enemy.setNPC(MyPlayer.currentRoom().getNearestNpc(".*wark beast.*"));
            MyMovement.turnTo(Enemy.getNPC());
            Enemy.interact("attack");
        }
    }

    private void killBoss() {
        if (MyPlayer.interacting() == null && !MyPlayer.inCombat()) {
            Enemy.setNPC(MyPlayer.currentRoom().getNearestNpc(".*wark beast.*"));
            Enemy.interact("attack");
        }
    }

    private boolean havePickAxe() {
        return util.playerHas("pickaxe", true);
    }

    private void takeAxe() {
        GameObject rock = Objects.getNearest(GameConstants.BULWARK_AXE_ROCKS);
        if (rock != null) {
            MyMovement.turnTo(rock);
            if (rock.interact("take")) {
                int count = Inventory.getCount();
                int timeout = 0;
                while (count == Inventory.getCount() && ++timeout <= 15)
                    sleep(100);
            }
            else if(Calculations.distanceTo(rock) < 3) {
                Game.openTab(Game.TAB_EQUIPMENT);
                sleep(Random.nextInt(400,500));
                Game.openTab(Game.TAB_INVENTORY);
            }
        }
    }

    public void reset() {
        destroyedArmour = false;
    }
}
