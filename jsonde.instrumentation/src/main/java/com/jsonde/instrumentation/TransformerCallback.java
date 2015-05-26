package com.jsonde.instrumentation;

import com.jsonde.profiler.Profiler;

import java.lang.reflect.Method;
import java.security.ProtectionDomain;

public class TransformerCallback {

    public boolean shouldTransformClass(ClassLoader loader,
                                        String className,
                                        Class<?> classBeingRedefined,
                                        ProtectionDomain protectionDomain) {
        return true;
    }

    public boolean shouldTransformClass(int version,
                                        int access,
                                        String name,
                                        String signature,
                                        String superName,
                                        String[] interfaces,
                                        ClassLoader classLoader,
                                        Class<?> classBeingRedefined,
                                        ProtectionDomain protectionDomain) {
        return true;
    }

    public long getClassId(int version,
                           int access,
                           String name,
                           String signature,
                           String superName,
                           String[] interfaces,
                           ClassLoader classLoader,
                           Class<?> classBeingRedefined,
                           ProtectionDomain protectionDomain) {
        return Profiler.getProfiler().registerClass(version, access, name, signature, superName, interfaces, classLoader);
    }

    public boolean shouldTransformMethod(
            long classId,
            int access,
            String name,
            String desc,
            String signature,
            String[] exceptions,
            String className,
            String parentClassName) {
        return true;
    }

    public long getMethodId(long classId,
                            int access,
                            String name,
                            String desc,
                            String signature,
                            String[] exceptions,
                            String className,
                            String parentClassName) {
        return Profiler.getProfiler().registerMethod(classId, access, name, desc, signature, exceptions);
    }

    public Method getEnterMethod() {
        try {
            return Profiler.class.getMethod("enterMethod", Long.TYPE);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Method getEnterConstructor() {
        try {
            return Profiler.class.getMethod("enterConstructor", long.class, Object.class);
        } catch (NoSuchMethodException e) {
            for (Method m : Profiler.class.getDeclaredMethods()) {
                System.out.println(m);
            }
            throw new RuntimeException(e);
        }
    }

    public Method getPreEnterConstructor() {
        try {
            return Profiler.class.getMethod("preEnterConstructor", long.class);
        } catch (NoSuchMethodException e) {
            for (Method m : Profiler.class.getDeclaredMethods()) {
                System.out.println(m);
            }
            throw new RuntimeException(e);
        }
    }

    public Method getDescribeClass() {
        try {
            return Profiler.class.getMethod("describeClass", long.class, Class.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Method getExitMethod() {
        try {
            return Profiler.class.getMethod("leaveMethod");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Method getExitMethodOnException() {
        try {
            return Profiler.class.getMethod("leaveMethod", Throwable.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
