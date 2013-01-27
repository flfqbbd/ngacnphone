package sp.phone.fragment;

import gov.pianzong.androidnga.R;
import sp.phone.utils.PhoneConfiguration;
import sp.phone.utils.StringUtil;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

public class SearchDialogFragment extends DialogFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//this.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());  
        final EditText input = new EditText(getActivity());
        //input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint("Ç¿ß£»Ò·ÉÑÌÃð");
        alert.setView(input);  
		alert.setMessage(R.string.search_hint);
		
		alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int whichButton) {
            	final String inputString = input.getText().toString();
            	if(!StringUtil.isEmpty(inputString))
            	{
            		Intent intent_search = new Intent(getActivity(), PhoneConfiguration.getInstance().topicActivityClass);
    				intent_search.putExtra("fid",getArguments().getInt("id",-7));
    				intent_search.putExtra("key", inputString);
    				intent_search.putExtra("authorid", getArguments().getInt("authorid",0));
    				startActivity(intent_search);
            	}
            	
            }
		});
		
		alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int whichButton) {  
                dialog.dismiss();
            }
		});
		return alert.show();
	}
	

}
