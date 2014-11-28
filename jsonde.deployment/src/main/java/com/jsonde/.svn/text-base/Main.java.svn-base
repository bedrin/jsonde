package com.jsonde;

import com.jsonde.util.file.FileUtils;
import com.jsonde.util.io.IO;

import javax.swing.*;
import java.io.*;

public class Main {

    private final static String JAVA_BIN;
    private final static String JSONDE_OUT;

    static {

        String jSondeOut = FileUtils.USER_HOME +
                FileUtils.FILE_SEPARATOR +
                ".jsonde" +
                FileUtils.FILE_SEPARATOR +
                "jsonde.out";

        try {
            jSondeOut = FileUtils.canonizePath(
                    jSondeOut);
        } catch (IOException e) {
            processException(e);
        }

        JSONDE_OUT = jSondeOut;

        String javaBin = System.getProperty("java.home") +
                FileUtils.FILE_SEPARATOR +
                "bin" +
                FileUtils.FILE_SEPARATOR +
                "java";

        try {
            javaBin = FileUtils.canonizePath(javaBin);
        } catch (IOException e) {
            processException(e);
        }

        JAVA_BIN = javaBin;

    }

    public static void main(String... arguments) {
        try {
            executionInNewVM(arguments);
        } catch (Exception e) {
            processException(e);
        }
    }

    private static void processException(Exception e) {
        e.printStackTrace(System.err);
        JOptionPane.showMessageDialog(null,e.getMessage(), "jSonde Error", JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("unused")
    private static void executeInSameVM(String... arguments) throws Exception {
        com.jsonde.gui.Main.main(arguments);
    }

    @SuppressWarnings("unused")
    private static void executionInNewVM(String... arguments) throws Exception {

        String guiJarName = new File(
                com.jsonde.gui.Main.class.
                        getProtectionDomain().getCodeSource().
                        getLocation().toURI()
        ).getName();

        String[] javaArguments = new String[3 + arguments.length];
        javaArguments[0] = JAVA_BIN;
        javaArguments[1] = "-jar";
        javaArguments[2] = "lib" + FileUtils.FILE_SEPARATOR + guiJarName;
        System.arraycopy(arguments,0,javaArguments,3,arguments.length);

        ProcessBuilder processBuilder = new ProcessBuilder(javaArguments);

        processBuilder.directory(
                new File(".").getAbsoluteFile()
        );

        processBuilder.redirectErrorStream(true);

        final Process process = processBuilder.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            public void run() {
                process.destroy();
            }

        }));

        File jSondeOutFile = new File(JSONDE_OUT);
        FileUtils.createFile(jSondeOutFile);
        OutputStream outputStream = new FileOutputStream(jSondeOutFile);

        redirectProcessStreams(process, outputStream);

    }

    private static void redirectProcessStreams(final Process process, final OutputStream outputStream) {
        new Thread(new Runnable() {

            public void run() {
                InputStream is = process.getInputStream();
                try {
                    for (int i = is.read(); i != -1; i = is.read()) {
                        outputStream.write((byte)i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IO.close(outputStream);
                }
            }


        }).start();
    }

}
