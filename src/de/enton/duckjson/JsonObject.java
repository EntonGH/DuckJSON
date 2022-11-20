package de.enton.duckjson;

import de.enton.duckjson.annotation.JsonField;
import de.enton.duckjson.annotation.JsonIgnore;
import de.enton.duckjson.annotation.Jsonable;
import de.enton.duckjson.exception.NotJsonableException;

import java.lang.reflect.Field;

public class JsonObject extends JsonSerializer {

    private final Object fromObject;

    public JsonObject(Object fromObject) throws NotJsonableException, IllegalAccessException {
        if(!fromObject.getClass().isAnnotationPresent(Jsonable.class))
            throw new NotJsonableException(
                    String.format("Object of type %s is not @Jsonable", fromObject.getClass().getSimpleName())
            );

        this.fromObject = fromObject;
        init();
    }

    private void init() throws NotJsonableException, IllegalAccessException {
        for(Field field : this.fromObject.getClass().getDeclaredFields())
        {
            if(
                field.isAnnotationPresent(JsonIgnore.class) || !field.canAccess(this.fromObject)
            ) continue;

            String name = field.getName();
            if(field.isAnnotationPresent(JsonField.class))
                name = field.getAnnotation(JsonField.class).name();

            if(
                field.getType().isPrimitive() ||
                field.getType().isAssignableFrom(JsonCompound.class) ||
                field.getType() == String.class
            ) {
                this.put(name, field.get(this.fromObject));
            } else throw new NotJsonableException(
                    String.format("Type %s of field %s is not a supported type for JSON Serialization. See @JsonCompound.",
                            this.fromObject.getClass().getSimpleName(),
                            field.getName()
                    )
            );
        }
    }

}
