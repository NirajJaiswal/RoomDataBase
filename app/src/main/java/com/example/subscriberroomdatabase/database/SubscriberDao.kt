package com.example.subscriberroomdatabase.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDao {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber):Long

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber)

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber)

    @Query("DELETE FROM subscriber_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM subscriber_table")
    fun getAllSubscribers(): LiveData<List<Subscriber>>
}