package com.ldnhat.demosearchmap.utils

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.ldnhat.demosearchmap.R
import com.ldnhat.demosearchmap.model.CountryDetail

@BindingAdapter("textCountryDetail")
fun textCountryDetail(textView: TextView, countryDetail: CountryDetail?){
    if (countryDetail == null){
        textView.text = ""
    }else{
        textView.text = countryDetail.name
    }
}

@BindingAdapter("stateButton")
fun stateButton(button: Button, boolean: Boolean?){
    boolean?.let {
        button.isEnabled = it
    }
}

@BindingAdapter("textInputState")
fun textInputState(textInputEditText: TextInputEditText, boolean: Boolean?){
    boolean?.let {
        if (it){
            textInputEditText.visibility = View.VISIBLE
        }else{
            textInputEditText.visibility = View.INVISIBLE
        }
    }
}

