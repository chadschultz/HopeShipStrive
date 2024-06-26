package com.example.hopeshipstrive

import android.os.Bundle
import android.view.Gravity
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hopeshipstrive.databinding.ActivityMainBinding
import com.example.hopeshipstrive.recyclerview.TripAdapter
import com.example.hopeshipstrive.recyclerview.TripListItem
import com.example.hopeshipstrive.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.stream.MalformedJsonException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.FileNotFoundException

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AlertDialogFragment.Listener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private var alertDialogFragment: AlertDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    handleUiState(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        alertDialogFragment = supportFragmentManager
            .findFragmentByTag(ALERT_DIALOG_FRAGMENT_TAG) as? AlertDialogFragment
    }

    private fun handleUiState(uiState: MainViewModel.UiState) {
        uiState.tripState.data?.let { trips ->
            setAdapter(trips)
        }
        uiState.tripState.error?.let { err ->
            Timber.e(err, "error loading trips")
            when (err) {
                is FileNotFoundException -> {
                    // NO-OP - user doesn't need to know prepackaged data failed to load
                }
                is MalformedJsonException -> {
                    showSnackbar(R.string.error_loading_data)
                }
            }
        }
    }

    // NOTE: a more sophisticated implementation would update only items that have changed,
    // instead of replacing the entire adapter.
    private fun setAdapter(trips: List<TripListItem>) {
        val adapter = TripAdapter(trips, object : TripAdapter.OnItemClickListener {
            override fun onItemClick(trip: TripListItem.TripItem, position: Int) {
                Timber.d("Trip[$position] clicked: $trip")
                alertDialogFragment = AlertDialogFragment.newInstance(
                    titleText = getString(R.string.dialog_title),
                    titleAlignment = Gravity.CENTER_HORIZONTAL,
                    titleTextColor = R.color.black,
                    subtitleText = getString(R.string.dialog_subtitle),
                    subtitleFont = R.font.cherry_cream_soda, // just to demonstrate changing font
                    button1Title = getString(R.string.dialog_button1_text),
                    button2Title = getString(R.string.dialog_button2_text),
                ).also { it.show(supportFragmentManager, ALERT_DIALOG_FRAGMENT_TAG) }
            }
        })
        binding.recyclerView.adapter = adapter
    }

    override fun onButton1Click() {
        alertDialogFragment?.dismiss()
    }

    override fun onButton2Click() {
        alertDialogFragment?.dismiss()
    }

    private fun showSnackbar(@StringRes resId: Int) {
        Snackbar.make(binding.root, resId, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val ALERT_DIALOG_FRAGMENT_TAG = "alert_dialog"
    }
}
