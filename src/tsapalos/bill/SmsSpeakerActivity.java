package tsapalos.bill;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.RemoteViews.ActionException;

public class SmsSpeakerActivity extends Activity {

	private String str;
	private SharedPreferences preferences;
	private boolean talkOrNot;
	private CheckBox talkingCheckbox;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//set up preferences about talking or not
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		talkOrNot = preferences.getBoolean("talking", true);
		
		//update the Combobox item as it must be changed due to preferences
		talkingCheckbox = (CheckBox) findViewById(R.id.checkbox);
		talkingCheckbox.setChecked(talkOrNot);
		
		//update the preferences from the Combobox item
		talkingCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Editor edit = preferences.edit();
				edit.putBoolean("talking", isChecked);
				edit.commit();
			}
		});
		
		//Check if the application must talk and talk or not
		if(talkOrNot){
			whatIam();//na ginei me mediaplayer class kai service
		}
	}
	
	//Introduce myself as an application
	public void whatIam(){
		str = "Στόχος της εφαρμογής αυτής είναι να κάνει ανάγνωση "
				+ "κάθε εισερχόμενο μήνυμα.";
		try {
			str = URLEncoder.encode(str, "UTF-8");
		} 
		catch (UnsupportedEncodingException uee) {
			// TODO Auto-generated catch block
			Toast.makeText(this, uee.getMessage(), Toast.LENGTH_LONG).show();
		}
		String url = "http://translate.google.gr/translate_tts?ie=UTF-8&q="
				+ str + "&tl=el&prev=input";
		Intent intent2 = new Intent(Intent.ACTION_VIEW);
		intent2.setDataAndType(Uri.parse(url), "video/*");
		intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent2);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}