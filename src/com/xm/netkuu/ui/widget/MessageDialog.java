package com.xm.netkuu.ui.widget;

import com.xm.netkuu.player.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

public class MessageDialog extends DialogFragment implements DialogInterface.OnClickListener{

	public static final int DIALOG_ALERT = 1;
	public static final int DIALOG_ERROR = 2;
	
	private Activity mActivity;
	private int mType = DIALOG_ERROR;
	private String mMsg;
	private Integer mMsgResId;
	private TextView mMsgView;
	//private boolean mQuit = false; 
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mActivity = getActivity();
		mMsgView = new TextView(mActivity);
		int padding = mActivity.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
		mMsgView.setPadding(padding, padding, padding, padding);
		if(mMsgResId != null)
			mMsgView.setText(mMsgResId);
		else if(mMsg != null){
			mMsgView.setText(mMsg);
		}
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity);
		builder = builder
				.setTitle(mType == DIALOG_ERROR ? R.string.error : R.string.alert)
				.setIcon(mType == DIALOG_ERROR ? android.R.drawable.ic_dialog_dialer : android.R.drawable.ic_dialog_info)
				.setView(mMsgView)
				.setPositiveButton(R.string.confirm, this);
		if(mType != DIALOG_ERROR){
			builder.setNegativeButton(R.string.cancle, this);
		}
		android.app.AlertDialog dialog = builder.create();
		
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		return dialog;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which){
		case AlertDialog.BUTTON_POSITIVE:			
			break;
		case AlertDialog.BUTTON_NEGATIVE:
			//mQuit = true;			
			break;
		}		
	}

	 public void onDismiss(DialogInterface dialog) {
			super.onDismiss(dialog);
			if(mType == DIALOG_ERROR){
				mActivity.finish();
			}
	 }
	 
	 public void setMessage(String message){
			mMsg = message;
			mMsgResId = null;
	 }
	 
	 public void setMessage(int resid){
		 mMsgResId = resid;
		 mMsg = null;
	 }
	 
	 public void setType(int type){
		 mType = type;
	 }
	 
	 public static MessageDialog error(FragmentManager manager, String message){
		 MessageDialog dialog =  new MessageDialog();
		 dialog.setMessage(message);
		 dialog.show(manager, null);
		 return dialog;
	 }
	 
	 public static MessageDialog error(FragmentManager manager, int message){
		 MessageDialog dialog =  new MessageDialog();
		 dialog.setMessage(message);
		 dialog.show(manager, null);
		 return dialog;
	 }
}
