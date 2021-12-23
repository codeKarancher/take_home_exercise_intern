package com.shutl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shutl.Application;
import com.shutl.model.Quote;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class QuoteControllerFunctionalTest {

    @Autowired private WebApplicationContext webApplicationContext;

    ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testBasicService() throws Exception {
        Quote quoteData = new Quote("SW1A1AA", "EC2A3LT");
        MvcResult result = this.mockMvc.perform(post("/quote")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(quoteData)))
                .andExpect(status().isOk())
                .andReturn();

        Quote quote = objectMapper.readValue(result.getResponse().getContentAsString(), Quote.class);
        assertEquals(quote.getPickupPostcode(), "SW1A1AA");
        assertEquals(quote.getDeliveryPostcode(), "EC2A3LT");
        assertEquals((long)quote.getPrice(), 316L);
    }

    @Test
    public void testVariablePricingByDistance() throws Exception {
        Quote quoteData = new Quote("SW1A1AA", "EC2A3LT");
        MvcResult result = this.mockMvc.perform(post("/quote")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(quoteData)))
            .andExpect(status().isOk())
            .andReturn();

        Quote quote = objectMapper.readValue(result.getResponse().getContentAsString(), Quote.class);
        assertEquals(quote.getPickupPostcode(), "SW1A1AA");
        assertEquals(quote.getDeliveryPostcode(), "EC2A3LT");
        assertEquals((long)quote.getPrice(), 316L);

        quoteData = new Quote("AL15WD", "EC2A3LT");
        result = this.mockMvc.perform(post("/quote")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(quoteData)))
            .andExpect(status().isOk())
            .andReturn();

        quote = objectMapper.readValue(result.getResponse().getContentAsString(), Quote.class);
        assertEquals(quote.getPickupPostcode(), "AL15WD");
        assertEquals(quote.getDeliveryPostcode(), "EC2A3LT");
        assertEquals((long)quote.getPrice(), 305L);
    }

    @Test
    public void testVehicleFunctionalities() throws Exception {
        // Run 100 randomly generated pairs of postcodes, on all 5 possible vehicles

        // Setup random postcode generation and vehicle strings
        Random rand = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        String[] vehicles = {"bicycle", "motorbike", "parcel_car", "small_van", "large_van"};

        // Run 100 tests
        for (int test = 0; test < 100; test++) {
            // Generate postcodes
            StringBuilder pickup = new StringBuilder(), delivery = new StringBuilder();
            for (int postcodeLen = 0; postcodeLen < 7; postcodeLen++) {
                pickup.append(chars[rand.nextInt(chars.length)]);
                delivery.append(chars[rand.nextInt(chars.length)]);
            }

            // Calculate basePrice (the price given if no vehicle is specified)
            long basePrice = Math.abs(Long.valueOf(pickup.toString(), 36) - Long.valueOf(delivery.toString(), 36)) / 100000000;

            // Calculate vehicle markups
            long[] prices = {(long) (basePrice * 1.1), (long) (basePrice * 1.15), (long) (basePrice * 1.2), (long) (basePrice * 1.3), (long) (basePrice * 1.4)};

            // Make POST requests to the Rest API and check if the returned price markups are correct
            for (int i = 0; i < 5; i++) {
                Quote quoteData = new Quote(pickup.toString(), delivery.toString());
                quoteData.setVehicle(vehicles[i]);
                MvcResult result = this.mockMvc.perform(post("/quote")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(quoteData)))
                        .andExpect(status().isOk())
                        .andReturn();

                Quote quote = objectMapper.readValue(result.getResponse().getContentAsString(), Quote.class);
                assertEquals((long) quote.getPrice(), prices[i]);
            }
        }
    }
}
