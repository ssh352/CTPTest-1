package com.xclz.lhz.ctptest;

public class Globals {
    public static final int FRONT_CONNECT = 0x001;
    public static final int DMDATA_RECEIVED = 0x002;
    public static final int SMDATA_RECEIVED = 0x003;
    public static final int FRONT_DISCONNECT = 0x004;
    
    private static int mRequestId;
    public static int getRequestId() {
        return mRequestId++;
    }
}
