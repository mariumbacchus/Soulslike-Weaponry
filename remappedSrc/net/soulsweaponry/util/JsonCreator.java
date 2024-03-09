package net.soulsweaponry.util;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;

public class JsonCreator {
    
    public static JsonObject createShapedRecipeJson(ArrayList<Character> keys, 
        ArrayList<Identifier> items, ArrayList<String> type, ArrayList<String> pattern, Identifier output) {
        
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");
        
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(pattern.get(0));
        jsonArray.add(pattern.get(1));
        jsonArray.add(pattern.get(2));
        /* 
         * "pattern": [
         *  "###"
         *  " / "
         *  " / "
         * ]
         */

        json.add("pattern", jsonArray);

        JsonObject key;
        JsonObject keyList = new JsonObject();

        for (int i = 0; i < keys.size(); i++) {
            key = new JsonObject();
            key.addProperty(type.get(i), items.get(i).toString());
            keyList.add(keys.get(i) + "", key);
        }
        json.add("key", keyList);
        /* 
         * "key": {
         *      "#": {
         *          "tag": "c:lord_soul"
         *      }
         * }
         */

        JsonObject result = new JsonObject();
        result.addProperty("item", output.toString());
        result.addProperty("count", 1);
        json.add("result", result);
        /* 
         * "result": {
         *      "item": "soulsweapons:item",
         *      "count": 1
         * }
         */
        return json;
    }

    public static JsonObject createSmithingRecipeJson(String baseType, Identifier baseIdentifier, String additionType, Identifier additionIdentifier, Identifier output) {
        
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:smithing_transform");

        JsonObject baseJson = new JsonObject();
        baseJson.addProperty(baseType, baseIdentifier.toString());
        json.add("base", baseJson);

        JsonObject additionJson = new JsonObject();
        additionJson.addProperty(additionType, additionIdentifier.toString());
        json.add("addition", additionJson);

        JsonObject result = new JsonObject();
        result.addProperty("item", output.toString());
        json.add("result", result);

        JsonObject template = new JsonObject();
        template.addProperty("item", "minecraft:netherite_upgrade_smithing_template");
        json.add("template", template);
        
        return json;
    }

    public static JsonObject createShapelessRecipeJson(ArrayList<ArrayList<Object>> ingredients, Identifier output, int outputCount) {

        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shapeless");

        JsonArray jsonArray = new JsonArray();
        for (ArrayList<Object> ingredient : ingredients) {
            JsonObject object = new JsonObject();
            object.addProperty(ingredient.get(0).toString(), ingredient.get(1).toString());
            jsonArray.add(object);
        }
        json.add("ingredients", jsonArray);

        JsonObject result = new JsonObject();
        result.addProperty("item", output.toString());
        result.addProperty("count", outputCount);
        json.add("result", result);
        
        return json;
    }

    public static JsonObject createBuiltinEntityModel(String[] elements, String[] properties, double[][][] mainArray) {
        //elements = thridperson_righthand osv.
        //properties = rotation, translation osv.
        //mainArray har alle arraysene med info etter hverandre
        JsonObject json = new JsonObject();
        json.addProperty("parent", "builtin/entity");

        JsonObject display = new JsonObject();
        for (int k = 0; k < elements.length; k++) {
            JsonObject object = new JsonObject();
            for (int i = 0; i < properties.length; i++) {
                JsonArray arr = new JsonArray();
                for (int j = 0; j < mainArray[k][i].length; j++) {
                    arr.add(mainArray[k][i][j]);
                }
                object.add(properties[i], arr);
            }        
            display.add(elements[k], object);
        }
        
        json.add("display", display);
        return json;
    }

    public static JsonObject createSmeltingRecipeJson(String ingredientType, Identifier ingredientId, Identifier output, double expirience, int cookingtime) {
        
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:smelting");

        JsonObject ingredientJson = new JsonObject();
        ingredientJson.addProperty(ingredientType, ingredientId.toString());
        json.add("ingredient", ingredientJson);

        json.addProperty("result", output.toString());
        json.addProperty("expirience", expirience);
        json.addProperty("cookingtime", cookingtime);
        
        return json;
    }
}
