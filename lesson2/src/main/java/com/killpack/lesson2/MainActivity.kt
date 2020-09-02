package com.killpack.lesson2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.killpack.lesson2.databinding.MainLayoutBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: MainLayoutBinding
    val myName= MyName("Миша")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.main_layout)
        binding.myName=myName
        binding.doneBtn.setOnClickListener { setNickName(it) }
    }

    private fun setNickName(view: View) {
        binding.apply {
            myName?.nick = nicknameEdit.text.toString()
            invalidateAll()
            nicknameEdit.visibility = View.GONE
            view.visibility = View.GONE
            nicknameText.visibility = View.VISIBLE
        }
        val imm =getSystemService(  INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }
}
