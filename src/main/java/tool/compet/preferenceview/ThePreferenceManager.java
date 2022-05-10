/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.preferenceview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import tool.compet.core.DkCollections;
import tool.compet.core.DkStrings;
import tool.compet.preference.DkPreference;

public class ThePreferenceManager {
	private final Context context;
	private final List<DkPreferenceComponent> preferences = new ArrayList<>();
	private final DkPreference storage;
	private final DkPreferenceListener listener;

	ThePreferenceManager(Context context, DkPreference storage, DkPreferenceListener listener) {
		this.context = context;
		this.storage = storage;
		this.listener = listener;
	}

	/**
	 * Call this when have changes at preference list.
	 */
	public void notifyDataSetChanged() {
		listener.notifyDataSetChanged();
	}

	public int count() {
		return preferences.size();
	}

	public void clear() {
		preferences.clear();
	}

	public DkPreferenceComponent findPreference(String key) {
		int index = DkCollections.findIndex(preferences, pref -> DkStrings.isEquals(key, pref.key()));
		return index < 0 ? null : preferences.get(index);
	}

	public List<DkPreferenceComponent> getPreferences() {
		return preferences;
	}

	public ThePreferenceManager addPreference(DkPreferenceComponent preference) {
		preference.init(context, storage, listener);
		preferences.add(preference);
		return this;
	}

	public void removePreference(String key) {
		int index = DkCollections.findIndex(preferences, pref -> DkStrings.isEquals(key, pref.key()));
		if (index >= 0) {
			preferences.remove(index);
		}
	}
}
