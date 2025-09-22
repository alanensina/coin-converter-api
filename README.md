# Coin Converter API
A small Spring Boot REST API that converts a value in cents into the optimal combination 
of US bills and/or coins. It exposes three endpoints for converting to bills only, coins only, 
or both, and provides OpenAPI documentation.

## Features
- Convert a value in cents to the minimal number of US denominations using a greedy algorithm across enums for bills and coins.
- Separate endpoints for bills only, coins only, and combined responses.
- DTO responses implemented with Java records for succinct, immutable payloads.
- OpenAPI/Swagger documentation configuration for quick exploration of endpoints.

## Tech stack

- Java 25+
- Spring Boot 3.5+
- Spring Web
- Jakarta Validation
- springdoc-openapi (Swagger)

## Getting started

- Prerequisites
  - Java 25 or later installed and on PATH.
  - Maven 3.9+ installed.


- Running locally
  - Build and run:
    - `mvn spring-boot:run`
  - Alternatively, build the JAR and run:
    - `mvn clean package`
    - `java -jar target/coin-converter-*.jar`


- By default the application runs on http://localhost:8080, but you can access Swagger UI on: http://localhost:8080/swagger-ui/index.html#/

## API overview

- **Base path:** `/api/v1/converter`


- **GET** `/convert-currency/{cents}`
  - **Description**: Convert cents to both bills and coins.
  - **Path variable**: cents (int, ≥ 1).
  - **Response**: quantities for each bill and coin denomination.


- **GET** `/convert-currency-to-coins/{cents}`
  - **Description**: Convert cents to coins only (includes one-dollar coin).
  - **Path variable**: cents (int, ≥ 1).
  - **Response**: ONE_DOLLAR, HALF_DOLLAR, QUARTER, DIME, NICKEL, PENNY.


- **GET** `/convert-currency-to-bills/{cents}`
  - **Description**: Convert cents to bills only, returning any leftover cents that cannot form a bill.
  - **Path variable**: cents (int, ≥ 1).
  - **Response**: ONE_HUNDRED, FIFTY, TWENTY, TEN, FIVE, TWO, ONE, and CENTS (remainder).

## Example requests

- Convert to both bills and coins:
  - `curl http://localhost:8080/api/v1/converter/convert-currency/1234`


- Coins only:
  - `curl http://localhost:8080/api/v1/converter/convert-currency-to-coins/287`


- Bills only:
  - `curl http://localhost:8080/api/v1/converter/convert-currency-to-bills/587`


## Data model

- **Bills** (in cents): 10,000 (100), 5,000 (50), 2,000 (20), 1,000 (10), 500 (5), 200 (2), 100 (1).
- **Coins** (in cents): 100, 50, 25, 10, 5, 1.

## Records:

- `CurrencyBillsAndCoinsResponseDTO`: bills + coins (coins start at `HALF_DOLLAR`, i.e., omits the one-dollar coin here by design).
- `CurrencyCoinsResponseDTO`: includes `ONE_DOLLAR` coin and smaller coins.
- `CurrencyBillsResponseDTO`: all bill counts plus CENTS remainder.

## Validation

- All endpoints validate cents ≥ 1 using `@Min(1)` on path parameters.
- Service layer methods for specific conversions also apply `@Min(1)` on inputs for defensive validation.

## How it works

- Uses `EnumMap<BillEnum, Integer>` and `EnumMap<CoinEnum, Integer>` to count denominations.
- Iterates denominations from largest to smallest, assigning count via integer division and tracking remainder with modulus (canonical greedy coin-change for US system).
- Combined conversion first parses bills, then coins using the leftover remainder.

## Project structure

- Application entry point: `CoinConverterApplication`
- Controller: `ConverterController` (REST endpoints)
- Service: `ConverterService` (conversion logic)
- Enums: `BillEnum`, `CoinEnum` (denomination values in cents)
- DTOs: `CurrencyBillsAndCoinsResponseDTO`, `CurrencyCoinsResponseDTO`, `CurrencyBillsResponseDTO`
- OpenAPI configuration: `OpenApiConfig`

## OpenAPI/Swagger

- OpenAPI bean provides title, version and description.
- With springdoc-openapi on the classpath, visit:
  - Swagger UI: http://localhost:8080/swagger-ui/index.html
  - OpenAPI JSON: http://localhost:8080/v3/api-docs

## Testing ideas

- Unit tests for greedy algorithm correctness across edge cases (e.g., 1, 4, 5, 6, 99, 100, 101, 199, 1234).
- Controller tests validating `@Min(1)` constraint behaviour and 400 responses for invalid input.

## Error handling

- Invalid cents (≤ 0) should return a `400 Bad Request` due to validation on path parameters.
- Consider adding a global exception handler (e.g., `@ControllerAdvice`) to standardise validation error payloads.

If you have any question, feel free to ask me.



Thanks,

Alan Ensina