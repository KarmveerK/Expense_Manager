package com.example.expensemanager.ui

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_all_transactions.*
import kotlinx.android.synthetic.main.fragment_transaction_list.*

class AllTransactionsFragment : Fragment() {
    private lateinit var viewModel: TransactionListViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_transactions, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)
                .get(TransactionListViewModel::class.java)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val toolbar : MaterialToolbar = requireActivity().findViewById(R.id.addAppBar_all)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        with(all_transactions){
            layoutManager = LinearLayoutManager(activity)
            adapter = TransactionAdapter {
                findNavController().navigate(
                        AllTransactionsFragmentDirections.actionAllTransactionsFragmentToTransactionDetailFragment(it)
                )
            }
        }

        add_transactions_all.setOnClickListener{
            findNavController().navigate(
                    //here id is passed 0 because the transaction is being added the first time
                    AllTransactionsFragmentDirections.actionAllTransactionsFragmentToTransactionDetailFragment(
                            0
                    )
            )
        }

        viewModel.transactions.observe(viewLifecycleOwner, Observer{
            all_transactions.scheduleLayoutAnimation()
            (all_transactions.adapter as TransactionAdapter).submitList(it)
        })
    }
}