package com.dudaj.cezspringapp.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DateTimeException;
import java.time.LocalDate;

public class PeselValidator implements ConstraintValidator<ValidPesel, String> {
    private static boolean hasValidChecksum(int[] digits) {
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int sum = 0;
        for (int i = 0; i < 10; ++i) {
            sum += weights[i] * digits[i];
        }

        int expectedChecksum = (10 - (sum % 10)) % 10;
        int actualChecksum = digits[10];
        return expectedChecksum == actualChecksum;
    }

    private static boolean hasCorrectDate(int[] digits) {
        int yearPrefix = digits[0] * 10 + digits[1];
        int monthEncoded = digits[2] * 10 + digits[3];
        int day = digits[4] * 10 + digits[5];

        int year;
        int month;

        if (monthEncoded >= 1 && monthEncoded <= 12) {
            year = 1900 + yearPrefix;
            month = monthEncoded;
        } else if (monthEncoded >= 21 && monthEncoded <= 32) {
            year = 2000 + yearPrefix;
            month = monthEncoded - 20;
        } else if (monthEncoded >= 41 && monthEncoded <= 52) {
            year = 2100 + yearPrefix;
            month = monthEncoded - 40;
        } else if (monthEncoded >= 61 && monthEncoded <= 72) {
            year = 2200 + yearPrefix;
            month = monthEncoded - 60;
        } else if (monthEncoded >= 81 && monthEncoded <= 92) {
            year = 1800 + yearPrefix;
            month = monthEncoded - 80;
        } else {
            return false;
        }

        try {
            @SuppressWarnings("unused")
            var date = LocalDate.of(year, month, day);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    @Override
    public void initialize(ValidPesel constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!value.matches("\\d{11}")) {
            return false;
        }
        int[] digits = value.chars().map(Character::getNumericValue).toArray();

        return hasValidChecksum(digits) && hasCorrectDate(digits);
    }
}
