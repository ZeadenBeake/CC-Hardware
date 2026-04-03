package org.zeaden.ccHardware;

import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaFunction;

public class CaseMetaAPI implements ILuaAPI {
    private final boolean isAdvanced;

    public CaseMetaAPI(boolean isAdvanced) {
        this.isAdvanced = isAdvanced;
    }

    @Override
    public String[] getNames() {
        return new String[]{ "_ccHardware" };
    }

    @LuaFunction
    public final boolean advanced() {
        return isAdvanced;
    }
}