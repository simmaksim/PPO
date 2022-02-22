package com.flawlesse.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.flawlesse.calculator.databinding.ActivityMainBinding
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding


    var lastNumeric = false
    var lastDot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
    }
    fun onDigit(view: View){
        binding.tvInput.append((view as Button).text)
        lastNumeric = true
    }

    fun onClear(view: View){
        binding.tvInput.text = ""
        lastNumeric = false
        lastDot = false
    }

    fun onDecimalPoint(view: View){
        if (lastNumeric && !lastDot){
            binding.tvInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View){
        if (lastNumeric && !isOperatorAdded(binding.tvInput.text.toString())){
            binding.tvInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    fun onEqual(view: View){
        if (lastNumeric){
            var tValue = binding.tvInput.text.toString()
            var prefix = ""

            try{
                if (tValue.startsWith("-"))
                {
                    prefix = "-"
                    tValue = tValue.substring(1)
                }

                if (tValue.contains("-")){
                    val splitValue = tValue.split("-")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    binding.tvInput.text = (one.toDouble() - two.toDouble()).toString()
                } else if (tValue.contains("+")){
                    val splitValue = tValue.split("+")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    binding.tvInput.text = (one.toDouble() + two.toDouble()).toString()
                } else if (tValue.contains("*")){
                    val splitValue = tValue.split("*")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    binding.tvInput.text = (one.toDouble() * two.toDouble()).toString()
                } else if (tValue.contains("/")){
                    val splitValue = tValue.split("/")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    binding.tvInput.text = (one.toDouble() / two.toDouble()).toString()
                }
            }
            catch (e: ArithmeticException){
                e.printStackTrace()
            }
        }
    }

    private fun isOperatorAdded(value: String) : Boolean {
        return if (value.startsWith("-")) {
            false
        } else {
            value.contains("+")
                    ||  value.contains("-")
                    ||  value.contains("/")
                    ||  value.contains("*")

        }
    }

}