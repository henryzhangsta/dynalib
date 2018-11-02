package io.paratek.dynanode.server;

import java.lang.instrument.Instrumentation;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class DynaNode {

    public static void agentmain(String arg, final Instrumentation inst) {
//        inst.addTransformer(new DynaTransformer());
        // Setup RMI
        try {
            LocateRegistry.getRegistry(Integer.valueOf(arg) + 20000)
                    .rebind("//localhost/dyna-" + arg, new DynaBridgeImpl(inst));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Set a flag to check
        System.setProperty("dyna.agent.installed", "true");
    }

}
