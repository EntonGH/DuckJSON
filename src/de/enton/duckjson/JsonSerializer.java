package de.enton.duckjson;

import java.util.LinkedHashMap;

class JsonSerializer extends LinkedHashMap<String, Object> {

    public String toJSON() {
        String outerJson = "{%}";
        StringBuilder jsonBuilder = new StringBuilder();

        this.forEach((name, entry) -> {
            if(entry.getClass().isAssignableFrom(JsonCompound.class))
            {
                jsonBuilder.append(String.format("\"%s\":", name));
                jsonBuilder.append(((JsonCompound)entry).toJSON());
            }
            else if(entry.getClass() == String.class)
                jsonBuilder.append(String.format("\"%s\":\"%s\"", name, entry));
            else
                jsonBuilder.append(String.format("\"%s\":%s", name, entry));
            jsonBuilder.append(",");
        });

        jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);

        return outerJson.replace("%", jsonBuilder.toString());
    }

}
