package ai.love.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Objects;

import ai.love.R;
import ai.love.adapter.ItemAdapter;
import ai.love.controllor.NoteEnityControllor;
import ai.love.model.NoteEnity;
import ai.love.utils.ClickListenerUtil;
import cn.refactor.lib.colordialog.PromptDialog;

import static ai.love.adapter.ItemAdapter.SPAN_COUNT_ONE;
import static ai.love.adapter.ItemAdapter.SPAN_COUNT_THREE;

public class NoteActivity extends AppCompatActivity {

    private ItemAdapter itemAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<NoteEnity> items;
    private NoteEnityControllor controllor;
    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        refreshLayout = findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        controllor = new NoteEnityControllor(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initToolBar();
        initData();
        initEditTtb();
        initRefresh();

    }

    private void initRefresh() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
                refreshlayout.finishRefresh();//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    protected void initData() {
        items = controllor.searchAll();
        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT_ONE);
        itemAdapter = new ItemAdapter(this, items, gridLayoutManager);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        initItemClickListener();
    }

    private void initEditTtb() {
        FloatingActionButton edit_btn = findViewById(R.id.edit_btn);

       edit_btn.setOnClickListener(new ClickListenerUtil() {
           @Override
           public void onMultiClick(View v) {
                startActivity(new Intent(NoteActivity.this, EditingActivity.class));
           }
       });
    }

    private void initItemClickListener() {
        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Long id, int position) {
                Intent intent = new Intent(NoteActivity.this,EditingActivity.class);
                intent.putExtra("Id",id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(final Long id, final int position) {
                final PromptDialog promptDialog = new PromptDialog(NoteActivity.this)
                        .setDialogType(PromptDialog.DIALOG_TYPE_WARNING)
                        .setAnimationEnable(true)
                        .setTitleText("确定删除吗")
                        .setContentText("id:"+id)
                        .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog dialog) {
                                controllor.delete(id);
                                itemAdapter.deleteItemByPosition(position);
                                dialog.dismiss();
                            }
                        });
                promptDialog.setCancelable(false);
                promptDialog.show();
            }
        });
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.RED);
        toolbar.setTitleTextColor(Color.BLACK);
    }

    @Override
    protected void onResume() {
        initData();
        itemAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meun_note_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_switch_layout:
                switchLayout();
                switchIcon(item);
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchLayout() {
        if (gridLayoutManager.getSpanCount() == SPAN_COUNT_ONE) {
            gridLayoutManager.setSpanCount(SPAN_COUNT_THREE);
        } else {
            gridLayoutManager.setSpanCount(SPAN_COUNT_ONE);
        }
        itemAdapter.notifyItemRangeChanged(0, itemAdapter.getItemCount());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void switchIcon(MenuItem item) {
        if (gridLayoutManager.getSpanCount() == SPAN_COUNT_THREE) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_span_3));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_span_1));
        }
    }
}