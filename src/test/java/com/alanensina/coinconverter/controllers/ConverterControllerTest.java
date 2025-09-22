package com.alanensina.coinconverter.controllers;

import com.alanensina.coinconverter.dtos.CurrencyBillsAndCoinsResponseDTO;
import com.alanensina.coinconverter.dtos.CurrencyBillsResponseDTO;
import com.alanensina.coinconverter.dtos.CurrencyCoinsResponseDTO;
import com.alanensina.coinconverter.services.ConverterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConverterController.class)
class ConverterControllerTest {

    public static final String BASE_URL = "/api/v1/converter";
    public static final String CONVERT_CURRENCY = "/convert-currency/";
    public static final String CONVERT_CURRENCY_TO_BILLS = "/convert-currency-to-bills/";
    public static final String CONVERT_CURRENCY_TO_COINS = "/convert-currency-to-coins/";
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConverterService service;

    @Test
    @DisplayName("GET " + CONVERT_CURRENCY + "{cents} returns OK and a DTO with bills and coins")
    void shouldConvertCurrencyToBillsAndCoins() throws Exception {
        int input = 186;

        CurrencyBillsAndCoinsResponseDTO dto = new CurrencyBillsAndCoinsResponseDTO(
                1, 0, 0, 0, 1, 0, 0, // bills
                1, 1, 1, 0, 1        // coins
        );
        when(service.convertCurrency(input)).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + CONVERT_CURRENCY + input)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ONE_HUNDRED").value(1))
                .andExpect(jsonPath("$.FIVE").value(1))
                .andExpect(jsonPath("$.HALF_DOLLAR").value(1))
                .andExpect(jsonPath("$.QUARTER").value(1))
                .andExpect(jsonPath("$.DIME").value(1))
                .andExpect(jsonPath("$.NICKEL").value(0))
                .andExpect(jsonPath("$.PENNY").value(1));
    }

    @Test
    @DisplayName("GET /" + CONVERT_CURRENCY_TO_COINS + "{cents} returns OK and a DTO only with coins")
    void shouldConvertCurrencyToCoins() throws Exception {
        int input = 286;

        CurrencyCoinsResponseDTO dto = new CurrencyCoinsResponseDTO(
                2, 1, 1, 1, 0, 1
        );
        when(service.convertCurrencyToCoins(input)).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + CONVERT_CURRENCY_TO_COINS + input)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ONE_DOLLAR").value(2))
                .andExpect(jsonPath("$.HALF_DOLLAR").value(1))
                .andExpect(jsonPath("$.QUARTER").value(1))
                .andExpect(jsonPath("$.DIME").value(1))
                .andExpect(jsonPath("$.NICKEL").value(0))
                .andExpect(jsonPath("$.PENNY").value(1));
    }

    @Test
    @DisplayName("GET " + CONVERT_CURRENCY_TO_BILLS + "{cents} returns OK and a DTO with bills and some cents.")
    void shouldConvertCurrencyToBills() throws Exception {
        int input = 3_000;

        CurrencyBillsResponseDTO dto = new CurrencyBillsResponseDTO(
                0, 0, 1, 1, 0, 0, 0, 0
        );
        when(service.convertCurrencyToBills(input)).thenReturn(dto);

        mockMvc.perform(get(BASE_URL+ CONVERT_CURRENCY_TO_BILLS + input)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.TWENTY").value(1))
                .andExpect(jsonPath("$.TEN").value(1))
                .andExpect(jsonPath("$.CENTS").value(0));
    }

    @Test
    @DisplayName("Returns 400 status code because the input is < 1")
    void shouldReturn400StatusForZeroCentsInput() throws Exception {
        int input = 0;

        mockMvc.perform(get(BASE_URL + CONVERT_CURRENCY + input)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(get(BASE_URL + CONVERT_CURRENCY_TO_BILLS + input)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(get(BASE_URL + CONVERT_CURRENCY_TO_COINS + input)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}