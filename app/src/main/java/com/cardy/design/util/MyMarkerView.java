package com.cardy.design.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.TextView;

import com.cardy.design.R;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import org.w3c.dom.Text;

import java.util.List;

public class MyMarkerView extends MarkerView {
    private TextView tvContent;
    private List<String> labels;

    public MyMarkerView(Context context, int layoutResource, List<String> labels){
        super(context,layoutResource);
        tvContent = findViewById(R.id.textViewContent);
        this.labels = labels;
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        tvContent.setText(labels.get((int) entry.getX())+"\n"+entry.getY());
        super.refreshContent(entry,highlight);
    }
}
