package com.example.stopwatch

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stopwatch.Service.Service
import com.example.stopwatch.databinding.ActivityMainBinding
import com.example.stopwatch.repository.Repository
import com.example.stopwatch.ui.activity.HistoryActivity
import com.example.stopwatch.utility.Utility
import com.example.stopwatch.viewModel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    // Log TAG
    private val TAG:String = "MainActivity"

    //context
    private val context: Context by lazy { this }

    // Utility
    private val util: Utility by lazy { Utility() }

    // ViewModel
    private val vm: ViewModel by lazy { ViewModel() }

    // View Binding
    private lateinit var binding: ActivityMainBinding

    // Intent
    private lateinit var intent: Intent

    // Repository
    private val repository: Repository by lazy { Repository.get() }

    //Recently List
    private lateinit var recentlyList: MutableList<Recently>

    //  Recently Adapter
    private lateinit var recentlyAdapter: RecentlyAdapter


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Repository.initialize(this)


        // View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)   // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목 삭제


        // 시작
        binding.btnStart.setOnClickListener {
            lifecycleScope.launch {
                vm.startStopwatch()
                binding.constAtm.visibility = View.GONE
                binding.constStarting.visibility = View.VISIBLE
            }
        }

        // 초기화
        binding.btnReset.setOnClickListener {
            lifecycleScope.launch {
                binding.tvTime.text = "00:00:00"
                vm.resetStopwatch()
                repository.clearRecodeList()
                recentlyListGet()
            }
        }

        // 시간정지
        binding.btnStop.setOnClickListener {
            vm.stopStopwatch()
            binding.constAtm.visibility = View.VISIBLE
            binding.constStarting.visibility = View.GONE
        }

        // 일시정지
        binding.btnPause.setOnClickListener {
            if(binding.btnPause.text == "재시작") {
                binding.btnPause.text ="일시정지"
                vm.startStopwatch()
            } else if(binding.btnPause.text == "일시정지") {
                binding.btnPause.text ="재시작"
                vm.pauseStopwatch()
            }
        }

        // 시간기록
        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                repository.saveRecodeList(binding.tvTime.text.toString())
                recentlyListGet()
            }
        }




        //코루틴 사용을 위한 라이프싸이클스코프

        lifecycleScope.launch {
            try {
                vm.timeFlow().collect { time ->
                    binding.tvTime.text = util.formatTime(time)
                }
            } catch (e: Exception) {
                Log.e(TAG, "lifecycleScope: error $e")
            }
        }
    }

    fun recentlyListGet() {
        recentlyList = mutableListOf()
        lifecycleScope.launch {
            repository.getRecodeList().mapIndexed { index, item ->
                recentlyList.add(Recently(item))
            }
            recentlyAdapter = RecentlyAdapter(recentlyList)
            binding.rvRecently.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rvRecently.setHasFixedSize(true)
            binding.rvRecently.adapter = recentlyAdapter
        }
    }


    // Menu Setting
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        return true
    }

    // Menu Click Event
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.history -> {
                intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //  Service Start
    fun serviceStart() {
        intent = Intent(this, Service::class.java)
        startService(intent)
    }
    //  Service Stop
    fun serviceStop() {
        intent = Intent(this, Service::class.java)
        startService(intent)
    }

    //  액티비티가 사용자에게 보일 때 호출
    override fun onResume() {
        super.onResume()
        recentlyListGet()
        serviceStart()
    }

    override fun onPause() {
        super.onPause()
        serviceStop()
    }

}

