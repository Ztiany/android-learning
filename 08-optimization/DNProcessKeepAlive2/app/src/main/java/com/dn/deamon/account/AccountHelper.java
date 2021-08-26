package com.dn.deamon.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

public class AccountHelper {

    //与authenticator.xml中accountType一致
    private static final String ACCOUNT_TYPE = "com.dn.daemon.account";
    private static final String CONTENT_AUTHORITY = "com.dn.daemon.provider";

    public static void addAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length > 0) {
            //账户已存在
            return;
        }
        Account dongnao = new Account("dongnao", ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(dongnao, "dn123", new Bundle());//直接添加账户
    }

    public static void autoSync() {
        Account dongnao = new Account("dongnao", ACCOUNT_TYPE);
        //设置同步
        ContentResolver.setIsSyncable(dongnao, CONTENT_AUTHORITY, 1);
        //设置自动同步
        ContentResolver.setSyncAutomatically(dongnao, CONTENT_AUTHORITY, true);
        //设置同步周期
        ContentResolver.addPeriodicSync(dongnao, CONTENT_AUTHORITY, new Bundle(), 1);
    }

}
