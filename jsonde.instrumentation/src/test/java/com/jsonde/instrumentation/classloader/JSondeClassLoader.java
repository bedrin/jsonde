package com.jsonde.instrumentation.classloader;

import com.jsonde.instrumentation.ByteCodeTransformer;
import com.jsonde.instrumentation.TransformerCallback;
import com.jsonde.util.ClassUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;

public class JSondeClassLoader extends ClassLoader {

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        Class clazz;

        try {
            clazz = findClass(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return super.loadClass(name, resolve);
        } catch (ClassFormatError e) {
            e.printStackTrace();
            return super.loadClass(name, resolve);
        }

        if (null == clazz) {
            return super.loadClass(name, resolve);
        } else {
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }

    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {

        if (name.startsWith("java.")) return null;
        if ((name.startsWith("com.jsonde")) && (!name.startsWith("com.jsonde.instrumentation.samples")))
            return null;

        ClassLoader parentClassLoader = getParentOrSystemClassLoader();

        InputStream byteCodeInputStream = null;
        byte[] transformedByteArray;

        try {
            byteCodeInputStream = parentClassLoader.getResourceAsStream(
                    ClassUtils.convertClassNameToResourceName(name));
            transformedByteArray = transform(name, byteCodeInputStream, true);
        } catch (IllegalClassFormatException e) {
            throw new ClassNotFoundException("Error while instrumenting class " + name, e);
        } finally {
            if (null != byteCodeInputStream) {
                try {
                    byteCodeInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace(); // todo refactor this line
                }
            }
        }

        return defineClass(name, transformedByteArray, 0, transformedByteArray.length);
    }

    private ClassLoader getParentOrSystemClassLoader() {

        ClassLoader parentClassLoader = getParent();

        if (null == parentClassLoader) {
            return getSystemClassLoader();
        } else {
            return parentClassLoader;
        }

    }

    public byte[] transform(String name, InputStream inputStream, boolean instrumentClass) throws IllegalClassFormatException {

        ByteArrayOutputStream originalByteArrayOutputStream = new ByteArrayOutputStream();

        try {

            while (inputStream.available() > 0) {
                originalByteArrayOutputStream.write(inputStream.read());
            }

            ByteCodeTransformer byteCodeTransformer = new ByteCodeTransformer(new TransformerCallback());

            return byteCodeTransformer.transform(this, name, null, null, originalByteArrayOutputStream.toByteArray());

        } catch (IOException e) {
            throw new IllegalClassFormatException(e.getMessage());
        } finally {
            try {
                originalByteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace(); // todo refactor this line
            }
        }

    }


}
