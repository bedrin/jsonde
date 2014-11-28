package com.jsonde.api.serialization;

import java.io.IOException;
import java.io.ObjectInput;

public abstract class ObjectInputDecorator implements ObjectInput {

    private final ObjectInput target;

    protected ObjectInputDecorator(ObjectInput target) {
        this.target = target;
    }

    public void readFully(byte[] b) throws IOException {
        target.readFully(b);
    }

    public Object readObject() throws ClassNotFoundException, IOException {
        return target.readObject();
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        target.readFully(b, off, len);
    }

    public int read() throws IOException {
        return target.read();
    }

    public int skipBytes(int n) throws IOException {
        return target.skipBytes(n);
    }

    public int read(byte[] b) throws IOException {
        return target.read(b);
    }

    public boolean readBoolean() throws IOException {
        return target.readBoolean();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return target.read(b, off, len);
    }

    public byte readByte() throws IOException {
        return target.readByte();
    }

    public long skip(long n) throws IOException {
        return target.skip(n);
    }

    public int readUnsignedByte() throws IOException {
        return target.readUnsignedByte();
    }

    public int available() throws IOException {
        return target.available();
    }

    public short readShort() throws IOException {
        return target.readShort();
    }

    public void close() throws IOException {
        target.close();
    }

    public int readUnsignedShort() throws IOException {
        return target.readUnsignedShort();
    }

    public char readChar() throws IOException {
        return target.readChar();
    }

    public int readInt() throws IOException {
        return target.readInt();
    }

    public long readLong() throws IOException {
        return target.readLong();
    }

    public float readFloat() throws IOException {
        return target.readFloat();
    }

    public double readDouble() throws IOException {
        return target.readDouble();
    }

    public String readLine() throws IOException {
        return target.readLine();
    }

    public String readUTF() throws IOException {
        return target.readUTF();
    }

}
