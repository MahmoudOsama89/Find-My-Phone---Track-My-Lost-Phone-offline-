package com.integratedappsforlife.tracking.findmyphone.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.integratedappsforlife.tracking.findmyphone.ManagmentOperations;

import java.util.HashMap;

import timber.log.Timber;

public class Contacts {

	private Context context;

	public Contacts(Context context) {
		this.context = context;
		if(BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
	}

	public HashMap<String, String> getContactList() {
		HashMap<String, String> contacts = new HashMap<>();
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);

		if ((cur != null ? cur.getCount() : 0) > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(
						cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString(cur.getColumnIndex(
						ContactsContract.Contacts.DISPLAY_NAME));

				if (cur.getInt(cur.getColumnIndex(
						ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
							new String[]{id}, null);
					if (pCur != null) {
						while (pCur.moveToNext()) {
							String phoneNo = pCur.getString(pCur.getColumnIndex(
									ContactsContract.CommonDataKinds.Phone.NUMBER));
							Timber.i("Name: %s", name);
							Timber.i("Phone Number: %s", phoneNo);
							contacts.put(ManagmentOperations.FormatPhoneNumber(phoneNo), name);
						}
						pCur.close();
					}
				}
			}
		}
		if(cur!=null){
			cur.close();
		}
		return contacts;
	}
}
