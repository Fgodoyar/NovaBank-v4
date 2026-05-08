package com.novabank.operation.controller;

import com.novabank.operation.dto.CreateOperationRequest;
import com.novabank.operation.dto.CreateTransferRequest;
import com.novabank.operation.dto.TransactionDTO;
import com.novabank.operation.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/operations")
public class OperationController {

    private final OperationService operationService;

    @PostMapping("/deposit")
    @Operation(summary = "Depositar dinero")
    @ApiResponse(responseCode = "200", description = "Depósito realizado")
    @ApiResponse(responseCode = "400", description = "Monto inválido")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    public ResponseEntity<TransactionDTO> deposit(@Valid @RequestBody CreateOperationRequest request) {
        return ResponseEntity.ok(operationService.deposit(request));
    }

    @PostMapping("/withdraw")
    @Operation(summary = "Retirar dinero")
    @ApiResponse(responseCode = "200", description = "Retiro realizado")
    @ApiResponse(responseCode = "400", description = "Monto inválido")
    @ApiResponse(responseCode = "422", description = "Saldo insuficiente")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    public ResponseEntity<TransactionDTO> withdrawal(@Valid @RequestBody CreateOperationRequest request) {
        return ResponseEntity.ok(operationService.withdraw(request));
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transferir dinero")
    @ApiResponse(responseCode = "200", description = "Transferencia realizada")
    @ApiResponse(responseCode = "400", description = "Monto inválido o cuentas iguales")
    @ApiResponse(responseCode = "422", description = "Saldo insuficiente")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    public ResponseEntity<List<TransactionDTO>> transfer(@Valid @RequestBody CreateTransferRequest request) {
        return ResponseEntity.ok(operationService.transfer(request));
    }
}