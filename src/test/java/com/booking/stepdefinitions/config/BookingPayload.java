package com.booking.stepdefinitions.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingPayload {

    @JsonProperty("roomid")
    private int roomid;

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonProperty("firstname")
    private String firstname = "";

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonProperty("lastname")
    private String lastname = "";

    @JsonProperty("depositpaid")
    private boolean depositpaid;

    @Builder.Default
    @JsonProperty("bookingdates")
    private BookingDates bookingdates = new BookingDates();

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonProperty("email")
    private String email = "";

    @Builder.Default
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonProperty("phone")
    private String phone = "";

    @JsonProperty("bookingdates.checkin")
    private void unpackCheckin(String checkin) {
        if (checkin != null && !checkin.isEmpty()) {
            if (this.bookingdates == null) this.bookingdates = new BookingDates();
            this.bookingdates.setCheckin(checkin);
        }
    }

    @JsonProperty("bookingdates.checkout")
    private void unpackCheckout(String checkout) {
        if (checkout != null && !checkout.isEmpty()) {
            if (this.bookingdates == null) this.bookingdates = new BookingDates();
            this.bookingdates.setCheckout(checkout);
        }
    }
}