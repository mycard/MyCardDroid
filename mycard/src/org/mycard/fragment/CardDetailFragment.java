package org.mycard.fragment;

import java.util.Arrays;

import org.mycard.R;
import org.mycard.common.Constants;
import org.mycard.provider.YGOCards;
import org.mycard.widget.adapter.CardDetailAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class CardDetailFragment extends BaseFragment implements OnTouchListener, LoaderCallbacks<Cursor>{
	
	private static final int QUERY_SOURCE_LOADER_ID = 0;
	private ViewPager mViewPager;
	private CardDetailAdapter<CardDetailPagerFragment> mAdapter;
	
	private String[] mProjection;
	private String mSelection;
	private String[] mSelectionExtra;
	private String mSortOrder;
	private CursorLoader mCursorLoader;
	private int mInitPos;
	
	public static CardDetailFragment newInstance(Bundle param) {
		CardDetailFragment fragment = new CardDetailFragment();
		
		fragment.setArguments(param);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle param = getArguments();
		String[] projection = param.getStringArray(CardWikiFragment.BUNDLE_KEY_PROJECTION);
		int size = projection.length;
		mProjection = Arrays.copyOf(projection, projection.length + 1);
		mProjection[size] = YGOCards.Texts.DESC;
		mSelection = param.getString(CardWikiFragment.BUNDLE_KEY_SELECTION);
		mSelectionExtra = param.getStringArray(CardWikiFragment.BUNDLE_KEY_SELECTION_EXTRA);
		mSortOrder = param.getString(CardWikiFragment.BUNDLE_KEY_SORT_ORDER);
		mInitPos = param.getInt(CardWikiFragment.BUNDLE_KEY_INIT_POSITON);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity.onActionBarChange(
				Constants.ACTION_BAR_CHANGE_TYPE_PAGE_CHANGE,
				FRAGMENT_ID_CARD_DETAIL, null);
	}

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.card_detail, null);
		mViewPager = (ViewPager) view.findViewById(R.id.card_pager);
		mAdapter = new CardDetailAdapter<CardDetailPagerFragment>(getChildFragmentManager(), CardDetailPagerFragment.class, mProjection, null);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(mInitPos);
		view.setOnTouchListener(this);
		initCursorLoader();
		return view;
	}
	
	private void initCursorLoader() {
		getLoaderManager().initLoader(QUERY_SOURCE_LOADER_ID, null, this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		mCursorLoader = new CursorLoader(mActivity, YGOCards.CONTENT_URI, mProjection,
				mSelection, mSelectionExtra, mSortOrder);
		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		mAdapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}
}
