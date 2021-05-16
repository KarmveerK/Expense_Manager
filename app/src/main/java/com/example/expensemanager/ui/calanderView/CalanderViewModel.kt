package com.example.expensemanager.ui.calanderView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.expensemanager.data.Transaction
import com.example.expensemanager.data.TransactionListRepository

class CalanderViewModel(application: Application) :AndroidViewModel(application){
    private val repo: TransactionListRepository = TransactionListRepository(application)

    private val _date = MutableLiveData<String>()

    val transactionsByDate: LiveData<List<Transaction>> = Transformations.switchMap(_date) { newDate ->
        repo.getTransactionByDate(newDate)
    }

    fun setDate(date: String) {
        _date.value = date
    }
}