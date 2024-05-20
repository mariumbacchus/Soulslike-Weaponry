package net.soulsweaponry.util;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.advancement.Advancement;
import net.minecraft.loot.condition.LootConditionManager;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
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
        json.addProperty("type", "minecraft:smithing");

        JsonObject baseJson = new JsonObject();
        baseJson.addProperty(baseType, baseIdentifier.toString());
        json.add("base", baseJson);

        JsonObject additionJson = new JsonObject();
        additionJson.addProperty(additionType, additionIdentifier.toString());
        json.add("addition", additionJson);

        JsonObject result = new JsonObject();
        result.addProperty("item", output.toString());
        json.add("result", result);

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

    public static JsonObject createRecipeBookAdvancement(Identifier recipeIdentifier, Identifier triggerItem, boolean isItemTag) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", "recipes/root");

        JsonObject additionJson = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(recipeIdentifier.toString());
        additionJson.add("recipes", jsonArray);
        json.add("rewards", additionJson);

        JsonObject conditions = new JsonObject();
        if (isItemTag) {
            conditions.addProperty("tag", triggerItem.toString());
        } else {
            JsonArray items = new JsonArray();
            JsonArray item = new JsonArray();
            item.add(triggerItem.toString());
            JsonObject itemObject = new JsonObject();
            itemObject.add("items", item);
            items.add(itemObject);
            conditions.add("items", items);
        }

        JsonObject hasItem = new JsonObject();
        hasItem.addProperty("trigger", "inventory_changed");
        hasItem.add("conditions", conditions);

        JsonObject hasRecipeAlready = new JsonObject();
        hasRecipeAlready.addProperty("trigger", "minecraft:recipe_unlocked");
        JsonObject condition = new JsonObject();
        condition.addProperty("recipe", recipeIdentifier.toString());
        hasRecipeAlready.add("conditions", condition);

        JsonObject overallCriteria = new JsonObject();
        overallCriteria.add("has_item", hasItem);
        overallCriteria.add("has_recipe", hasRecipeAlready);
        json.add("criteria", overallCriteria);

        JsonArray requirements = new JsonArray();
        JsonArray list = new JsonArray();
        list.add("has_item");
        list.add("has_recipe");
        requirements.add(list);
        json.add("requirements", requirements);

        return json;
    }

    public static Advancement.Builder buildAdvancement(JsonObject recipeBookObject, Identifier advancementId) {
        return Advancement.Builder.fromJson(recipeBookObject, new AdvancementEntityPredicateDeserializer(advancementId, new LootConditionManager()));
    }
}