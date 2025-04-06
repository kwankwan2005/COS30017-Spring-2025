package com.example.assignment3.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.assignment3.R
import com.example.assignment3.data.database.AppDatabase
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.launch
import android.util.Log

class ChartFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var txtPlaceholder: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_chart, container, false)
        pieChart = view.findViewById(R.id.pieChart)
        txtPlaceholder = view.findViewById(R.id.txtPlaceholder)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun loadChartData(context: Context, staticCategory: String, staticStart: Long, staticEnd: Long) {
        val dao = AppDatabase.getInstance(context).transactionDao()

        Log.d("ChartFragment", "Loading chart data: $staticCategory from $staticStart to $staticEnd")

        lifecycleScope.launch {
            val transactions = dao.getTransactionsCategoryInRange(staticCategory, staticStart, staticEnd)
                .filter { it.category == staticCategory }
                .groupBy { it.id }

            if (transactions.isEmpty()) {
                pieChart.visibility = View.GONE
                txtPlaceholder.visibility = View.VISIBLE
                return@launch
            }

            val entries = transactions.map { (_, items) ->
                val total = items.sumOf { it.amount }
                PieEntry(total.toFloat(), items.first().title)
            }

            val dataSet = PieDataSet(entries, "").apply {
                setColors(
                    resources.getColor(R.color.red, null),
                    resources.getColor(R.color.blue, null),
                    resources.getColor(R.color.green, null),
                    resources.getColor(R.color.bg_brown, null),
                    resources.getColor(R.color.purple, null)
                )
                valueTextSize = 14f
                valueTextColor = resources.getColor(android.R.color.white, null)
            }

            val pieData = PieData(dataSet).apply {
                setValueTextColor(resources.getColor(android.R.color.white, null))
            }

            pieChart.data = pieData
            pieChart.description.isEnabled = false
            pieChart.legend.isEnabled = false
            pieChart.setEntryLabelColor(resources.getColor(android.R.color.white, null))
            pieChart.setEntryLabelTextSize(12f)
            pieChart.invalidate()

            pieChart.visibility = View.VISIBLE
            txtPlaceholder.visibility = View.GONE
        }
    }
}