package com.example.expensemanager.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDetailDao{
    @Query("SELECT * FROM `Transaction` WHERE `id` = :id")
    fun getTransaction(id: Long): LiveData<Transaction>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(task: Transaction): Long

    @Update
    suspend fun updateTransaction(task: Transaction)

    @Delete
    suspend fun deleteTransaction(task: Transaction)

}