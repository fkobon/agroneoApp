package com.agroneo.app.oauth.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class AccountUtils {
	
	public static Account getAccount(Context context, String accountName) {
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccountsByType("com.agroneo");
		for (Account account : accounts) {
			if (account.name.equalsIgnoreCase(accountName)) {
				return account;
			}
		}
		return null;
	}
	
}
