package me.firdaus1453.crudmakanan.ui.makananbycategory;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.firdaus1453.crudmakanan.R;
import me.firdaus1453.crudmakanan.adapter.MakananAdapter;
import me.firdaus1453.crudmakanan.model.makanan.MakananData;
import me.firdaus1453.crudmakanan.utils.Constants;

public class MakananByCategoryActivity extends AppCompatActivity implements MakananByCategoryContract.View {

    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.txt_info)
    TextView txtInfo;
    @BindView(R.id.rl_progress)
    RelativeLayout rlProgress;
    @BindView(R.id.rv_makanan)
    RecyclerView rvMakanan;
    @BindView(R.id.sr_makanan)
    SwipeRefreshLayout srMakanan;

    private MakananByCategoryPresenter mMakananByCategoryPresenter = new MakananByCategoryPresenter(this);
    private String idCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan_by_category);
        ButterKnife.bind(this);

        // Mengambil id category dari kiriman halaman sebelumnya
        idCategory = getIntent().getStringExtra(Constants.KEY_EXTRA_ID_CATEGORY);

        // Merequest makanan by id category
        mMakananByCategoryPresenter.getListFoodByCategory(idCategory);

        // Mensetting swiperefresh menghilangkan loading dan memanggil ulang data makanan by category
        srMakanan.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srMakanan.setRefreshing(false);
                mMakananByCategoryPresenter.getListFoodByCategory(idCategory);
            }
        });
    }

    @Override
    public void showProgress() {
        rlProgress.setVisibility(View.VISIBLE);
        srMakanan.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        rlProgress.setVisibility(View.GONE);
        rvMakanan.setVisibility(View.VISIBLE);
        srMakanan.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFoodByCategory(List<MakananData> foodNewsList) {
        rvMakanan.setLayoutManager(new LinearLayoutManager(this));
        rvMakanan.setAdapter(new MakananAdapter(MakananAdapter.TYPE_4, this, foodNewsList));
    }

    @Override
    public void showFailureMessage(String msg) {
        srMakanan.setVisibility(View.VISIBLE);
        rvMakanan.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        txtInfo.setText(msg);
    }
}
