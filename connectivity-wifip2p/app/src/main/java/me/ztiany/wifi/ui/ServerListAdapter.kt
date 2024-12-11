package me.ztiany.wifi.ui

import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import me.ztiany.wifi.kit.arch.SimpleRecyclerAdapter
import me.ztiany.wifi.kit.arch.ViewBindingViewHolder
import me.ztiany.wifi.kit.wifi.convertWiFiP2PDeviceStatus
import me.ztiany.wifip2p.databinding.ItemWifip2pDeviceBinding

class ServerListAdapter(
    context: Context,
    private val onItemClicked: (WifiP2pDevice) -> Unit,
) : SimpleRecyclerAdapter<WifiP2pDevice, ItemWifip2pDeviceBinding>(context) {

    override fun onViewHolderCreated(viewHolder: ViewBindingViewHolder<ItemWifip2pDeviceBinding>) {
        viewHolder.vb.root.setOnClickListener {
            (it.tag as? WifiP2pDevice)?.let(onItemClicked)
        }
    }

    override fun onBindItem(viewHolder: ViewBindingViewHolder<ItemWifip2pDeviceBinding>, item: WifiP2pDevice) = with(viewHolder.vb) {
        tvDeviceName.text = item.deviceName
        tvDeviceAddress.text = item.deviceAddress
        tvDeviceStatus.text = item.status.convertWiFiP2PDeviceStatus()
        root.tag = item
    }

}