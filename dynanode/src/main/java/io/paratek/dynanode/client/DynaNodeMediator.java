package io.paratek.dynanode.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class DynaNodeMediator {

    private static io.paratek.dynanode.DynaBridge dynaBridge;

    public static io.paratek.dynanode.DynaBridge getBridge(final String arg) {
        if (dynaBridge == null) {
            try {
                dynaBridge = (io.paratek.dynanode.DynaBridge) LocateRegistry
                        .getRegistry(Integer.valueOf(arg) + 20000)
                        .lookup("//localhost/dyna-" + arg);
            } catch (NotBoundException | RemoteException e) {
                e.printStackTrace();
            }
        }
        return dynaBridge;
    }

}
