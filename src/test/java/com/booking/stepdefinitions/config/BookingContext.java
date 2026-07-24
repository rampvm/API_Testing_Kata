package com.booking.stepdefinitions.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class BookingContext {
    private static final String CACHE_FILE = "target/context_output.properties";
    private static final Properties properties = new Properties();

    static {
        try {
            if (Files.exists(Paths.get(CACHE_FILE))) {
                try (var reader = Files.newBufferedReader(Paths.get(CACHE_FILE))) {
                    properties.load(reader);
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Failed to load execution context file: " + e.getMessage());
        }
    }

    private static void save() {
        try (var writer = Files.newBufferedWriter(Paths.get(CACHE_FILE))) {
            properties.store(writer, "Shared Test Execution Context");
        } catch (IOException e) {
            System.err.println("Warning: Failed to save context state to disk: " + e.getMessage());
        }
    }

    public static void setToken(String token) {
        properties.setProperty("AUTH_TOKEN", token);
        save();
    }

    public static String getToken() {
        return properties.getProperty("AUTH_TOKEN", "");
    }

    public static void setBookingId(Object bookingId) {
        properties.setProperty("BOOKING_ID", String.valueOf(bookingId));
        save();
    }

    public static String getBookingId() {
        return properties.getProperty("BOOKING_ID", "0");
    }
}