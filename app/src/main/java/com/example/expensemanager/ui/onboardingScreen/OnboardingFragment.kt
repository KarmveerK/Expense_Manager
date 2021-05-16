package com.example.expensemanager.ui.onboardingScreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import androidx.navigation.fragment.findNavController
import com.example.expensemanager.R
import kotlinx.android.synthetic.main.fragment_onboarding.*
//https://medium.com/material-design-in-action/implementing-bottomappbar-behavior-fbfbc3a30568
//https://stackoverflow.com/questions/7238532/how-to-launch-activity-only-once-when-app-is-opened-for-first-time use this for reference
class OnboardingFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //(activity as AppCompatActivity).supportActionBar?.hide()          //good way to remove action bar from all fragments
        val sharedPreferences:SharedPreferences = this.requireActivity().getSharedPreferences("OnboardingDetails", Context.MODE_PRIVATE)

        val boarded: Boolean = sharedPreferences.getBoolean("isLogin", false)

        if (boarded) {
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToTransactionListFragment())
        } else {
            sharedPreferences.edit().putBoolean("isLogin", true).apply()
        }

        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        continue_button.isEnabled = false
        name_field.editText?.addTextChangedListener(boardingTextWatcher)
        monthly_budget_field.editText?.addTextChangedListener(boardingTextWatcher)
        monthly_income_field.editText?.addTextChangedListener(boardingTextWatcher)

        continue_button.setOnClickListener{
            val editor: SharedPreferences.Editor = this.requireActivity().getSharedPreferences("OnboardingDetails", Context.MODE_PRIVATE).edit()
            editor.putBoolean("isLogin", true).apply()
            editor.putString("name", name_field.editText?.text.toString())
            editor.putFloat("monthlyBudget", monthly_budget_field.editText?.text.toString().toFloat())
            if(!monthly_income_field.editText?.text.toString().isEmpty()){
                editor.putFloat("monthlyIncome", monthly_income_field.editText?.text.toString().toFloat())
            }
            editor.apply()
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToTransactionListFragment())
        }
    }

    private val boardingTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            val name = name_field.editText?.text.toString()
            val monthlyBudget = monthly_budget_field.editText?.text.toString()

            if(name.isEmpty()){
                continue_button.isEnabled = false
                name_field.error = "This field cannot be empty"
                name_field.isEndIconVisible = true

            }
            if(monthlyBudget.isEmpty()){
                continue_button.isEnabled = false
                monthly_budget_field.error = "This field cannot be empty"
                monthly_budget_field.isEndIconVisible = true
            }
            else if(name.isNotEmpty() && monthlyBudget.isNotEmpty()){
                continue_button.isEnabled = true
                monthly_budget_field.isEndIconVisible = false
                name_field.isEndIconVisible = false
                monthly_budget_field.error = null
                name_field.error = null
            }

        }

    }


}