package com.killpackoff.lesson2_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val big = findViewById<EditText>(R.id.editTextBig)
        val smal = findViewById<EditText>(R.id.editTextSmal)
        big.addTextChangedListener (object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    println(p1)
                }


                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    println(p1)
                }

                override fun afterTextChanged(editable: Editable?) {

                }
            })

        big.setOnKeyListener { view, i, keyEvent ->
            if (keyEvent.keyCode==KeyEvent.KEYCODE_1){
                big.clearFocus()
                smal.requestFocus()
                smal.isCursorVisible = true
            }
           return@setOnKeyListener true
        }
    }
}
