package io.paratek.dynanode.transformers.impl;

import com.runemate.game.api.hybrid.Environment;
import io.paratek.dynanode.transformers.AbstractTransformer;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EngineTransformer extends AbstractTransformer {

    @Override
    public void run() {
        final String className = "client"; // replace with CDN call for hook, GameEngine
        try {
            final ClassNode classNode = super.getClassNode(className);
            if (classNode != null) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.name.equals("ad")) {
                        Environment.getLogger().info("Injected Tick Delay");
                        // Engine Tick Delay
                        final InsnList list = new InsnList();
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                                "getSupplier",
                                "()Lio/paratek/dynanode/rmi/server/DynaActionSupplier;", false));
                        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                                "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                                "sleepForTickDelay",
                                "()V", false));
                        methodNode.instructions.insert(list);
                    }
                }
                super.writeToBridge(classNode, className);
            } else {
                throw new IllegalStateException("Engine Transformer Failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}