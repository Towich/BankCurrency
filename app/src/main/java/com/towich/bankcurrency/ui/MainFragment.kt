package com.towich.bankcurrency.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.towich.bankcurrency.util.NetworkConnection
import com.towich.bankcurrency.App
import com.towich.bankcurrency.R
import com.towich.bankcurrency.databinding.FragmentMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainFragment : Fragment() {

    interface ProgressBarListener { fun showProgressBar(show: Boolean) }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MainAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var networkConnection: NetworkConnection

    private val handler = Handler()
    private var progressBarListener: ProgressBarListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProgressBarListener) {
            progressBarListener = context
        } else {
            throw RuntimeException(getString(R.string.must_implement_progressbar, context))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = activity?.application as App
        val repository = application.repository
        val factory = MainViewModel.MainViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        adapter = MainAdapter()

        networkConnection = NetworkConnection(requireContext())

        networkConnection.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                loadData()
                binding.textLastRequest.visibility = View.VISIBLE
                binding.textRelevance.visibility = View.VISIBLE
                binding.textNoConnection.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                binding.textNoConnection.visibility = View.VISIBLE
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (networkConnection.value == true) {
                loadData()
                binding.swipeRefreshLayout.isRefreshing = false
            } else {
                Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        handler.postDelayed(updateRunnable, 30 * 1000)
    }

    private fun loadData() {
        progressBarListener?.showProgressBar(true)

        val recyclerViewState = binding.recyclerView.layoutManager?.onSaveInstanceState()
        adapter.notifyItemRangeChanged(0, adapter.itemCount)

        viewModel.getCurrency()
        viewModel.listCurrency.observe(viewLifecycleOwner) { list ->
            list.body()?.let {
                progressBarListener?.showProgressBar(false)
                adapter.setList(it)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)

                binding.textLastRequest.text = getString(R.string.time_last_request, formatCurrentDate())
                binding.textRelevance.text =
                    getString(R.string.recent_changes_server, formatInputDate(list.body()?.Timestamp.toString()))
            } ?: run {
                progressBarListener?.showProgressBar(false)
                Toast.makeText(requireContext(),
                    getString(R.string.data_upload_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            if (networkConnection.value == true) {
                loadData()
            } else {
                Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
            }
            handler.postDelayed(this, 30 * 1000)
        }
    }

    private fun formatInputDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val date: Date = inputFormat.parse(inputDate)!!
        val is24HourFormat = DateFormat.is24HourFormat(context)

        val outputFormat = if (is24HourFormat) {
            SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        } else {
            SimpleDateFormat("dd.MM.yyyy hh:mm:ss a", Locale.getDefault())
        }

        return outputFormat.format(date)
    }

    private fun formatCurrentDate(): String {
        val currentTime = System.currentTimeMillis()
        val is24HourFormat = DateFormat.is24HourFormat(context)

        val outputFormat = if (is24HourFormat) {
            SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        } else {
            SimpleDateFormat("dd.MM.yyyy hh:mm:ss a", Locale.getDefault())
        }

        return outputFormat.format(currentTime)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateRunnable)
        _binding = null
    }
}