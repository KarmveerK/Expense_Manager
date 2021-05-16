package com.example.expensemanager.data

import android.app.Application
import androidx.lifecycle.LiveData


class TransactionListRepository(context: Application){
    private val transactionListDao: TransactionListDao = TransactionDatabase.getDatabase(context).transactionListDao()

    fun getTransactions(): LiveData<List<Transaction>> {
        return transactionListDao.getTransactions()
    }
    fun getCashAmount(): LiveData<Float>{
        return transactionListDao.getCashAmount()
    }
    fun getBankAmount(): LiveData<Float>{
        return transactionListDao.getBankAmount()
    }
    fun getSumOfTransactions():LiveData<Float>{
        return transactionListDao.getSumOfTransactions()
    }
    fun getTransactionByDate(date: String): LiveData<List<Transaction>> {
        return transactionListDao.getTransactionByDate(date)
    }
    fun getUpcomingTransactions():LiveData<List<Transaction>>{
        return transactionListDao.getUpcomingTransactions()
    }

    fun getTransactionMonth(monthYear: Long): LiveData<List<Transaction>> {
        return transactionListDao.getTransactionMonth(monthYear)
    }

    fun getSumMonth(monthYear: Long): LiveData<Float> {
        return transactionListDao.getSumMonth(monthYear)
    }

    fun getMonths():LiveData<List<Long>>{
        return transactionListDao.getMonths()
    }
}