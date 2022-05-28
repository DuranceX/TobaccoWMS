package com.cardy.design.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cardy.design.R;
import com.cardy.design.entity.CustomerAmount;
import com.cardy.design.entity.MaterialPurchaseAmount;
import com.cardy.design.entity.ProductSaleAmount;
import com.cardy.design.entity.SupplierAmount;
import com.cardy.design.util.MyMarkerView;
import com.cardy.design.util.MyMarkerViewMultiple;
import com.cardy.design.viewmodel.ReportViewModel;
import com.cardy.design.widget.IconFontTextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.kongzue.dialogx.dialogs.WaitDialog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ReportFragment extends Fragment {
    IconFontTextView menuButton;
    TextView totalSale, totalPurchase;
    BarChart productBC, materialBC, customerBC, supplierBC;
    ReportViewModel viewModel;

    List<ProductSaleAmount> productSaleAmountList;
    List<MaterialPurchaseAmount> materialPurchaseAmountList;
    List<CustomerAmount> customerAmountList;
    List<SupplierAmount> supplierAmountList;
    Double totalSalePrice, totalPurchasePrice;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuButton = getView().findViewById(R.id.menuButton);
        totalSale = view.findViewById(R.id.textViewTotalSalePrice);
        totalPurchase = view.findViewById(R.id.textViewTotalPurchasePrice);
        productBC = view.findViewById(R.id.ProductSalePriceBarChart);
        materialBC = view.findViewById(R.id.MaterialPurchasePriceBarChart);
        customerBC = view.findViewById(R.id.CustomerSaleBarChart);
        supplierBC = view.findViewById(R.id.SupplierPurchaseBarChart);
        ScrollView scrollView = view.findViewById(R.id.scrollView2);
        scrollView.setScrollBarSize(0);

        WaitDialog.show("加载数据，请稍候");
        initData();

        menuButton.setOnClickListener(v->{
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(GravityCompat.START);
        });
    }

    public void initData(){
        Handler mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 10){
                    WaitDialog.dismiss();
                    totalSale.setText(String.valueOf(totalSalePrice));
                    totalPurchase.setText(String.valueOf(totalPurchasePrice));

                    setProductBarChart();
                    setMaterialBarChart();
                    setCustomerBarChart();
                    setSupplierBarChart();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                LocalDate nowDate = LocalDate.now();
                LocalDate date = LocalDate.of(nowDate.getYear(),nowDate.getMonthValue()-1,nowDate.getDayOfMonth());
                totalSalePrice = viewModel.getLastMonthSalePrice(date.toString());
                totalPurchasePrice = viewModel.getLastMonthPurchasePrice(date.toString());
                productSaleAmountList = viewModel.getProductSaleAmount();
                materialPurchaseAmountList = viewModel.getMaterialPurchaseAmount();
                customerAmountList = viewModel.getCustomerAmount();
                supplierAmountList = viewModel.getSupplierAmount();
                Message message = Message.obtain();
                message.what = 10;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    public void setProductBarChart(){
        //定义数组
        List<BarEntry> entries = new ArrayList<>();
        List<String> xLabel = new ArrayList<>();
        //写入数据
        for (int i = 0; i < productSaleAmountList.size(); i++) {
            entries.add(new BarEntry(i, (float) productSaleAmountList.get(i).getPrice()));
            xLabel.add(productSaleAmountList.get(i).getProductName()+" - "+productSaleAmountList.get(i).getProductModel());
        }
        BarDataSet set = new BarDataSet(entries,"ProductBarDataSet");
        styleBarChart(set,xLabel,productBC);
        //更新数据
        productBC.invalidate();
    }

    public void setMaterialBarChart(){
        List<BarEntry> entries = new ArrayList<>();
        List<String> xLabel = new ArrayList<>();
        for (int i = 0; i < materialPurchaseAmountList.size(); i++) {
            entries.add(new BarEntry(i,(float) materialPurchaseAmountList.get(i).getPrice()));
            xLabel.add(materialPurchaseAmountList.get(i).getMaterialName()+" - "+materialPurchaseAmountList.get(i).getMaterialModel());
        }
        BarDataSet set = new BarDataSet(entries,"MaterialBarDataSet");
        styleBarChart(set,xLabel,materialBC);
        materialBC.invalidate();
    }

    public void setCustomerBarChart(){
        List<BarEntry> priceEntries = new ArrayList<>();
        List<BarEntry> timesEntries = new ArrayList<>();
        List<String> xLabel = new ArrayList<>();
        for (int i = 0; i < customerAmountList.size(); i++) {
            priceEntries.add(new BarEntry(i, (float) customerAmountList.get(i).getPrice()));
            timesEntries.add(new BarEntry(i,customerAmountList.get(i).getTimes()*10000));
            xLabel.add(customerAmountList.get(i).getCustomer());
        }
        BarDataSet priceSet = new BarDataSet(priceEntries,"客户购买金额");
        BarDataSet timesSet = new BarDataSet(timesEntries,"客户购买次数");
        styleBarChartMultiple(xLabel,customerBC,priceSet,timesSet);
        customerBC.invalidate();
    }

    public void setSupplierBarChart(){
        List<BarEntry> priceEntries = new ArrayList<>();
        List<BarEntry> timesEntries = new ArrayList<>();
        List<String> xLabel = new ArrayList<>();
        for (int i = 0; i < supplierAmountList.size(); i++) {
            priceEntries.add(new BarEntry(i, (float) supplierAmountList.get(i).getPrice()));
            timesEntries.add(new BarEntry(i,supplierAmountList.get(i).getTimes()*10000));
            xLabel.add(supplierAmountList.get(i).getSupplier());
        }
        BarDataSet priceSet = new BarDataSet(priceEntries,"供货商供应金额");
        BarDataSet timesSet = new BarDataSet(timesEntries,"供货商供应次数");
        styleBarChartMultiple(xLabel,supplierBC,priceSet,timesSet);
        supplierBC.invalidate();
    }

    public void styleBarChart(BarDataSet set, List<String> xLabel, BarChart barChart){
        //设置颜色
        set.setColors(new int[]{R.color.red_soft,R.color.blue_soft,R.color.green_soft,R.color.orange_soft},getActivity());
        BarData data = new BarData(set);
        //设置每列宽度
        data.setBarWidth(0.9f);
        //设置数据
        barChart.setData(data);
        //设置为true则调整列宽，使得能够显示下所有的列
        barChart.setFitBars(true);
        //定义x标签格式化
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return xLabel.get((int)value);
            }
        };
        //获取x轴对象
        XAxis xAxis = barChart.getXAxis();
        //设置x轴的step
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxisLeft = barChart.getAxisLeft();
        //设置金额显示为大数据效果1000->1k
        yAxisLeft.setValueFormatter(new LargeValueFormatter());
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        //删除图例
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        //删除图标描述
        Description desc = new Description();
        desc.setText("");
        barChart.setDescription(desc);

        //设置，点击显示弹窗呈现数据
        MyMarkerView mv = new MyMarkerView(getActivity(),R.layout.custom_marker_view,xLabel);
        mv.setChartView(barChart);
        barChart.setMarker(mv);
    }

    public void styleBarChartMultiple(List<String> xLabel, BarChart barChart, BarDataSet... sets){
        sets[0].setColors(new int[]{R.color.red_soft},getActivity());
        sets[1].setColors(new int[]{R.color.blue_soft},getActivity());

        //设置次数显示效果
        ValueFormatter timesValueFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue((int) value/10000);
            }
        };
        ValueFormatter timesAxisFormatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return String.valueOf((int)value/10000);
            }
        };
        sets[1].setValueFormatter(timesValueFormatter);
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setValueFormatter(timesAxisFormatter);


        BarData data = new BarData(sets);
        data.setBarWidth(0.45f);
        //设置数据
        barChart.setData(data);
        //设置为true则调整列宽，使得能够显示下所有的列
        barChart.setFitBars(true);

        //设置图例
        Legend legend = barChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);

        //设置分组显示效果
        barChart.groupBars(-0.45f,0.1f,0f);
        //设置为true则调整列宽，使得能够显示下所有的列
        barChart.setFitBars(true);
        //定义x标签格式化
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return xLabel.get((int)value);
            }
        };
        //获取x轴对象
        XAxis xAxis = barChart.getXAxis();
        //设置x轴的step
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12f);
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //设置金额显示为大数据效果1000->1k
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setValueFormatter(new LargeValueFormatter());

        //删除图标描述
        Description desc = new Description();
        desc.setText("");
        barChart.setDescription(desc);

        //设置，点击显示弹窗呈现数据
        MyMarkerViewMultiple mv = new MyMarkerViewMultiple(getActivity(),R.layout.custom_marker_view,xLabel);
        mv.setChartView(barChart);
        barChart.setMarker(mv);
    }
}