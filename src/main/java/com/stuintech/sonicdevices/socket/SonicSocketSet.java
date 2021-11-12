package com.stuintech.sonicdevices.socket;

import com.stuintech.socketwrench.socket.ExtensionSocket;
import com.stuintech.socketwrench.socket.SocketSetLoader;
import com.stuintech.socketwrench.socket.SocketSetManager;
import com.stuintech.sonicdevices.SonicDevices;
import com.stuintech.sonicdevices.util.DeferredList;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class SonicSocketSet implements SocketSetLoader {
    //Base screwdriver modes
    public static final Identifier HIDDEN_MODE = new Identifier(SonicDevices.MODID, "base.hidden");
    public static final Identifier ACTIVATE_MODE = new Identifier(SonicDevices.MODID, "base.activate");
    public static final Identifier DEACTIVATE_MODE = new Identifier(SonicDevices.MODID, "base.deactivate");
    public static final Identifier WRENCH_MODE = new Identifier(SonicDevices.MODID, "base.wrench");
    public static final ArrayList<Identifier> BASE_MODES = new ArrayList<>();
    public static final DeferredList<Identifier> BASE_MODES_HIDDEN = new DeferredList<>(HIDDEN_MODE, BASE_MODES);

    //Advanced screwdriver modes
    public static final Identifier ADV_ACTIVATE_MODE = new Identifier(SonicDevices.MODID, "adv.activate");
    public static final Identifier ADV_DEACTIVATE_MODE = new Identifier(SonicDevices.MODID, "adv.deactivate");
    public static final Identifier ADV_SCANNER_MODE = new Identifier(SonicDevices.MODID, "adv.scanner");
    public static final ArrayList<Identifier> ADV_MODES = new ArrayList<>();
    public static final DeferredList<Identifier> ADV_MODES_HIDDEN = new DeferredList<>(HIDDEN_MODE, ADV_MODES);

    //Blaster modes
    public static final Identifier BLASTER_SHIFT_MODE = new Identifier(SonicDevices.MODID, "blaster.shift");
    public static final Identifier BLASTER_ATTACK_MODE = new Identifier(SonicDevices.MODID, "blaster.attack");
    public static final ArrayList<Identifier> BLASTER_MODES = new ArrayList<>();

    @Override
    public void registerSockets() {
        //Set up mode lists
        BASE_MODES.add(ACTIVATE_MODE);
        BASE_MODES.add(DEACTIVATE_MODE);
        BASE_MODES.add(WRENCH_MODE);
        ADV_MODES.add(ADV_ACTIVATE_MODE);
        ADV_MODES.add(ADV_DEACTIVATE_MODE);
        ADV_MODES.add(WRENCH_MODE);
        ADV_MODES.add(ADV_SCANNER_MODE);
        BLASTER_MODES.add(BLASTER_SHIFT_MODE);
        //BLASTER_MODES.add(BLASTER_ATTACK_MODE);

        //Send lists to SocketManager
        SocketSetManager.registerSetList(BASE_MODES);
        SocketSetManager.registerSetList(ADV_MODES_HIDDEN);
        SocketSetManager.registerSetList(BLASTER_MODES);

        //Base Screwdriver Sockets
        SocketSetManager.addSocket(new ActivateSocket(false), ACTIVATE_MODE);
        SocketSetManager.addSocket(new ActivateSocket(true), DEACTIVATE_MODE);
        SocketSetManager.addSubSet(SocketSetManager.WRENCH_MASTER_KEY, WRENCH_MODE);

        //Advanced Screwdriver Sockets
        SocketSetManager.addSubSet(ACTIVATE_MODE, ADV_ACTIVATE_MODE);
        SocketSetManager.addSocket(new ExtensionSocket(ACTIVATE_MODE), ADV_ACTIVATE_MODE);
        SocketSetManager.addSubSet(DEACTIVATE_MODE, ADV_DEACTIVATE_MODE);
        SocketSetManager.addSocket(new ExtensionSocket(DEACTIVATE_MODE), ADV_DEACTIVATE_MODE);
        SocketSetManager.addSocket(new ScanBlockAction(), ADV_SCANNER_MODE);
        SocketSetManager.addSocket(new ScanEntityAction(), ADV_SCANNER_MODE);

        //Sonic Blaster Sockets
        SocketSetManager.addSocket(new BlasterShiftSocket(), BLASTER_SHIFT_MODE);
    }
}
