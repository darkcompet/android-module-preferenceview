/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.preferenceview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import tool.compet.preference.DkPreference;

/**
 * Represents for a preference item.
 */
public interface DkPreferenceComponent {
	/**
	 * Preference key in storage.
	 */
	CharSequence key();

	/**
	 * Call this to pass customized params to this preference.
	 *
	 * @param context To init layout
	 * @param preference To store setting
	 * @param listener To tell caller know changes from this preference.
	 */
	void init(Context context, DkPreference preference, DkPreferenceListener listener);

	/**
	 * Called when init view.
	 */
	View createView(Context context, ViewGroup parent);

	/**
	 * Called after init view for decoration.
	 */
	void decorateView(View itemView);
}
