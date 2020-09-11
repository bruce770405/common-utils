package tw.com.bruce.common.utils.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * <p>
 * 針對Gson，將 java.math.BigDecimal，轉型後保留所有精度.
 * </p>
 *
 * @author BruceHsu
 * @version 1.0, Nov 22, 2019
 * @see
 * @since
 */
public class BigDecimalTypeSerializer implements JsonSerializer<BigDecimal> {

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonElement serialize(BigDecimal value, Type typeOfSrc, JsonSerializationContext context) {
        String bigDecimalValue = value.toPlainString();
        return new JsonPrimitive(bigDecimalValue);
    }

}
