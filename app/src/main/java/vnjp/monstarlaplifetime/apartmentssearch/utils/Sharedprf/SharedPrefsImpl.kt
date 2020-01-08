package vnjp.monstarlaplifetime.apartmentssearch.utils.Sharedprf

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Lớp thao tác với SharePreferences
 *
 */
@Suppress("UNCHECKED_CAST")
class SharedPrefsImpl(context: Context) : SharedPrefsApi {

    val sharedPreferences: SharedPreferences

    /**
     * Phương thức lấy dữ liệu
     * @param key   - Key cho dữ liệu
     * @param <T>   - Kiểu dữ liệu nguyên thủy
    </T> */

    override fun <T> get(key: String?, clazz: Class<T>?): T? {
        if (clazz == String::class.java) {
            return sharedPreferences.getString(key, "") as T
        } else if (clazz == Boolean::class.java) {
            return java.lang.Boolean.valueOf(sharedPreferences.getBoolean(key, false)) as T
        } else if (clazz == Float::class.java) {
            return java.lang.Float.valueOf(sharedPreferences.getFloat(key, 0f)) as T
        } else if (clazz == Int::class.java) {
            return Integer.valueOf(sharedPreferences.getInt(key, 0)) as T
        } else if (clazz == Long::class.java) {
            return java.lang.Long.valueOf(sharedPreferences.getLong(key, 0)) as T
        }
        return null
    }

    /**
     * Phương thức thêm/gán dữ liệu
     * @param key   - Key cho dữ liệu
     * @param data  - Dữ liệu
     * @param <T>   - Kiểu dữ liệu nguyên thủy
    </T> */
    override fun <T> put(key: String?, data: T) {
        val editor = sharedPreferences.edit()
        if (data is String) {
            editor.putString(key, data as String)
        } else if (data is Boolean) {
            editor.putBoolean(key, (data as Boolean))
        } else if (data is Float) {
            editor.putFloat(key, (data as Float))
        } else if (data is Int) {
            editor.putInt(key, (data as Int))
        } else if (data is Long) {
            editor.putLong(key, (data as Long))
        }
        editor.apply()
    }

    /**
     * Phương thức xóa toàn bộ dữ liệu
     *
     */
    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun <T> putObject(key: String, y: T) {
        //Convert object to JSON String.
        val inString = gson.toJson(y)
        //Save that String in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString(key, inString).apply()
    }

    fun <T> getObject(key: String, c: Class<T>): T? {
        //We read JSON String which was saved.
        val value = sharedPreferences.getString(key, null)
        if (value != null) {
            //JSON String was found which means object can be read.
            //We convert this JSON String to model object. Parameter "c" (of
            return gson.fromJson(value, c)
        } else {
            //No JSON String with this key was found which means key is invalid or object was not saved.
            throw IllegalArgumentException("No object with key: $key was saved")
        }
    }

    /**
     * Phương thức lấy danh sách đối tượng từ string json từ SharePref
     * @param <T>   - Kiểu đối tượng
    </T> */
    fun <T> getListObject(): List<T> {
        val tracks =
            sharedPreferences.getString(SharedPrefsKey.KEY_LIST_OBJECT, null)
        val listType =
            object : TypeToken<ArrayList<T>?>() {}.type
        return Gson().fromJson(tracks, listType)
    }

    /**
     * Phương thức thêm/gán danh sách đối tượng sang json string vào SharePref
     * @param <T>   - Kiểu đối tượng
    </T> */
    fun <T> putListMsg(objects: List<T>?) {
        val editor = sharedPreferences.edit()
        editor.putString(SharedPrefsKey.KEY_LIST_OBJECT, Gson().toJson(objects)).apply()
    }

    companion object {
        private const val PREFS_NAME = "Apartments Search"
        private val gson = GsonBuilder().create()
    }

    init {
        sharedPreferences = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }
}