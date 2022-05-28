package com.cardy.design.util;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class ExcelUtil {
    static String TAG = "excelTest";
    public static List<List<Object>> read2003XLS(String path) {
        List<List<Object>> dataList = new ArrayList<List<Object>>();
        try {
            Workbook book = Workbook.getWorkbook(new File(path));
            // book.getNumberOfSheets();  //获取sheet页的数目
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();
            int Cols = sheet.getColumns();
            Log.d(TAG, "当前工作表的名字:" + sheet.getName());
            Log.d(TAG, "总行数:" + Rows + ", 总列数:" + Cols);

            List<Object> objList = new ArrayList<Object>();
            String val = null;
            for (int i = 0; i < Rows; i++) {
                boolean null_row = true;
                for (int j = 0; j < Cols; j++) {
                    // getCell(Col,Row)获得单元格的值，注意getCell格式是先列后行，不是常见的先行后列
                    Log.d(TAG, (sheet.getCell(j, i)).getContents() + "\t");
                    val = (sheet.getCell(j, i)).getContents();
                    if (val == null || val.equals("")) {
                        val = "null";
                    } else {
                        null_row = false;
                    }
                    objList.add(val);
                }
                Log.d(TAG, "\n");
                if (null_row != true) {
                    dataList.add(objList);
                    null_row = true;
                }
                objList = new ArrayList<Object>();
            }
            book.close();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return dataList;
    }
}
