package com.example.eucurrencyconverter.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.eucurrencyconverter.R
import kotlinx.android.synthetic.main.row_vat_rate.view.*

// setting 2 string array list as parameters for both vat rate names and vat rates value
class VatRatesAdapter(val vatRatesName: ArrayList<String>,
                      val vatRatesValue: ArrayList<String>): RecyclerView.Adapter<VatRatesAdapter.CustomViewHolder>(){

    // Fields ==========================================================================================================
    private var mSelectedItem = -1

    // =================================================================================================================

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
        val optionText = vatRatesName.get(position) + " (" + vatRatesValue.get(position) + "%)"
        // setting the text to radio buttons
        holder.view.vatRateName.text = optionText
        // since only one radio button is allowed to be selected, this condition un-checks previous selections
        holder.mRadioButton?.setChecked(mSelectedItem == position)
        // binding items
        holder.bindItems(vatRatesName[position], position, mSelectedItem)
    }

    inner class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        var mRadioButton: RadioButton? = null

        init {
            mRadioButton = view.findViewById(R.id.radioButton)      // getting radio button instance from row layout
            // setting on click listener to the radio buttons
            view.setOnClickListener {
                mSelectedItem = getAdapterPosition()    // setting current selected item to current adapter position
                notifyDataSetChanged()                  // notifies that the underlying data has change
            }
        }

        // test
        fun bindItems(name: String, position: Int, selectedPosition: Int) {
            if((selectedPosition == -1 && position == 0)){
                view.radioButton.isChecked = true
            }else{
                if(selectedPosition == position){
                    view.radioButton.isChecked = true
                }else{
                    view.radioButton.isChecked = false

                    view.radioButton.setOnClickListener {
                        mSelectedItem = getAdapterPosition()    // setting current selected item to current adapter position
                        notifyDataSetChanged()                  // notifies that the underlying data has change
                    }
                }
            }
        }
    }
}