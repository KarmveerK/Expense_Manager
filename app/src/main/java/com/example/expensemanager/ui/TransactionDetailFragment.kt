package com.example.expensemanager.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expensemanager.R
import com.example.expensemanager.data.Transaction
import com.example.expensemanager.data.TransactionCategory
import com.example.expensemanager.data.TransactionMode
import com.google.android.material.appbar.MaterialToolbar


import kotlinx.android.synthetic.main.fragment_transaction_detail.*
import java.text.SimpleDateFormat
import java.util.*


class TransactionDetailFragment : Fragment() {

    private lateinit var viewModel: TransactionDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)
            .get(TransactionDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val toolbar : MaterialToolbar = requireActivity().findViewById(R.id.addAppBar_detail)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        dateselect.editText?.transformIntoDatePicker(requireContext(), "yyyy-MM-dd")
        dateselect.editText?.transformIntoDatePicker(requireContext(), "yyyy-MM-dd", Date())

        from_date_select.editText?.transformIntoDatePicker(requireContext(), "dd/MM/yyyy")
        from_date_select.editText?.transformIntoDatePicker(requireContext(), "dd/MM/yyyy", Date())

        to_date_select.editText?.transformIntoDatePicker(requireContext(), "dd/MM/yyyy")
        to_date_select.editText?.transformIntoDatePicker(requireContext(), "dd/MM/yyyy", Date())

        val transactionModes = mutableListOf<String>()
        TransactionMode.values().forEach { transactionModes.add(it.name)}
        val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, transactionModes)
        transaction_mode.adapter = arrayAdapter

        transaction_mode?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                updateTaskPriorityView(position)
            }
        }

        val transactionCategories = mutableListOf<String>()
        TransactionCategory.values().forEach { transactionCategories.add(it.name)}
        val arrayAdapter1 = ArrayAdapter(requireActivity(), R.layout.spinner_item, transactionCategories)
        transaction_categry.adapter = arrayAdapter1

        transaction_categry?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                updateTaskPriorityView(position)
            }
        }

        check_box.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                from_date_select.isEnabled = true
                to_date_select.isEnabled = true
                from_date_select.editText?.setText("From Date")
                to_date_select.editText?.setText("To Date")
            }else{
                from_date_select.isEnabled = false
                to_date_select.isEnabled = false
                from_date_select.editText?.setText("From Date")
                to_date_select.editText?.setText("To Date")
                from_date_select.editText?.setText("")
                to_date_select.editText?.setText("")
            }
        }

        val id = TransactionDetailFragmentArgs.fromBundle(requireArguments()).id
        viewModel.setTransactionId(id)

        viewModel.transaction.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                disableFields()
            }
            it?.let{ setData(it) }
        })

        save_transaction_income.setOnClickListener {
            val isNull = checkNullValues()
            if(isNull)
                saveTaskIncome()

        }
        save_transaction_expense.setOnClickListener {
            val isNull = checkNullValues()
            if(isNull)
                saveTaskExpense()

        }



