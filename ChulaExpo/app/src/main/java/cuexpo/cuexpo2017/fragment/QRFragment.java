package cuexpo.cuexpo2017.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.glxn.qrgen.android.QRCode;

import cuexpo.cuexpo2017.R;
import cuexpo.cuexpo2017.activity.MainActivity;
import cuexpo.cuexpo2017.adapter.ScannerActivity;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by James on 20/01/2017.
 */

public class QRFragment extends Fragment implements View.OnClickListener {

    ImageView ivQRProfile,ivQR, ivClear;
    TextView tvQRName, tvQRPersonalInfo;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String id,name,year,school,workerJob,type;
    private final static int REQUEST_QR = 0;

    public QRFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static QRFragment newInstance() {
        QRFragment fragment = new QRFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qrcode, container, false);
        initInstances(rootView, savedInstanceState);
        rootView.findViewById(R.id.scan_qr).setOnClickListener(scanQrOCL);
        return rootView;
    }

    private View.OnClickListener scanQrOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ScannerActivity.class);
            startActivityForResult(intent, REQUEST_QR);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (requestCode == REQUEST_QR && resultCode == MainActivity.RESULT_OK) {
            String qrValue = data.getExtras().getString("QR_VALUE", "something went wrong");
            if (qrValue.contains("staff.chulaexpo.com/api/activities/")) {
                int startIndex = qrValue.indexOf("activities/") + 11;
                int endIndex = qrValue.indexOf('/', startIndex);
                String eventId = qrValue.substring(startIndex, endIndex);

                SharedPreferences activitySharedPref = getActivity().getSharedPreferences("Event", Context.MODE_PRIVATE);
                activitySharedPref.edit().putString("EventID", eventId).apply();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.qr_container, new EventDetailFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("QR Code ไม่ถูกต้อง")
                        .setMessage(qrValue + "ไม่ใช่ URL ของ Chula Expo Event")
                        .setNeutralButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    private void init() {
        // Init Fragment level's variable(s) here
        //get SharedPref
        sharedPref = this.getActivity().getSharedPreferences("FacebookInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        id = sharedPref.getString("id","");
        name = sharedPref.getString("name","");
        type = sharedPref.getString("type","Guest");
        if(type.equals("Academic")){
            year = sharedPref.getString("academicYear","");
            school = sharedPref.getString("academicSchool","");
        } else if(type.equals("Worker")) {
            workerJob = sharedPref.getString("workerJob","");
        }
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        ivQRProfile = (ImageView) rootView.findViewById(R.id.ivQRProfile);
        ivQR = (ImageView) rootView.findViewById(R.id.ivQR);
        tvQRName = (TextView) rootView.findViewById(R.id.tvQrName);
        tvQRPersonalInfo = (TextView) rootView.findViewById(R.id.tvQRPersonalInfo);
        ivClear = (ImageView) rootView.findViewById(R.id.ivClear);
        ivClear.setOnClickListener(this);

        Glide.with(getContext())
                .load("http://graph.facebook.com/"+id+"/picture?type=large")
                .placeholder(R.drawable.iv_profile_temp)
                .error(R.drawable.iv_profile_temp)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(ivQRProfile);

        tvQRName.setText(name);
        //if(type.equals("Academic")) tvQRPersonalInfo.setText("Year"+year+" • "+school);
        if(type.equals("Academic")) tvQRPersonalInfo.setText(school);
        else if(type.equals("Worker")) tvQRPersonalInfo.setText(workerJob);
        else tvQRPersonalInfo.setText("Guest");

        try {
            Bitmap qrBm = QRCode.from((String) tvQRName.getText()).bitmap();
            ivQR.setImageBitmap(qrBm);
        } catch (Exception e){

        }
    }

    @Override
    public void onClick(View v) {
        if(v==ivClear){
            getFragmentManager().popBackStack();
        }
    }
}