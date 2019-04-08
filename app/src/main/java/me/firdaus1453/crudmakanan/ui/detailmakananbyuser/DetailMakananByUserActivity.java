package me.firdaus1453.crudmakanan.ui.detailmakananbyuser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.firdaus1453.crudmakanan.R;
import me.firdaus1453.crudmakanan.model.makanan.MakananData;
import me.firdaus1453.crudmakanan.utils.Constants;

public class DetailMakananByUserActivity extends AppCompatActivity implements DetailMakananByUserContract.View {

    @BindView(R.id.img_picture)
    ImageView imgPicture;
    @BindView(R.id.fab_choose_picture)
    FloatingActionButton fabChoosePicture;
    @BindView(R.id.layoutPicture)
    CardView layoutPicture;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_desc)
    EditText edtDesc;
    @BindView(R.id.spin_category)
    Spinner spinCategory;
    @BindView(R.id.layoutSaveMakanan)
    CardView layoutSaveMakanan;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private DetailMakananByUserPresenter mDetailMakananByUserPresenter = new DetailMakananByUserPresenter(this);
    private Uri filePath = null;
    private String idCategory, idMakanan;
    private MakananData mMakananData;
    private String namaFotoMakanan;
    private String[] mIdCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_makanan_by_user);
        ButterKnife.bind(this);

        // Melakukan pengecekan dan permission untuk bisa mengakses gallery
        PermissionGalery();

        // Menangkap id makanan yang dikirimkan dari activity sebelumnya
        idMakanan = getIntent().getStringExtra(Constants.KEY_EXTRA_ID_MAKANAN);

        // Mengambil data category untuk ditampilkan di spinner
        mDetailMakananByUserPresenter.getCategory();

        // Mensetting swiperefresh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);

                // Mengambil data category untuk ditampilkan di spinner
                mDetailMakananByUserPresenter.getCategory();
            }
        });

    }

    private void PermissionGalery() {
        // Mencek apakah user sudah memberikan permission untuk mengakses external storage
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.STORAGE_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                showMessage("Permission granted now you can read the storage");
                Log.i("Permission on", "onRequestPermissionsResult: " + String.valueOf(grantResults));
            } else {
                //Displaying another toast if permission is not granted
                showMessage("Oops you just denied the permission");
                Log.i("Permission off", "onRequestPermissionsResult: " + String.valueOf(grantResults));
            }
        }
    }

    @Override
    public void showProgress() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showDetailMakanan(MakananData makananData) {
        // Kita ambil semua data detail makanan
        mMakananData = makananData;

        // Mengambil id category
        idCategory = makananData.getIdKategori();

        // Mengambil nama foto makanan
        namaFotoMakanan = makananData.getFotoMakanan();

        // Menampilkan semua data ke layar
        edtName.setText(makananData.getNamaMakanan());
        edtDesc.setText(makananData.getDescMakanan());

        // Memilih spinner sesuai dengan category makanan yang ada di dalam database
        for (int i = 0 ;i < mIdCategory.length; i++){

            Log.i("cek", "isi loop select mIdCategory: " + mIdCategory[i]);

            if (Integer.valueOf(mIdCategory[i]).equals(Integer.valueOf(idCategory))){
                spinCategory.setSelection(i);
                Log.i("cek", "isi select mIdCategory: " + mIdCategory[i]);
                Log.i("cek", "isi select idCategory: " + idCategory);
            }
        }


        // Menampilkan gambar makanan
        RequestOptions options = new RequestOptions().error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_broken_image);
        Glide.with(this).load(makananData.getUrlMakanan()).apply(options).into(imgPicture);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successDelete() {
        finish();
    }

    @Override
    public void successUpdate() {
        mDetailMakananByUserPresenter.getCategory();
    }

    @Override
    public void showSpinnerCategory(List<MakananData> categoryDataList) {
        // Membuat data penampung untuk spinner
//        List<String> listSpinner = new ArrayList<>();

        String [] namaCategory = new String[categoryDataList.size()];
        mIdCategory = new String[categoryDataList.size()];

        for (int i = 0; i < categoryDataList.size(); i++) {
//            listSpinner.add(categoryDataList.get(i).getNamaKategori());
            namaCategory[i] = categoryDataList.get(i).getNamaKategori();
            mIdCategory[i] = categoryDataList.get(i).getIdKategori();

            Log.i("cek", "isi show namaCategory: " + namaCategory[i]);
            Log.i("cek", "isi show mIdCategory: " + mIdCategory[i]);
        }

        // Membuat adapter spinner
        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namaCategory);
        // Kita setting untuk menampilkan spinner dengan 1 line
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Memasukkan adapter ke spinner
        spinCategory.setAdapter(categorySpinnerAdapter);


        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Mengambil id category sesuai dengan pilihan user
                idCategory = categoryDataList.get(position).getIdKategori();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Mengambil data detail makanan
        mDetailMakananByUserPresenter.getDetailMakanan(idMakanan);

    }

    @OnClick({R.id.fab_choose_picture, R.id.btn_update, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_choose_picture:
                PermissionGalery();
                // Mengambil gambar dari storage
                ShowFileChooser();
                break;
            case R.id.btn_update:
                mDetailMakananByUserPresenter.updateDataMakanan(
                        this,
                        filePath,
                        edtName.getText().toString(),
                        edtDesc.getText().toString(),
                        idCategory,
                        namaFotoMakanan,
                        idMakanan
                );
                break;
            case R.id.btn_delete:
                mDetailMakananByUserPresenter.deleteMakanan(idMakanan, namaFotoMakanan);
                break;
        }
    }

    private void ShowFileChooser() {
        // Membuat object intent untuk dapat memilih data
        Intent intentGallery = new Intent(Intent.ACTION_PICK);
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentGallery, "Select Pictures"),
                1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null
        ) {
            // mengambil data foto dan memasukkan ke dalam variable filePath
            filePath = data.getData();

            try {
                // Mengambil data gambar lalu di convert ke bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Tampilkan gambar yang baru dipilih ke layar
                imgPicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Mengambil data category untuk ditampilkan di spinner
//        mDetailMakananByUserPresenter.getCategory();
//    }
}
