package io.paratek.dynanode.transformers.impl;

import io.paratek.dynanode.transformers.AbstractTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;

public class ModelRenderingTransformer extends AbstractTransformer {

    @Override
    public void run() {
        final String className = "ds"; // Replace with CDN call Model
        try {
            final ClassNode classNode = super.getClassNode(className);
            for (Object method : classNode.methods) {
                final MethodNode methodNode = (MethodNode) method;
                if (methodNode.desc.equals("(ZZZJ)V")
                        || methodNode.desc.equals("(I)V")) {
                    final InsnList list = new InsnList();

                    final LabelNode labelNode = new LabelNode();
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            "io/paratek/dynanode/server/DynaActionSupplier",
                            "getSupplier",
                            "()Lio/paratek/dynanode/server/DynaActionSupplier;", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/server/DynaActionSupplier",
                            "isRenderingModels",
                            "()Z", false));
                    list.add(new JumpInsnNode(Opcodes.IFNE, labelNode));
                    list.add(new InsnNode(Opcodes.RETURN));
                    list.add(labelNode);
                    methodNode.instructions.insert(list);
                }
            }
            super.writeToBridge(classNode, className);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
