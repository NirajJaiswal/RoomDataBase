package com.example.subscriberroomdatabase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.subscriberroomdatabase.database.Subscriber
import com.example.subscriberroomdatabase.databinding.ListItemBinding

class SubscriberAdapter(private val clickListener:(Subscriber)->Unit):RecyclerView.Adapter<MyViewHolder>()
{
    private val subscriberList=ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val binding:ListItemBinding=DataBindingUtil.inflate(layoutInflater,R.layout.list_item,parent,false)
        return MyViewHolder(binding)

    }

    override fun getItemCount(): Int {
         return subscriberList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      holder.bind(subscriberList[position],clickListener)
    }
    fun setList(subscriber: List<Subscriber>)
    {
        subscriberList.clear()
        subscriberList.addAll(subscriber)
    }

}
class MyViewHolder(private val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root)
{
    fun bind(subscriber: Subscriber,clickListener:(Subscriber)->Unit)
    {
        binding.tvName.text=subscriber.name
        binding.tvEmail.text=subscriber.email
        binding.listItemLayout.setOnClickListener{
            clickListener(subscriber)
        }
    }
}