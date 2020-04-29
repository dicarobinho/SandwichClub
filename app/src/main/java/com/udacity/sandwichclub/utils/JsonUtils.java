package com.udacity.sandwichclub.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {

    @SuppressLint("Assert")
    public static Sandwich parseSandwichJson(String json) {
        String description = "";
        StringBuilder ingredients = new StringBuilder();
        StringBuilder knownAs = new StringBuilder();
        String image = "";
        String mainName = "";
        String placeOfOrigin = "";

        try {
            JSONObject obj = new JSONObject(json);
            description = obj.getString("description");
            image = obj.getString("image");
            placeOfOrigin = obj.getString("placeOfOrigin");

            JSONObject name = obj.getJSONObject("name");
            mainName = name.getString("mainName");
            int jsonArraySize;

            JSONArray alsoKnown = name.getJSONArray("alsoKnownAs");
            jsonArraySize = alsoKnown.length();
            for (int i = 0; i < alsoKnown.length(); i++) {
                if (i < jsonArraySize - 1)
                    knownAs.append(alsoKnown.getString(i)).append(", ");
                else knownAs.append(alsoKnown.getString(i));
            }

            JSONArray ingredient = obj.getJSONArray("ingredients");
            jsonArraySize = ingredient.length();
            for (int i = 0; i < ingredient.length(); i++) {
                if (i < jsonArraySize - 1)
                    ingredients.append(ingredient.getString(i)).append(", ");
                else ingredients.append(ingredient.getString(i));
            }

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
        }

        return new Sandwich(mainName, knownAs.toString(), placeOfOrigin, description, image, ingredients.toString());
    }
}
