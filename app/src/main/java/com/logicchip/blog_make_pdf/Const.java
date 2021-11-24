package com.logicchip.blog_make_pdf;

import android.os.Environment;

import com.itextpdf.text.BaseColor;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Akhil Ashok
 * akhilashok123@gmail.com
 */
public class Const {


    public static String FOLDER_PDF=Environment.getExternalStorageDirectory() + File.separator+"Logicchip";

    public static BaseColor printAccent=new BaseColor(216,27,96);
    public static BaseColor printPrimary=new BaseColor(0,133,119);//A = 1

    public static ArrayList<ListItem> tempList(){
        ArrayList<ListItem> listItems = new ArrayList<ListItem>();
        for (int i=0;i<30;i++){
        listItems.add(new ListItem("Item Name "+String.valueOf(i),"Company "+String.valueOf(i),"01 Jan 2018",String.valueOf(i+1*100)));
        }
        return listItems;
    }
}
