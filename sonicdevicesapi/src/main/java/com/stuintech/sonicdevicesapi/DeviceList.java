package com.stuintech.sonicdevicesapi;

import java.util.ArrayList;

public class DeviceList {
    public static final int SCREWDRIVER = 0;
    public static final int ADVANCEDSCREWDRIVER = 1;
    public static final int BLASTER = 2;

    public static final int ACTIVATE = 1;
    public static final int DEACTIVATE = 2;
    public static final int SCAN = 4;

    public static int[] maxLevel = new int[] {
            3,4,1
    };

    public static final ArrayList<IDevice>[] allDevices = new ArrayList[3];
    public static final ArrayList<IAction>[][] allActions = new ArrayList[3][5];


    static {
        for(int i = 0; i < allActions.length; i++) {
            allDevices[i] = new ArrayList<>();
            for(int j = 0; j < allActions[i].length; j++)
                allActions[i][j] = new ArrayList<>();
        }
    }
}
