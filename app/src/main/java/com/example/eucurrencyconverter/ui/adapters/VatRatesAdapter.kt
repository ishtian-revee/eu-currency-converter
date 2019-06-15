package com.example.eucurrencyconverter.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eucurrencyconverter.R
import kotlinx.android.synthetic.main.row_vat_rate.view.*

// setting 2 string array list as parameters for both vat rate names and vat rates value
class VatRatesAdapter(val vatRatesName: ArrayList<String>,
                      val vatRatesValue: ArrayList<String>): RecyclerView.Adapter<CustomViewHolder>(){

    override fun getItemCount(): Int {
        return vatRatesName.count()     // returns the total number of elements in that list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowLayout = layoutInflater.inflate(R.layout.row_vat_rate, parent, false)
        return CustomViewHolder(rowLayout)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        // organizing radio button text
        val optionText = vatRatesName.get(position) + " (" + vatRatesValue.get(position) + ")"
        // setting the text to radio buttons
        holder.view.vatRateName.text = optionText
    }
}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}