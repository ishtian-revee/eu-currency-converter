package com.example.eucurrencyconverter.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.eucurrencyconverter.R
import com.example.eucurrencyconverter.core.Constants
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    // Fields ==========================================================================================================
    private var countries = ArrayList<String>()
    // =================================================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // method calls
        getData()
        initSpinner()
        displayOriginalAmount()
    }

    // this method will fetch all the JSON data from the URL
    private fun getData(){
        // creating request
        val request = object : JsonObjectRequest(Constants.API_METHOD, Constants.API_URL, null,
            Response.Listener<JSONObject> { response ->
                // get the root JSON array
                val jsonArray = response.getJSONArray(Constants.API_ROOT_ENTRY)
                // loop through each object
                for(i in 0 until jsonArray.length()){
                    // takes each object
                    val obj = jsonArray.getJSONObject(i)
                    // getting each country name and adding it to the list
                    val countryName = obj.getString("name")
                    countries.add(countryName)
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
        // creating request queue and adding the request
        val mRequestQueue = Volley.newRequestQueue(this)
        mRequestQueue.add(request)
    }

    // this method will initialize the spinner with country names
    private fun initSpinner(){
        // setting first entry as some text
        countries.add("Select")
        // create an Array Adapter using a simple sinner layout and languages array
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item, countries)
        // set layout to use when the list of choices appear
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_item)
        // set the adapter to spinner
        spinner_country.setAdapter(adapter)
    }

    // this method will set original amount text to whatever user input in the amount field
    private fun displayOriginalAmount(){
        et_currency_amount.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // set the text
                originalAmount.text = s

                // calculation of input amount + tax = total amount
                val inputValue = originalAmount.text.toString().toFloat()
                val taxValue = taxAmount.text.toString().toFloat()
                val totalValue = inputValue + taxValue
                totalAmount.text = totalValue.toString()
            }
        })
    }
}
