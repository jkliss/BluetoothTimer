package com.example.juliu.bluetoothtimer;

import android.bluetooth.BluetoothAdapter;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public boolean stop = false;
    public boolean recentlyChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.Timerfield);
        final Button button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stop = false;
                final EditText editText = (EditText) findViewById(R.id.editText);
                String gottext = String.valueOf(editText.getText());
                try{
                    double snum = Double.parseDouble(gottext);
                    System.out.println(snum);
                    long number = (long) (snum*60);
                    startTimer(number);
                } catch (Exception ex){
                    ex.getStackTrace();
                }
            }
        });
        final Button button2 = (Button) findViewById(R.id.button4);

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stop = true;

                String text = "Canceled";
                textView.setText(text);
            }
        });

        Switch mySwitch = (Switch) findViewById(R.id.switch1);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                recentlyChanged = true;
                if(isChecked){
                    //Enable bluetooth
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();
                    }
                } else {
                    //Disable bluetooth
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }
                }
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });

        periodicBluetoothCheck();
        textView.setText("");
    }

    void startTimer(long input){
        final TextView textView = (TextView) findViewById(R.id.Timerfield);
        final Switch myswitch = (Switch) findViewById(R.id.switch1);

        new CountDownTimer(1000*input, 1000) {

            public void onTick(long millisUntilFinished) {
                if(stop){
                    String text = "Canceled";
                    textView.setText(text);
                    cancel();
                } else {
                    String seconds = String.format("%02d", (millisUntilFinished / 1000) % 60);
                    String minutes = Long.toString((millisUntilFinished / 1000) / 60);
                    String time = minutes + ":" + seconds + " min remaining...";
                    textView.setText(time);
                }
            }

            public void onFinish() {
                String time = "done";
                textView.setText(time);

                //Disable bluetooth
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                    myswitch.setChecked(false);
                }
            }
        }.start();
    }

    void periodicBluetoothCheck(){
        new CountDownTimer(Integer.MAX_VALUE, 10000) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            final Switch mySwitch = (Switch) findViewById(R.id.switch1);

            public void onTick(long millisUntilFinished) {
                if (mBluetoothAdapter != null) {
                    if(recentlyChanged) {
                        //skip check iteration if changed recently
                        recentlyChanged = false;
                    } else if(mBluetoothAdapter.isEnabled()){
                        mySwitch.setChecked(true);
                    } else {
                        mySwitch.setChecked(false);
                    }
                } else {
                    mySwitch.setChecked(false);
                }
            }

            public void onFinish() {

            }
        }.start();

    }
}
