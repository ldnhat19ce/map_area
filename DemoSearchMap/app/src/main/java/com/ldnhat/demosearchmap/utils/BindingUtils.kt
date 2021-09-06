package com.ldnhat.demosearchmap.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.ldnhat.demosearchmap.R
import com.ldnhat.demosearchmap.model.CountryDetail
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

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

@BindingAdapter("editTextListner")
fun editTextListener(editText: EditText, subject: BehaviorSubject<CharSequence>){
    editText.addTextChangedListener(object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            subject.onNext(s)
        }

        override fun afterTextChanged(s: Editable?) {

        }
    })
}
