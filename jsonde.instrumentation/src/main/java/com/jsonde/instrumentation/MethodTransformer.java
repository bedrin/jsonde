package com.jsonde.instrumentation;

import com.jsonde.profiler.Profiler;
import com.jsonde.util.ClassUtils;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.reflect.Method;

public class MethodTransformer extends AdviceAdapter {

    private final TransformerCallback callback;

    private final long methodId;
    private final long classId;

    private final String className;
    private final String name;
    private final int access;

    public MethodTransformer(
            TransformerCallback callback,
            MethodVisitor mv,
            long methodId,
            long classId,
            int access,
            String name,
            String desc,
            String className,
            String parentClassName) {
        super(Opcodes.ASM5, mv, access, name, desc);
        this.callback = callback;
        this.methodId = methodId;
        this.classId = classId;
        this.access = access;
        this.name = name;
        this.className = className;
    }

    private static final String PROFILER_CLASS_INTERNAL_NAME =
            ClassUtils.getInternalClassName(Profiler.CLASS_CANONICAL_NAME);

    private final Label startFinallyLabel = new Label();

    @Override
    public void visitCode() {
        super.visitCode();

        boolean isConstructor = name.equals(ClassUtils.CONSTRUCTOR_METHOD_NAME);

        if ((ACC_STATIC & methodAccess) == 0 && isConstructor) {
            Method enterMethod = callback.getPreEnterConstructor();
            visitLdcInsn(methodId);
            visitMethodInsn(
                    INVOKESTATIC,
                    ClassUtils.getInternalClassName(enterMethod.getDeclaringClass().getName()),
                    enterMethod.getName(),
                    Type.getMethodDescriptor(enterMethod),
                    false);
        }

    }

    @Override
    protected void onMethodEnter() {
        visitLabel(startFinallyLabel);

        boolean isConstructor = name.equals(ClassUtils.CONSTRUCTOR_METHOD_NAME);
        boolean isStaticConstructor = name.equals(ClassUtils.STATIC_CONSTRUCTOR_METHOD_NAME);

        // todo: now we call this guy for all method but we should think about an improvement:
        Method describeClassMethod = callback.getDescribeClass();
        visitLdcInsn(classId);
        visitLdcInsn(Type.getType("L" + ClassUtils.getInternalClassName(className) + ";"));
        visitMethodInsn(
                INVOKESTATIC,
                ClassUtils.getInternalClassName(describeClassMethod.getDeclaringClass().getName()),
                describeClassMethod.getName(),
                Type.getMethodDescriptor(describeClassMethod),
                false);

        if ((ACC_STATIC & methodAccess) != 0 && isStaticConstructor) {
            /*Method describeClassMethod = callback.getDescribeClass();
            visitLdcInsn(classId);
            visitLdcInsn(Type.getType("L" + ClassUtils.getInternalClassName(className) + ";"));
            visitMethodInsn(
                    INVOKESTATIC,
                    ClassUtils.getInternalClassName(describeClassMethod.getDeclaringClass().getName()),
                    describeClassMethod.getName(),
                    Type.getMethodDescriptor(describeClassMethod),
                    false);*/
        } else if ((ACC_STATIC & methodAccess) == 0 && isConstructor) {
            Method enterConstructorMethod = callback.getEnterConstructor();
            visitLdcInsn(methodId);
            visitVarInsn(ALOAD, 0);
            visitMethodInsn(
                    INVOKESTATIC,
                    ClassUtils.getInternalClassName(enterConstructorMethod.getDeclaringClass().getName()),
                    enterConstructorMethod.getName(),
                    Type.getMethodDescriptor(enterConstructorMethod),
                    false);
        }

        Method enterMethod = callback.getEnterMethod();
        visitLdcInsn(methodId);
        visitMethodInsn(
                INVOKESTATIC,
                ClassUtils.getInternalClassName(enterMethod.getDeclaringClass().getName()),
                enterMethod.getName(),
                Type.getMethodDescriptor(enterMethod),
                false);

    }

    @Override
    protected void onMethodExit(int opcode) {
        if (opcode != ATHROW) {
            onFinally(opcode);
        }
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        Label endFinallyLabel = new Label();
        visitTryCatchBlock(startFinallyLabel, endFinallyLabel, endFinallyLabel, null);
        visitLabel(endFinallyLabel);
        onFinally(ATHROW);
        visitInsn(ATHROW);
        super.visitMaxs(maxStack, maxLocals);
    }

    private void onFinally(int opcode) {
        if (opcode == ATHROW) {
            Method exitMethod = callback.getExitMethodOnException();
            visitInsn(DUP);
            visitMethodInsn(
                    INVOKESTATIC,
                    ClassUtils.getInternalClassName(exitMethod.getDeclaringClass().getName()),
                    exitMethod.getName(),
                    Type.getMethodDescriptor(exitMethod),
                    false
            );

        } else {
            Method exitMethod = callback.getExitMethod();
            visitMethodInsn(
                    INVOKESTATIC,
                    ClassUtils.getInternalClassName(exitMethod.getDeclaringClass().getName()),
                    exitMethod.getName(),
                    Type.getMethodDescriptor(exitMethod),
                    false);
        }
    }

}
