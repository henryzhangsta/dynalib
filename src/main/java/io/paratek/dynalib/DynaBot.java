package io.paratek.dynalib;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.script.framework.LoopingBot;
import io.paratek.dynanode.DynaBridge;
import io.paratek.dynanode.DynaLoader;
import io.paratek.dynanode.transformers.impl.ActionTransformer;
import io.paratek.dynanode.transformers.impl.ModelRenderingTransformer;

import java.rmi.RemoteException;

public class DynaBot extends LoopingBot {

    private final DynaLoader loader;

    public DynaBot() {
        this.loader = new DynaLoader(Environment.getRuneScapeProcessId());
        this.loader.submitTransformer(new ModelRenderingTransformer());
        this.loader.submitTransformer(new ActionTransformer());
        this.loader.init();
    }

    @Override
    public void onStart(String... arguments) {
        Mouse.setPathGenerator(Mouse.HOPPING_PATH_GENERATOR);
    }

    public void onLoop() {
        System.out.println("LOOP");
    }

    @Override
    public void onResume() {
        if (this.getBridge() != null) {
            try {
                this.getBridge().setRenderingModels(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        if (this.getBridge() != null) {
            try {
                this.getBridge().setRenderingModels(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private DynaBridge getBridge() {
        if (this.loader != null) {
            return this.loader.getBridge();
        }
        return null;
    }


}
