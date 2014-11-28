package com.jsonde.util.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    private final Class clazz;
    private final String className;
    private final Logger logger;

    public Log(Class clazz) {
        this.clazz = clazz;
        this.className = clazz.getName();
        logger = Logger.getLogger(clazz.getName());
        logger.setLevel(Level.SEVERE);
    }

    public static Log getLog(Class clazz) {
        return new Log(clazz);
    }

    public void entering(String methodName, Object... arguments) {
        logger.entering(className, methodName, arguments);
    }

    public void exiting(String methodName) {
        logger.exiting(className, methodName);
    }

    public void exiting(String methodName, Object returnValue) {
        logger.exiting(className, methodName, returnValue);
    }

    public void throwing(String methodName, Throwable exception) {
        logger.throwing(className, methodName, exception);
    }

    public void error(String methodName, Throwable exception) {
        logger.logp(Level.SEVERE, className, methodName, exception.getMessage(), exception);
    }

    public void trace(String methodName, Throwable exception) {
        logger.logp(Level.FINER, className, methodName, exception.getMessage(), exception);
    }

    public void trace(String methodName, String message) {
        logger.logp(Level.FINER, className, methodName, message);
    }

    public void info(String message) {
        logger.info(message);
    }

    public boolean isTraceEnabled() {
//        return true;
        return false;
    }

}
