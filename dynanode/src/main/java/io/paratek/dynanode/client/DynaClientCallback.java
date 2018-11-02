package io.paratek.dynanode.client;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Parametric
 */
public interface DynaClientCallback extends Remote, Serializable {

    /**
     * Called when a skill update takes place
     * @param skillIndex the index of the skill in the skill array
     * @param xp updated xp value
     * @param level updated level value
     */
    void onSkillUpdate(int skillIndex, int xp, int level) throws RemoteException;

    /**
     * Used for debugging
     * @param line
     * @throws RemoteException
     */
    void println(String line) throws RemoteException;

}
