package com.example.garagemanagement;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy, MM, dd");

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            // Extract "YYYY, MM, DD"
            String dateStr = json.getAsString();
            return sdf.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Date: " + json.getAsString(), e);
        }
    }
}