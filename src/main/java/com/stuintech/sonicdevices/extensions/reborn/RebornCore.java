package com.stuintech.sonicdevices.extensions.reborn;

import com.stuintech.sonicdevices.extensions.ILoader;
import reborncore.api.ToolManager;

public class RebornCore implements ILoader {
    @Override
    public void onInitialize() {
        ToolManager.INSTANCE.customToolHandlerList.add(new SonicToolHandler());
    }
}
