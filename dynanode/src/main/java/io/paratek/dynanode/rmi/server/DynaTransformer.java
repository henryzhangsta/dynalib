package io.paratek.dynanode.rmi.server;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class DynaTransformer implements java.lang.instrument.ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.length() <= 3 || className.equals("client")) {
            DynaActionSupplier.classData.put(className, classfileBuffer);
        }
        return classfileBuffer;
    }

}
