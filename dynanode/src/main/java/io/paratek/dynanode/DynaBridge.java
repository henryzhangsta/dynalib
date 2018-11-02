package io.paratek.dynanode;

import io.paratek.dynanode.client.DynaClientCallback;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface to interact with OSRS remotely
 *
 * @author Parametric
 */
public interface DynaBridge extends Remote {


    /**
     * Passes a reference to our client side callback to the RMI server
     * @param callback
     * @throws RemoteException
     */
    void registerCallback(final DynaClientCallback callback) throws RemoteException;


    /**
     * Every time the game is clicked, an action is created and stored for this use
     * @return the action we stored
     * @throws RemoteException
     */
    DynaAction getLastAction() throws RemoteException;


    /**
     * Sets the Action that will override the next action that will be sent upon a mouse click
     * @param action action to execute
     * @throws RemoteException
     */
    void setAction(final DynaAction action) throws RemoteException;


    /**
     * Tells the game whether or not to Render Models
     * @param val if true the game will render models, if false it wont
     * @throws RemoteException
     */
    void setRenderingModels(final boolean val) throws RemoteException;


    /**
     * Tells the game whether or not to Render the Scene
     * @param val if true the game will render scene, if false it wont
     * @throws RemoteException
     */
    void setRenderingScene(final boolean val) throws RemoteException;


    /**
     * Sets the tick delay, every tick the thread is slept for n ms
     * @param ms the ms to sleep after each tick
     * @throws RemoteException
     */
    void setTickDelay(final int ms) throws RemoteException;


    /**
     * Retrieves a loaded class in the remote JVM
     * @param name the name of the desired class
     * @return the byte[] representation of the class
     * @throws RemoteException
     */
    byte[] fetchClass(final String name) throws RemoteException;


    /**
     * Redefines a class in the remote JVM
     * @param name the name of the class to redefine
     * @param bytes the bytes of the new class
     * @return if redefinition was successful
     * @throws RemoteException
     */
    boolean redefineClass(final String name, final byte[] bytes) throws RemoteException;

}
