package com.doctorzee.fortuneblocks;

public enum Permission
{

    ERROR("fortuneblocks.error"),
    SILENT_PLACE("fortuneblocks.silent"),
    PICKUP("fortuneblocks.pickup"),
    ADMIN("fortuneblocks.admin"),
    ADMIN_RELOAD("fortuneblocks.admin.reload"),
    ADMIN_LIST("fortuneblocks.admin.list"),
    ADMIN_REMOVE("fortuneblocks.admin.remove"),
    ADMIN_ADD("fortuneblocks.admin.add");

    private String permission;

    private Permission(String permission)
    {
        this.permission = permission;
    }

    public String getPermission()
    {
        return permission;
    }

}
