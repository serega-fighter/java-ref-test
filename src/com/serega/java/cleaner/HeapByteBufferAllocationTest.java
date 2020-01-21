package com.serega.java.cleaner;

import java.nio.ByteBuffer;

public class HeapByteBufferAllocationTest {

    private static volatile Object store;

    public static void main(String[] args) {

        long i = 1;
        long lastTimeMillis = System.currentTimeMillis();
        while (true) {
            store = ByteBuffer.allocate(1024);
            i++;

            if ((i & (1024L * 1024L - 1L)) == 0L) {
                System.out.println(System.currentTimeMillis() - lastTimeMillis);
                lastTimeMillis = System.currentTimeMillis();
            }
        }
    }
}
