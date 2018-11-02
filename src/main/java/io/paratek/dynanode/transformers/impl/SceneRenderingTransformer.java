package io.paratek.dynanode.transformers.impl;

import io.paratek.dynanode.transformers.AbstractTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;

public class SceneRenderingTransformer extends AbstractTransformer {

    @Override
    public void run() {
        final String className = "dh"; // Replace with CDN call, SceneGraph / Landscape
        try {
            final ClassNode classNode = super.getClassNode(className);
            for (Object method : classNode.methods) {
                final MethodNode methodNode = (MethodNode) method;
                if (methodNode.desc.equals("(IIIIIIIIIIIIIIIIIIII)V")
                        || methodNode.desc.equals("(IIIIII)V")
                        || methodNode.desc.equals("(IIIIIIII)V")) {
                    final InsnList list = new InsnList();

                    final LabelNode labelNode = new LabelNode();
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            "io/paratek/dynanode/server/DynaActionSupplier",
                            "getSupplier",
                            "()Lio/paratek/dynanode/server/DynaActionSupplier;", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/server/DynaActionSupplier",
                            "isRenderingScene",
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
