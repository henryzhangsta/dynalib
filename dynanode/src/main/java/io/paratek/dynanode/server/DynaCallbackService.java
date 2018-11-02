package io.paratek.dynanode.server;

import io.paratek.dynanode.client.DynaClientCallback;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Service that will mediate events when our injection calls the function
 *
 * @author Parametric
 */
public class DynaCallbackService {

    private final List<DynaClientCallback> callbacks;

    private DynaCallbackService() {
        this.callbacks = new ArrayList<>();
    }

    /**
     *
     * {@inheritDoc}
     */
    public void onSkillUpdate(int skillIndex, int xp, int level) {
        final Iterator<DynaClientCallback> callbackIterator = this.callbacks.iterator();
        while (callbackIterator.hasNext()) {
            final DynaClientCallback callback = callbackIterator.next();
            if (callback == null) {
                // If the callback is null the client has disconnected so we can remove it
                callbackIterator.remove();
            } else {
                try {
                    callback.onSkillUpdate(skillIndex, xp, level);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Registers a Callback interface with the Service
     * @param callback
     */
    public void registerCallback(final DynaClientCallback callback) {
        if (!this.callbacks.contains(callback)) {
            this.callbacks.add(callback);
            try {
                callback.println("Successfully Registered Callback Object!");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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
