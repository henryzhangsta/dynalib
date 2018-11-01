package io.paratek.dynanode.rmi.server;


import io.paratek.dynanode.DynaAction;

import java.util.HashMap;

/**
 * Handles actions in the client instance
 *
 * @author Parametric
 */
public class DynaActionSupplier {

    public static HashMap<String, byte[]> classData = new HashMap<>();
    private static final DynaActionSupplier mediator = new DynaActionSupplier();

    public static DynaActionSupplier getSupplier() {
        return mediator;
    }

    private int tickDelay = 0;
    private boolean renderingModels = true, renderingScene = true;

    private volatile DynaAction action = null, lastAction;

    public void setTickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
    }

    public void setRenderingModels(boolean renderingModels) {
        this.renderingModels = renderingModels;
    }

    public void setRenderingScene(boolean renderingScene) {
        this.renderingScene = renderingScene;
    }

    public void setAction(DynaAction action) {
        this.action = action;
    }

    public void setLastAction(int i, int i2, int i3, int i4, String s1, String s2) {
        this.lastAction = new DynaAction(i, i2, i3, i4, s1, s2);
    }

    public void sleepForTickDelay() {
        try {
            Thread.sleep(this.tickDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isRenderingScene() {
        return renderingScene;
    }

    public boolean isRenderingModels() {
        return renderingModels;
    }

    public DynaAction getAction() {
        return action;
    }

    public DynaAction getLastAction() {
        return lastAction;
    }

}
