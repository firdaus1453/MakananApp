package me.firdaus1453.crudmakanan.ui.detailmakanan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.firdaus1453.crudmakanan.R;
import me.firdaus1453.crudmakanan.model.makanan.MakananData;
import me.firdaus1453.crudmakanan.utils.Constants;

public class DetailMakananActivity extends AppCompatActivity implements DetailMakananContract.View {

    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.txt_info)
    TextView txtInfo;
    @BindView(R.id.rv_progress)
    RelativeLayout rvProgress;
    @BindView(R.id.img_makanan_detail)
    ImageView imgMakananDetail;
    @BindView(R.id.txt_name_makanan_detail)
    TextView txtNameMakananDetail;
    @BindView(R.id.txt_time_makanan_detail)
    TextView txtTimeMakananDetail;
    @BindView(R.id.txt_name_user)
    TextView txtNameUser;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_desc_makanan)
    WebView txtDescMakanan;
    @BindView(R.id.sv_detail)
    NestedScrollView svDetail;

    private DetailMakananPresenter mDetailMakananPresenter = new DetailMakananPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_makanan);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        String idMakanan = getIntent().getStringExtra(Constants.KEY_EXTRA_ID_MAKANAN);
        mDetailMakananPresenter.getDetailMakanan(idMakanan);
    }

    @Override
    public void showProgress() {
        rvProgress.setVisibility(View.VISIBLE);
        svDetail.setVisibility(View.GONE);
    }


    @Override
    public void hideProgress() {
        rvProgress.setVisibility(View.GONE);
        svDetail.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void showDetailMakanan(MakananData makananData) {

//        getSupportActionBar().setTitle(makananData.getNamaMakanan());

        RequestOptions options = new RequestOptions().error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_broken_image);
        Glide.with(this).load(makananData.getUrlMakanan()).apply(options).into(imgMakananDetail);

        txtNameMakananDetail.setText(makananData.getNamaMakanan());
        String name = "Oleh : " + makananData.getNamaUser();
        txtNameUser.setText(name);
        txtTimeMakananDetail.setText(newDate(makananData.getInsertTime()));

        // set isi berita sebagai html ke webview
        txtDescMakanan.getSettings().setJavaScriptEnabled(true);
        txtDescMakanan.loadData(makananData.getDescMakanan(), "text/html; charset=utf-8", "UTF-8");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            }
        }
        return true;
    }

    @Override
    public void showFailureMessage(String msg) {
        svDetail.setVisibility(View.GONE);
        rvProgress.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.GONE);
        txtInfo.setText(msg);
    }

    public String newDate(String insertTime) {
        Date date2 = null;
        String newDate = insertTime;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date2 = sdf.parse(insertTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date2 != null) {
            newDate = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(date2);
        }
        return newDate;
    }
}
