package realapps.live.gundasupermarket.zSharedPreference

import android.content.Context
import realapps.live.gundasupermarket.zConstants.Constants

object LoginData {
    private const val PREFS_NAME = "ParishLoginData"

    fun putUserLoginStatus(context: Context, value: Boolean) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(Constants.LOGIN_STATUS, value).apply()
    }

    fun getUserLoginStatus(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(Constants.LOGIN_STATUS, false)
    }

    fun saveUserName(context: Context, value: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(Constants.USER_NAME, value).apply()
    }

    fun getUserName(context: Context): String {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(Constants.USER_NAME, "EMPTY")?:"EMPTY"
    }

    fun saveUserPhone(context: Context, value: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(Constants.USER_PHONE, value).apply()
    }

    fun getUserPhone(context: Context): String {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(Constants.USER_PHONE, "EMPTY")?:"EMPTY"
    }

    fun saveUserId(context: Context, value: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(Constants.USER_ID, value).apply()
    }

    fun getUserId(context: Context): String {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(Constants.USER_ID, "EMPTY")?:"EMPTY"
    }

    fun saveUserImageURL(context: Context, imageURL: String){
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(Constants.USER_IMAGE, imageURL).apply()
    }

    fun getUserImageURL(context: Context): String {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(Constants.USER_IMAGE, "EMPTY")?:"EMPTY"
    }

    fun clear(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear().apply()
    }
}
