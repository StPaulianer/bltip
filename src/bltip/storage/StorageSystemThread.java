/*
 * Created on 20.10.2004
 */
package bltip.storage;

import bltip.common.BlTipException;

import java.io.File;

/**
 * @author Nico
 * @version 20.10.2004 todo
 */
public class StorageSystemThread extends Thread {
    public static final int EXPORT_USERTABLE = 0;
    public static final int EXPORT_TIPTABLES = 1;
    public static final int DB_FILL = 6;
    private static final int RESULT_SUBMISSION = 2;
    private static final int BLTABLE = 3;
    private static final int USERTABLE = 4;
    private static final int DB_CREATION = 5;
    private final StorageSystem storesys;
    private final int mode;
    private final Object[] obj;

    public StorageSystemThread(StorageSystem store, int mode, Object[] obj) {
        this.storesys = store;
        this.mode = mode;
        this.obj = obj;
    }

    public void run() {
        try {
            switch (mode) {
                case EXPORT_USERTABLE:
                    storesys.printUserTableInHTML((File) obj[0]);
                    break;
                case EXPORT_TIPTABLES:
                    storesys.printAllTiptables((File) obj[0]);
                    break;
                case RESULT_SUBMISSION:
                    break;
                case BLTABLE:
                    break;
                case USERTABLE:
                    break;
                case DB_CREATION:
                    break;
                case DB_FILL:
                    storesys.initialize((File) obj[0], (File) obj[1], (File) obj[2]);
                    break;
            }
        } catch (BlTipException e) {
            // todo ich m�chte die hier nicht fangen, sondern eigentlich im
            // Mainframe ausgeben
            e.printStackTrace();
        }
    }

}
