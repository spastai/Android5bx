package com.mk.help.bx5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SummaryActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congrats);

        findViewById(R.id.home_button_start).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SummaryActivity.this, ExercisizeActivity.class);
				intent.putExtra("autostart", true);
				startActivity(intent);
			}
		});
    } 
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);

    	findViewById(R.id.summary_text_congrats).setVisibility(View.VISIBLE);
		final ImageButton button = ((ImageButton) findViewById(R.id.exercisize_button_next));
		button.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_lock_power_off));
        findViewById(R.id.home_button_start).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
    	
    }
}