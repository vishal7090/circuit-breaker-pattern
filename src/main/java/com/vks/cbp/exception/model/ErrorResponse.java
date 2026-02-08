package com.vks.cbp.exception.model;

public record ErrorResponse(String timestamp, int status, String error, String message, String path) {
}
