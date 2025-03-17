package net.soulsweaponry.util;

public class LuckChosenObject<T> {

    private final T object;
    private final WeaponUtil.LuckType luckType;
    private int luckFactor;

    public LuckChosenObject(T object, WeaponUtil.LuckType luckType) {
        this.object = object;
        this.luckType = luckType;
        this.luckFactor = 10;
    }

    public T getObject() {
        return this.object;
    }

    public int getLuckFactor() {
        return this.luckFactor;
    }

    public WeaponUtil.LuckType getLuckType() {
        return this.luckType;
    }

    public void setLuckFactor(int luckFactor) {
        this.luckFactor = luckFactor;
    }
}