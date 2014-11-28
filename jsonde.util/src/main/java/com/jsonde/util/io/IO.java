package com.jsonde.util.io;

import com.jsonde.util.log.Log;

import java.io.Closeable;
import java.io.IOException;

public class IO {

    private static final Log log = Log.getLog(IO.class);

    public static void close(Closeable closeable) {

        final String METHOD_NAME = "close(Closeable)";

        if (null == closeable)
            return;

        try {
            closeable.close();
        } catch (IOException e) {
            log.error(METHOD_NAME, e);
        }

    }

    /*public static void close(ObjectInput objectInput) {

        final String METHOD_NAME = "close(ObjectInput)";

        if (null == objectInput)
            return;

        try {
            objectInput.close();
        } catch (IOException e) {
            log.error(METHOD_NAME, e);
        }

    }*/

}
