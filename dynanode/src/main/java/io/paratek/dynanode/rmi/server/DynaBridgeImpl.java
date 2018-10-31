package io.paratek.dynanode.rmi.server;

import io.paratek.dynanode.DynaAction;
import io.paratek.dynanode.DynaBridge;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DynaBridgeImpl extends UnicastRemoteObject implements DynaBridge {

    private final Instrumentation inst;

    DynaBridgeImpl(Instrumentation inst) throws RemoteException {
        super();
        this.inst = inst;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAction(DynaAction action) throws RemoteException {
        DynaActionSupplier.getSupplier().setAction(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRenderingModels(boolean val) throws RemoteException {
        DynaActionSupplier.getSupplier().setRenderingModels(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRenderingScene(boolean val) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTickDelay(int ms) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] fetchClass(String name) throws RemoteException {
        try {
            // We Have to iterate over all of the loaded classes sadly
            for (Class clazz : this.inst.getAllLoadedClasses()) {
                if (clazz != null && clazz.getName().equals(name)) {
                    InputStream classStream = clazz.getClassLoader().getResourceAsStream(clazz.getName().replace(".", "/") + ".class");
                    if (classStream != null) {
                        // Read the stream to a byte array
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        byte[] buffer = new byte[0xFFFF];
                        for (int len = classStream.read(buffer); len != -1; len = classStream.read(buffer)) {
                            os.write(buffer, 0, len);
                        }
                        return os.toByteArray();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean redefineClass(String name, byte[] bytes) throws RemoteException {
        try {
            // We Have to iterate over all of the loaded classes sadly
            for (Class clazz : this.inst.getAllLoadedClasses()) {
                if (clazz != null && clazz.getName().equals(name)) {
                    // Create definition and redefine
                    final ClassDefinition definition = new ClassDefinition(clazz, bytes);
                    this.inst.redefineClasses(definition);
                    return true;
                }
            }
        } catch (ClassNotFoundException | UnmodifiableClassException e) {
            e.printStackTrace();
        }
        return false;
    }

}
