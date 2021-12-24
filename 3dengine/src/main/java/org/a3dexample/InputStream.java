package org.a3dexample;

import java.io.Closeable;
import java.io.IOException;

public abstract class InputStream implements Closeable {
    public InputStream() {
        throw new RuntimeException("Stub!");
    }

    public abstract int read() throws IOException;

    public int read(byte[] b) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public int read(byte[] b, int off, int len) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public long skip(long n) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public int available() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public synchronized void mark(int readlimit) {
        throw new RuntimeException("Stub!");
    }

    public synchronized void reset() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public boolean markSupported() {
        throw new RuntimeException("Stub!");
    }
}

