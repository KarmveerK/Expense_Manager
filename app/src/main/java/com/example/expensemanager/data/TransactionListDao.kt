package com.example.expensemanager.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query


@Dao
interface TransactionListDao{
    @Query("SELECT * FROM `Transaction`")
    fun getTransactions(): LiveData<List<Transaction>>

    @Query("SELECT Sum(amount) FROM `Transaction` WHERE transactionMode = 0")
    fun getCashAmount(): LiveData<Float>

    @Query("SELECT Sum(amount) FROM `Transaction` WHERE transactionMode = 1")
    fun getBankAmount(): LiveData<Float>

    @Query("SELECT * FROM `Transaction` WHERE transactionType = 0")
    fun getIncomeTransactions(): LiveData<List<Transaction>>

    @Query("SELECT * FROM `Transaction` WHERE transactionType = 1")
    fun getExpenseTransactions(): LiveData<List<Transaction>>

    @Query("SELECT Sum(amount) FROM `Transaction`")
    fun getSumOfTransactions(): LiveData<Float>

    @Query("SELECT * FROM `transaction` WHERE date=:date")
    fun getTransactionByDate(date: String): LiveData<List<Transaction>>

    @Query("Select * from `transaction` WHERE  date >= date('now') order by date ASC")
    fun getUpcomingTransactions(): LiveData<List<Transaction>>

    @Query("SELECT * FROM `Transaction` WHERE date ")
    fun getTransactionsByMonth(): LiveData<List<Transaction>>



    @Query("SELECT * FROM `transaction` WHERE monthYear = :monthYear ORDER BY id")
    fun getTransactionMonth(monthYear: Long): LiveData<List<Transaction>>

    @Query("SELECT SUM(amount) FROM `transaction` WHERE monthYear = :monthYear")
    fun getSumMonth(monthYear: Long): LiveData<Float>

    @Query("SELECT monthYear FROM `Transaction` GROUP BY monthYear")
    fun getMonths():LiveData<List<Long>>
}