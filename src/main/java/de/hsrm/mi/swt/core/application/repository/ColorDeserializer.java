// package de.hsrm.mi.swt.core.application.repository;

// import com.google.gson.JsonDeserializationContext;
// import com.google.gson.JsonDeserializer;
// import com.google.gson.JsonElement;
// import com.google.gson.JsonObject;
// import javafx.scene.paint.Color;

// import java.lang.reflect.Type;

// public class ColorDeserializer implements JsonDeserializer<Color> {
//     @Override
//     public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
//         JsonObject jsonObject = json.getAsJsonObject();
//         double red = jsonObject.get("red").getAsDouble();
//         double green = jsonObject.get("green").getAsDouble();
//         double blue = jsonObject.get("blue").getAsDouble();
//         double opacity = jsonObject.get("opacity").getAsDouble();
//         return new Color(red, green, blue, opacity);
//     }
// }