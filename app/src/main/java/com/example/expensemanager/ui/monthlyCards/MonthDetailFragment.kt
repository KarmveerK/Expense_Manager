package com.example.expensemanager.ui.monthlyCards

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.R
import com.example.expensemanager.ui.TransactionAdapter
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_month_detail.*


class MonthDetailFragment : Fragment() {
    lateinit var viewModel: MonthlyCardsViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_month_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MonthlyCardsViewModel::class.java)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val monthYear = MonthDetailFragmentArgs.fromBundle(requireArguments()).id
        val month = monthYear.toString().substring(4)
        val yearName = monthYear.toString().substring(0,4)
        var monthName:String = ""
        when(month){
            "01" ->{
                monthName = "January"
            }
            "02" ->{
                monthName = "February"
            }
            "03" ->{
                monthName = "March"
            }
            "04" ->{
                monthName = "April"
            }
            "05" ->{
                monthName = "May"
            }
            "06" ->{
                monthName = "June"
            }
            "07" ->{
                monthName = "July"
            }
            "08" ->{
                monthName = "August"
            }
            "09" ->{
                monthName = "September"
            }
            "10" ->{
                monthName = "October"
            }
            "11" ->{
                monthName = "November"
            }
            "12" ->{
                monthName = "December"
            }
        }
        val toolbar : MaterialToolbar = requireActivity().findViewById(R.id.addAppBar_cards_details)
        toolbar.title = "Transactions for $monthName $yearName"
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }


        viewModel.setTransactionMonthYear(monthYear)

        with(month_transactions_list){
            layoutManager = LinearLayoutManager(activity)
            adapter = TransactionAdapter {
            }
        }

        viewModel.transactionMonth.observe(viewLifecycleOwner, Observer{
            month_transactions_list.scheduleLayoutAnimation()
            (month_transactions_list.adapter as TransactionAdapter).submitList(it)
        })

        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences("OnboardingDetails", Context.MODE_PRIVATE)
        val monthlyBudget = sharedPreferences.getFloat("monthlyBudget", 0F)
        net_balance.text = monthlyBudget.toString()

        viewModel.sumMonth.observe(viewLifecycleOwner, Observer {
            progressBalance.max = monthlyBudget.toInt()
            progressBalance.progress = monthlyBudget.toInt() - (monthlyBudget+it).toInt()
            amount_saved.text = ((monthlyBudget+it).toString())
            amount_spent.text = (monthlyBudget-(monthlyBudget+it)).toString()
        })


    }
}