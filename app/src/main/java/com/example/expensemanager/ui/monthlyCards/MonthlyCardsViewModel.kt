package com.example.expensemanager.ui.monthlyCards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.expensemanager.data.Transaction
import com.example.expensemanager.data.TransactionListRepository

class MonthlyCardsViewModel(application: Application):AndroidViewModel(application) {
    private val repo: TransactionListRepository = TransactionListRepository(application)

    private val _transactionMonthYear = MutableLiveData<Long>(0)
    val transactionMonthYear: LiveData<Long>
        get() = _transactionMonthYear
    //private val _date = MutableLiveData<String>()

    val transactionMonth: LiveData<List<Transaction>> =
            Transformations.switchMap(_transactionMonthYear) { id ->
                repo.getTransactionMonth(id)
            }
    fun setTransactionMonthYear(id: Long){
        if(_transactionMonthYear.value != id ) {
            _transactionMonthYear.value = id
        }
    }
    val months: LiveData<List<Long>> =
            repo.getMonths()

    val sumMonth = Transformations.switchMap(_transactionMonthYear) { id ->
        repo.getSumMonth(id)
    }
}