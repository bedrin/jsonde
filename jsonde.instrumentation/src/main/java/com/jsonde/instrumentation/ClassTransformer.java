package com.jsonde.instrumentation;

import com.jsonde.util.ClassUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.lang.reflect.Method;
import java.security.ProtectionDomain;

public class ClassTransformer extends ClassVisitor {

    private final TransformerCallback callback;

    private final ClassLoader classLoader;
    private final Class<?> classBeingRedefined;
    private final ProtectionDomain protectionDomain;

    private final String className;

    private String parentClassName;

    private boolean shouldTransform;
    private long classId;

    private boolean staticConstructorExists;

    public ClassTransformer(
            TransformerCallback callback,
            ClassVisitor classVisitor,
            ClassLoader classLoader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain) {
        super(Opcodes.ASM5, classVisitor);
        this.callback = callback;
        this.classLoader = classLoader;
        this.classBeingRedefined = classBeingRedefined;
        this.protectionDomain = protectionDomain;
        this.className = className;
    }

    @Override
    public void visit(
            int version,
            int access,
            String name,
            String signature,
            String superName,
            String[] interfaces) {

        parentClassName = superName;

        if (shouldTransform = callback.shouldTransformClass(
                version,
                access,
                name,
                signature,
                superName,
                interfaces,
                classLoader,
                classBeingRedefined,
                protectionDomain
        )) classId = callback.getClassId(
                version,
                access,
                name,
                signature,
                superName,
                interfaces,
                classLoader,
                classBeingRedefined,
                protectionDomain
        );

        super.visit(version, access, name, signature, superName, interfaces);

    }

    @Override
    public MethodVisitor visitMethod(
            int access,
            String name,
            String desc,
            String signature,
            String[] exceptions) {

        MethodVisitor methodVisitor = super.visitMethod(
                access,
                name,
                desc,
                signature,
                exceptions
        );

        if (shouldTransform && callback.shouldTransformMethod(
                classId, access, name, desc, signature, exceptions, className, parentClassName
        )) {

            if (ClassUtils.STATIC_CONSTRUCTOR_METHOD_NAME.equals(name)) {
                staticConstructorExists = true;
            }

            long methodId = callback.getMethodId(
                    classId, access, name, desc, signature, exceptions, className, parentClassName
            );

            methodVisitor = new MethodTransformer(
                    callback,
                    methodVisitor,
                    methodId,
                    classId,
                    access,
                    name,
                    desc,
                    className,
                    parentClassName
            );

        }

        return methodVisitor;

    }

    @Override
    public void visitEnd() {
        if (!staticConstructorExists) {
            // TODO: does NOT work for redefinition
            /*GeneratorAdapter mv = new GeneratorAdapter(
                    super.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null),
                    0, "<clinit>", "()V"
            );
            mv.visitCode();
            Method describeClassMethod = callback.getDescribeClass();
            mv.visitLdcInsn(classId);
            mv.visitLdcInsn(Type.getType("L" + ClassUtils.getInternalClassName(className) + ";"));
            mv.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    ClassUtils.getInternalClassName(describeClassMethod.getDeclaringClass().getName()),
                    describeClassMethod.getName(),
                    Type.getMethodDescriptor(describeClassMethod),
                    false);
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(3, 0);
            mv.visitEnd();*/
        }
    }
}