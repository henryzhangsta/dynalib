package io.paratek.dynanode.transformers.impl;

import com.runemate.game.api.hybrid.Environment;
import io.paratek.dynanode.transformers.AbstractTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ListIterator;

public class ClientTransformer extends AbstractTransformer {

    private int index = -1, xp = -1, level = -1;

    // Skill Listener Data
    private final String xpOwner = "client", xpName = "kx";
    private final String levelOwner = "client", levelName = "kd";

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

//                            this.injectInventoryCallback(nodeListIterator, cur);
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

}
