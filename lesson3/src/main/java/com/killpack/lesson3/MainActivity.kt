package com.killpack.lesson3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bigText = findViewById<EditText>(R.id.textBig)
        val smallText = findViewById<EditText>(R.id.textSmall)
        bigText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var result = s.toString()
                if (result.endsWith(".")) {
                    result = result.substring(0, result.length - 1)
                    switchFocus(bigText, smallText)
                }
                //todo дебильное рещение от зацикливания
                bigText.removeTextChangedListener(this)
                val spaced = insertSpaces(result.replace(" ",""))
                bigText.setText(spaced)
                bigText.addTextChangedListener(this)
                bigText.setSelection(bigText.length())
            }

            override fun afterTextChanged(p0: Editable) {
            }
        })
        bigText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && bigText.text.isEmpty() && smallText.text.isNotEmpty()) {
                switchFocus(bigText, smallText)
            }
            return@setOnKeyListener false
        }
        smallText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && smallText.text.isEmpty() ) {
                switchFocus(smallText, bigText)
            }
            return@setOnKeyListener false
        }
    }
    private fun insertSpaces(src:String) :String{
        val sb =  StringBuilder(src)
        for (i in src.length downTo 0 step 3)
            if (i<src.length){
                sb.insert(i, " ")
            }
        return sb.toString()
    }
    private fun switchFocus(bigText: EditText, smallText: EditText) {
        bigText.clearFocus()
        smallText.requestFocus()
        smallText.isCursorVisible = true
        // если надо поставить курсор в начало копеек
//        smallText.setSelection(0)
    }
}
