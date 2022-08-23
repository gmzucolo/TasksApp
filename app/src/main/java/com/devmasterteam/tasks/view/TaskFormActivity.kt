package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormart: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // Eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        viewModel.loadPriorities()

        observe()

        // Layout
        setContentView(binding.root)
    }

    private fun observe() {
        viewModel.priorityList.observe(this) {
            // Spinner
            val list = mutableListOf<String>()
            for (priority in it) {
                list.add(priority.description)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerPriority.adapter = adapter
        }
    }

    override fun onDateSet(v: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Insntacia o calendario
        val calendar = Calendar.getInstance()
        // Formata o valor
        calendar.set(year, month, dayOfMonth)
        // Faz a conversão
        val dueData = dateFormart.format(calendar.time)
        // Atribui a UI
        binding.buttonDate.text = dueData
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_date) {
            handleDate()
        }
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, dayOfMonth).show()
    }
}