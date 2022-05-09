package com.cardy.design.util;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.cardy.design.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.List;

public class MyMarkerViewMultiple extends MarkerView {
    private TextView tvContent;
    private List<String> labels;

    public MyMarkerViewMultiple(Context context, int layoutResource, List<String> labels){
        super(context,layoutResource);
        tvContent = findViewById(R.id.textViewContent);
        this.labels = labels;
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        float index = entry.getX()+0.45f;
        if(index%1 > 0.5){
            tvContent.setText(labels.get((int)index)+"\n"+(int)entry.getY()/10000);
        }else
            tvContent.setText(labels.get((int)index)+"\n"+entry.getY());
        super.refreshContent(entry,highlight);
    }
}
