package com.jsonde.api.serialization;

import java.io.IOException;
import java.io.ObjectOutput;

public abstract class ObjectOutputDecorator implements ObjectOutput {

    private final ObjectOutput target;

    protected ObjectOutputDecorator(ObjectOutput target) {
        this.target = target;
    }

    public void writeObject(Object obj) throws IOException {
        target.writeObject(obj);
    }

    public void write(int b) throws IOException {
        target.write(b);
    }

    public void write(byte[] b) throws IOException {
        target.write(b);
    }

    public void writeBoolean(boolean v) throws IOException {
        target.writeBoolean(v);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        target.write(b, off, len);
    }

    public void writeByte(int v) throws IOException {
        target.writeByte(v);
    }

    public void flush() throws IOException {
        target.flush();
    }

    public void writeShort(int v) throws IOException {
        target.writeShort(v);
    }

    public void close() throws IOException {
        target.close();
    }

    public void writeChar(int v) throws IOException {
        target.writeChar(v);
    }

    public void writeInt(int v) throws IOException {
        target.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        target.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        target.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        target.writeDouble(v);
    }

    public void writeBytes(String s) throws IOException {
        target.writeBytes(s);
    }

    public void writeChars(String s) throws IOException {
        target.writeChars(s);
    }

    public void writeUTF(String s) throws IOException {
        target.writeUTF(s);
    }

}
