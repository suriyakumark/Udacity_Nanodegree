package com.simplesolutions2003.nanodegreeprojectzero;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tvAppTitle;
    private Button btnApp_01;
    private Button btnApp_02;
    private Button btnApp_03;
    private Button btnApp_04;
    private Button btnApp_05;
    private Button btnApp_06;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnApp_01 = (Button) findViewById(R.id.btnApp_01);
        btnApp_02 = (Button) findViewById(R.id.btnApp_02);
        btnApp_03 = (Button) findViewById(R.id.btnApp_03);
        btnApp_04 = (Button) findViewById(R.id.btnApp_04);
        btnApp_05 = (Button) findViewById(R.id.btnApp_05);
        btnApp_06 = (Button) findViewById(R.id.btnApp_06);

        btnApp_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.ToastPrefix).toString() + " " + getResources().getText(R.string.App_01).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        btnApp_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.ToastPrefix).toString() + " " + getResources().getText(R.string.App_02).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        btnApp_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.ToastPrefix).toString() + " " + getResources().getText(R.string.App_03).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        btnApp_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.ToastPrefix).toString() + " " + getResources().getText(R.string.App_04).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        btnApp_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.ToastPrefix).toString() + " " + getResources().getText(R.string.App_05).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        btnApp_06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.ToastPrefix).toString() + " " + getResources().getText(R.string.App_06).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    
}
