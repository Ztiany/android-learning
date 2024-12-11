package me.ztiany.bt.ui.ble

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import me.ztiany.ble.databinding.ItemDeviceBinding
import me.ztiany.bt.kit.arch.SimpleRecyclerAdapter
import me.ztiany.bt.kit.arch.ViewBindingViewHolder
import me.ztiany.bt.kit.ble.BtResult
import me.ztiany.bt.kit.sys.dp
import timber.log.Timber

private class BTAdapter(
    context: Context,
    private val onItemSelected: (BtResult) -> Unit = {},
) : SimpleRecyclerAdapter<BtResult, ItemDeviceBinding>(context) {

    override fun onViewHolderCreated(viewHolder: ViewBindingViewHolder<ItemDeviceBinding>) {
        viewHolder.itemView.setOnClickListener {
            onItemSelected(it.tag as BtResult)
        }
    }

    override fun onBindItem(
        viewHolder: ViewBindingViewHolder<ItemDeviceBinding>,
        item: BtResult,
    ) {
        runCatching {
            viewHolder.withVB {
                showDeviceInfo(item)
            }
        }.onFailure {
            Timber.e(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun ItemDeviceBinding.showDeviceInfo(item: BtResult) {
        root.tag = item
        tvDeviceName.text = item.scanResult.scanRecord?.deviceName ?: item.scanResult.device.name ?: "Unknown"
        tvDeviceAddress.text = item.scanResult.device.address
        tvDeviceUuid.text = item.scanResult.scanRecord?.serviceUuids?.joinToString { it.toString() } ?: "Unknown"
        tvDeviceStatus.text = "TODO"
    }

}

class DeviceListPresenter(
    private val host: Fragment,
    rv: RecyclerView,
    onItemSelected: (BtResult) -> Unit,
) {

    private val deviceAdapter = BTAdapter(host.requireContext(), onItemSelected)

    init {
        with(rv) {
            layoutManager = LinearLayoutManager(host.requireContext())
            addItemDecoration(MaterialDividerItemDecoration(host.requireContext(), RecyclerView.VERTICAL).apply {
                dividerColor = Color.parseColor("#E0E0E0")
                dividerThickness = host.requireContext().dp(1)
                dividerInsetStart = host.requireContext().dp(16)
                dividerInsetEnd = host.requireContext().dp(16)
            })
            adapter = deviceAdapter
        }
    }

    fun update(list: List<BtResult>) {
        Timber.d("update: $list")
        deviceAdapter.replaceAll(list)
    }

}