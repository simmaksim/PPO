package com.example.lr1

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.jetbrains.annotations.NotNull
import com.example.lr1.fragments.BaseKeyboardFragment
import com.example.lr1.fragments.ScienceKeyboardFragment
import org.mariuszgromada.math.mxparser.Expression

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        val functionality = listOf("sin", "cos", "tan", "ctg", "ln", "log2", "log10", "sqrt")
    }

    private lateinit var input: TextView
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.expression)
        input.movementMethod = ScrollingMovementMethod()
        @Suppress("DEPRECATION")
        val display = (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay
        @Suppress("DEPRECATION")
        if(display.orientation % 2 == 0) {
            viewPager = findViewById(R.id.keyboards_vp)
            viewPager.adapter = MainActivityAdapter(supportFragmentManager)
        } else {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.base_frame, BaseKeyboardFragment())
                .add(R.id.science_frame, ScienceKeyboardFragment()).commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("expression_text", input.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        input.text = savedInstanceState.getString("expression_text")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i("SWAP_ORIENTATION", "TRUE")
    }

    @NotNull
    override fun onClick(view: View) {
        when(view.id) {
            R.id.res_op -> onSolve()
            R.id.del_op -> onDelete(1)
            R.id.clear_button -> onDelete(input.text.length)
            else -> onAppend((view as Button).text.toString())
        }
    }

    private fun onSolve() {

        if(input.text.isNotEmpty()) {

            val expression = Expression(input.text.toString())
            val text = SpannableStringBuilder("NaN")
            text.clearSpans()
            if(expression.checkSyntax()) {
                val check = expression.errorMessage;
                input.text = expression.calculate().toString()
                val text2 = input.text.toString()
                text2.trim()
                if(text2 == text.toString()) {
                    input.text = ""
                    Toast.makeText(baseContext, "Делить на ноль нельзя!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.i("Syntax error", expression.errorMessage)
                Toast.makeText(baseContext, "Неправильное выражение!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun onDelete(tmp: Int) {
        if(input.text.isNotEmpty()) {
            input.text = input.text.dropLast(tmp)
        }
    }

    private fun onAppend(operation: String) {
        if(functionality.contains(operation)) {
            input.append("$operation(")
        } else {
            input.append(operation)
        }
    }

}
