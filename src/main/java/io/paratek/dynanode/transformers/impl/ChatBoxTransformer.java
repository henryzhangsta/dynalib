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
                    list.add(new VarInsnNode(Opcodes.ILOAD, 0)); // type
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1)); // sender
                    list.add(new VarInsnNode(Opcodes.ALOAD, 2)); // message
                    list.add(new VarInsnNode(Opcodes.ALOAD, 3)); // clan
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            "io/paratek/dynanode/server/DynaCallbackService",
                            "onChatBoxUpdate",
                            "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false));
                    methodNode.instructions.insert(list);
                }
            }
            super.writeToBridge(container, msgContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
