package com.mk.help.bx5;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

// TODO Progress steps by age
// TODO Tick
// TODO Timer for running
public class ExercisizeActivity extends Activity {
	
	static final int DIALOG_SELECT_LEVEL = 0;

	static final int PHASE_READY = 0;
	static final int PHASE_PLAYING = 1;
	static final int PHASE_LAST = 2;
	static final int PHASE_STOPED = 3;

	int phase = 0;
	
	MediaPlayer mp = null;
	ProgressBar countProgress = null;
	int level = 0;
	int exercize = 0;
	int period = 0;
	
	int counts[][] = new int[72][5];

    long periods[] = new long[] {2*60*1000,1*60*1000,1*60*1000,1*60*1000,6*60*1000};
	int pictures[][] = new int[][] { 
			new int[] {R.drawable.ch1_1, R.drawable.ch1_2, R.drawable.ch1_3, R.drawable.ch1_4,	R.drawable.ch1_5},
			new int[] {R.drawable.ch2_1, R.drawable.ch2_2, R.drawable.ch2_3, R.drawable.ch2_4,	R.drawable.ch2_5}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercisize);
        
        counts =  new int[][] {
        		new int[] {2, 3, 4, 2, 100},
    			new int[] {10, 11, 13, 7, 280},
    			new int[] {10, 11, 13, 7, 280},
    			new int[] {10, 11, 13, 7, 280},
        		new int[] {10, 11, 13, 7, 280},
        		new int[] {10, 11, 13, 7, 280},
        		new int[] {12, 12, 14, 8, 280},
        		new int[] {14, 13, 15, 9, 320},
        		new int[] {16, 15, 16, 11, 353},
        		new int[] {18, 17, 17, 12, 375},
        		new int[] {20, 18, 18, 13, 400},
        		new int[] {14, 10, 13, 9, 353},
        		new int[] {16, 15, 16, 11, 353},
        		new int[] {16, 15, 16, 11, 353},
        		new int[] {16, 15, 16, 11, 353},
        		new int[] {16, 15, 16, 11, 353},
        		new int[] {16, 15, 16, 11, 353},
        		new int[] {16, 15, 16, 11, 353},
        		new int[] {16, 15, 16, 11, 353},
        		new int[] {16, 15, 16, 11, 353},
        		new int[] {14, 13, 15, 9, 320}
        };
       
		mp = MediaPlayer.create(this, R.raw.old_tick);   
        
        countProgress = ((ProgressBar) findViewById(R.id.exercisize_progress_bar));
		final ImageButton button = ((ImageButton) findViewById(R.id.exercisize_button_next));
        
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        level = prefs.getInt("level", 6);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	startExercize(button);
        }
        updateExercisizeDescription();
        
        findViewById(R.id.exercisize_button_next).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				switch(phase) {
				case PHASE_READY:
					startExercize(button);
			        break;
				case PHASE_PLAYING:
					if(exercize < 3) {
						exercize++;
						period++;
						updateExercisizeDescription();
					} else {
						exercize++;
						period++;
						updateExercisizeDescription();
						phase = PHASE_LAST;
						button.setImageDrawable(getResources().getDrawable(R.drawable.stop));
					}
					break;
				case PHASE_LAST:
					phase = PHASE_STOPED;
					level++;
				    SharedPreferences.Editor editor = prefs.edit();
				    editor.putInt("level", level);
				    // Commit the edits!
				    editor.commit();
				    
				    // startActivity(new Intent(ExercisizeActivity.this, SummaryActivity.class));
				    finish();
				    break; 
				}
			}
		});
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	phase = PHASE_STOPED;
        mp.release();
    }
    
	private void startCountDownExercize() {
		final long periodTime = periods[period];
		final long tickTime = periodTime / counts[level][period];
    	long o = 0;
    	for(int i=0; i<period; i++) { 
    		o += periods[i]; 
    	}
    	final long offset = o;
    	Log.d("5Bx","Counting "+periodTime+":"+tickTime+"+"+offset);
    	
		new CountDownTimer(periodTime, tickTime) {
		     public void onTick(long millisUntilFinished) {
		     	Log.d("5Bx","Tick "+millisUntilFinished+"+"+offset+"/"+11*60*1000+"="+ 100.0 * (offset+millisUntilFinished) / (11*60*1000));
		     	if(PHASE_STOPED != phase) {
		     		playSound();
		     	}
		    	updateProgress(offset, millisUntilFinished);
		     }
		     public void onFinish() {	    	 
		    	 period++;
		     	 if(PHASE_STOPED != phase) {
		     		 startCountDownExercize();
		     	 }
		     }
		  }.start();
	}
	
	private void updateProgress(final long offset,
			long millisUntilFinished) {
		countProgress.setProgress((int) (100.0 * (offset+millisUntilFinished) / (11*60*1000.0)));
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.exercize, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    	case R.id.options:
	    		showDialog(DIALOG_SELECT_LEVEL);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}	
    
	protected Dialog onCreateDialog(int id) {
	    switch(id) {
	    case DIALOG_SELECT_LEVEL: 
//	    	Context mContext = getApplicationContext();
	    	final Dialog dialog = new Dialog(this);
	    	
	    	dialog.setContentView(R.layout.dialog_select_level);
	    	final TextView levelText = (TextView) dialog.findViewById(R.id.select_level_picker);
	    	levelText.setText(""+(level+1));
	    	dialog.setTitle("Select level");
	    	dialog.findViewById(R.id.select_level_button_enter).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					level = Integer.parseInt(""+levelText.getText())-1;
			        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ExercisizeActivity.this);
			        SharedPreferences.Editor editor = prefs.edit();
			        editor.putInt("level", level);
			         // Commit the edits!
			        editor.commit();
			        dialog.dismiss();
				}
			});
		    return dialog;
	    default:
	        return null;
	    }
	}    

	private void startExercize(ImageButton button) {
		phase = PHASE_PLAYING;
		period = 0;
		button.setImageDrawable(getResources().getDrawable(R.drawable.next));
		startCountDownExercize();
		
	}
	
	private void updateExercisizeDescription() {
		String levelText = String.format(getResources().getString(R.string.exercize_format_level), (level / 12), 'D'-((level % 12 )/3), level+1);
		((TextView) findViewById(R.id.exercisize_text_level)).setText(levelText);
		((TextView) findViewById(R.id.exercisize_text_nr)).setText(""+(exercize+1));
		((TextView) findViewById(R.id.exercisize_text_count)).setText(""+counts[level][exercize]);
		((ImageView) findViewById(R.id.exercisize_picture)).setImageDrawable(getResources().getDrawable(pictures[level / 12][exercize]));
		
		long millisUntilFinished = 0;
		long periodTime = periods[period];
		long tickTime = periodTime / counts[level][period];
    	long o = 0;
    	for(int i=0; i<=period; i++) { 
    		o += periods[i]; 
    	}
    	final long offset = o;
    	Log.d("5Bx","Counting "+periodTime+":"+tickTime+"+"+offset);
    	updateProgress(offset, millisUntilFinished);
	}

	
	private void playSound() {
		/*
        mp.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
            }
        });
        */
        mp.start();

	}
}