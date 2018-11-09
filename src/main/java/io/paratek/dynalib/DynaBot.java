package io.paratek.dynalib;

import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Region;
import com.runemate.game.api.script.framework.LoopingBot;
import io.paratek.dynalib.action.ActionGenerator;
import io.paratek.dynanode.DynaBridge;
import io.paratek.dynanode.DynaLoader;
import io.paratek.dynanode.callback.DynaClientCallbackImpl;
import io.paratek.dynanode.transformers.impl.*;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

public class DynaBot extends LoopingBot {

    private final DynaLoader loader;

    public DynaBot() {
        this.loader = new DynaLoader(Environment.getRuneScapeProcessId());
        this.loader.submitTransformer(new ModelRenderingTransformer());
        this.loader.submitTransformer(new SceneRenderingTransformer());
        this.loader.submitTransformer(new ActionTransformer());
        this.loader.submitTransformer(new EngineTransformer());
        this.loader.submitTransformer(new ClientTransformer());
        this.loader.submitTransformer(new ChatBoxTransformer());
        this.loader.submitTransformer(new ItemNodeTransformer());
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
