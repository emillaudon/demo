package com.example.shopbackend.demo.common;

public class MaxUploadSizeException extends RuntimeException{
    private long maxBytes;
    private long actualBytes;

    public MaxUploadSizeException(long maxBytes, long actualBytes) {
        super("Upload size above limit, maximum size is: " + maxBytes + " but is: " + actualBytes);
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
