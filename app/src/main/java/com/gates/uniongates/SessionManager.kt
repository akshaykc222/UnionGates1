package com.gates.uniongates

import android.content.Context
import android.content.SharedPreferences
import com.gates.uniongates.objects.SessionSave
import com.google.gson.Gson

class SessionManager(val contex:Context) {
    private val sharedPrefFile = "kotlinsharedpreference"
    val PHONE_NUM = "PHONE_NUMBER"
    val Language = "Language"
    val SelpanelNo="PANELNUMBER"
    val OrderAllowed="ORDERALLOWED"
    private var sharedPrefs: SharedPreferences? = contex.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
    private var Editor: SharedPreferences.Editor? = sharedPrefs?.edit()
    fun SavePhone(phone:String){
        Editor?.putString(PHONE_NUM,phone)
        Editor?.apply()
        Editor?.commit()

    }
    fun GetPhone():String{
        return sharedPrefs?.getString(PHONE_NUM,"1")!!
    }
    fun Savelan(Lang:String){
        Editor?.putString(Language,Lang)
        Editor?.apply()
        Editor?.commit()
    }
    fun GetLang():String{
        return sharedPrefs?.getString(Language,"1")!!
    }
    fun saveCurentSel(selPanel:SessionSave){
        val gson=Gson()
        val json=gson.toJson(selPanel)
        Editor?.putString(SelpanelNo,json)
        Editor?.apply()
        Editor?.commit()
    }
    fun getCurentSel():SessionSave{
        val gson=Gson()
        val json=sharedPrefs?.getString(SelpanelNo,"1")!!
        return gson.fromJson(json,SessionSave::class.java)
    }
    fun saveOrderAllow(orderAllowed: Boolean){
        Editor?.putBoolean(OrderAllowed,orderAllowed)
        Editor?.apply()
        Editor?.commit()
    }
    fun GetOrderAllow():Boolean{
        return sharedPrefs!!.getBoolean(OrderAllowed,false)
    }
}