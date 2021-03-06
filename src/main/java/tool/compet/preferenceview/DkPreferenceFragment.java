/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.preferenceview;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tool.compet.compactview.DkCompactFragment;
import tool.compet.navigation.DkNavigatorOwner;
import tool.compet.topic.DkTopicProvider;

/**
 * Subclass can extend this to implement preference version of Fragment.
 */
public abstract class DkPreferenceFragment<B extends ViewDataBinding>
	extends DkCompactFragment<B>
	implements ThePreferenceView, DkTopicProvider, DkNavigatorOwner {

	/**
	 * Caller must provide preference view id (id of recycler view)
	 */
	protected abstract int preferenceViewId();

	protected ThePreferenceManager preferenceManager;
	protected MyPreferenceAdapter preferenceAdapter;
	private final DkPreferenceListener preferenceListener = new DkPreferenceListener() {
		@Override
		public void onPreferenceChanged(String key) {
			DkPreferenceFragment.this.onPreferenceChanged(key);
		}

		@Override
		public void notifyDataSetChanged() {
			DkPreferenceFragment.this.preferenceAdapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		preferenceManager = new ThePreferenceManager(context, storage(), preferenceListener);
		preferenceAdapter = new MyPreferenceAdapter(preferenceManager.getPreferences());
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		RecyclerView prefView = layout.findViewById(preferenceViewId());

		// Subclass can override this to customize preference view (recycler view)
		onSetupPreferenceView(prefView);

		// Subclass can override this to create preference list
		onCreatePreferences(preferenceManager);
	}

	/**
	 * Subclass can override this to setup preference view (RecyclerView)
	 */
	protected void onSetupPreferenceView(RecyclerView prefView) {
		prefView.setAdapter(preferenceAdapter);
		prefView.setHasFixedSize(true);
		prefView.setLayoutManager(new LinearLayoutManager(context));
//		prefView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
	}

	@Override
	public ThePreferenceManager getPreferenceManager() {
		return preferenceManager;
	}

	private static class MyViewHolder extends RecyclerView.ViewHolder {
		public MyViewHolder(@NonNull View itemView) {
			super(itemView);
		}

		void decorate(DkPreferenceComponent preference) {
			preference.decorateView(itemView);
		}
	}

	private static class MyPreferenceAdapter extends RecyclerView.Adapter<MyViewHolder> {
		private final List<DkPreferenceComponent> preferences;

		MyPreferenceAdapter(List<DkPreferenceComponent> preferences) {
			this.preferences = preferences;
		}

		@NonNull
		@Override
		public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			// At below method `getItemViewType()`, we has set viewType as position of the preference in list
			DkPreferenceComponent preference = preferences.get(viewType);
			return new MyViewHolder(preference.createView(parent.getContext(), parent));
		}

		@Override
		public int getItemViewType(int position) {
			// Consider viewType as position
			return position;
		}

		@Override
		public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
			holder.decorate(preferences.get(position));
		}

		@Override
		public int getItemCount() {
			return preferences.size();
		}
	}
}
