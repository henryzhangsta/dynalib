package io.paratek.dynanode.server;

import io.paratek.dynanode.client.DynaClientCallback;

import java.rmi.RemoteException;

/**
 * Service that will mediate events when our injection calls the function
 *
 * @author Parametric
 */
public class DynaCallbackService {

    private DynaClientCallback callback;

    private DynaCallbackService() {

    }

    /**
     *
     * {@inheritDoc}
     */
    public void onSkillUpdate(int skillIndex, int xp, int level) {
        if (this.callback != null) {
            try {
                callback.onSkillUpdate(skillIndex, xp, level);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    public void onChatBoxUpdate(int type, String sender, String clan, String message) {
        if (this.callback != null) {
            try {
                callback.onChatBoxUpdate(type, sender, clan, message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Registers a Callback interface with the Service
     * @param callback
     */
    public void registerCallback(final DynaClientCallback callback) {
        this.callback = callback;
        try {
            callback.println("Successfully Registered Callback Object!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
    Singleton
     */

    private static DynaCallbackService service = new DynaCallbackService();

    public static DynaCallbackService getService() {
        return service;
    }

}
