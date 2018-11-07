package io.paratek.dynalib.action;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Region;
import io.paratek.dynanode.DynaAction;
import io.paratek.dynanode.server.DynaActionSupplier;

import java.util.List;

/**
 * Create Actions for execution in game, added as needed.
 * To check action ops do something in game and print out lastAction from DynaNode
 * @see DynaActionSupplier#getLastAction()
 *
 * @ Cloud pls don't add this and let me use it my own bots kthx.
 * You can use it too but don't let prime have it, fuck'm
 *
 * @author Parametric
 */
public class ActionGenerator {

    /**
     * Creates a DynaAction with the given parameters
     * @param i1
     * @param i2
     * @param i3
     * @param i4
     * @return
     */
    public static DynaAction create(final int i1, int i2, int i3, int i4) {
        return new DynaAction(i1, i2, i3, i4);
    }


    /**
     * Creates an action for an InterfaceComponent
     * @param component
     * @param action
     * @return
     */
    public static DynaAction createInterfaceAction(final InterfaceComponent component, final String action) {
        if (component == null) {
            return null;
        }
        final List<String> actions;
        if ((actions = component.getActions()) != null) {
            int index = actions.indexOf(action);
            final InterfaceComponent parent;
            if ((parent = component.getParentComponent()) != null) {
                final List<InterfaceComponent> children = parent.getChildren();
                if (children != null) {
                    for (int i = 0; i < children.size(); i++) {
                        InterfaceComponent child = children.get(i);
                        if (child != null && child.equals(component)) {
                            index = i;
                        }
                    }
                }
            }
            return create(index, component.getId(), ActionOps.INTERFACE_ACTION, 1);
        } else {
            return create(-1, component.getId(), ActionOps.INTERFACE_ACTION, 0);
        }
    }

    /**
     * Creates an action for a GameObject
     * @param object
     * @param action
     * @return
     */
    public static DynaAction createGameObjectAction(final GameObject object, final String action) {
        if (object == null) {
            return null;
        }
        int op;
        if (Inventory.getSelectedItem() != null) {
            op = ActionOps.ITEM_ON_OBJECT;
        } else {
            if (action.equals("Examine")) {
                op = ActionOps.EXAMINE_OBJECT;
            } else {
                final List<String> actions;
                if ((actions = object.getDefinition().getActions()) != null && actions.contains(action)) {
                    int index = actions.indexOf(action);
                    op = ActionOps.OBJECT_ACTION_0 + index;
                } else {
                    return null;
                }
            }
        }
        final Coordinate c = object.getPosition(Region.getBase());
        final Area a = object.getArea();
        int x = (int) (c.getX() - Math.floor(((Area.Rectangular) a).getHeight() / 2)); // yes height/x
        int y = (int) (c.getY() - Math.floor(((Area.Rectangular) a).getWidth() / 2)); // yes width/y
//        return create(x, y, )
        return null;
    }

}
