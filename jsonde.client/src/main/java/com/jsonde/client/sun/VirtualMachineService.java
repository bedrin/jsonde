package com.jsonde.client.sun;

import com.jsonde.util.file.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

public class VirtualMachineService {

    private URLClassLoader toolsClassLoader;
    private Class virtualMachineClass;

    private static VirtualMachineService instance;

    public static VirtualMachineService getInstance() throws VirtualMachineServiceException {
        if (null == instance) {
            instance = new VirtualMachineService();
        }
        return instance;
    }

    /*static {
        try {
            System.setOut(new PrintStream(new FileOutputStream("C:/out.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    private VirtualMachineService() throws VirtualMachineServiceException {

        try {

            // todo load attach native library
            URL toolsJarURL = getToolsJarURL();

            final String jdkPath = new File(toolsJarURL.toURI()).
                    getParentFile().
                    getParentFile().
                    getAbsolutePath();

            final String jdkJrePath = jdkPath +
                    FileUtils.FILE_SEPARATOR +
                    "jre";

            toolsClassLoader = new URLClassLoader(new URL[]{toolsJarURL}, ClassLoader.getSystemClassLoader()) {

                @Override
                protected String findLibrary(String libname) {
                    //System.out.println("Loading native library " + libname);

                    String libraryFileName;

                    libraryFileName =
                            jdkJrePath +
                            FileUtils.FILE_SEPARATOR +
                            "bin" +
                            FileUtils.FILE_SEPARATOR +
                            System.mapLibraryName(libname);

                    if (new File(libraryFileName).exists()) return libraryFileName;

                    libraryFileName =
                            jdkJrePath +
                            FileUtils.FILE_SEPARATOR +
                            "lib" +
                            FileUtils.FILE_SEPARATOR +
                            "amd64" +
                            FileUtils.FILE_SEPARATOR +
                            System.mapLibraryName(libname);

                    if (new File(libraryFileName).exists()) return libraryFileName;

                    libraryFileName =
                            jdkJrePath +
                            FileUtils.FILE_SEPARATOR +
                            "lib" +
                            FileUtils.FILE_SEPARATOR +
                            "i386" +
                            FileUtils.FILE_SEPARATOR +
                            System.mapLibraryName(libname);

                    if (new File(libraryFileName).exists()) return libraryFileName;

                    return super.findLibrary(libname);

                }

            };

            //System.out.println(toolsClassLoader);

            virtualMachineClass = toolsClassLoader.loadClass("com.sun.tools.attach.VirtualMachine");

            for (Method method : virtualMachineClass.getMethods()) {
                //System.out.println(method);
            }


            //System.out.println(virtualMachineClass);

        } catch (Throwable e) {
            throw new VirtualMachineServiceException(e);
        }

    }

    public boolean isSun16JVM() throws VirtualMachineServiceException {
        return null != getToolsJarURL();
    }

    public URL getToolsJarURL() throws VirtualMachineServiceException {

        try {
            String javaHome = System.getenv("JAVA_HOME");

            File toolsJarFile;

            toolsJarFile = new File(javaHome +
                    FileUtils.FILE_SEPARATOR +
                    "lib" +
                    FileUtils.FILE_SEPARATOR +
                    "tools.jar");

            //System.out.println(toolsJarFile);

            if (toolsJarFile.exists()) {
                //System.out.println("Used 1");
                return toolsJarFile.toURI().toURL();
            }

            toolsJarFile = new File(javaHome +
                    FileUtils.FILE_SEPARATOR +
                    ".." +
                    FileUtils.FILE_SEPARATOR +
                    "lib" +
                    FileUtils.FILE_SEPARATOR +
                    "tools.jar");

            //System.out.println(toolsJarFile);

            if (toolsJarFile.exists()) {
                //System.out.println("Used 2");
                return toolsJarFile.toURI().toURL();
            }

            String jdkHome = System.getenv("JDK_HOME");

            toolsJarFile = new File(jdkHome +
                    FileUtils.FILE_SEPARATOR +
                    "lib" +
                    FileUtils.FILE_SEPARATOR +
                    "tools.jar");

            //System.out.println(toolsJarFile);

            if (toolsJarFile.exists()) {
                //System.out.println("Used 3");
                return toolsJarFile.toURI().toURL();
            }

            javaHome = System.getProperty("java.home");

            toolsJarFile = new File(javaHome +
                    FileUtils.FILE_SEPARATOR +
                    "lib" +
                    FileUtils.FILE_SEPARATOR +
                    "tools.jar");

            //System.out.println(toolsJarFile);

            if (toolsJarFile.exists()) {
                //System.out.println("Used 4");
                return toolsJarFile.toURI().toURL();
            }

            toolsJarFile = new File(javaHome +
                    FileUtils.FILE_SEPARATOR +
                    ".." +
                    FileUtils.FILE_SEPARATOR +
                    "lib" +
                    FileUtils.FILE_SEPARATOR +
                    "tools.jar");

            //System.out.println(toolsJarFile);

            if (toolsJarFile.exists()) {
                //System.out.println("Used 5");
                return toolsJarFile.toURI().toURL();
            }

            return null;

        } catch (MalformedURLException e) {
            throw new VirtualMachineServiceException(e);
        }

    }

    public List<VirtualMachineData> getVirtualMachines() throws VirtualMachineServiceException {

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        List<VirtualMachineData> virtualMachinesData = new LinkedList<VirtualMachineData>();

        try {

            Thread.currentThread().setContextClassLoader(toolsClassLoader);

            Class virtualMachineDescriptorClass = toolsClassLoader.loadClass("com.sun.tools.attach.VirtualMachineDescriptor");

            //System.out.println(virtualMachineDescriptorClass);

            Method virtualMachineDescriptorIdMethod = virtualMachineDescriptorClass.getMethod("id");

            //System.out.println(virtualMachineDescriptorIdMethod);

            Method virtualMachineDescriptorDisplayNameMethod = virtualMachineDescriptorClass.getMethod("displayName");

            //System.out.println(virtualMachineDescriptorDisplayNameMethod);

            Method listMethod = virtualMachineClass.getMethod("list");

            //System.out.println(listMethod);

            List virtualMachines = (List) listMethod.invoke(null);

            //System.out.println(virtualMachines);

            for (Object virtualMachine : virtualMachines) {

                //System.out.println("Virtual Machine found: " + virtualMachine);

                String id = (String) virtualMachineDescriptorIdMethod.invoke(virtualMachine);
                String description = (String) virtualMachineDescriptorDisplayNameMethod.invoke(virtualMachine);
                virtualMachinesData.add(new VirtualMachineData(id, description));
            }

        } catch (IllegalAccessException e) {
            throw new VirtualMachineServiceException(e);
        } catch (InvocationTargetException e) {
            throw new VirtualMachineServiceException(e);
        } catch (NoSuchMethodException e) {
            throw new VirtualMachineServiceException(e);
        } catch (ClassNotFoundException e) {
            throw new VirtualMachineServiceException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }

        return virtualMachinesData;

    }

    public void attachAgent(String virtualMachineId, String agentJar, String agentParameters) throws VirtualMachineServiceException {

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        try {

            Thread.currentThread().setContextClassLoader(toolsClassLoader);

            Method attachMethod = virtualMachineClass.getMethod("attach", String.class);
            Object virtualMachine = attachMethod.invoke(null, virtualMachineId);

            Method loadAgentMethod = virtualMachineClass.getMethod("loadAgent", String.class, String.class);
            loadAgentMethod.invoke(virtualMachine, agentJar, agentParameters);

        } catch (NoSuchMethodException e) {
            throw new VirtualMachineServiceException(e);
        } catch (InvocationTargetException e) {
            throw new VirtualMachineServiceException(e);
        } catch (IllegalAccessException e) {
            throw new VirtualMachineServiceException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }

    }

    public static void main(String[] args) throws Exception {

        VirtualMachineService virtualMachineService = new VirtualMachineService();

        for (VirtualMachineData vmData : virtualMachineService.getVirtualMachines()) {
            //System.out.println(vmData);
        }

    }

}