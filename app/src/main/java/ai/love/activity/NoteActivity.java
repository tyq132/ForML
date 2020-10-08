package ai.love.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import ai.love.R;
import ai.love.adapter.ItemAdapter;
import ai.love.controllor.NoteEnityControllor;
import ai.love.model.NoteEnity;
import ai.love.utils.ClickListenerUtil;

import static ai.love.adapter.ItemAdapter.SPAN_COUNT_ONE;
import static ai.love.adapter.ItemAdapter.SPAN_COUNT_THREE;

public class NoteActivity extends AppCompatActivity {

    private ItemAdapter itemAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<NoteEnity> items;
    private NoteEnityControllor controllor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        controllor = new NoteEnityControllor(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initToolBar();
        initEditTtb();

    }

    @Override
    protected void onResume() {
        Log.e("onResume","启动");
        initItemsData();
        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT_ONE);
        itemAdapter = new ItemAdapter(this, items, gridLayoutManager);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        initItemClickListener();
        super.onResume();
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
        });
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.RED);
        toolbar.setTitleTextColor(Color.BLACK);
    }

    private void initItemsData() {
        items = controllor.searchAll();
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