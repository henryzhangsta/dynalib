package io.paratek.dynalib;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.script.framework.LoopingBot;
import io.paratek.dynanode.DynaBridge;
import io.paratek.dynanode.DynaLoader;
import io.paratek.dynanode.callback.DynaClientCallbackImpl;
import io.paratek.dynanode.client.DynaClientCallback;
import io.paratek.dynanode.transformers.impl.*;

import java.rmi.RemoteException;

public class DynaBot extends LoopingBot {

    private final DynaLoader loader;

    public DynaBot() {
        this.loader = new DynaLoader(Environment.getRuneScapeProcessId());
        this.loader.submitTransformer(new ModelRenderingTransformer());
        this.loader.submitTransformer(new SceneRenderingTransformer());
        this.loader.submitTransformer(new ActionTransformer());
        this.loader.submitTransformer(new EngineTransformer());
        this.loader.submitTransformer(new SkillCallbackTransformer());
        this.loader.init();
        try {
            this.loader.registerCallback(new DynaClientCallbackImpl());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(String... arguments) {
        Mouse.setPathGenerator(Mouse.HOPPING_PATH_GENERATOR);
    }

    public void onLoop() {
//        if (this.getBridge() != null) {
//            try {
//                final DynaAction action = this.getBridge().getLastAction();
//                if (action != null) {
//                    System.out.println("LOOP " + action);
//                }
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onResume() {
//        if (this.getBridge() != null) {
//            try {
////                this.getBridge().setRenderingModels(false);
////                this.getBridge().setRenderingScene(false);
//                this.getBridge().setTickDelay(18);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onPause() {
//        if (this.getBridge() != null) {
//            try {
////                this.getBridge().setRenderingModels(true);
////                this.getBridge().setRenderingScene(true);
//                this.getBridge().setTickDelay(0);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private DynaBridge getBridge() {
        if (this.loader != null) {
            return this.loader.getBridge();
        }
        return null;
    }


}
