package io.paratek.dynanode.transformers.impl;

import io.paratek.dynanode.transformers.AbstractTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;

public class ActionTransformer extends AbstractTransformer {

    @Override
    public void run() {
        final String className = "ju"; // replace with CDN call for hook
        final String methodName = "hc"; // replace with CDN call for method name
        try {
            final ClassNode classNode = super.getClassNode(className);
            for (MethodNode methodNode : classNode.methods) {
                // Find the desired method
                if (methodNode.name.equals(methodName)) {
                    /*
                        When the mediator has an action available, modify the parameters to what we want them to do.
                        Once the parameters are modified, we want to set the current action to null so it won't be executed again
                     */
                    final InsnList list = new InsnList();
                    final LabelNode labelNode = new LabelNode();

                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "getSupplier",
                            "()Lio/paratek/dynanode/rmi/server/DynaActionSupplier;", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "getAction",
                            "()Lio/paratek/dynanode/DynaAction;", false));
                    list.add(new InsnNode(Opcodes.ACONST_NULL));
                    list.add(new JumpInsnNode(Opcodes.IF_ACMPEQ, labelNode));

                    // Param 1
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "getSupplier",
                            "()Lio/paratek/dynanode/rmi/server/DynaActionSupplier;", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "getAction",
                            "()Lio/paratek/dynanode/DynaAction;", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/DynaAction",
                            "getP1", "()I", false));
                    list.add(new VarInsnNode(Opcodes.ISTORE, 0));

                    // Param 2
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "getSupplier",
                            "()Lio/paratek/dynanode/rmi/server/DynaActionSupplier;", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "getAction",
                            "()Lio/paratek/dynanode/DynaAction;", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/DynaAction",
                            "getP2", "()I", false));
                    list.add(new VarInsnNode(Opcodes.ISTORE, 1));

                    // Param 3
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "getSupplier",
                            "()Lio/paratek/dynanode/rmi/server/DynaActionSupplier;", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "getAction",
                            "()Lio/paratek/dynanode/DynaAction;", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/DynaAction",
                            "getP3", "()I", false));
                    list.add(new VarInsnNode(Opcodes.ISTORE, 2));

                    // Param 4
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "getSupplier",
                            "()Lio/paratek/dynanode/rmi/server/DynaActionSupplier;", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "getAction",
                            "()Lio/paratek/dynanode/DynaAction;", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/DynaAction",
                            "getP4", "()I", false));
                    list.add(new VarInsnNode(Opcodes.ISTORE, 3));

                    // Param 7
                    list.add(new IntInsnNode(Opcodes.SIPUSH, -1000));
                    list.add(new VarInsnNode(Opcodes.ISTORE, 6));

                    // Param 8
                    list.add(new IntInsnNode(Opcodes.SIPUSH, -1000));
                    list.add(new VarInsnNode(Opcodes.ISTORE, 7));

                    // Set current action to null
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "getSupplier",
                            "()Lio/paratek/dynanode/rmi/server/DynaActionSupplier;", false));
                    list.add(new InsnNode(Opcodes.ACONST_NULL));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/rmi/server/DynaActionSupplier",
                            "setAction",
                            "(Lio/paratek/dynanode/DynaAction;)V", false));

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
