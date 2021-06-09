package com.example.bookreviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookreviewapp.adapter.BookAdapter
import com.example.bookreviewapp.api.BookService
import com.example.bookreviewapp.databinding.ActivityMainBinding
import com.example.bookreviewapp.model.BestSellerDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: BookAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks("F8E7CACAE6156007B5E0A5B40CDF46B1BEB38AB563A5171300B90ADB8C94F6AA")
            .enqueue(object : Callback<BestSellerDto>{
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    //성공 처리

                    if(response.isSuccessful.not()){
                        Log.e(TAG,"NOT Success")
                        return
                    }

                    response.body()?.let {
                        Log.d(TAG,it.toString())
                        it.books.forEach { book->
                            Log.d(TAG,book.toString())
                        }

                        adapter.submitList(it.books)
                    }
                    }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                        // 실패처리

                    Log.e(TAG,t.toString())
                    }

            })


    }

    fun initBookRecyclerView(){
         adapter = BookAdapter()
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter

    }
    companion object {
        private const val TAG = "MainActivity"
    }
}