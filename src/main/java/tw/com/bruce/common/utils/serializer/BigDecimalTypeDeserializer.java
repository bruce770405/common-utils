package tw.com.bruce.common.utils.serializer;

import com.google.gson.*;
import jdk.internal.joptsimple.internal.Strings;
import tw.com.bruce.common.utils.StringUtilsEx;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BigDecimalTypeDeserializer implements JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The json must instanceof JsonPrimitive (should be a string value)");
        }
        String value = json.getAsJsonPrimitive().getAsString();
        if (Strings.isNullOrEmpty(value)) {
            throw new JsonParseException("The date value should be a string value");
        }
        // 移除千分位符號
        value = StringUtilsEx.replace(value, ",", "");
        return new BigDecimal(value);
    }

}
