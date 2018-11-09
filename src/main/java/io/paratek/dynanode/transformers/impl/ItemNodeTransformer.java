package io.paratek.dynanode.transformers.impl;

import com.runemate.game.api.hybrid.Environment;
import io.paratek.dynanode.transformers.AbstractTransformer;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ItemNodeTransformer extends AbstractTransformer {

    @Override
    public void run() {
        String methodOwner = "at", methodName = "s";
        try {
            final ClassNode classNode = super.getClassNode(methodOwner);
            if (classNode != null) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.name.equals(methodName)
                            && methodNode.desc.startsWith("(IIII") && methodNode.desc.endsWith(")V")
                            && (methodNode.access & Opcodes.ACC_STATIC) != 0) {
                        Environment.getLogger().debug("Injected ItemNode Listener");
                        final InsnList list = new InsnList();
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                "io/paratek/dynanode/server/DynaCallbackService",
                                "getService",
                                "()Lio/paratek/dynanode/server/DynaCallbackService;", false));
                        list.add(new VarInsnNode(Opcodes.ILOAD, 0));
                        list.add(new VarInsnNode(Opcodes.ILOAD, 1));
                        list.add(new VarInsnNode(Opcodes.ILOAD, 2));
                        list.add(new VarInsnNode(Opcodes.ILOAD, 3));
                        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                                "io/paratek/dynanode/server/DynaCallbackService",
                                "onInventoryUpdate",
                                "(IIII)V", false));
                        methodNode.instructions.insert(list);
                    }
                }
                super.writeToBridge(classNode, methodOwner);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
