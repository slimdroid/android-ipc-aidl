package com.slimdroid.ipcaidl.ui

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.slimdroid.ipcaidl.R
import com.slimdroid.ipcaidl.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setListeners()
        setObservable()
    }

    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setListeners() {
        binding.apply {
            btnSendIn.setOnClickListener {
                viewModel.sendObjectIn()
            }
            btnSendOut.setOnClickListener {
                viewModel.sendObjectOut()
            }
            btnSendInout.setOnClickListener {
                viewModel.sendObjectInOut()
            }
            btnGet.setOnClickListener {
                viewModel.getObject()
            }
            btnAsyncWork.setOnClickListener {
                viewModel.doAsyncWork()
            }
            btnKillProcess.setOnClickListener {
                viewModel.killProcess()
            }
        }
    }

    private fun setObservable() {
        viewModel.uiState
            .onEach { handleUiState(it) }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun handleUiState(state: MainUiState) {
        binding.apply {
            progressIndicator.isVisible = state.isProgress
            btnAsyncWork.isEnabled = !state.isProgress
            btnKillProcess.isEnabled = state.isServiceConnected
            ivConnectionIndicator.setColorFilter(
                if (state.isServiceConnected) Color.GREEN else Color.RED,
                PorterDuff.Mode.SRC_IN
            )
            tvAsyncResultValue.text = state.response
        }
    }

}