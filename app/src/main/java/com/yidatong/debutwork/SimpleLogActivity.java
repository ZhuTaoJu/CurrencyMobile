package com.yidatong.debutwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SimpleLogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_simple_log);
            TextView textView = null;
            textView=(TextView)findViewById(R.id.simpleLogView);
            textView.setMovementMethod(new ScrollingMovementMethod());
            Bundle bundle = null;
            Intent intent = null;
            intent=  getIntent();
            if(intent != null){
                bundle = intent.getExtras();
            }
            if (bundle != null) {
                String value = bundle.getString("err"); // 获取的值应与放入的值类型对应
                textView.append(value);
            }
            if(TransitionParameters.mTp.simpleLogs !=null )
            {
                for(int i=0;i<TransitionParameters.mTp.simpleLogs.size();i++){
                    textView.append(TransitionParameters.mTp.simpleLogs.get(i));
                }
            }
            TransitionParameters.mTp.logTV = textView;
        }
        catch (Exception ex){
//            Log.d("SimpleLogActivity", ex.toString());
        }
    }

}