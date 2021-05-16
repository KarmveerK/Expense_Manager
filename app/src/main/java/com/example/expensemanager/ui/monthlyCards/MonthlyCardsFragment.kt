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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.R
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_monthly_cards.*
import kotlinx.android.synthetic.main.fragment_transaction_list.*


class MonthlyCardsFragment : Fragment() {
    lateinit var viewModel:MonthlyCardsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monthly_cards, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MonthlyCardsViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val toolbar : MaterialToolbar = requireActivity().findViewById(R.id.addAppBar_cards)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences("OnboardingDetails", Context.MODE_PRIVATE)
        with(months_list) {
            layoutManager = LinearLayoutManager(activity)
            adapter = MonthCardsAdapter ({
                findNavController().navigate(MonthlyCardsFragmentDirections.actionMonthlyCardsFragmentToMonthDetailFragment(it))
            }, sharedPreferences, requireContext(),this@MonthlyCardsFragment,viewLifecycleOwner)
        }

        viewModel.months.observe(viewLifecycleOwner, Observer {
            //transaction_list.scheduleLayoutAnimation()
            (months_list.adapter as MonthCardsAdapter).submitList(it)
        })
    }
}