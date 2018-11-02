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

public class ChatBoxTransformer extends AbstractTransformer {

    @Override
    public void run() {
        // This is a static void function that constructs a MessageContainer
        // if you remove dummy methods and parameters just
        // check for desc == (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        // and if the method is static
        final String msgContainer = "ao";
        final String bldMsgMethod = "l";
        try {
            final ClassNode container = super.getClassNode(msgContainer);
            for (MethodNode methodNode : container.methods) {
                if (methodNode.name.equals(bldMsgMethod)
                        && methodNode.desc.startsWith("(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;")) {
                    Environment.getLogger().debug("Injected ChatBox Callback");
                    final InsnList list = new InsnList();
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            "io/paratek/dynanode/server/DynaCallbackService",
                            "getService",
                            "()Lio/paratek/dynanode/server/DynaCallbackService;", false));
                    // just grabbing the parameters so iload1, aload2-4, load onto stack
                    list.add(new VarInsnNode(Opcodes.ILOAD, 1)); // type
                    list.add(new VarInsnNode(Opcodes.ALOAD, 2)); // sender
                    list.add(new VarInsnNode(Opcodes.ALOAD, 3)); // message
                    list.add(new VarInsnNode(Opcodes.ALOAD, 4)); // clan
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/server/DynaCallbackService",
                            "onChatBoxUpdate",
                            "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false));
                    methodNode.instructions.insert(list);
//                    final ListIterator<AbstractInsnNode> nodeListIterator = methodNode.instructions.iterator();
//                    while (nodeListIterator.hasNext()) {
//                        final AbstractInsnNode cur = nodeListIterator.next();
//                        // find the location
//                        if (cur instanceof MethodInsnNode && ((MethodInsnNode) cur).name.equals("<init>")
//                                && ((MethodInsnNode) cur).owner.equals(msgClass) && cur.getNext().getOpcode() == Opcodes.ASTORE) {
//                            Environment.getLogger().debug("Injected ChatBox Callback");
//                            nodeListIterator.next(); // want to move past the storing
//                            nodeListIterator.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
//                                    "io/paratek/dynanode/server/DynaCallbackService",
//                                    "getService",
//                                    "()Lio/paratek/dynanode/server/DynaCallbackService;", false));
//                            // just grabbing the parameters so iload1, aload2-4, load onto stack
//                            nodeListIterator.add(new VarInsnNode(Opcodes.ILOAD, 1)); // type
//                            nodeListIterator.add(new VarInsnNode(Opcodes.ALOAD, 2)); // sender
//                            nodeListIterator.add(new VarInsnNode(Opcodes.ALOAD, 3)); // message
//                            nodeListIterator.add(new VarInsnNode(Opcodes.ALOAD, 4)); // clan
//                            nodeListIterator.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
//                                    "io/paratek/dynanode/server/DynaCallbackService",
//                                    "onChatBoxUpdate",
//                                    "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false));
//                        }
//                    }
                }
            }
            final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            container.accept(classWriter);
            final FileOutputStream stream = new FileOutputStream(new File("C:\\Users\\Parametric\\Desktop\\rs\\co.class"));
            stream.write(classWriter.toByteArray());
            stream.close();
            super.writeToBridge(container, msgContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
