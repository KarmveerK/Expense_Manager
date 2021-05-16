package com.example.expensemanager.ui

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.R
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_onboarding.*
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import kotlinx.android.synthetic.main.set_balance_details.*
import kotlinx.android.synthetic.main.set_balance_details.view.*
import org.eazegraph.lib.models.PieModel


class TransactionListFragment : Fragment() {
    //declaring the view model
    private lateinit var viewModel: TransactionListViewModel
    var cashAmount: Float = 0F
    var bankAmount: Float = 0F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setHasOptionsMenu(true)
        //(activity as AppCompatActivity?)!!.setSupportActionBar(addAppBar)
        viewModel = ViewModelProvider(this)
                .get(TransactionListViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //recycler view for showing all the transactions
        val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences("OnboardingDetails", Context.MODE_PRIVATE)


        val toolbar : MaterialToolbar = requireActivity().findViewById(R.id.addAppBar)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_people_24)
        toolbar.setNavigationOnClickListener {
            val boolean = sharedPreferences.getBoolean("isLogin",false)
            sharedPreferences.edit().putBoolean("isLogin",false).apply()
            findNavController().navigate(TransactionListFragmentDirections.actionTransactionListFragmentToOnboardingFragment())
        }
        //appBar()


        with(transaction_list) {
            layoutManager = LinearLayoutManager(activity)
            adapter = TransactionAdapter {
                findNavController().navigate(
                        TransactionListFragmentDirections.actionTransactionListFragmentToTransactionDetailFragment(
                                it
                        )
                )
            }
        }
        //code for the floating action button in the main screen
        add_transaction.setOnClickListener {
            findNavController().navigate(
                    //here id is passed 0 because the transaction is being added the first time
                    TransactionListFragmentDirections.actionTransactionListFragmentToTransactionDetailFragment(
                            0
                    )
            )
        }

        addAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.calendar_button -> {
                    findNavController().navigate(TransactionListFragmentDirections.actionTransactionListFragmentToCalanderViewFragment())
                    true
                }
                R.id.monthly_cards -> {
                    findNavController().navigate(TransactionListFragmentDirections.actionTransactionListFragmentToMonthlyCardsFragment())
                    true
                }
                else -> false
            }
        }
        see_all_transactions.setOnClickListener {
            findNavController().navigate(TransactionListFragmentDirections.actionTransactionListFragmentToAllTransactionsFragment())
        }


        //submitting the new list of upcoming Transactions after getting it from the db
        viewModel.upcomingTransactions.observe(viewLifecycleOwner, Observer {
            //transaction_list.scheduleLayoutAnimation()
            (transaction_list.adapter as TransactionAdapter).submitList(it)
        })



        val monthlyBudget = sharedPreferences.getFloat("monthlyBudget", 0F)
        var totalBalance = monthlyBudget * 12
        net_balance.text = totalBalance.toString()
        Log.d("netbalance", totalBalance.toString())
        //the net balance (yearly) is calculated wrt the transactions already done
        viewModel.sumOfTransactions.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                totalBalance += it
                net_balance.text = totalBalance.toString()
            }
        })
        val budgetPreferences: SharedPreferences =
                this.requireActivity().getSharedPreferences("Balance_details", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = budgetPreferences.edit()

        //setting pie chart initially to 0
        setPieChart()
        //observing the cash details and the bank details to update the text view and the pie chart
        observeBalance(budgetPreferences, editor)
        //GraphCardView code
        //button for setting the balance details
        set_balance_details.setOnClickListener {
            setBalanceDetails(budgetPreferences, editor)
        }
    }

    //dialog box for setting the balance details
    private fun setBalanceDetails(budgetPreferences: SharedPreferences, editor: SharedPreferences.Editor) {

        val dialog = LayoutInflater.from(requireContext()).inflate(
                R.layout.set_balance_details,
                null
        )
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext())
                .setView(dialog)
        //show dialog
        val mAlertDialog = mBuilder.show()
        checkValues(dialog)
        dialog.save_details.setOnClickListener {
            cashAmount = dialog.cash_amount.editText?.text.toString().toFloat()
            bankAmount = dialog.bank_amount.editText?.text.toString().toFloat()
            //saving the cashAmount and bankAmount to shared preferences for future use
            editor.putFloat("cashAmount", cashAmount).apply()
            editor.putFloat("bankAmount", bankAmount).apply()
            cash.text = cashAmount.toString()
            bank.text = bankAmount.toString()
            //setting the pie chart with new values
            setPieChart()
            mAlertDialog.dismiss()
        }
        dialog.cancel_details.setOnClickListener { mAlertDialog.dismiss() }
        mAlertDialog.show()
    }

    private fun checkValues(dialog: View) {
//        val sharedPreferences : SharedPreferences = this.requireActivity().getSharedPreferences("OnboardingDetails", Context.MODE_PRIVATE)
//        var yearlyBudget = sharedPreferences.getFloat("monthlyBudget",0f)*12
        dialog.save_details.isEnabled = false
        val boardingTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val cashAmount = dialog.cash_amount.editText?.text.toString()
                val bankAmount = dialog.bank_amount.editText?.text.toString()

                if (cashAmount.isEmpty()) {
                    dialog.save_details.isEnabled = false
                    dialog.cash_amount.error = "Fields cannot be empty!"
                    dialog.cash_amount.isEndIconVisible = true
                }
                else if (bankAmount.isEmpty()) {
                    dialog.save_details.isEnabled = false
                    dialog.bank_amount.error = "Fields cannot be empty!"
                    dialog.bank_amount.isEndIconVisible = true
                }
                else {
                    dialog.save_details.isEnabled = true
                    dialog.cash_amount.isEndIconVisible = false
                    dialog.bank_amount.isEndIconVisible = false
                    dialog.cash_amount.error = null
                    dialog.bank_amount.error = null
                }
            }
        }
        dialog.cash_amount.editText?.addTextChangedListener(boardingTextWatcher)
        dialog.bank_amount.editText?.addTextChangedListener(boardingTextWatcher)
    }

    private fun observeBalance(budgetPreferences: SharedPreferences, editor: SharedPreferences.Editor) {
        //getting the cashAmount and bankAmount and updating the views with live data

        var cashAmount = budgetPreferences.getFloat("cashAmount", 0F)
        var bankAmount = budgetPreferences.getFloat("bankAmount", 0F)
        //                cash.text = "${cashAmount}"
        //                bank.text = "${bankAmount}"
        if (cashAmount < 0)
            cash.text = "0.0"
        else cash.text = "${cashAmount}"
        if (bankAmount < 0)
            bank.text = "0.0"
        else
            bank.text = "${bankAmount}"
        viewModel.cashAmount.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                cashAmount += it
//                cash.text = "${cashAmount}"
//                bank.text = "${bankAmount}"
                if (cashAmount < 0)
                    cash.text = "0.0"
                else cash.text = "${cashAmount}"
                if (bankAmount < 0)
                    bank.text = "0.0"
                else
                    bank.text = "${bankAmount}"
                setPieChart()
            }
        })

        viewModel.bankAmount.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                bankAmount += it
