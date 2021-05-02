package com.rajsuvariya.pixabaysearch.util

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.lifecycle.MutableLiveData
import com.google.android.material.internal.TextWatcherAdapter
import com.rajsuvariya.pixabaysearch.R

object EditTextBindings {
    @JvmStatic
    @BindingAdapter("onTextChange")
    fun bindEditText(view: EditText, observableString: MutableLiveData<String>) {
        if (view.getTag(R.id.onTextChange) == null) {
            view.setTag(R.id.onTextChange, true)
            view.addTextChangedListener(object : TextWatcherAdapter() {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    observableString.value = s.toString()
                }
            })
        }
        val newValue: String = observableString.value ?: ""
        if (view.text.toString() != newValue) {
            view.setText(newValue)
        }
    }

    @JvmStatic
    @BindingConversion
    fun convertObservableStringToString(observableString: MutableLiveData<String>): String {
        return observableString.value ?: ""
    }
}