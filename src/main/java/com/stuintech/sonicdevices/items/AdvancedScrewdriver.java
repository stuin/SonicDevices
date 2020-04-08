package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.actions.LongActivateAction;
import com.stuintech.sonicdevices.actions.ScanBlockAction;
import com.stuintech.sonicdevices.actions.ScanEntityAction;

public class AdvancedScrewdriver extends Screwdriver {
    public AdvancedScrewdriver(boolean hidden) { this(hidden, 4); }

    //Actual constructor
    public AdvancedScrewdriver(boolean hidden, int maxLevel) {
        super(hidden, maxLevel);
        actions[1].add(new LongActivateAction(false));
        actions[2].add(new LongActivateAction(true));
        actions[4].add(new ScanBlockAction());
        actions[4].add(new ScanEntityAction());
    }
}
