package com.example.bookreviewapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreviewapp.databinding.ItemBookBinding
import com.example.bookreviewapp.model.Book

class BookAdapter() : ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.binding(currentList[position])
    }
    inner class BookItemViewHolder(private val binding : ItemBookBinding):RecyclerView.ViewHolder(binding.root){
            fun binding(bookModel : Book){
                binding.titleTextView.text = bookModel.title
            }
    }

companion object{
    val diffUtil = object  : DiffUtil.ItemCallback<Book>(){
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem ==newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

    }
}
}