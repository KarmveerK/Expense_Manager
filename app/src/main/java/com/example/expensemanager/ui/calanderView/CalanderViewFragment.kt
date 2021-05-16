package com.example.expensemanager.ui.calanderView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.R
import com.example.expensemanager.ui.TransactionAdapter
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_calander_view.*

class CalanderViewFragment : Fragment() {
    var date: String = ""
    private lateinit var viewModel: CalanderViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calander_view, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalanderViewModel::class.java)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Log.d("date now", )
        val toolbar : MaterialToolbar = requireActivity().findViewById(R.id.addAppBar_calender)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }


        calender?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.


            val stringMonth:String
            val stringDay:String
            if (month+1<10){
                stringMonth = "0${month+1}"
            }
            else{
                stringMonth = "${month+1}"
            }
            if (dayOfMonth<10){
                stringDay = "0${dayOfMonth}"
            }
            else{
                stringDay = "${dayOfMonth}"
            }
            date = "$year-$stringMonth-$stringDay"
            viewModel.setDate(date)
            with(calander_recycler_view) {
                layoutManager = LinearLayoutManager(activity)
                adapter = TransactionAdapter {
                }
            }
            viewModel.transactionsByDate.observe(viewLifecycleOwner, {
                (calander_recycler_view.adapter as TransactionAdapter).submitList(it)
            })

        }
    }
}