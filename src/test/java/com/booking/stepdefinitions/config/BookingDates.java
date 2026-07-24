package com.booking.stepdefinitions.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDates {

    @Builder.Default
    @JsonProperty("checkin")
    private String checkin = "2026-07-23";

    @Builder.Default
    @JsonProperty("checkout")
    private String checkout = "2026-07-24";
}