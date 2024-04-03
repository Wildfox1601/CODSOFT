package com.txn.quotify

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.txn.quotify.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        getQuote()

        binding.nextBtn.setOnClickListener {
            getQuote()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getQuote(){
        setInProgress(true)

        GlobalScope.launch {
            try {
                val response = RetrofitInstance.quoteApi.getRandomQuote()
                runOnUiThread {
                    setInProgress(false)
                    response.body()?.first()?.let {
                        setUI(it)
                    }
                }
            }catch (e : Exception){
                runOnUiThread {
                    setInProgress(false)
                    Toast.makeText(applicationContext,"Check Internet Connection",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUI(quote : QuoteModel){
        binding.quoteTv.text = quote.q
        binding.authorTv.text = quote.a

    }

    private fun setInProgress(inProgress : Boolean){
        if(inProgress){
            binding.progressBar.visibility = View.VISIBLE
            binding.nextBtn.visibility = View.GONE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.nextBtn.visibility = View.VISIBLE
        }
    }
}