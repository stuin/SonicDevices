package com.stuintech.sonicdevices.extensions.reborn;

import com.stuintech.sonicdevices.extensions.ILoader;
import reborncore.api.ToolManager;

/*
 * Created by Stuart Irwin on 4/7/2020.
 */

public class RebornCore implements ILoader {
    @Override
    public void onInitialize() {
        ToolManager.INSTANCE.customToolHandlerList.add(new SonicToolHandler());
    }
}
