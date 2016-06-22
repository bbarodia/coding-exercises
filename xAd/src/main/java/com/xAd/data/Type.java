package com.xAd.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Type {

    private static final Logger logger = LogManager.getLogger(Type.class);
    private int id;
    private String name;

    public Type() {

    }

    public Type(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static List<Type> readTypesFromJson(final String typeJson) {
        logger.trace("Decoding " + typeJson);
        Gson gson = new Gson();
        List<Type> connectorTypes = new ArrayList<>();
        try {
            JsonArray jsonElement = gson.fromJson(typeJson, JsonArray.class);
            for (JsonElement jsonElement1 : jsonElement) {
                connectorTypes.add(new Type(jsonElement1.getAsJsonArray().get(0).getAsInt(),
                                            jsonElement1.getAsJsonArray().get(1).getAsString()));
            }
        } catch (Exception ex){
            throw new RuntimeException("File contents not as required.");
        }
        return connectorTypes;
    }

}
