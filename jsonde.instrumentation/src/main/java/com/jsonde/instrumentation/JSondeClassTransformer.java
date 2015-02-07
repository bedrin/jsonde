package com.jsonde.instrumentation;

import com.jsonde.profiler.Profiler;
import com.jsonde.util.ClassUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class JSondeClassTransformer extends ClassVisitor {

    private String className;
    private String parentClassName;

    private boolean instrumentClass;
    private ClassLoader classLoader;
    private Class<?> classBeingRedefined;

    private long classId;

    public JSondeClassTransformer(ClassVisitor classVisitor, boolean instrumentClass, ClassLoader classLoader, Class<?> classBeingRedefined) {
        super(Opcodes.ASM4, classVisitor);
        this.instrumentClass = instrumentClass;
        this.classLoader = classLoader;
        this.classBeingRedefined = classBeingRedefined;
    }

    @Override
    public void visit(
            int version,
            int access,
            String name,
            String signature,
            String superName,
            String[] interfaces) {

        className = ClassUtils.getFullyQualifiedName(name);

        parentClassName = superName;

        classId = Profiler.getProfiler().registerClass(
                version,
                access,
                name,
                signature,
                superName,
                interfaces,
                classLoader
        );

        if (null != classBeingRedefined) {

            Profiler.getProfiler().describeRedefinableClass(
                    classId,
                    classBeingRedefined
            );

        }

        super.visit(version, access, name, signature, superName, interfaces);

    }

    @Override
    public MethodVisitor visitMethod(
            int access,
            String name,
            String desc,
            String signature,
            String[] exceptions) {

        long methodId = Profiler.getProfiler().registerMethod(
                classId, access, name, desc, signature, exceptions
        );

        MethodVisitor methodVisitor = super.visitMethod(
                access,
                name,
                desc,
                signature,
                exceptions
        );

        if (
                ("loadClass".equals(name) &&
                        ("(Ljava/lang/String;)Ljava/lang/Class;".equals(desc) ||
                                "(Ljava/lang/String;Z)Ljava/lang/Class;".equals(desc))
                )
                ) {

            if (0 == (access & Opcodes.ACC_STATIC)) {
                methodVisitor = new JSondeClassLoaderMethodTransformer(methodVisitor);
            }

        }

        if (instrumentClass) {
            methodVisitor = new JSondeMethodTransformer(
                    methodId,
                    methodVisitor,
                    access,
                    name,
                    desc,
                    className,
                    parentClassName
            );
        }

        return methodVisitor;

    }

}
