package com.jsonde.instrumentation;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class JSondeClassLoaderMethodTransformer extends MethodVisitor implements Opcodes {

    public JSondeClassLoaderMethodTransformer(MethodVisitor methodVisitor) {
        super(ASM4, methodVisitor);
    }

    @Override
    public void visitCode() {
        super.visitCode();

        mv.visitVarInsn(ALOAD, 1);
        mv.visitLdcInsn("com.jsonde");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z");
        Label l0 = new Label();
        mv.visitJumpInsn(IFEQ, l0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitLdcInsn(".samples.");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z");
        mv.visitJumpInsn(IFNE, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitTypeInsn(INSTANCEOF, "java/lang/ClassLoader");
        mv.visitJumpInsn(IFEQ, l0);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;");
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");
        mv.visitInsn(ARETURN);
        mv.visitLabel(l0);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
    }

}
