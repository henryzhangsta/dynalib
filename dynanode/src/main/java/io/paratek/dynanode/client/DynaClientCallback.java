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
     * Called when a message is added to the Chatbox
     * @param type the type of message
     * @param sender who sent the message
     * @param clan the clan tag
     * @param message the actual message
     * @throws RemoteException
     */
    void onChatBoxUpdate(int type, String sender, String clan, String message) throws RemoteException;

    /**
     * Called when inventory contents change
     * @param index the slot
     * @param stack new stack size
     * @param id new id
     * @param type type
     * @throws RemoteException
     */
    void onInventoryUpdate(int type, int index, int id, int stack) throws RemoteException;

    /**
     * Called after every game tick
     * @throws RemoteException
     */
    void onTick() throws RemoteException;

    /**
     * Used for debugging
     * @param line
     * @throws RemoteException
     */
    void println(String line) throws RemoteException;

}
