package com.jsonde.agent;

import junit.framework.TestCase;
import org.objectweb.asm.ClassReader;

public class ResolveAgentLibrariesClassLoaderTest extends TestCase {

    public void testResolveAgentLibrariesClassLoader() throws Exception {

        ResolveAgentLibrariesClassLoader cl = new ResolveAgentLibrariesClassLoader();

        Class class1 = cl.loadClass(ClassReader.class.getName());
        Class class2 = ClassReader.class;

        assertNotSame(class2, class1);
        assertNotSame(class2.getClassLoader(), class1.getClassLoader());
        assertNotSame(System.identityHashCode(class2), System.identityHashCode(class1));

    }

}
