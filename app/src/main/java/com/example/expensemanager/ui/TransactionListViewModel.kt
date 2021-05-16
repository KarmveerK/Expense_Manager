package com.example.expensemanager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.expensemanager.data.*

class TransactionListViewModel(application: Application): AndroidViewModel(application){
    private val repo: TransactionListRepository =
        TransactionListRepository(application)

    val upcomingTransactions:LiveData<List<Transaction>> =
        repo.getUpcomingTransactions()
    val transactions: LiveData<List<Transaction>> =
        repo.getTransactions()

    val cashAmount: LiveData<Float>
        get() = repo.getCashAmount()

    val bankAmount:LiveData<Float>
        get() = repo.getBankAmount()

    val sumOfTransactions:LiveData<Float>
        get() = repo.getSumOfTransactions()

}
