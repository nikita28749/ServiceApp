package com.example.nikitamathur.myapplication;

/**
 * Created by Nikita Mathur on 10/2/2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadPDF extends AppCompatActivity {

    public static final String Link1 = "https://www.cisco.com/web/about/ac79/docs/innov/IoE.pdf";
    public static final String Link2 = "http://www.cisco.com/web/about/ac79/docs/innov/IoE_Economy.pdf";
    public static final String Link3 = "http://www.cisco.com/web/strategy/docs/gov/everything-for-cities.pdf";
    public static final String Link4 = "http://www.cisco.com/web/offer/gist_ty2_asset/Cisco_2014_ASR.pdf";
    public static final String Link5 = "http://www.cisco.com/web/offer/emear/38586/images/Presentations/P3.pdf";
    static String myDownloadLink;

    public static final String file1 = "IoE.pdf";
    public static final String file2 = "IoE_Economy.pdf";
    public static final String file3 = "everything-for-cities.pdf";
    public static final String file4 = "Cisco_2014_ASR.pdf";
    public static final String file5 = "P3.pdf";
    static String myFileName;


    public static int file1Flag = 0;
    public static int file2Flag = 0;
    public static int file3Flag = 0;
    public static int file4Flag = 0;
    public static int file5Flag = 0;
    static int myFileFlag;

    static TextView tv1;
    static TextView tv2;
    static TextView tv3;
    static TextView tv4;
    static TextView tv5;
    static TextView myTextView;

    static String downloadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerReceiver(receiver, new IntentFilter(
                ServiceA.NOTIFICATION));
        tv1 = (TextView) findViewById(R.id.pdfFile1);
        tv2 = (TextView) findViewById(R.id.pdfFile2);
        tv3 = (TextView) findViewById(R.id.pdfFile3);
        tv4 = (TextView) findViewById(R.id.pdfFile4);
        tv5 = (TextView) findViewById(R.id.pdfFile5);
        refreshAllTextViews();
        downloadPath = getFilesDir().toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAllTextViews();
        registerReceiver(receiver, new IntentFilter(
                ServiceA.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void downloadFile (View view) {
        Toast.makeText(this, "Starting all downloads !", Toast.LENGTH_SHORT).show();
        startDownloadService(Link1, file1);
        file1Flag = 1;
        refreshTextView(1);

        startDownloadService(Link2, file2);
        file2Flag = 1;
        refreshTextView(2);

        startDownloadService(Link3, file3);
        file3Flag = 1;
        refreshTextView(3);

        startDownloadService(Link4, file4);
        file4Flag = 1;
        refreshTextView(4);

        startDownloadService(Link5, file5);
        file5Flag = 1;
        refreshTextView(5);
    }

    private void startDownloadService (String link, String file) {
        Intent intent = new Intent(getBaseContext(), ServiceA.class);
        intent.putExtra("urlpath", link);
        intent.putExtra("filename", file);
        startService(intent);
    }

    private void refreshTextView (int x) {
        if (x > 5 || x < 1) {
            System.out.println("refreshTextView wrong parameter, returning");
            return;
        }

        if (x == 1) {
            myFileFlag = file1Flag;
            myTextView = tv1;
            myFileName = file1;
            myDownloadLink = Link1;
        } else if (x == 2) {
            myFileFlag = file2Flag;
            myTextView = tv2;
            myFileName = file2;
            myDownloadLink = Link2;
        } else if (x == 3) {
            myFileFlag = file3Flag;
            myTextView = tv3;
            myFileName = file3;
            myDownloadLink = Link3;
        } else if (x == 4) {
            myFileFlag = file4Flag;
            myTextView = tv4;
            myFileName = file4;
            myDownloadLink = Link4;
        } else {
            myFileFlag = file5Flag;
            myTextView = tv5;
            myFileName = file5;
            myDownloadLink = Link5;
        }

        if (myFileFlag == 0) myTextView.setText("PDF" + x + " File Location: " + myDownloadLink);
        else if (myFileFlag == 1) myTextView.setText(myFileName + " is downloading...");
        else if (myFileFlag == 2) myTextView.setText(myFileName + " downloaded at " + downloadPath);
        else if (myFileFlag == -1) myTextView.setText(myFileName + " downloaded failed!");
        else myTextView.setText("unknown internal error");
    }

    private void refreshAllTextViews () {
        refreshTextView(1);
        refreshTextView(2);
        refreshTextView(3);
        refreshTextView(4);
        refreshTextView(5);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int x;
            if (bundle != null) {
                String filename = bundle.getString("file");
                System.out.println("BroadcastReceiver -> onReceive -> Filename -> " + filename);
                if (filename.equals(file1)) x=1;
                else if (filename.equals(file2)) x=2;
                else if (filename.equals(file3)) x=3;
                else if (filename.equals(file4)) x=4;
                else x=5;
                refreshTextView(x);
            }
        }
    };
}
