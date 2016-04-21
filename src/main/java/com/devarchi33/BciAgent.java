package com.devarchi33;

import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * Created by donghoon on 2016. 4. 22..
 */
public class BciAgent implements ClassFileTransformer {

    public BciAgent() {
        super();
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        instrumentation.addTransformer(new BciAgent());
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String[] ignore = new String[]{"java/", "sun/", "javax"};
        for (int i = 0; i < ignore.length; i++) {
            if (className.startsWith(ignore[i])) {
                return classfileBuffer;
            }
        }
        return transformClass(classBeingRedefined, classfileBuffer);
    }

    private byte[] transformClass(Class classToTransform, byte[] b) {
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
            if (cl.isInterface() == false) {
                CtBehavior[] methods = cl.getDeclaredBehaviors();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].isEmpty() == false) {
                        doTransform(methods[i]);
                    }
                }
            }
            b = cl.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return b;
    }

    private void doTransform(CtBehavior method) throws NotFoundException, CannotCompileException {
        if (method.getName().equals("doSomething")) {
            method.insertBefore("System.out.println(\"started method at\" + new java.util.Date());");
            method.insertAfter("System.out.println(\"ended method at\" + new java.util.Date();)");
        }
    }
}
