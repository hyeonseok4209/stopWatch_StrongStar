package com.example.stopwatch.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.stopwatch.HistoryAdapter
import com.example.stopwatch.R
import com.example.stopwatch.Recently
import com.example.stopwatch.RecentlyAdapter
import com.example.stopwatch.databinding.ActivityHistoryBinding
import com.example.stopwatch.model.History
import com.example.stopwatch.repository.Repository
import com.example.stopwatch.utility.Utility
import com.example.stopwatch.viewModel.ViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private val TAG = "HistoryActivity"

    //context
    private val context: Context by lazy { this }

    // Utility
    private val util: Utility by lazy { Utility() }

    // ViewModel
    private val vm: ViewModel by lazy { ViewModel() }

    // View Binding
    private lateinit var binding: ActivityHistoryBinding

    // Intent
    private lateinit var intent: Intent

    // Repository
    private val repository: Repository by lazy { Repository.get() }

    //History List
    private lateinit var historyList: MutableList<History>

    //recode List
    private lateinit var recodeList: MutableList<String>

    // 어뎁터
    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var recentlyAdapter: RecentlyAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Repository.initialize(this)


        // View Binding
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.historyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목 삭제

        historyList = mutableListOf()
        recodeList = mutableListOf()

        historyListGet()








        historyAdapter.setItemClickListener(object : HistoryAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                historyAdapter.removeItem(position)
                lifecycleScope.launch {
                    repository.deleteRecodeList(position)
                }
            }
        })
    }

    fun historyListGet() {
        lifecycleScope.launch {
            repository.getRecodeList().mapIndexed { index, item ->
                historyList.add(History(item))
            }
            historyAdapter = HistoryAdapter(historyList)
            binding.rvHistory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rvHistory.setHasFixedSize(true)
            binding.rvHistory.adapter = historyAdapter
        }
    }

    // Menu Click Event
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}




