package com.example.lab03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button bottomR, bottomL;
    TextView topL, topR;
    String TAG = "com.example.lab03.sharedprefs";
    SharedPreferences spref;
    SharedPreferences.Editor edt;
    ConstraintLayout lyt;
    SeekBar seekb;
    TextView[] vw;
    long st, cl;
    float cps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomR = findViewById(R.id.bottomright);
        bottomL = findViewById(R.id.bottomleft);
        topL = findViewById(R.id.topleft);
        topR = findViewById(R.id.topright);

        bottomR.setOnClickListener(this);
        bottomL.setOnClickListener(this);
        topL.setOnClickListener(this);
        topR.setOnClickListener(this);

        seekb = findViewById(R.id.seekb);
        vw = new TextView[]{bottomR, bottomL, topR, topL};
        lyt = findViewById(R.id.activity_main_layout);

        spref = getSharedPreferences(TAG,MODE_PRIVATE);
        edt = spref.edit();

        seekb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pc;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for (TextView tv : vw) {
                    tv.setTextSize(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//record state
                pc = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//pop snackbar
                Snackbar snackbar = Snackbar.make(lyt, "Font Size Changed To " + seekBar.getProgress() + "sp", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v2) {
                                seekBar.setProgress(pc);
                                for (TextView tv : vw) {
                                    tv.setTextSize(pc);
                                }
                                Snackbar.make(lyt,"Font Size Reverted Back To " + pc + "sp", Snackbar.LENGTH_LONG);
                            }
                        }
                );
                snackbar.setActionTextColor(Color.BLUE);
                View snackBarView = snackbar.getView();
                TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            }
        });
        lyt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                edt.clear().apply();
                setInitialValues();
                return false;
            }
        });
        setInitialValues();
        st = System.currentTimeMillis();
    }

    private void setInitialValues() {
        for (TextView tv : vw) {
            tv.setText(spref.getString(tv.getTag().toString(),"0"));
        }
        seekb.setProgress(30);
    }

    @Override
    public void onClick(View v) {
        TextView tv = (TextView)v;
        tv.setText(""+ (Integer.parseInt(tv.getText().toString()) + 1));
        edt.putString(tv.getTag().toString(),tv.getText().toString()).apply();
        cps = ++cl / ((60 - st) / 1000f);
        Toast.makeText(this,"" + cps, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setInitialValues();
    }
}