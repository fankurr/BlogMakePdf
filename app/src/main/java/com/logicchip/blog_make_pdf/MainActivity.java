package com.logicchip.blog_make_pdf;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.logicchip.blog_make_pdf.Const.FOLDER_PDF;
import static com.logicchip.blog_make_pdf.Const.printAccent;
import static com.logicchip.blog_make_pdf.Const.printPrimary;
import static com.logicchip.blog_make_pdf.Const.tempList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerMoney;
    private ListAdapter listAdapter;
    private ArrayList<ListItem> listItems;
    private SwipeRefreshLayout swipeMoney;
    private MaterialButton buttonPdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeMoney=findViewById(R.id.swipeMoney);
        recyclerMoney=findViewById(R.id.recyclerMoney);
        buttonPdf=findViewById(R.id.buttonPdf);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        recyclerMoney.setHasFixedSize(true);
        recyclerMoney.setLayoutManager(new LinearLayoutManager(this));

        swipeMoney.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeMoney.setRefreshing(true);
                loadItems();
            }
        });

        swipeMoney.setRefreshing(true);
        loadItems();

        buttonPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncMakePdf asyncMakePdf=new AsyncMakePdf();
                asyncMakePdf.execute(tempList());
            }
        });
    }


    public void loadItems(){
        listItems=new ArrayList<ListItem>();
        listItems=tempList();
        listAdapter=new ListAdapter(this,listItems);
        recyclerMoney.setAdapter(listAdapter);
        swipeMoney.setRefreshing(false);
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/

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


    public class AsyncMakePdf extends AsyncTask<ArrayList<ListItem>,String ,Integer>{

        AlertDialog dialogToShow;
        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.preloader_dialog,null);
        PlayGifView imgGif=view.findViewById(R.id.imgGif);
        TextView txtUpdate=view.findViewById(R.id.txtUpdate);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        String path=FOLDER_PDF+File.separator+"Report.pdf";
        PdfWriter writer;

        @Override
        protected Integer doInBackground(ArrayList<ListItem>... arrayLists) {

            /**
             * Creating Document for report
             */

            BaseFont baseFont = null;
            try {
                baseFont = BaseFont.createFont("res/font/montserratregular.ttf", "UTF-8",BaseFont.EMBEDDED);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }


            Font regularHead = new Font(baseFont, 15,Font.BOLD,BaseColor.WHITE);
            Font regularReport = new Font(baseFont, 30,Font.BOLD,printAccent);
            Font regularName = new Font(baseFont, 25,Font.BOLD,BaseColor.BLACK);
            Font regularAddress = new Font(baseFont, 15,Font.BOLD,BaseColor.BLACK);
            Font regularSub = new Font(baseFont, 12);
            Font regularTotal = new Font(baseFont, 16,Font.NORMAL,BaseColor.BLACK);
            Font regularTotalBold = new Font(baseFont, 16,Font.BOLD,BaseColor.BLACK);
            Font footerN = new Font(baseFont, 15,Font.BOLD,printAccent);
            Font footerE = new Font(baseFont, 12,Font.NORMAL,BaseColor.BLACK);


         //   Document document = new Document(PageSize.A4);
            Document document = new Document(PageSize.A4, 36, 36, 36, 72);
            document.addCreationDate();
            document.addAuthor("Akhil");
            document.addCreator("logicchip.com");
            //document.setMargins(0,0,0,0);

// Location to save
            try {
                writer =PdfWriter.getInstance(document, new FileOutputStream(path));
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }




            PdfPTable tableFooter = new PdfPTable(1);
            tableFooter.setTotalWidth(523);

            PdfPCell footerName = new PdfPCell(new Phrase("LOGICCHIP",footerN));
            PdfPCell footerEmail = new PdfPCell(new Phrase("info@logicchip.com",footerE));

            PdfPCell footerEmpty = new PdfPCell(new Phrase(""));

            footerName.setBorder(Rectangle.NO_BORDER);
            footerEmpty.setBorder(Rectangle.NO_BORDER);
            footerEmail.setBorder(Rectangle.NO_BORDER);


            PdfPCell preBorderBlue = new PdfPCell(new Phrase(""));
            preBorderBlue.setMinimumHeight(5f);
            preBorderBlue.setUseVariableBorders(true);
            preBorderBlue.setBorder(Rectangle.TOP);
            preBorderBlue.setBorderColorTop(printPrimary);
            preBorderBlue.setBorderWidthTop(3);
            tableFooter.addCell(preBorderBlue);
            tableFooter.addCell(footerName);
            tableFooter.addCell(footerEmail);




            HeaderFooter event = new HeaderFooter(tableFooter);
            writer.setPageEvent(event);

            document.open();

            onProgressUpdate("Please wait...");


            //5












            PdfPCell preReport = new PdfPCell(new Phrase("REPORT",regularReport));


            preReport.setHorizontalAlignment(Element.ALIGN_RIGHT);
            preReport.setVerticalAlignment(Element.ALIGN_BOTTOM);
            preReport.setBorder(Rectangle.NO_BORDER);
            PdfPTable tableHeader = new PdfPTable(2);


            try {
                tableHeader.setWidths(new float[] { 1,3});
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            try {
                Drawable d = getResources().getDrawable(R.drawable.logo);
                BitmapDrawable bitDw = ((BitmapDrawable) d);
                Bitmap bmp = bitDw.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                // image.scaleToFit(50, 50);
                PdfPCell preImage =     new PdfPCell(image, true);
                preImage.setBorder(Rectangle.NO_BORDER);

                tableHeader.addCell(preImage);
                tableHeader.addCell(preReport);

                document.add(tableHeader);

            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }




            PdfPTable tableHeading = new PdfPTable(2);
            tableHeading.setSpacingBefore(50);

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c);

            String theName="",theAddress="";

            theName="Name of person";
            theAddress="Address of person";

            PdfPCell preName = new PdfPCell(new Phrase(theName,regularName));
            PdfPCell preAddress = new PdfPCell(new Phrase(theAddress,regularAddress));
            PdfPCell preDate = new PdfPCell(new Phrase("DATE: "+formattedDate,regularAddress));
            PdfPCell preBill=new PdfPCell(new Phrase("No : 0001",regularAddress));

            preBill.setVerticalAlignment(Element.ALIGN_BOTTOM);
            preBill.setHorizontalAlignment(Element.ALIGN_RIGHT);

            preDate.setVerticalAlignment(Element.ALIGN_BOTTOM);
            preDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
            preName.setBorder(Rectangle.NO_BORDER);
            preAddress.setBorder(Rectangle.NO_BORDER);
            preDate.setBorder(Rectangle.NO_BORDER);
            preBill.setBorder(Rectangle.NO_BORDER);

            try {
                tableHeading.addCell(preName);
                tableHeading.addCell(preBill);
                tableHeading.addCell(preAddress);
                tableHeading.addCell(preDate);
                document.add(tableHeading);
            } catch (DocumentException e) {
                e.printStackTrace();
            }




            PdfPTable table = new PdfPTable(4);
            table.setSpacingBefore(20);
            try {
                table.setWidths(new float[] { 1f,2, 3,1.5f});
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            table.setHeaderRows(1);

            table.setSplitRows(false);
            table.setComplete(false);




            PdfPCell headDate = new PdfPCell(new Phrase("NO",regularHead));
            PdfPCell headName = new PdfPCell(new Phrase("ITEM",regularHead));
            PdfPCell headDis = new PdfPCell(new Phrase("COMPANY",regularHead));
            PdfPCell headCr = new PdfPCell(new Phrase("AMOUNT",regularHead));
            PdfPCell headDe = new PdfPCell(new Phrase("DEBIT",regularHead));

            headDate.setPaddingLeft(15);
            headDate.setPaddingTop(10);
            headDate.setPaddingBottom(14);
            headDate.setVerticalAlignment(Element.ALIGN_MIDDLE);

            headName.setPaddingTop(10);
            headName.setPaddingBottom(14);
            headName.setVerticalAlignment(Element.ALIGN_MIDDLE);

            headDis.setPaddingTop(10);
            headDis.setPaddingBottom(14);
            headDis.setVerticalAlignment(Element.ALIGN_MIDDLE);

            headCr.setPaddingTop(10);
            headCr.setPaddingBottom(14);
            headCr.setVerticalAlignment(Element.ALIGN_MIDDLE);

            headDe.setPaddingTop(10);
            headDe.setPaddingBottom(14);
            headDe.setVerticalAlignment(Element.ALIGN_MIDDLE);



            headDate.setBackgroundColor(printPrimary);
            headName.setBackgroundColor(printPrimary);
            headDis.setBackgroundColor(printPrimary);
            headCr.setBackgroundColor(printPrimary);
            headDe.setBackgroundColor(printPrimary);


            headDate.setBorder(Rectangle.NO_BORDER);
            headName.setBorder(Rectangle.NO_BORDER);
            headDis.setBorder(Rectangle.NO_BORDER);
            headCr.setBorder(Rectangle.NO_BORDER);
            headDe.setBorder(Rectangle.NO_BORDER);




            table.addCell(headDate);
            table.addCell(headName);
            table.addCell(headDis);
            table.addCell(headCr);


            int amountFull=0;
            for (int aw=0;aw<arrayLists[0].size();aw++){
                ListItem listItem=arrayLists[0].get(aw);

                onProgressUpdate(String.valueOf(aw)+" items processing...");

                PdfPCell cellNo = new PdfPCell(new Phrase(String.valueOf(aw+1),regularSub));
                PdfPCell cellName = new PdfPCell(new Phrase(listItem.getName(),regularSub));
                PdfPCell cellCompany = new PdfPCell(new Phrase(listItem.getCompany(),regularSub));
                PdfPCell cellAmount = new PdfPCell(new Phrase(listItem.getAmount(),regularSub));
                amountFull+=Integer.parseInt(listItem.getAmount());
                cellNo.setPaddingLeft(15);
                cellNo.setPaddingBottom(8);
                cellNo.setPaddingTop(5);
                cellNo.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cellName.setPaddingBottom(8);
                cellName.setPaddingTop(5);
                cellName.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cellCompany.setPaddingBottom(8);
                cellCompany.setPaddingTop(5);
                cellCompany.setVerticalAlignment(Element.ALIGN_MIDDLE);


                cellAmount.setPaddingBottom(8);
                cellAmount.setPaddingTop(5);
                cellAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cellNo.setBorder(Rectangle.NO_BORDER);
                cellName.setBorder(Rectangle.NO_BORDER);
                cellCompany.setBorder(Rectangle.NO_BORDER);
                cellAmount.setBorder(Rectangle.NO_BORDER);

                if (aw%2==0){
                    cellNo.setBackgroundColor(BaseColor.WHITE);
                    cellName.setBackgroundColor(BaseColor.WHITE);
                    cellCompany.setBackgroundColor(BaseColor.WHITE);
                    cellAmount.setBackgroundColor(BaseColor.WHITE);
                }else {
                    cellNo.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cellName.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cellCompany.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cellAmount.setBackgroundColor(BaseColor.LIGHT_GRAY);
                }

                table.addCell(cellNo);
                table.addCell(cellName);
                table.addCell(cellCompany);
                table.addCell(cellAmount);
            }




            PdfPCell preBorderGray = new PdfPCell(new Phrase(""));
            preBorderGray.setPaddingTop(10);
            preBorderGray.setMinimumHeight(20f);
            preBorderGray.setUseVariableBorders(true);
            preBorderGray.setBorder(Rectangle.BOTTOM);
            preBorderGray.setBorderColorBottom(BaseColor.GRAY);
            preBorderGray.setBorderWidthBottom(3);
            preBorderGray.setColspan(5);

            table.addCell(preBorderGray);

            PdfPCell preTotal = new PdfPCell(new Phrase("TOTAL",regularTotalBold));
            PdfPCell preTotalAmount = new PdfPCell(new Phrase(String.valueOf(amountFull),regularTotalBold));

            preTotal.setPaddingTop(50);
            preTotalAmount.setPaddingTop(50);
            preTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            preTotal.setPaddingRight(50);

            preTotal.setBorder(Rectangle.NO_BORDER);
            preTotalAmount.setBorder(Rectangle.NO_BORDER);

            preTotal.setColspan(3);

            table.addCell(preTotal);
            table.addCell(preTotalAmount);

            table.setComplete(true);
            try {
                document.add(table);
            } catch (DocumentException e) {
                e.printStackTrace();
            }



            onProgressUpdate("Finishing up...");
            document.close();
            return 1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            imgGif.setImageResource(R.drawable.preloader);
            builder.setView(view);
            builder.setCancelable(false);
            dialogToShow = builder.create();
            dialogToShow.setCanceledOnTouchOutside(false);
            dialogToShow.show();


            File file =new File(FOLDER_PDF);
            if (file.exists()){
                file.mkdir();
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            final String a=values[0].toString();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtUpdate.setText(a);
                }
            });
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);


            File file = new File(path);
            Uri pdfURI=FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
            Intent target = new Intent(Intent.ACTION_VIEW);
            // target.setDataAndType(Uri.fromFile(file),"application/pdf");
            target.setDataAndType(pdfURI,"application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            txtUpdate.setText("Finished");
            dialogToShow.dismiss();

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }

        }

    }
}
