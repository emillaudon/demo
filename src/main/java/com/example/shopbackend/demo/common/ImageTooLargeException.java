package com.example.shopbackend.demo.common;

public class ImageTooLargeException extends RuntimeException{
    private long maxBytes;
    private long actualBytes;

    public ImageTooLargeException(long maxBytes, long actualBytes) {
        super("File size too large, maximum size is: " + maxBytes + " but was: " + actualBytes);
        this.maxBytes = maxBytes;
        this.actualBytes = actualBytes;
    }

    public long getMaxBytes() {
        return maxBytes;
    }

    public long getActualBytes() {
        return actualBytes;
    }
}
