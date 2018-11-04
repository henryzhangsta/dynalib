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
        Environment.getLogger().debug("Skill Update: " + skillIndex + " -> " + xp + ", " + level);
    }

    @Override
    public void onChatBoxUpdate(int type, String sender, String clan, String message) {
        Environment.getLogger().debug("ChatBox Update: " + type + ", " + sender + ", " + clan + ", " + message);
    }

    @Override
    public void onInventoryUpdate(int index, int stack, int id) throws RemoteException {
        Environment.getLogger().debug("Inventory Change: " + index + ", " + stack + ", " + id);
    }

    @Override
    public void onTick() throws RemoteException {
        System.out.println("Tick");
    }

    @Override
    public void println(String line) throws RemoteException {
        Environment.getLogger().debug("[GAME MESSAGE]: " + line);
    }

}
