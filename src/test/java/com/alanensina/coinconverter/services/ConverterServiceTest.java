package com.alanensina.coinconverter.services;

import com.alanensina.coinconverter.dtos.CurrencyBillsAndCoinsResponseDTO;
import com.alanensina.coinconverter.dtos.CurrencyBillsResponseDTO;
import com.alanensina.coinconverter.dtos.CurrencyCoinsResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConverterServiceTest {

    private final ConverterService service = new ConverterService();

    @Test
    @DisplayName("convertCurrency - breaks down large amounts into notes and coins")
    void shouldConvertCurrencyInBillsAndCoins() {
        // 18_786 cents = 187.86
        CurrencyBillsAndCoinsResponseDTO dto = service.convertCurrency(18_786);

        // expected Bills: 1x100, 1x50, 1x20, 1x10, 1x5, 1x2, 0x1 => total 18700; rest 86
        assertEquals(1, dto.ONE_HUNDRED());
        assertEquals(1, dto.FIFTY());
        assertEquals(1, dto.TWENTY());
        assertEquals(1, dto.TEN());
        assertEquals(1, dto.FIVE());
        assertEquals(1, dto.TWO());
        assertEquals(0, dto.ONE());

        // Expected Coins 86 cents: 1x50, 1x25, 1x10, 0x5, 1x1
        assertEquals(1, dto.HALF_DOLLAR());
        assertEquals(1, dto.QUARTER());
        assertEquals(1, dto.DIME());
        assertEquals(0, dto.NICKEL());
        assertEquals(1, dto.PENNY());
    }

    @Test
    @DisplayName("convertCurrency - zero cents returns all counters to zero")
    void shouldNotConvertZeroCents() {
        CurrencyBillsAndCoinsResponseDTO dto = service.convertCurrency(0);

        assertEquals(0, dto.ONE_HUNDRED());
        assertEquals(0, dto.FIFTY());
        assertEquals(0, dto.TWENTY());
        assertEquals(0, dto.TEN());
        assertEquals(0, dto.FIVE());
        assertEquals(0, dto.TWO());
        assertEquals(0, dto.ONE());
        assertEquals(0, dto.HALF_DOLLAR());
        assertEquals(0, dto.QUARTER());
        assertEquals(0, dto.DIME());
        assertEquals(0, dto.NICKEL());
        assertEquals(0, dto.PENNY());
    }

    @Test
    @DisplayName("convertCurrency - exact value in a single bill type")
    void shouldConvertInASingleBillType() {
        CurrencyBillsAndCoinsResponseDTO dto = service.convertCurrency(20_000); // $200
        assertEquals(2, dto.ONE_HUNDRED());
        assertEquals(0, dto.FIFTY());
        assertEquals(0, dto.TWENTY());
        assertEquals(0, dto.TEN());
        assertEquals(0, dto.FIVE());
        assertEquals(0, dto.TWO());
        assertEquals(0, dto.ONE());
        assertEquals(0, dto.HALF_DOLLAR());
        assertEquals(0, dto.QUARTER());
        assertEquals(0, dto.DIME());
        assertEquals(0, dto.NICKEL());
        assertEquals(0, dto.PENNY());
    }

    @Test
    @DisplayName("convertCurrency - exact value in a single currency")
    void shouldConvertInASingleCoin() {
        CurrencyBillsAndCoinsResponseDTO dto = service.convertCurrency(25);
        assertEquals(0, dto.ONE_HUNDRED());
        assertEquals(0, dto.FIFTY());
        assertEquals(0, dto.TWENTY());
        assertEquals(0, dto.TEN());
        assertEquals(0, dto.FIVE());
        assertEquals(0, dto.TWO());
        assertEquals(0, dto.ONE());
        assertEquals(0, dto.HALF_DOLLAR());
        assertEquals(1, dto.QUARTER());
        assertEquals(0, dto.DIME());
        assertEquals(0, dto.NICKEL());
        assertEquals(0, dto.PENNY());
    }

    // ---------- convertCurrencyToCoins ----------

    @Test
    @DisplayName("convertCurrencyToCoins - breaks down into coins (includes dollar as 100c coin))")
    void shouldConvertCurrencyToCoins() {
        // 286 = 2x100,1x50,1x25,1x10,0x5,1x1
        CurrencyCoinsResponseDTO dto = service.convertCurrencyToCoins(286);
        assertEquals(2, dto.ONE_DOLLAR());
        assertEquals(1, dto.HALF_DOLLAR());
        assertEquals(1, dto.QUARTER());
        assertEquals(1, dto.DIME());
        assertEquals(0, dto.NICKEL());
        assertEquals(1, dto.PENNY());
    }

    @Test
    @DisplayName("convertCurrencyToCoins - minimum value (1 cent) results in 1 penny")
    void shouldReturnOnlyOnePenny() {
        CurrencyCoinsResponseDTO dto = service.convertCurrencyToCoins(1);
        assertEquals(0, dto.ONE_DOLLAR());
        assertEquals(0, dto.HALF_DOLLAR());
        assertEquals(0, dto.QUARTER());
        assertEquals(0, dto.DIME());
        assertEquals(0, dto.NICKEL());
        assertEquals(1, dto.PENNY());
    }

    @Test
    @DisplayName("convertCurrencyToCoins - exact dollar amount (100c coin)")
    void shouldReturnExactDollarAmount() {
        CurrencyCoinsResponseDTO dto = service.convertCurrencyToCoins(300);
        assertEquals(3, dto.ONE_DOLLAR());
        assertEquals(0, dto.HALF_DOLLAR());
        assertEquals(0, dto.QUARTER());
        assertEquals(0, dto.DIME());
        assertEquals(0, dto.NICKEL());
        assertEquals(0, dto.PENNY());
    }

    // ---------- convertCurrencyToBills ----------

    @Test
    @DisplayName("convertCurrencyToBills - breaks down into notes and returns the remaining in cents")
    void shouldConvertInBillsAndReturnTheRemainingInCents() {
        CurrencyBillsResponseDTO dto = service.convertCurrencyToBills(12_345);

        int totalBillsCents =
                dto.ONE_HUNDRED() * 10_000 +
                        dto.FIFTY() * 5_000 +
                        dto.TWENTY() * 2_000 +
                        dto.TEN() * 1_000 +
                        dto.FIVE() * 500 +
                        dto.TWO() * 200 +
                        dto.ONE() * 100;

        assertEquals(12_345, totalBillsCents + dto.CENTS());
        assertTrue(dto.CENTS() >= 0);
    }

    @Test
    @DisplayName("convertCurrencyToBills - exact value without remaining in cents")
    void shouldConvertTheAmountWithoutAnyCent() {
        CurrencyBillsResponseDTO dto = service.convertCurrencyToBills(3_000); // $30
        assertEquals(0, dto.ONE_HUNDRED());
        assertEquals(0, dto.FIFTY());
        assertEquals(1, dto.TWENTY());
        assertEquals(1, dto.TEN());
        assertEquals(0, dto.FIVE());
        assertEquals(0, dto.TWO());
        assertEquals(0, dto.ONE());
        assertEquals(0, dto.CENTS());
    }

    @Test
    @DisplayName("convertCurrencyToBills - minimum valid value (1 cent) must result in CENTS=1 and all zero notes")
    void shouldReturnOnlyOneCent() {
        CurrencyBillsResponseDTO dto = service.convertCurrencyToBills(1);
        assertEquals(0, dto.ONE_HUNDRED());
        assertEquals(0, dto.FIFTY());
        assertEquals(0, dto.TWENTY());
        assertEquals(0, dto.TEN());
        assertEquals(0, dto.FIVE());
        assertEquals(0, dto.TWO());
        assertEquals(0, dto.ONE());
        assertEquals(1, dto.CENTS());
    }
}