package com.example.eucurrencyconverter.core

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class Eu {
    companion object {
        // Fields ======================================================================================================
        var mCountries = ArrayList<String>()
        var mVatRatesName = ArrayList<String>()
        var mVatRatesValue = ArrayList<String>()
        var mSelectedVat = ""

        // =============================================================================================================

        // this method will clear vat rates name list and also vat rates value list
        fun clearList(){
            // clearing array lists
            mVatRatesName.clear()
            mVatRatesValue.clear()
        }

        // this method will fetch all the JSON data from the URL
        fun getCountryNames(mContext: Context){
            // creating request for fetching country names data
            val request = object : JsonObjectRequest(Constants.API_METHOD_GET, Constants.API_URL, null,
                Response.Listener<JSONObject> { response ->
                    // get the root JSON array
                    val jsonArray = response.getJSONArray(Constants.API_ROOT_ENTRY)
                    // loop through each object
                    for(i in 0 until jsonArray.length()){
                        // takes each object
                        val obj = jsonArray.getJSONObject(i)
                        // getting each country name and adding it to the list
                        val countryName = obj.getString("name")
                        mCountries.add(countryName)
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Content-Type", "application/json")
                    return headers
                }
            }
            // add the request to the request queue
            VolleySingleton.getInstance(mContext).addToRequestQueue(request)
        }
    }
}