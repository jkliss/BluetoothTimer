package com.example.juliu.bluetoothtimer;

import android.bluetooth.BluetoothAdapter;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public boolean stop = false;

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
                    int number = Integer.parseInt(gottext);
                    startTimer(number*60);
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
        textView.setText("");
    }

    void startTimer(int input){
        final TextView textView = (TextView) findViewById(R.id.Timerfield);

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
                }
            }
        }.start();
    }
}