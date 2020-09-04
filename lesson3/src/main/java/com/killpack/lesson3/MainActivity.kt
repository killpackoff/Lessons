package com.killpack.lesson3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.EditText
import java.lang.Math.max
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    private val regexStartWithZero = Regex("^0+")
    private val regexSpace = Regex(" +")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bigText = findViewById<EditText>(R.id.textBig)
        val smallText = findViewById<EditText>(R.id.textSmall)
        bigText.addTextChangedListener(object : TextWatcher {


            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var result = s.toString()
                if (result.contains(".")) {
                    result = result.replace(".", "")
                    switchFocus(bigText, smallText)
                }
                //todo дебильное рещение от зацикливания
                bigText.removeTextChangedListener(this)
                var zeroOffset = 0
                val zeroSubstring = regexStartWithZero.find(result)
                if (zeroSubstring != null && zeroSubstring.value.isNotEmpty()) {
                    result = result.replace(regexStartWithZero, "")
                    zeroOffset = zeroSubstring.value.length
                }
                val substring =
                    bigText.text.toString().substring(0, max(bigText.selectionStart - 1, 0))
                val deltaSelection = regexSpace.findAll(substring).count()
                val srcSelection = kotlin.math.max(bigText.selectionStart - deltaSelection, 0)
                val src = result.replace(" ", "")
                val spaced = insertSpaces(src, srcSelection)
                bigText.setText(spaced.first)
                //
                bigText.addTextChangedListener(this)
                val newSelection = srcSelection + spaced.second - zeroOffset
                if (newSelection > 0 && newSelection <= bigText.text.length) {
                    bigText.setSelection(newSelection)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
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
            if (keyCode == KeyEvent.KEYCODE_DEL && smallText.text.isEmpty()) {
                switchFocus(smallText, bigText)
                bigText.setSelection(bigText.text.length)
            }
            return@setOnKeyListener false
        }

        bigText.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun sendAccessibilityEvent(host: View?, eventType: Int) {
                super.sendAccessibilityEvent(host, eventType)
                if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
                    if (bigText.selectionStart > 0) {
                        if (bigText.text[bigText.selectionStart - 1] == ' ') {
                            bigText.setSelection(bigText.selectionStart - 1)
                        }
                    }
                }
            }
        }
    }

    private fun insertSpaces(src: String, position: Int): Pair<String?, Int> {
        val sb = StringBuilder(src)
        var deltaPos = 0
        for (i in src.length downTo 0 step 3) {
            if (i > 0 && i < src.length) {
                sb.insert(i, " ")
                if (i < position) {
                    deltaPos++
                }
            }
        }
        return Pair(sb.toString(), deltaPos)
    }

    private fun switchFocus(bigText: EditText, smallText: EditText) {
        bigText.clearFocus()
        smallText.requestFocus()
        smallText.isCursorVisible = true
    }
}
