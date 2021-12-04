package com.example.lr2.utility

import android.text.InputFilter
import android.text.Spanned
import java.lang.NumberFormatException

class TimeInputFilter() : InputFilter {

    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned, dstart: Int,
                        dend: Int): CharSequence? {
        try{
            val input = (dest.subSequence(0,dstart).toString() + source
                    + dest.subSequence(dend, dest.length)).toFloat()
            if(input in 0.0..59.0) {
                return null
            }
        }
        catch(nfe: NumberFormatException){
            return "00"
        }
        return ""
    }
}