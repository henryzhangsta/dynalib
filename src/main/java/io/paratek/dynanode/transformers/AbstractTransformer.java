package io.paratek.dynanode.transformers;

import io.paratek.dynanode.DynaBridge;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Class for implementing Transformers that inject code
 *
 * @author Parametric
 */
public abstract class AbstractTransformer {

    private static final String CDN_URL = "https://updates.paratek.io";

    private DynaBridge bridge;

    /**
     * Executes modification and sends them over the bridge to the DynaNode
     */
    public abstract void run();


    /**
     * Sends the ClassNode to DynaNode over the bridge
     * @param classNode the ClassNode to send
     * @param name the name of the ClassNode
     * @return true if no exceptions thrown
     */
    protected boolean writeToBridge(final ClassNode classNode, final String name) {
        final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        final byte[] b = classWriter.toByteArray();
        try {
            this.bridge.redefineClass(name, b);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Fetches a from the CDN and writes it into ClassNode
     * @param name name of class to fetch
     * @return
     */
    protected ClassNode getClassNode(final String name) throws IOException {
        final ClassReader classReader = new ClassReader(this.getRawClass(name));
        final ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        return classNode;
    }


    /**
     * Pulls the raw class from my server
     * @param name the class name from current revision to pull
     * @return the input stream for the class file
     * @throws IOException
     */
    protected InputStream getRawClass(final String name) throws IOException {
        return new URL(CDN_URL + "/pack/class/" + name).openStream();
    }

    /**
     * Sets the bridge, not in constructor so we can encapsulate this
     * @param bridge
     */
    public void setBridge(final DynaBridge bridge) {
        this.bridge = bridge;
    }

}
