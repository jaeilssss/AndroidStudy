package com.example.bookreviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.bookreviewapp.adapter.BookAdapter
import com.example.bookreviewapp.adapter.HistoryAdapter
import com.example.bookreviewapp.api.BookService
import com.example.bookreviewapp.databinding.ActivityMainBinding
import com.example.bookreviewapp.model.BestSellerDto
import com.example.bookreviewapp.model.History
import com.example.bookreviewapp.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookService : BookService
    private lateinit var adapter: BookAdapter
    private lateinit var db : AppDatabase
    private lateinit var historyAdapter : HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()

        initHistoryRecyclerView()
        db  = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

         bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks(getString(R.string.interParkAPIKey))
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

    private fun search(keyword : String){
        bookService.getBooksByName(getString(R.string.interParkAPIKey),keyword)
             .enqueue(object : Callback<SearchBookDto>{
            override fun onResponse(
                call: Call<SearchBookDto>,
                response: Response<SearchBookDto>
            ) {
                //성공 처리
               hideHistoryView()
                saveSearchKeyword(keyword)

                if(response.isSuccessful.not()){
                    Log.e(TAG,"NOT Success")
                    return
                }
                adapter.submitList(response.body()?.books.orEmpty())

            }
            override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                // 실패처리
                hideHistoryView()
                Log.e(TAG,t.toString())
            }

        })
    }


    private fun initSearchEditText(){

        binding.searchEditText.setOnKeyListener { view, keyCode, keyEvent ->

            if(keyCode==KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN){
                search(binding.searchEditText.text.toString())
                // setOnKeyListener는 반환값이 있어야한다!
                return@setOnKeyListener true
            }

            return@setOnKeyListener false

        }
        binding.searchEditText.setOnTouchListener { view, motionEvent ->

            if(motionEvent.action == MotionEvent.ACTION_DOWN){
                showHistoryView()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }
    fun initBookRecyclerView(){
         adapter = BookAdapter()
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter

    }

    private fun initHistoryRecyclerView(){
        historyAdapter = HistoryAdapter(historyDeleteClickedListener = {
            deleteSearchKeyword(it)
        })

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
        initSearchEditText()
    }
    private fun showHistoryView(){
        Thread{
            val keywords = db.historyDao().getAll().reversed()
            runOnUiThread{
                binding.historyRecyclerView.isVisible=true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()
    }

    private fun hideHistoryView(){
        binding.historyRecyclerView.isVisible = false
    }
    private fun saveSearchKeyword(keyword: String){
        Thread{
            db.historyDao().insertHistory(History(null,keyword))
        }.start()
    }
    private fun deleteSearchKeyword(keyword: String){
        Thread{
            db.historyDao().delete(keyword)
            showHistoryView()
            //todo view 갱신
        }.start()
    }
    companion object {
        private const val TAG = "MainActivity"
    }
}