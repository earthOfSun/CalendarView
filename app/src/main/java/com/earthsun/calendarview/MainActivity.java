package com.earthsun.calendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.earthsun.calendarview.calendarView.CalendarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnSign = findViewById(R.id.btn_sign);
        final CalendarView calendarView = findViewById(R.id.calendar_view_sign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> sign = new ArrayList<>();
                sign.add(4);
                sign.add(6);
                calendarView.setTime(1525164868000l);
                calendarView.setSignDays(sign);
            }
        });
    }
}
