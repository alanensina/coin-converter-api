package com.alanensina.coinconverter.services;

import com.alanensina.coinconverter.dtos.CurrencyBillsAndCoinsResponseDTO;
import com.alanensina.coinconverter.dtos.CurrencyBillsResponseDTO;
import com.alanensina.coinconverter.dtos.CurrencyCoinsResponseDTO;
import com.alanensina.coinconverter.enums.BillEnum;
import com.alanensina.coinconverter.enums.CoinEnum;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;

import java.util.EnumMap;

import static com.alanensina.coinconverter.enums.BillEnum.*;
import static com.alanensina.coinconverter.enums.CoinEnum.*;

@Service
public class ConverterService {

    public CurrencyBillsAndCoinsResponseDTO convertCurrency(int cents) {

        int remaining = cents;
        var billCounter = new EnumMap<BillEnum, Integer>(BillEnum.class);
        var coinsCounter = new EnumMap<CoinEnum, Integer>(CoinEnum.class);

        remaining = parseBills(remaining, billCounter);
        parseCoins(remaining, coinsCounter);

        return new CurrencyBillsAndCoinsResponseDTO(
                billCounter.get(HUNDRED),
                billCounter.get(FIFTY),
                billCounter.get(TWENTY),
                billCounter.get(TEN),
                billCounter.get(FIVE),
                billCounter.get(TWO),
                billCounter.get(ONE),
                coinsCounter.get(HALF_DOLLAR),
                coinsCounter.get(QUARTER),
                coinsCounter.get(DIME),
                coinsCounter.get(NICKEL),
                coinsCounter.get(PENNY)
        );
    }

    public CurrencyCoinsResponseDTO convertCurrencyToCoins(@Min(1) int cents) {

        int remaining = cents;
        var coinsCounter = new EnumMap<CoinEnum, Integer>(CoinEnum.class);

        parseCoins(remaining, coinsCounter);

        return new CurrencyCoinsResponseDTO(
                coinsCounter.get(DOLLAR),
                coinsCounter.get(HALF_DOLLAR),
                coinsCounter.get(QUARTER),
                coinsCounter.get(DIME),
                coinsCounter.get(NICKEL),
                coinsCounter.get(PENNY)
        );
    }

    public CurrencyBillsResponseDTO convertCurrencyToBills(@Min(1) int cents) {
        int remaining = cents;
        var billCounter = new EnumMap<BillEnum, Integer>(BillEnum.class);

        remaining = parseBills(remaining, billCounter);

        return new CurrencyBillsResponseDTO(
                billCounter.get(HUNDRED),
                billCounter.get(FIFTY),
                billCounter.get(TWENTY),
                billCounter.get(TEN),
                billCounter.get(FIVE),
                billCounter.get(TWO),
                billCounter.get(ONE),
                remaining
        );
    }

    private static int parseBills(int remaining, EnumMap<BillEnum, Integer> billCounter) {
        for (BillEnum b : BillEnum.values()) {
            int n = remaining / b.cents();
            billCounter.put(b, n);
            remaining %= b.cents();
        }
        return remaining;
    }

    private static void parseCoins(int remaining, EnumMap<CoinEnum, Integer> coinsCounter) {
        for (CoinEnum c : CoinEnum.values()) {
            int n = remaining / c.cents();
            coinsCounter.put(c, n);
            remaining %= c.cents();
        }
    }
}
