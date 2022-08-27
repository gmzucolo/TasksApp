package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormart: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var listPriority: List<PriorityModel> = listOf()

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

    override fun onDateSet(v: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Instancia o calendario
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
        } else if (v.id == R.id.button_save) {
            handleSave()
        }
    }

    private fun observe() {
        viewModel.priorityList.observe(this) {
            listPriority = it
            // Spinner
            val list = mutableListOf<String>()
            for (priority in it) {
                list.add(priority.description)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerPriority.adapter = adapter
        }
        viewModel.taskSave.observe(this) {
            if (it.status()) {
                toast("Sucesso")
                finish()
            } else {
                toast(it.message())
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, dayOfMonth).show()
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = 0
            val index = binding.spinnerPriority.selectedItemPosition
            this.priorityId = listPriority[index].id
            this.description = binding.editDescription.text.toString()
            this.dueData = binding.buttonDate.text.toString()
            this.complete = binding.checkComplete.isChecked
        }

        viewModel.save(task)
    }

    private fun list(listener: ApiListener<List<TaskModel>>) {
        viewModel.list(listener)
    }

    private fun listNext(listener: ApiListener<List<TaskModel>>) {
        viewModel.listNext(listener)
    }

    private fun listOverdue(listener: ApiListener<List<TaskModel>>) {
        viewModel.listOverdue(listener)
    }
}