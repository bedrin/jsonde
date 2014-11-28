package com.jsonde.samples;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Application3 extends URLClassLoader {

    public Application3() {
        super(new URL[]{Application3.class.getProtectionDomain().getCodeSource().getLocation()});
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (name.startsWith("com.jsonde")) {
            if (name.contains(".samples.")) {
                Class clazz = findClass(name);
                if (resolve) {
                    resolveClass(clazz);
                }
                return clazz;
            } else {
                throw new ClassNotFoundException();
            }
        } else {
            return super.loadClass(name, resolve);
        }
    }

    public static void main(String... arguments) throws Exception {
        ClassLoader customClassLoader = new Application3();
        Thread.currentThread().setContextClassLoader(customClassLoader);

        Class application1Class =
                customClassLoader.loadClass("com.jsonde.samples.Application1");

        Method method1 = application1Class.getMethod("method1", null);
        method1.invoke(null);

    }

}