//                cash.text = "${cashAmount}"
//                bank.text = "${bankAmount}"
                if (cashAmount < 0)
                    cash.text = "0.0"
                else cash.text = "${cashAmount}"
                if (bankAmount < 0)
                    bank.text = "0.0"
                else
                    bank.text = "${bankAmount}"
                setPieChart()
            }
        })
        setPieChart()
    }

    //https://www.geeksforgeeks.org/how-to-add-a-pie-chart-into-an-android-application/ use this for reference
    private fun setPieChart() {
        val cashAmount = cash.text.toString().toFloat()
        val bankAmount = bank.text.toString().toFloat()
        val pieEntries = arrayListOf<PieEntry>()
        pieEntries.add(PieEntry(cashAmount))
        pieEntries.add(PieEntry(bankAmount))
        pieChart.animateXY(1000, 1000)

        // setup PieChart Entries Colors
        val pieDataSet = PieDataSet(pieEntries, "This is Pie Chart Label")
        pieDataSet.setColors(
                ContextCompat.getColor(requireActivity(), R.color.blue1),
                ContextCompat.getColor(requireActivity(), R.color.blue2)
        )
        val pieData = PieData(pieDataSet)
        // setip text in pieChart centre
        //piechart.setHoleColor(R.color.teal_700)
        pieChart.setHoleColor(getColorWithAlpha(Color.BLACK, 0.0f))
        // hide the piechart entries tags
        pieChart.legend.isEnabled = false
//        now hide the description of piechart
        pieChart.description.isEnabled = false
        pieChart.description.text = "Expanses"
        pieChart.holeRadius = 40f
        // this enabled the values on each pieEntry
        pieData.setDrawValues(true)
        pieChart.data = pieData

    }

    fun getColorWithAlpha(color: Int, ratio: Float): Int {
        var newColor = 0
        val alpha = Math.round(Color.alpha(color) * ratio)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        newColor = Color.argb(alpha, r, g, b)
        return newColor
    }



}
