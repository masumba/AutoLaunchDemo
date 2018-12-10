package com.demo.goon.autolaunch;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnE, btnL;

    static final int RESULT_ENABLE = 1;
    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnE = (Button)findViewById(R.id.btnEnable);
        btnL = (Button)findViewById(R.id.btnLock);

        devicePolicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(MainActivity.this,Controller.class);

        boolean active = devicePolicyManager.isAdminActive(componentName);
        if (active){
            btnE.setText("DISABLE");
            btnL.setVisibility(View.VISIBLE);
        } else {
            btnE.setText("ENABLE");
            btnL.setVisibility(View.GONE);
        }

        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean active = devicePolicyManager.isAdminActive(componentName);
                if (active){
                    devicePolicyManager.removeActiveAdmin(componentName);
                    btnE.setText("ENABLE");
                    btnL.setVisibility(View.GONE);
                } else {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"You should enable app");
                    startActivityForResult(intent,RESULT_ENABLE);
                }
            }
        });

        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devicePolicyManager.lockNow();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK){
                    btnE.setText("DISABLE");
                    btnL.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this,"Failed!",Toast.LENGTH_SHORT).show();
                }
                return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
