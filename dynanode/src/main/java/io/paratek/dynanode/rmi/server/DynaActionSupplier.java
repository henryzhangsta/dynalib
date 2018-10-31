package io.paratek.dynanode.rmi.server;


import io.paratek.dynanode.DynaAction;

/**
 * Handles actions in the client instance
 *
 * @author Parametric
 */
public class DynaActionSupplier {

    private static final DynaActionSupplier mediator = new DynaActionSupplier();

    public static DynaActionSupplier getSupplier() {
        return mediator;
    }

    private int tickDelay = 0;
    private boolean renderingModels = true, renderingScene = true;
    private DynaAction action = null;

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

    public int getTickDelay() {
        return tickDelay;
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

}
