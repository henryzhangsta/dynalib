package io.paratek.dynanode;

import com.runemate.game.api.hybrid.Environment;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import io.paratek.dynanode.client.DynaClientCallback;
import io.paratek.dynanode.client.DynaNodeMediator;
import io.paratek.dynanode.transformers.AbstractTransformer;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

public class DynaLoader {

    private final List<AbstractTransformer> transformers = new ArrayList<>();

    private DynaBridge bridge = null;
    private final String vid;

    public DynaLoader(String vid) {
        this.vid = vid;
    }

    /**
     * Registers a Callback with the Callback Service
     * @param callback
     */
    public void registerCallback(final DynaClientCallback callback) {
        try {
            this.getBridge().registerCallback(callback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a Transformer to be called in DynaLoader#init
     * @param transformer the transformer to add
     */
    public void submitTransformer(final AbstractTransformer transformer) {
        this.transformers.add(transformer);
    }

    /**
     * Get Access to the Bridge
     * @return
     */
    public DynaBridge getBridge() {
        return bridge;
    }

    /**
     * Creates the tmp agent, attaches it to the jvm, and stores a reference to the bridge
     * @return
     */
    public boolean init() {
        try {
            // Check if there isn't an DynaNode already loaded
            if (this.initRegistry(Integer.valueOf(this.vid) + 20000)) {
                final File tmp;
                if ((tmp = this.buildTempAgent()) != null) {
                    VirtualMachine vm = VirtualMachine.attach(this.vid);
                    if (!vm.getSystemProperties().contains("dyna.agent.installed")) {
                        vm.loadAgent(tmp.getPath(), this.vid);
                    }
                    bridge = DynaNodeMediator.getBridge(this.vid);
                    // Order is funny for a reason, we don't want to double inject, but we can't inject before loading agent
                    if (!vm.getSystemProperties().contains("dyna.agent.installed")) {
                        for (AbstractTransformer t : this.transformers) {
                            t.setBridge(this.bridge);
                            t.run();
                        }
                    }
                    return true;
                }
            } else {
                Environment.getLogger().debug("DynaNode is already loaded!");
            }
        } catch (AttachNotSupportedException | IOException | AgentLoadException | AgentInitializationException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Clones the agent jar so it can be loaded from classpath
     * @return the cloned jar
     * @throws IOException
     */
    private File buildTempAgent() throws IOException {
        File tmpFile = File.createTempFile("dynanode", ".jar");
        tmpFile.deleteOnExit();
        final InputStream in = DynaNodeMediator.class.getResourceAsStream("/dynanode.jar");
        final OutputStream out = new FileOutputStream(tmpFile);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        return tmpFile;
    }

    /**
     * Starts the RMI Registry, if it's already running it returns false
     * @return false if already running, true if started on call
     */
    private boolean initRegistry(int port) {
        try {
            LocateRegistry.createRegistry(port);
            return true;
        } catch (RemoteException ignored) {
            return false;
        }
    }

}
