package io.paratek.dynanode.callback;

import com.runemate.game.api.hybrid.Environment;
import io.paratek.dynanode.client.DynaClientCallback;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DynaClientCallbackImpl extends UnicastRemoteObject implements DynaClientCallback {

    public DynaClientCallbackImpl() throws RemoteException {
        super();
    }

    @Override
    public void onSkillUpdate(int skillIndex, int xp, int level) {
        Environment.getLogger().debug("CALLBACK: " + skillIndex + " -> " + xp + ", " + level);
    }

    @Override
    public void println(String line) throws RemoteException {
        Environment.getLogger().debug("[GAME MESSAGE]: " + line);
    }

}
