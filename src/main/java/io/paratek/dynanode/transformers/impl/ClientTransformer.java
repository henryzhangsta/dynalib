package io.paratek.dynanode.transformers.impl;

import com.runemate.game.api.hybrid.Environment;
import io.paratek.dynanode.transformers.AbstractTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ListIterator;

public class ClientTransformer extends AbstractTransformer {

    private int index = -1, xp = -1, level = -1;
    private int i2 = -1, stack = -1, id = -1, ref = -1;

    // Skill Listener Data
    private final String xpOwner = "client", xpName = "kx";
    private final String levelOwner = "client", levelName = "kd";
    // Inventory Listener Data
    private final String invIdsName = "ew";
    private final String invStackName = "ek";
    private final String widgetOwner = "hw", parentId = "u";
    private final int parentMod = -142812563;

    private boolean notifiedSkill = false, notifiedInventory = false;

    @Override
    public void run() {
        try {
            final ClassNode classNode = super.getClassNode("client");
            if (classNode != null) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.desc.startsWith("(L") && methodNode.desc.endsWith(")Z")) {
                        final ListIterator<AbstractInsnNode> nodeListIterator = methodNode.instructions.iterator();
                        while (nodeListIterator.hasNext()) {
                            final AbstractInsnNode cur = nodeListIterator.next();

                            this.injectInventoryCallback(nodeListIterator, cur);
                            this.injectSkillsCallback(nodeListIterator, cur);
                        }
                    }
                }
                super.writeToBridge(classNode, "client");
            } else {
                throw new IllegalStateException("SkillCallback Transformer Failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void injectSkillsCallback(final ListIterator<AbstractInsnNode> nodeListIterator, final AbstractInsnNode cur) {
        // Skill Listener
        if (index != -1 && xp != -1 && level != -1 && cur.getPrevious().getOpcode() == Opcodes.IASTORE) {
            if (!this.notifiedSkill) {
                // The field is used in some dummy methods, only need to print it once
                Environment.getLogger().debug("Injected Skill Callback");
                this.notifiedSkill = true;
            }

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
            index = -1;
            xp = -1;
            level = -1;
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
                    if (cur.getNext() instanceof VarInsnNode && cur.getNext().getOpcode() == Opcodes.ILOAD) {
                        index = ((VarInsnNode) cur.getNext()).var;
                    }
                }
            }
        }
    }


    private void injectInventoryCallback(final ListIterator<AbstractInsnNode> nodeListIterator, final AbstractInsnNode cur) {
        if (i2 != -1 && id != -1 && stack != -1 && ref != -1 && cur.getOpcode() == Opcodes.IASTORE) {

            if (!this.notifiedInventory) {
                Environment.getLogger().debug("Injected Inventory Listener " + ref);
                this.notifiedInventory = true;
            }

            // check if widget id is correct
            final LabelNode labelNode = new LabelNode();
            nodeListIterator.add(new VarInsnNode(Opcodes.ALOAD, ref));
            nodeListIterator.add(new FieldInsnNode(Opcodes.GETFIELD, widgetOwner, parentId, "I"));
            nodeListIterator.add(new LdcInsnNode(parentMod));
            nodeListIterator.add(new InsnNode(Opcodes.IMUL));
            nodeListIterator.add(new LdcInsnNode(9764864));
            nodeListIterator.add(new JumpInsnNode(Opcodes.IF_ICMPNE, labelNode));

            nodeListIterator.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    "io/paratek/dynanode/server/DynaCallbackService",
                    "getService",
                    "()Lio/paratek/dynanode/server/DynaCallbackService;", false));
            nodeListIterator.add(new VarInsnNode(Opcodes.ILOAD, i2));
            nodeListIterator.add(new VarInsnNode(Opcodes.ILOAD, stack));
            nodeListIterator.add(new VarInsnNode(Opcodes.ILOAD, id));
            nodeListIterator.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                    "io/paratek/dynanode/server/DynaCallbackService",
                    "onInventoryUpdate",
                    "(III)V", false));

            nodeListIterator.add(labelNode);
            i2 = -1;
            id = -1;
            stack = -1;
            ref = -1;
        } else {
            if (cur instanceof FieldInsnNode) {
                if (((FieldInsnNode) cur).owner.equals(widgetOwner) && ((FieldInsnNode) cur).name.equals(invIdsName)) {
                    if (cur.getNext().getNext() instanceof VarInsnNode && cur.getNext().getNext().getOpcode() == Opcodes.ILOAD) {
                        id = ((VarInsnNode) cur.getNext().getNext()).var;
                        ref = ((VarInsnNode) cur.getPrevious()).var;
                    }
                } else if (((FieldInsnNode) cur).owner.equals(widgetOwner) && ((FieldInsnNode) cur).name.equals(invStackName)) {
                    if (cur.getNext().getNext() instanceof VarInsnNode && cur.getNext().getNext().getOpcode() == Opcodes.ILOAD) {
                        stack = ((VarInsnNode) cur.getNext().getNext()).var;
                    }
                    if (cur.getNext() instanceof VarInsnNode) {
                        i2 = ((VarInsnNode) cur.getNext()).var;
                    }
                }
            }
        }
    }

}
