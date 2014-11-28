package com.jsonde.agent;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

public class ResolveAgentLibrariesClassLoader extends URLClassLoader {

    private static String jSondeLibrariesRegexp;

    public ResolveAgentLibrariesClassLoader() {
        super(getUrls(), null);
    }

    private static URL[] getUrls() {

        try {

            URL agentJarLocation = ResolveAgentLibrariesClassLoader.class.getProtectionDomain().getCodeSource().getLocation();

            URI agentJarURI = agentJarLocation.toURI();

            JarFile agentJarFile = new JarFile(agentJarURI.getPath());

            Manifest agentManifest = agentJarFile.getManifest();

            Attributes agentManifestAttributes = agentManifest.getMainAttributes();

            String jSondeClassPath = agentManifestAttributes.getValue("JSonde-Class-Path");
            jSondeLibrariesRegexp = agentManifestAttributes.getValue("JSonde-Libraries-Regexp");

            List<URL> urls = new LinkedList<URL>();

            for (String jar : jSondeClassPath.split("\\s")) {
                try {
                    URL o = new URL(agentJarLocation, jar);

                    urls.add(o);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            return urls.toArray(new URL[urls.size()]);

        } catch (IOException e) {
            e.printStackTrace();
            return new URL[]{};
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new URL[]{};
        }

    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (Pattern.matches(jSondeLibrariesRegexp, name)) {
            return super.findClass(name);
        } else {
            return findSystemClass(name);
        }
    }

}
