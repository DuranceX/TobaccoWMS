package com.cardy.design.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

public class Util {

    public static Uri getImagePath(Context context, String path){
        if(DocumentsContract.isDocumentUri(context,Uri.parse(path))){
            Uri uri = Uri.parse(path);
            String imagePath = "";
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                if (docId.startsWith("raw:")) {
                    imagePath = docId.replaceFirst("raw:", "");
                } else {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                    imagePath = getImagePath(context,contentUri, null);
                }
            }
            return Uri.parse(imagePath);
        }
        else
            return Uri.parse(path);
    }

    @SuppressLint("Range")
    private static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
