package ai.love.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ai.love.R;
import ai.love.adapter.ItemAdapter;
import ai.love.model.Item;
import ai.love.utils.ClickListenerUtil;

import static ai.love.adapter.ItemAdapter.SPAN_COUNT_ONE;
import static ai.love.adapter.ItemAdapter.SPAN_COUNT_THREE;

public class NoteActivity extends AppCompatActivity {

    private static int[] imageResources = new int[]{
            R.drawable.bat,
            R.drawable.bear,
            R.drawable.bee,
            R.drawable.butterfly,
    };

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<Item> items;
    private Toolbar toolbar;
    private FloatingActionButton edit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initItemsData();
        initToolBar();
        initEditTtb();


        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT_ONE);
        itemAdapter = new ItemAdapter(items, gridLayoutManager);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        initItemClickListener();

    }

    private void initEditTtb() {
       edit_btn = findViewById(R.id.edit_btn);

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
            public void onItemClick(TextView view, int position) {
                String s = "点击了"+position+","+view.getText().toString();
                Toast.makeText(NoteActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.RED);
        toolbar.setTitleTextColor(Color.BLACK);
    }

    private void initItemsData() {
        items = new ArrayList<>(4);
        items.add(new Item(R.drawable.ic_plus, "Image 1", 20, 33));
        items.add(new Item(R.drawable.img2, "Image 2", 10, 54));
        items.add(new Item(R.drawable.img3, "Image 3", 27, 20));
        items.add(new Item(R.drawable.img4, "Image 4", 45, 67));
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

    private void switchIcon(MenuItem item) {
        if (gridLayoutManager.getSpanCount() == SPAN_COUNT_THREE) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_span_3));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_span_1));
        }
    }
}