package com.example.expensemanager.ui.monthlyCards



import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanager.R
import com.example.expensemanager.data.Transaction
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.monthly_card_item.*

class MonthCardsAdapter(private val listener: (Long) -> Unit ,val sharedPreferences: SharedPreferences,val context: Context,val viewModelStoreOwner: ViewModelStoreOwner,val lifecycleOwner: LifecycleOwner):
    ListAdapter<Long, MonthCardsAdapter.ViewHolder>(
        DiffCallback1()
    ){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.monthly_card_item, parent, false)

        return ViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder (override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        init{
            itemView.setOnClickListener{
                listener.invoke(getItem(adapterPosition))
            }
        }

        fun bind(monthYear: Long){
            val monthYearString = monthYear.toString()
            val month = monthYearString.substring(4)
            when(month){
                "01" ->{
                    month_name.text = "JANUARY"
                }
                "02" ->{
                    month_name.text = "FEBRUARY"
                }
                "03" ->{
                    month_name.text = "MARCH"
                }
                "04" ->{
                    month_name.text = "APRIL"
                }
                "05" ->{
                    month_name.text = "MAY"
                }
                "06" ->{
                    month_name.text = "JUNE"
                }
                "07" ->{
                    month_name.text = "JULY"
                }
                "08" ->{
                    month_name.text = "AUGUST"
                }
                "09" ->{
                    month_name.text = "SEPTEMBER"
                }
                "10" ->{
                    month_name.text = "OCTOBER"
                }
                "11" ->{
                    month_name.text = "NOVEMBER"
                }
                "12" ->{
                    month_name.text = "DECEMBER"
                }
            }
            year_name.text = monthYearString.substring(0,4)
//            val monthlyBudget = sharedPreferences.getFloat("monthlyBudget",0F)

//            val viewModel = ViewModelProvider(viewModelStoreOwner).get(MonthlyCardsViewModel::class.java)
//            viewModel.setTransactionMonthYear(monthYear)
//            viewModel.sumMonth.observe(lifecycleOwner, Observer{
//                if (monthlyBudget+it<0){
//                    constraint_layout_card.setBackgroundColor(ContextCompat.getColor(context ,R.color.colorExpense))
//                }
//                else{
//                    constraint_layout_card.setBackgroundColor(ContextCompat.getColor(context ,R.color.blue2))
//                }
//            })
        }
    }
}
class DiffCallback1 : DiffUtil.ItemCallback<Long>() {
    override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean {
        return oldItem == newItem
    }
}