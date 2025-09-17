package com.alanensina.coinconverter.enums;

public enum CoinEnum {
    DOLLAR(100),
    HALF_DOLLAR(50),
    QUARTER(25),
    DIME(10),
    NICKEL(5),
    PENNY(1);

    private final int cents;
    CoinEnum(int cents) { this.cents = cents; }
    public int cents() { return cents; }
}
