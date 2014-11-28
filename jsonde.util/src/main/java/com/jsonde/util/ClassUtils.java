package com.jsonde.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils {

    public static final String CONSTRUCTOR_METHOD_NAME = "<init>";
    public static final String STATIC_CONSTRUCTOR_METHOD_NAME = "<clinit>";
    private static final String EMPTY_STRING = "";

    public static String getFullyQualifiedName(String className) {
        if (null == className) return EMPTY_STRING;
        return className.replace('/', '.');
    }

    public static String getInternalClassName(String className) {
        if (null == className) return EMPTY_STRING;
        return className.replace('.', '/');
    }

    public static String convertClassNameToResourceName(String className) {
        if (null == className) return EMPTY_STRING;
        return className.replace('.', '/') + ".class";
    }

    public static Set<String> getPackagesFromClassPath() throws IOException {

        String classPath = System.getProperty("java.class.path");
        String pathSeparator = System.getProperty("path.separator");

        Set<String> packages = new HashSet<String>();

        for (String classPathElement : classPath.split(pathSeparator)) {
            System.out.println(classPathElement);

            File classPathFile = new File(classPathElement);

            if (classPathFile.exists()) {

                if (classPathFile.isDirectory()) {
                    packages.addAll(getPackagesFromDirectory(classPathFile));
                } else {

                    JarFile jarFile = new JarFile(classPathFile, false);

                    Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();

                    while (jarEntryEnumeration.hasMoreElements()) {

                        JarEntry jarEntry = jarEntryEnumeration.nextElement();


                        String jarEntryName = jarEntry.getName();

                        if (jarEntryName.endsWith(".class")) {

                            if (jarEntryName.contains("/")) {

                                String packageName =
                                        jarEntryName.
                                                substring(0, jarEntryName.lastIndexOf("/")).
                                                replaceAll("/", ".");

                                packages.add(packageName);

                            } else {

                                String packageName =
                                        jarEntryName.
                                                substring(0, jarEntryName.length() - ".class".length());

                                packages.add(packageName);


                            }

                        }

                    }

                }

            }

        }

        return packages;

    }

    private static Set<String> getPackagesFromDirectory(File directory) {
        return getPackagesFromDirectory(directory, directory);
    }

    private static Set<String> getPackagesFromDirectory(File rootDirectory, File directory) {

        Set<String> packages = new HashSet<String>();

        String rootDirectoryFileName = rootDirectory.getAbsolutePath();

        for (File file : directory.listFiles()) {

            if (file.isDirectory()) {
                packages.addAll(getPackagesFromDirectory(rootDirectory, file));
            } else if (file.getName().endsWith(".class")) {

                String directoryFileName = directory.getAbsolutePath();

                String packageName =
                        directoryFileName.
                                substring(rootDirectoryFileName.length() + 1).
                                replaceAll("/", ".");

                packages.add(packageName);

            }

        }

        return packages;

    }

}
