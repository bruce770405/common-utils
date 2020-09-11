package tw.com.bruce.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tw.com.bruce.common.utils.serializer.BigDecimalTypeDeserializer;
import tw.com.bruce.common.utils.serializer.BigDecimalTypeSerializer;

import java.math.BigDecimal;
import java.text.DateFormat;

public abstract class JsonUtilsEx {

//    private static Log logger = LogFactory.getLog(JsonUtils.class);

    private final static Gson gsonInstance;

    /**
     * 建立 Gson 物件
     *
     * @return
     */
    static {
        gsonInstance = new GsonBuilder().setDateFormat(DateFormat.LONG)
                .registerTypeAdapter(BigDecimal.class, new BigDecimalTypeSerializer())
                .registerTypeAdapter(BigDecimal.class, new BigDecimalTypeDeserializer())
                .create();
    }

    /**
     * Json字串轉換為物件
     *
     * @param <POJO>
     * @param json
     * @param clazz
     * @return
     */
    public static <POJO> POJO getObject(String json, Class<POJO> clazz) {
        try {
            return (POJO) gsonInstance.fromJson(json, clazz);
        } catch (Exception ex) {
//            logger.error("json data parser to object was fail", ex);
        }

        return null;
    }

    /**
     * 物件轉換為Json字串
     *
     * @param object
     * @return
     */
    public static String getJson(Object object) {
        try {
            return gsonInstance.toJson(object);
        } catch (Exception ex) {
//            logger.error("object parser to json was fail", ex);
        }

        return "";
    }

}
