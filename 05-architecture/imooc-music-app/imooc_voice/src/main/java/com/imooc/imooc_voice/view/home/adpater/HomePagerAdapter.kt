package com.imooc.imooc_voice.view.home.adpater

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.imooc.imooc_voice.model.*
import com.imooc.imooc_voice.view.VideoFragment
import com.imooc.imooc_voice.view.discory.DiscoryFragment
import com.imooc.imooc_voice.view.friend.FriendFragment
import com.imooc.imooc_voice.view.mine.MineFragment

/**
 * 首页框架adapter
 */
class HomePagerAdapter : FragmentPagerAdapter {

    private var mList: Array<KotCHANNEL>? = null

    constructor(fragmentManager: FragmentManager, list: Array<KotCHANNEL>) : super(fragmentManager) {
        mList = list
    }

    override fun getItem(position: Int): Fragment? {
        val type = mList!![position].value
        when (type) {
            MINE_ID -> return MineFragment.newInstance()
            DISCORY_ID -> return DiscoryFragment.newInstance()
            FRIEND_ID -> return FriendFragment.newInstance()
            VIDEO_ID -> return VideoFragment.newInstance()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return mList!!.size
    }
}