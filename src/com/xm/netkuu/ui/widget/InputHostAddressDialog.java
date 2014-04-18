package com.xm.netkuu.ui.widget;

import java.lang.reflect.Field;

import com.xm.netkuu.player.R;
import com.xm.netkuu.util.Verification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class InputHostAddressDialog extends DialogFragment implements DialogInterface.OnClickListener{

	// private DialogInterface.OnClickListener

	private EditText mInputView;
	private OnDismissListener mOnDismissListener;
	private String mHostAddress;
	private boolean mQuit = false;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.input_host_address_dialog, null);
		mInputView = (EditText) view.findViewById(R.id.host_address);
		mInputView.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void afterTextChanged(Editable s) {
				mHostAddress = null;
				mInputView.setTextColor(Color.BLACK);
				mInputView.refreshDrawableState();			
			}
			
		});
		AlertDialog dialog = new AlertDialog.Builder(getActivity())
			.setIcon(android.R.drawable.ic_menu_preferences)
			.setTitle(R.string.host_addr_title)
			.setView(view)
			.setPositiveButton(R.string.confirm, this)
			.setNegativeButton(R.string.quit, this).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		return dialog;
	}

	private void setDialogCancelable(Dialog dialog, boolean cancelable) {
		if(dialog != null){
			try {
				Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
				field.setAccessible(true);
				field.set(dialog, cancelable);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setOnDismissListener(OnDismissListener l){
		mOnDismissListener = l;
	}
	
	public String getHostAddress(){
		return mHostAddress;
	}
	
	public boolean isQuit(){
		return mQuit;
	}

	@Override  
    public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if(mOnDismissListener != null){
			mOnDismissListener.onDismiss(this);
		}
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which){
		case AlertDialog.BUTTON_POSITIVE:
			String text = mInputView.getText().toString();
			if (Verification.verifyHostAddress(text)) {
				mInputView.setTextColor(Color.BLACK);
				mInputView.refreshDrawableState();
				mHostAddress = text;
				setDialogCancelable((Dialog)dialog, true);
			} else {
				mInputView.setTextColor(Color.RED);
				mInputView.refreshDrawableState();
				mHostAddress = null;
				setDialogCancelable((Dialog)dialog, false);
			}
			break;
		case AlertDialog.BUTTON_NEGATIVE:
			mQuit = true;
			mHostAddress = null;
			setDialogCancelable((Dialog)dialog, true);
			break;
		}		
	}
	
	public interface OnDismissListener{
		public void onDismiss(InputHostAddressDialog dialog);
	}
}
