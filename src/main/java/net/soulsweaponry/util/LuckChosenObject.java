package net.soulsweaponry.util;

public class LuckChosenObject<T> {

    private final T object;
    private final WeaponUtil.LuckType luckType;
    private int luckFactor;

    /**
     * @param object the object
     * @param luckType luck type to determine whether luckFactor should be increased or decreased
     * @param luckFactor start weight (normally at 10)
     */
    public LuckChosenObject(T object, WeaponUtil.LuckType luckType, int luckFactor) {
        this.object = object;
        this.luckType = luckType;
        this.luckFactor = luckFactor;
    }

    /**
     * @param object the object
     * @param luckType luck type to determine whether luckFactor should be increased or decreased
     */
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
