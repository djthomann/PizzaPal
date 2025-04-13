// package de.hsrm.mi.swt.core.application.repository;

// import com.google.gson.JsonElement;
// import com.google.gson.JsonObject;
// import com.google.gson.JsonSerializationContext;
// import com.google.gson.JsonSerializer;
// import javafx.scene.paint.Color;

// import java.lang.reflect.Type;

// public class ColorSerializer implements JsonSerializer<Color> {
//     @Override
//     public JsonElement serialize(Color color, Type typeOfSrc, JsonSerializationContext context) {
//         JsonObject jsonColor = new JsonObject();
//         jsonColor.addProperty("red", color.getRed());
//         jsonColor.addProperty("green", color.getGreen());
//         jsonColor.addProperty("blue", color.getBlue());
//         jsonColor.addProperty("opacity", color.getOpacity());
//         return jsonColor;
//     }
// }