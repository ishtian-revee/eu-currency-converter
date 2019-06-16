package com.example.eucurrencyconverter.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.example.eucurrencyconverter.R
import com.example.eucurrencyconverter.core.Constants
import com.example.eucurrencyconverter.core.Eu
import com.example.eucurrencyconverter.core.VolleySingleton
import com.example.eucurrencyconverter.ui.adapters.VatRatesAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    // Fields ==========================================================================================================
    private var mSelectedCountry = ""

    // =================================================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // method calls
        Eu.getCountryNames(this)
        initSpinner()
        displayOriginalAmount()
    }

    private fun runThread(){
        // make it run on UI thread
        runOnUiThread {
            recyclerView.adapter = VatRatesAdapter(Eu.mVatRatesName, Eu.mVatRatesValue)
        }
    }

    // this method will fetch the vat rates data of selected country item for spinner
    fun getVatRatesData(){
        Eu.clearList()

        // creating request for fetching vat rates
        val request = object : JsonObjectRequest(Constants.API_METHOD_GET, Constants.API_URL, null,
            Response.Listener<JSONObject> { response ->
                // get the root JSON array
                val rootArray = response.getJSONArray(Constants.API_ROOT_ENTRY)

                // loop through each object
                for(i in 0 until rootArray.length()){
                    // takes each object
                    val obj = rootArray.getJSONObject(i)

                    // clear list if default text 'Select' is selected in the spinner
                    if(mSelectedCountry.equals("Select")){
                        Eu.clearList()
                        runThread()
                    }else if(obj.get("name").equals(mSelectedCountry)){       // checking with the selected item name
                        // get the periods JSON array
                        val periods = obj.getJSONArray("periods")

                        // loop through each periods object
                        for(j in 0 until periods.length()){
                            // get the each rates object
                            val objRates = periods.getJSONObject(j)
                            val rates = objRates.getJSONObject("rates")

                            // get the keys
                            val keys = rates.keys()
                            // loop through all keys
                            while (keys.hasNext()){
                                // get each key names
                                val key = keys.next().toString()
                                Eu.mVatRatesName.add(key)
                                // get each value
                                val value = rates.getString(key)
                                Eu.mVatRatesValue.add(value)
                            }
                        }
                        runThread()
                    }
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
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    // this method will initialize the spinner with country names
    private fun initSpinner(){
        // setting first entry as some text
        Eu.mCountries.add("Select")
        // create an Array Adapter using a simple sinner layout and languages array
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item, Eu.mCountries)
        // set layout to use when the list of choices appear
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_item)
        // set the adapter to spinner
        spinner_country.setAdapter(adapter)

        // setting item click listener to the spinner
        spinner_country.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // get the selected item name
                mSelectedCountry = parent.getItemAtPosition(position).toString()
                // display the selected item text on test text view
                test.text = mSelectedCountry
                // method call
                getVatRatesData()
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
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
                // method call
                calculateTotalAmount()
            }
        })
    }

    // this method will calculate the total amount
    private fun calculateTotalAmount(){
        // calculation of input amount + tax = total amount
        try {
            val inputValue = originalAmount.text.toString().toFloat()
            val percent = 4.0   // TODO: get the selected vat rate value
            val taxValue = (percent*inputValue) / 100.00
            taxAmount.text = taxValue.toString()
            val totalValue = inputValue + taxValue
            totalAmount.text = totalValue.toString()
        } catch(e: NumberFormatException){
            originalAmount.text = "0.0"
            taxAmount.text = "0.0"
            totalAmount.text = "0.0"
        }
    }
}
