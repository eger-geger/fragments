package com.astound.fragments;

public class FragmentSettings {

    private long implicitWait = 500;

    private static FragmentSettings instance;

    public static FragmentSettings getInstance() {
        if (instance == null) {
            instance = new FragmentSettings();
        }
        return instance;
    }

    public void setImplicitWait(long implicitWait) {
        this.implicitWait = implicitWait;
    }

    public long getImplicitWait() {
        return implicitWait;
    }
}
