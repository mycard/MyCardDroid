package org.mycard.fragment;

import org.mycard.R;
import org.mycard.common.Constants;
import org.mycard.core.Controller;
import org.mycard.core.UserStatusTracker;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PersonalCenterFragment extends BaseFragment {

	private static final int REQUEST_CODE_LOGIN = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_personal_center, null);
		int loginStatus = Controller.peekInstance().getLoginStatus();
		switchState(loginStatus);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity.onActionBarChange(
				Constants.ACTION_BAR_CHANGE_TYPE_PAGE_CHANGE,
				FRAGMENT_ID_PERSONAL_CENTER, 0, null);
		Controller.peekInstance().registerForLoginStatusChange(mHandler);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Controller.peekInstance().unregisterForLoginStatusChange(mHandler);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Controller.peekInstance().unregisterForActionPersonalCenter(mHandler);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Controller.peekInstance().registerForActionPersonalCenter(mHandler);
	}

	private void switchState(int state) {
		Fragment fragment = null;
		switch (state) {
		case UserStatusTracker.LOGIN_STATUS_LOGGED_IN:
			fragment = new UserStatusFragment();
			break;
		case UserStatusTracker.LOGIN_STATUS_LOGGING:
		case UserStatusTracker.LOGIN_STATUS_LOGIN_FAILED:
		case UserStatusTracker.LOGIN_STATUS_LOG_OUT:
			fragment = new UserLoginFragment();
			break;
		default:
			break;
		}
		Bundle args = new Bundle();
		args.putString("username", Controller.peekInstance().getLoginName());
		args.putInt("userstatus", state);
		fragment.setArguments(args);
		FragmentManager fm = getChildFragmentManager();
		fragment.setTargetFragment(this, REQUEST_CODE_LOGIN);
		FragmentTransaction transaction = fm.beginTransaction();
		fm.popBackStack();
		transaction.replace(R.id.personal_center_panel, fragment).commit();
	}

	@Override
	public void onEventFromChild(int requestCode, int eventType, int arg1,
			int arg2, Object data) {
		if (requestCode == REQUEST_CODE_LOGIN) {
			if (eventType == FRAGMENT_NAVIGATION_DUEL_LOGIN_SUCCEED_EVENT) {
				switchState(UserStatusTracker.LOGIN_STATUS_LOGGED_IN);
			}
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		int msgType = msg.what;
		if (msgType == Constants.ACTION_BAR_EVENT_TYPE_PERSONAL_CENTER) {
			Controller.peekInstance().asyncLogout();
		} else if (msgType == Constants.MSG_ID_LOGIN) {
			if (msg.arg1 == UserStatusTracker.LOGIN_STATUS_LOG_OUT) {
				switchState(UserStatusTracker.LOGIN_STATUS_LOG_OUT);
			}
		}
		return false;
	}
}
