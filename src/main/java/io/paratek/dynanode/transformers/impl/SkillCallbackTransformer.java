package io.paratek.dynanode.transformers.impl;

import com.runemate.game.api.hybrid.Environment;
import io.paratek.dynanode.transformers.AbstractTransformer;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ListIterator;

public class SkillCallbackTransformer extends AbstractTransformer {

    @Override
    public void run() {
        final String xpOwner = "client", xpName = "kx";
        final String levelOwner = "client", levelName = "kd";
        try {
            final ClassNode classNode = super.getClassNode("client");
            if (classNode != null) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.desc.startsWith("(L") && methodNode.desc.endsWith(")Z")) {
                        int index = -1, xp = -1, level = -1;
                        final ListIterator<AbstractInsnNode> nodeListIterator = methodNode.instructions.iterator();
                        while (nodeListIterator.hasNext()) {
                            final AbstractInsnNode cur = nodeListIterator.next();
                            if (index != -1 && cur.getPrevious().getOpcode() == Opcodes.IASTORE) {
                                Environment.getLogger().debug("Injected Skill Callback");
                                nodeListIterator.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                        "io/paratek/dynanode/server/DynaCallbackService",
                                        "getService",
                                        "()Lio/paratek/dynanode/server/DynaCallbackService;", false));
                                nodeListIterator.add(new VarInsnNode(Opcodes.ILOAD, index));
                                nodeListIterator.add(new VarInsnNode(Opcodes.ILOAD, xp));
                                nodeListIterator.add(new VarInsnNode(Opcodes.ILOAD, level));
                                nodeListIterator.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                                        "io/paratek/dynanode/server/DynaCallbackService",
                                        "onSkillUpdate",
                                        "(III)V", false));
                                break;
                            } else {
                                if (cur instanceof FieldInsnNode) {
                                    if (((FieldInsnNode) cur).owner.equals(xpOwner) && ((FieldInsnNode) cur).name.equals(xpName)) {
                                        if (cur.getNext().getNext() instanceof VarInsnNode && cur.getNext().getNext().getOpcode() == Opcodes.ILOAD) {
                                            xp = ((VarInsnNode) cur.getNext().getNext()).var;
                                        }
                                    } else if (((FieldInsnNode) cur).owner.equals(levelOwner) && ((FieldInsnNode) cur).name.equals(levelName)) {
                                        if (cur.getNext().getNext() instanceof VarInsnNode && cur.getNext().getNext().getOpcode() == Opcodes.ILOAD) {
                                            level = ((VarInsnNode) cur.getNext().getNext()).var;
                                        }
                                        if (cur.getNext().getNext() instanceof VarInsnNode) {
                                            index = ((VarInsnNode) cur.getNext()).var;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                classNode.accept(classWriter);
                final FileOutputStream stream = new FileOutputStream(new File("C:\\Users\\Parametric\\Desktop\\rs\\client.class"));
                stream.write(classWriter.toByteArray());
                stream.close();
                super.writeToBridge(classNode, "client");
            } else {
                throw new IllegalStateException("SkillCallback Transformer Failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
