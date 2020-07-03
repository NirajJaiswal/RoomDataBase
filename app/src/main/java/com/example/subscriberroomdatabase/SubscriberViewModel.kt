package com.example.subscriberroomdatabase

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subscriberroomdatabase.database.Subscriber
import com.example.subscriberroomdatabase.database.SubscriberRepository
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SubscriberViewModel(private  val repository: SubscriberRepository):ViewModel(),Observable
{
    val subscriber=repository.subscriber
    private var isUpdateOrDelete=false
    private lateinit var subscriberUpdateOrDelete:Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()
    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()
    private val event=MutableLiveData<SingleEventClass<String>>()
    val statusMessage:LiveData<SingleEventClass<String>>
    get() = event



    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }
    fun saveOrUpdate(){

        if(inputName.value==null)
        {
            event.value=SingleEventClass("Name should not be empty")
        }
        else if(inputEmail.value==null)
        {
            event.value=SingleEventClass("Email should not be empty")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches() )
        {
            event.value=SingleEventClass("Email be in correct format")

        }
        else {


            if (isUpdateOrDelete) {
                subscriberUpdateOrDelete.name = inputName.value!!
                subscriberUpdateOrDelete.email = inputEmail.value!!
                update(subscriberUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0, name, email))
                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete)
        {
            delete(subscriberUpdateOrDelete)
        }
        else {
            clearAll()
            event.value=SingleEventClass("Successfully clearAll Subscriber")
        }
    }

    fun insert(subscriber:Subscriber)= viewModelScope.launch {
        repository.insert(subscriber)
       event.value=SingleEventClass("Successfully insert Subscriber")
    }

    fun update(subscriber: Subscriber) = viewModelScope.launch {
        repository.update(subscriber)
        event.value=SingleEventClass("Successfully update Subscriber")
        inputName.value=null
        inputEmail.value=null
        isUpdateOrDelete=false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch {
        repository.delete(subscriber)
        event.value=SingleEventClass("Successfully delete Subscriber")
        inputName.value=null
        inputEmail.value=null
        isUpdateOrDelete=false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"

    }
    fun initUpdateOrDelete(subscriber: Subscriber)
    {
        isUpdateOrDelete=true
        subscriberUpdateOrDelete=subscriber
        inputName.value=subscriber.name
        inputEmail.value=subscriber.email
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    fun clearAll()=viewModelScope.launch {
        repository.deleteAll()
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}