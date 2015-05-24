package com.jsonde.instrumentation;

import com.jsonde.util.ClassUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class ByteCodeTransformer implements ClassFileTransformer {

    private final static String transformerPackage = ByteCodeTransformer.class.getPackage().getName();

    private final TransformerCallback callback;

    public ByteCodeTransformer(TransformerCallback configuration) {
        this.callback = configuration;
    }

    public ByteCodeTransformer() {
        this(new TransformerCallback());
    }

    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {

        className = ClassUtils.getFullyQualifiedName(className);

        if ((className.startsWith(transformerPackage) && (!className.startsWith("com.jsonde.instrumentation.samples"))) ||
                className.startsWith("org.objectweb.asm")) {
            // DO NOT transform objectweb ASM and jSONDE itself !!!11
            return classfileBuffer;
        }

        if (callback.shouldTransformClass(loader, className, classBeingRedefined, protectionDomain)) {
            return doTransform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        } else {
            return classfileBuffer;
        }

    }

    private byte[] doTransform(
            final ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {

        ClassReader classReader = new ClassReader(classfileBuffer, 0, classfileBuffer.length);

        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES) {

            @Override
            protected String getCommonSuperClass(String type1, String type2) {
                Class<?> c, d;
                ClassLoader classLoader = getClass().getClassLoader();
                try {
                    c = Class.forName(type1.replace('/', '.'), false, classLoader);
                } catch (ClassNotFoundException e) {
                    try {
                        c = Class.forName(type1.replace('/', '.'), false, loader);
                    } catch (ClassNotFoundException e2) {
                        throw new RuntimeException(e.toString());
                    }
                }
                try {
                    d = Class.forName(type2.replace('/', '.'), false, classLoader);
                } catch (ClassNotFoundException e) {
                    try {
                        d = Class.forName(type2.replace('/', '.'), false, loader);
                    } catch (ClassNotFoundException e2) {
                        throw new RuntimeException(e.toString());
                    }
                }
                if (c.isAssignableFrom(d)) {
                    return type1;
                }
                if (d.isAssignableFrom(c)) {
                    return type2;
                }
                if (c.isInterface() || d.isInterface()) {
                    return "java/lang/Object";
                } else {
                    do {
                        c = c.getSuperclass();
                    } while (!c.isAssignableFrom(d));
                    return c.getName().replace('.', '/');
                }
            }
        };

        ClassVisitor classVisitor;
        classVisitor = new ClassTransformer(callback, classWriter, loader, className, classBeingRedefined, protectionDomain);

        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

        byte[] bytes = classWriter.toByteArray();

        // Uncomment code below to debug

        /*classReader.accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(
                System.out)), 0);*/

        /*

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        CheckClassAdapter.verify(new ClassReader(bytes), false, pw);
        if (!sw.toString().isEmpty()) {
            System.out.println(sw.toString());
        }*/


        return bytes;

    }

}
