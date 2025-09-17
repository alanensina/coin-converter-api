package com.alanensina.coinconverter.controllers;

import com.alanensina.coinconverter.dtos.CurrencyBillsAndCoinsResponseDTO;
import com.alanensina.coinconverter.dtos.CurrencyBillsResponseDTO;
import com.alanensina.coinconverter.dtos.CurrencyCoinsResponseDTO;
import com.alanensina.coinconverter.services.ConverterService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/converter")
public class ConverterController {

    private final ConverterService service;

    public ConverterController(ConverterService service) {
        this.service = service;
    }

    @GetMapping("/convert-currency/{cents}")
    @Operation(summary = "Convert currency to bills and coins", description = "Receive an amount of cents as input and return the currency organized in bills and coins.")
    public ResponseEntity<CurrencyBillsAndCoinsResponseDTO> convertCurrency(@PathVariable @Min(1) int cents){
        return new ResponseEntity<>(service.convertCurrency(cents), HttpStatus.OK);
    }

    @GetMapping("/convert-currency-to-coins/{cents}")
    @Operation(summary = "Convert currency to coins", description = "Receive an amount of cents as input and return the currency organized in coins.")
    public ResponseEntity<CurrencyCoinsResponseDTO> convertCurrencyToCoins(@PathVariable @Min(1) int cents){
        return new ResponseEntity<>(service.convertCurrencyToCoins(cents), HttpStatus.OK);
    }

    @GetMapping("/convert-currency-to-bills/{cents}")
    @Operation(summary = "Convert currency to bills", description = "Receive an amount of cents as input and return the currency organized in bills.")
    public ResponseEntity<CurrencyBillsResponseDTO> convertCurrencyToBills(@PathVariable @Min(1) int cents){
        return new ResponseEntity<>(service.convertCurrencyToBills(cents), HttpStatus.OK);
    }
}
