package com.alanensina.coinconverter.enums;

public enum BillEnum {
    HUNDRED(10_000),
    FIFTY(5_000),
    TWENTY(2_000),
    TEN(1_000),
    FIVE(500),
    TWO(200),
    ONE(100);

    private final int cents;
    BillEnum(int cents) { this.cents = cents; }
    public int cents() { return cents; }
}
