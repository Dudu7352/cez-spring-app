package com.dudaj.cezspringapp.exception;

import org.jspecify.annotations.Nullable;

import java.util.List;

public record MethodArgumentExceptionDetails(List<@Nullable String> errors) {
}
