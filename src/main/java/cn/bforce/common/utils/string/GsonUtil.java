package cn.bforce.common.utils.string;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;


public class GsonUtil
{

    private static Gson gson = new Gson();

    public static final Gson getGson()
    {
        return gson;
    }

    /**
     * <p class="detail"> string转List </p>
     * 
     * @author yuandx
     * @param json
     * @param clazz
     * @return
     * @throws
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz)
    {
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    /**
     * <p class="detail"> string转map </p>
     * 
     * @author yuandx
     * @param json
     * @param clazz
     * @return
     * @throws
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> stringToMap(String str)
    {
        Map<String, String> map = gson.fromJson(str, (new HashMap<String, String>()).getClass());
        return map;
    }

    /**
     * <p class="detail"> 功能：对象转json格式字符串 </p>
     * 
     * @author yuandx
     * @param bean
     * @return
     * @throws
     */
    public static String beanToJSONString(Object bean)
    {
        return new Gson().toJson(bean);
    }

    /**
     * <p class="detail">
     * 功能：将Json字符串转换成对象
     * </p>
     * @author yuandx
     * @param json
     * @param beanClass
     * @return
     * @throws
     */
    public static Object JSONToObject(String json, Class beanClass)
    {
        Object res = gson.fromJson(json, beanClass);
        return res;
    }
}
