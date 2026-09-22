package dev.cruzs.gustavo.FlashSale.exceptions;

import dev.cruzs.gustavo.FlashSale.exceptions.dtos.ExceptionsResponseDto;
import dev.cruzs.gustavo.FlashSale.ticket.exceptions.TicketNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionsHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ExceptionsResponseDto> illegalArgumentException(
      IllegalArgumentException e,
      HttpServletRequest request
  ) {
    var error = new ExceptionsResponseDto(400, e.getMessage(), Instant.now(), request.getRequestURI());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(TicketNotFoundException.class)
  public ResponseEntity<ExceptionsResponseDto> ticketNotFoundException(
      TicketNotFoundException e,
      HttpServletRequest request) {
    var error = new ExceptionsResponseDto(404, e.getMessage(), Instant.now(), request.getRequestURI());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }
}
