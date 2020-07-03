package com.example.subscriberroomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subscriberroomdatabase.database.Subscriber
import com.example.subscriberroomdatabase.database.SubscriberDataBase
import com.example.subscriberroomdatabase.database.SubscriberRepository
import com.example.subscriberroomdatabase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SubscriberViewModel
    private lateinit var adapter: SubscriberAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        val dao=SubscriberDataBase.getInstance(applicationContext).subscriberDAO
        val repository=SubscriberRepository(dao)
        val factory=SubscriberViewModelFactory(repository)
        viewModel=ViewModelProvider(this,factory).get(SubscriberViewModel::class.java)
        binding.myViewModel=viewModel
        binding.lifecycleOwner=this
        initRecyclerView()
        showStatus()




    }

    private fun showStatus() {
        viewModel.statusMessage.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun initRecyclerView()
    {
        binding.subscriberRecyclerView.layoutManager=LinearLayoutManager(this)
         adapter=SubscriberAdapter { selectedItem:Subscriber->onClickItem(selectedItem)}
        binding.subscriberRecyclerView.adapter=adapter
        displaySubscribersList()

    }
    private fun displaySubscribersList(){
        viewModel.subscriber.observe(this, Observer {
            Log.i("MYTAG",it.toString())
        adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun onClickItem(subscriber: Subscriber)
    {
        Toast.makeText(this,"selected item name is ${subscriber.name}",Toast.LENGTH_SHORT).show()
        viewModel.initUpdateOrDelete(subscriber)
    }
}