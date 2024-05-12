package com.example.cheapy;


public class isReturn {
    private static isReturn instance;
    private boolean isReturn;

    private isReturn() {
        isReturn = false;
    }

    public static synchronized isReturn getInstance() {
        if (instance == null) {
            instance = new isReturn();
        }
        return instance;
    }

    public boolean getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(boolean isReturn) {
        this.isReturn=isReturn;
    }
}

