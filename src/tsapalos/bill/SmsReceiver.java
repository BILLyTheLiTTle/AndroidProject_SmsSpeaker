package tsapalos.bill;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	
	private String strMessage = "";
	private SharedPreferences preferences;
	private boolean talkOrNot;
	@Override
	public void onReceive(Context ctx, Intent intent) {
		// TODO Auto-generated method stub
		
		//handle the message data
		Bundle myBundle = intent.getExtras();
        SmsMessage [] messages = null;
        if (myBundle != null)
        {
            Object [] pdus = (Object[]) myBundle.get("pdus");
            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++)
            {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                strMessage += "Έχετε μήνυμα από: " + messages[i].getOriginatingAddress();
                strMessage += " και σας γράφει: ";
                strMessage += messages[i].getMessageBody();
            }
            Toast.makeText(ctx, strMessage, Toast.LENGTH_LONG).show();
            
            //check if the application must speak and do it
            preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
    		talkOrNot = preferences.getBoolean("talking", true);
    		if(talkOrNot){
    			strMessage=convertUpperCaseToLower(strMessage);
    			speakMessage(strMessage,ctx);//na ginei me mediaplayer class kai service
    		}
        }
    }
	
	//create the url and speaks the incoming message
	public void speakMessage(String sms, Context ctx){
		try {
        	sms = URLEncoder.encode(sms, "UTF-8");

        } catch (UnsupportedEncodingException uee) {
            // TODO Auto-generated catch block
        	Toast.makeText(ctx, uee.getMessage(), Toast.LENGTH_LONG).show();
        }
        String url="http://translate.google.gr/translate_tts?ie=UTF-8&q="+sms+"&tl=el&prev=input";
        Intent intent2=new Intent(Intent.ACTION_VIEW);
        intent2.setDataAndType(Uri.parse(url), "video/*");
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent2);
	}
	
	//convert the characters of the incoming message
	public String convertUpperCaseToLower(String str){
		str=str.replace("Α", "α");
		str=str.replace("Ά", "ά");
		str=str.replace("A", "α");//English letter
		str=str.replace("Β", "β");
		str=str.replace("B", "β");//English letter
		str=str.replace("Γ", "γ");
		str=str.replace("Δ", "δ");
		str=str.replace("Ε", "ε");
		str=str.replace("Έ", "έ");
		str=str.replace("E", "ε");//English letter
		str=str.replace("Ζ", "ζ");
		str=str.replace("Z", "ζ");//English letter
		str=str.replace("Η", "η");
		str=str.replace("Ή", "ή");
		str=str.replace("H", "η");//English letter
		str=str.replace("Θ", "θ");
		str=str.replace("Ι", "ι");
		str=str.replace("Ί", "ί");
		str=str.replace("I", "ι");//English letter
		str=str.replace("Κ", "κ");
		str=str.replace("K", "κ");//English letter
		str=str.replace("Λ", "λ");
		str=str.replace("Μ", "μ");
		str=str.replace("M", "μ");//English letter
		str=str.replace("Ν", "ν");
		str=str.replace("N", "ν");//English letter
		str=str.replace("Ξ", "ξ");
		str=str.replace("Ο", "ο");
		str=str.replace("Ό", "ό");
		str=str.replace("O", "ο");//English letter
		str=str.replace("Π", "π");
		str=str.replace("Ρ", "ρ");
		str=str.replace("P", "ρ");//English letter
		str=str.replace("Σ", "σ");
		str=str.replace("Τ", "τ");
		str=str.replace("T", "τ");//English letter
		str=str.replace("Υ", "υ");
		str=str.replace("Ύ", "ύ");
		str=str.replace("Y", "υ");//English letter
		str=str.replace("Φ", "φ");
		str=str.replace("Χ", "χ");
		str=str.replace("X", "χ");//English letter
		str=str.replace("Ψ", "ψ");
		str=str.replace("Ω", "ω");
		str=str.replace("Ώ", "ώ");
		
		//modify some shortcuts
		/*
		   First check for bigger words with the same letters.
		   For example first check for "μλκια" and then check for "μλκ"
		   If I do the opposite then the result will "μαλάκαια" 
		   instead of "μαλακία".
		 */
		str=str.replace("κ ", "και");
		str=str.replace("μνμ", "μήνυμα");
		str=str.replace("τπτ", "τίποτα");
		str=str.replace("μθμ", "μάθημα");
		str=str.replace("δν", "δεν");
		str=str.replace("μλκια", "μαλακία");
		str=str.replace("μλκ", "μαλάκα");
		
		//I want to let the user add shortcuts using sqlite or something else.
		
		
		return str;
	}
}
