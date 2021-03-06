package util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/14.
 */
public class SPFUtils {

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String)
        {
            editor.putString(key, (String) object).commit();
        } else if (object instanceof Integer)
        {
            editor.putInt(key, (Integer) object).commit();
        } else if (object instanceof Boolean)
        {
            editor.putBoolean(key, (Boolean) object).commit();
        } else if (object instanceof Float)
        {
            editor.putFloat(key, (Float) object).commit();
        } else if (object instanceof Long)
        {
            editor.putLong(key, (Long) object).commit();
        } else
        {
            editor.putString(key, object.toString()).commit();
        }
    }
    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject  默认值
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_MULTI_PROCESS);
        if (defaultObject instanceof String)
        {
            return sp.getString(key, (String)defaultObject);
        } else if (defaultObject instanceof Integer)
        {
            return sp.getInt(key, (Integer)defaultObject);
        } else if (defaultObject instanceof Boolean)
        {
            return sp.getBoolean(key, (Boolean)defaultObject);
        } else if (defaultObject instanceof Float)
        {
            return sp.getFloat(key, (Float)defaultObject);
        } else if (defaultObject instanceof Long)
        {
            return sp.getLong(key, (Long)defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     * @param context
     * @param key
     */
    public static void remove(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key).commit();
    }

    /**
     * 清除所有数据
     * @param context
     */
    public static void clear(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }

    /**
     * 查询某个key是否已经存在
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }
    /**
     * 返回所有的键值对
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }
}
