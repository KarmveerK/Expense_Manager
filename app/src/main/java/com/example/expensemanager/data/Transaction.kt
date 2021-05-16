package com.example.expensemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionMode{
    CASH,BANK
}
enum class TransactionCategory{
    EDUCATION,BILLS,HEALTH,FOOD,TRANSPORT,GAMING,RECHARGE,ENTERTAINMENT,OTHERS
}
@Entity(tableName = "Transaction")
data class Transaction(@PrimaryKey(autoGenerate = true) val id: Long,
                       val title: String,
                       val amount: Float,
                       val date:String,
                       val transactionMode: Int,
                       val transactionType:Int,
                       val monthYear: Long,
                       val fromDate:String,
                       val toDate:String,
                       val comments:String,
                       val transactionCategory:Int
                )
