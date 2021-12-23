package com.shutl.controller;

import com.shutl.model.Quote;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class QuoteController {

    @RequestMapping(value = "/quote", method = POST)
    public @ResponseBody Quote quote(@RequestBody Quote quote) {
        System.out.println("\n\n RECEIVED REQ:\n\n Quote(" + quote.getPickupPostcode() + ", " + quote.getDeliveryPostcode() + ", " + quote.getVehicle() + ")");
        Long price = Math.abs((Long.valueOf(quote.getDeliveryPostcode(), 36) - Long.valueOf(quote.getPickupPostcode(), 36))/100000000);
        if (quote.getVehicle() == null) {
            return new Quote(quote.getPickupPostcode(), quote.getDeliveryPostcode(), price);
        }
        switch (quote.getVehicle()) {
            case "bicycle":
                price = (long)(price * 1.1);
                break;
            case "motorbike":
                price = (long)(price * 1.15);
                break;
            case "parcel_car":
                price = (long)(price * 1.2);
                break;
            case "small_van":
                price = (long)(price * 1.3);
                break;
            case "large_van":
                price = (long)(price * 1.4);
                break;
            default:
                return new Quote(quote.getPickupPostcode(), quote.getDeliveryPostcode(), "invalid", price);
        }

        return new Quote(quote.getPickupPostcode(), quote.getDeliveryPostcode(), quote.getVehicle(), price);
    }
}