//        delete_transaction.setOnClickListener {
//            deleteTask()
//        }

        addAppBar_detail.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){

                R.id.delete_button-> {
                    val builder = AlertDialog.Builder(requireContext())
                    //set title for alert dialog
                    builder.setTitle("DELETE TRANSACTION")
                    //set message for alert dialog
                    builder.setMessage("Are you sure you want to delete this transaction?")
                    //builder.setIcon(android.R.drawable.ic_dialog_alert)

                    //performing positive action
                    builder.setPositiveButton("Yes"){dialogInterface, which ->
                        //Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
                        deleteTask()
                    }
                    //performing cancel action
                    builder.setNeutralButton("Cancel"){dialogInterface , which ->
                        //Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
                    }
                    //performing negative action
//                    builder.setNegativeButton("No"){dialogInterface, which ->
//                        Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
//                    }
                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()

                    true
                }
                R.id.edit_button->{
                    enableFields()
                    true
                }
                else -> false
            }
        }

    }

    private fun setData(transaction: Transaction){
        transaction_title.editText?.setText(transaction.title)
        transaction_amount.editText?.setText(transaction.amount.toString())
        transaction_mode.setSelection(transaction.transactionMode)
        dateselect.editText?.setText(transaction.date)
        from_date_select.editText?.setText(transaction.fromDate)
        to_date_select.editText?.setText(transaction.toDate)
        transaction_comment.editText?.setText(transaction.comments)
        transaction_categry.setSelection(transaction.transactionCategory)
    }


    //saves data in the database
    private fun saveTaskIncome(){
        val title = transaction_title.editText?.text.toString()
        val amount = transaction_amount.editText?.text.toString()
        val transactionMode = transaction_mode.selectedItemPosition
        val transactionCategory = transaction_categry.selectedItemPosition
        val selectDate = dateselect.editText?.text.toString()
        val fromDate = from_date_select.editText?.text.toString()
        val toDate = to_date_select.editText?.text.toString()
        val comments = transaction_comment.editText?.text.toString()
        val transaction = Transaction(viewModel.transactionId.value!!, title, amount.toFloat(),selectDate,transactionMode,0,(selectDate.substring(0, 4) + selectDate.substring(5, 7)).toLong(),fromDate,toDate,comments,transactionCategory)   //change the date here!!!!!!!!!!!
        viewModel.saveTransaction(transaction)

        requireActivity().onBackPressed()
    }
    private fun saveTaskExpense(){
        val title = transaction_title.editText?.text.toString()
        val amount = (0-transaction_amount.editText?.text.toString().toFloat()).toString()
        val transactionMode = transaction_mode.selectedItemPosition
        val transactionCategory = transaction_categry.selectedItemPosition
        val selectDate = dateselect.editText?.text.toString()
        val fromDate = from_date_select.editText?.text.toString()
        val toDate = to_date_select.editText?.text.toString()
        val comments = transaction_comment.editText?.text.toString()
        val transaction = Transaction(viewModel.transactionId.value!!, title, amount.toFloat(),selectDate,transactionMode,1,(selectDate.substring(0, 4) + selectDate.substring(5, 7)).toLong(),fromDate,toDate,comments,transactionCategory)   //change the date here!!!!!!!!!!!
        viewModel.saveTransaction(transaction)

        requireActivity().onBackPressed()
    }
    private fun deleteTask(){
        viewModel.deleteTransaction()

        requireActivity().onBackPressed()
    }

    fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val sdf = SimpleDateFormat(format, Locale.UK)
                    setText(sdf.format(myCalendar.time))
                }

        setOnClickListener {
            DatePickerDialog(
                    context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
//                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }
    // Checking for null values
    private fun checkNullValues(): Boolean {
        val name = transaction_title.editText?.text.toString()
        val amount = transaction_amount.editText?.text.toString()
        val date = dateselect.editText?.text.toString()
        if(name==""||amount==""||date==""){
            Toast.makeText(
                    context,
                    "Please fill all the mandatory fields",
                    Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    // Enabling fields
    private fun enableFields() {
        transaction_title.isEnabled = true
        transaction_amount.isEnabled = true
        dateselect.isEnabled = true
//        recurring_to_date.isEnabled = true
//        recurring_from_date.isEnabled = true
//        recurring_transaction.isEnabled = true
        transaction_mode.isEnabled = true
//        transaction_type_spinner_layout.isEnabled = true
//        comments.isEnabled = true
        save_transaction_expense.isEnabled = true
        save_transaction_income.isEnabled = true
    }

    // Disabling fields
    private fun disableFields() {
        transaction_title.isEnabled = false
        transaction_amount.isEnabled = false
        dateselect.isEnabled = false
//        recurring_to_date.isEnabled = true
//        recurring_from_date.isEnabled = true
//        recurring_transaction.isEnabled = true
        transaction_mode.isEnabled = false
//        transaction_type_spinner_layout.isEnabled = true
//        comments.isEnabled = true
        save_transaction_expense.isEnabled = false
        save_transaction_income.isEnabled = false
    }

}
