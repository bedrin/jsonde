package com.jsonde.api.methodCall;

import com.jsonde.api.Message;

import java.util.Arrays;

public class RegisterClassMessage extends Message {

    private long classId;
    private int version;
    private int access;
    private String name;
    private String signature;
    private String superName;
    private String[] interfaces;

    public RegisterClassMessage() {
    }

    public RegisterClassMessage(long classId, String name) {
        this.classId = classId;
        this.name = name;
    }

    public RegisterClassMessage(
            long classId,
            int version,
            int access,
            String name,
            String signature,
            String superName,
            String[] interfaces) {
        this.classId = classId;
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.superName = superName;
        this.interfaces = interfaces;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSuperName() {
        return superName;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String[] interfaces) {
        this.interfaces = interfaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisterClassMessage that = (RegisterClassMessage) o;

        if (access != that.access) return false;
        if (classId != that.classId) return false;
        if (version != that.version) return false;
        if (!Arrays.equals(interfaces, that.interfaces)) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (signature != null ? !signature.equals(that.signature) : that.signature != null) return false;
        if (superName != null ? !superName.equals(that.superName) : that.superName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (classId ^ (classId >>> 32));
        result = 31 * result + version;
        result = 31 * result + access;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (signature != null ? signature.hashCode() : 0);
        result = 31 * result + (superName != null ? superName.hashCode() : 0);
        result = 31 * result + (interfaces != null ? Arrays.hashCode(interfaces) : 0);
        return result;
    }
}
