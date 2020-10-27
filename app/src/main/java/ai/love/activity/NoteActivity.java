package ai.love.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private LinkedList<NoteEnity> items;
    private NoteEnityControllor controllor;
    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private Toolbar toolbar;
    private EditText editText;
    private PromptDialog deleteDialog;
    private boolean isDeleting;
    private boolean isAllChoiced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        items = new LinkedList<>();
        refreshLayout = findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        controllor = NoteEnityControllor.getInstance(this);
        editText = findViewById(R.id.search_edittext);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initToolBar();
        initData();
        initSearchEdit();
        initDeleteDialog();
        initFloatingBtn();
        initRefresh();

    }

    protected void initData() {
        List<NoteEnity> list = controllor.searchAll();
        for (NoteEnity enity : list) {
            items.add(enity);
        }
        Log.e("初始化的数据", Arrays.toString(items.toArray()));
        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT_ONE);
        itemAdapter = new ItemAdapter(this, items, gridLayoutManager);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        initItemClickListener();
    }

    private void initSearchEdit() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void initRefresh() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                itemAdapter.refreshList(controllor.searchAll());
                Log.e("初始化的数据", Arrays.toString(controllor.searchAll().toArray()));
                refreshlayout.finishRefresh();//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore();//传入false表示加载失败
            }
        });
    }

    private void initFloatingBtn() {
        FloatingActionButton edit_btn = findViewById(R.id.add_btn);
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
                Log.e("短按",""+id+"||"+position);
                if(isDeleting) {
                    itemAdapter.addWillRemoveItem(position);
                    if (itemAdapter.getRemoveList().size() >= items.size()) {
                        isAllChoiced = true;
                    } else {
                        isAllChoiced = false;
                    }
                    invalidateOptionsMenu();
                }else{
                    Intent intent = new Intent(NoteActivity.this,EditingActivity.class);
                    intent.putExtra("Id",id);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(final Long id, final int position) {
                Log.e("长按",""+id+"||"+position);
               if(!isDeleting){
                   isDeleting = true;
               }
               itemAdapter.addWillRemoveItem(position);
               if(itemAdapter.getRemoveList().size()<items.size()){
                   isAllChoiced = false;
               }else{
                   isAllChoiced = true;
               }
               toolbar.setNavigationIcon(R.drawable.cancel);
               toolbar.setTag("cancel");
               invalidateOptionsMenu();
            }
        });
    }

    private void updateDataList() {
        items = itemAdapter.getmDataList();
    }

    private void initDeleteDialog() {
        deleteDialog = new PromptDialog(NoteActivity.this)
                .setDialogType(PromptDialog.DIALOG_TYPE_WARNING)
                .setAnimationEnable(true)
                .setTitleText("确定删除吗")
                .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        toolbar.setTag("default");
                        isDeleting = false;
                        itemAdapter.deleteItem(controllor);
                        updateDataList();
                        invalidateOptionsMenu();
                        dialog.dismiss();
                    }
                });
        deleteDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                toolbar.setTag("cancel");
                isDeleting = true;
                invalidateOptionsMenu();
            }
        });
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_btn);
        toolbar.setBackgroundColor(Color.RED);
        toolbar.setTitleTextColor(Color.BLACK);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Long id  = NoteEnityControllor.tempId;
        if(NoteEnityControllor.tempId!=-1) {
            itemAdapter.addItem(controllor, id);
            updateDataList();
            //items.add(controllor.searchById(id));
        }
        NoteEnityControllor.tempId = -1L;
        recyclerView.scrollToPosition(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meun_note_layout, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(isDeleting){
            menu.findItem(R.id.menu_delete_layout).setVisible(true);
            menu.findItem(R.id.menu_choiceall_layout).setVisible(true);
            menu.findItem(R.id.menu_search_layout).setVisible(false);
            menu.findItem(R.id.menu_switch_layout).setVisible(false);
        }else{
            toolbar.setNavigationIcon(R.drawable.back_btn);
            toolbar.setTag("default");
            menu.findItem(R.id.menu_search_layout).setVisible(true);
            menu.findItem(R.id.menu_delete_layout).setVisible(false);
            menu.findItem(R.id.menu_choiceall_layout).setVisible(false);
            menu.findItem(R.id.menu_switch_layout).setVisible(true);
        }
        if(isAllChoiced){
            menu.findItem(R.id.menu_choiceall_layout).setIcon(R.drawable.picture_icon_checked);
        }else{
            menu.findItem(R.id.menu_choiceall_layout).setIcon(R.drawable.picture_icon_check);
        }

        if(editText.getVisibility() == View.VISIBLE){
            toolbar.setNavigationIcon(R.drawable.cancel);
            toolbar.setTag("search_cancel");
            menu.findItem(R.id.menu_search_layout).setVisible(false);
            menu.findItem(R.id.menu_delete_layout).setVisible(false);
            menu.findItem(R.id.menu_choiceall_layout).setVisible(false);
            menu.findItem(R.id.menu_switch_layout).setVisible(false);
        }
        Log.e("Switce",""+gridLayoutManager.getSpanCount());
        if (gridLayoutManager.getSpanCount() == SPAN_COUNT_THREE) {
            menu.findItem(R.id.menu_switch_layout).setIcon(R.drawable.ic_span_1);
        } else {
            menu.findItem(R.id.menu_switch_layout).setIcon(R.drawable.ic_span_3);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                if(toolbar.getTag().equals("cancel")){
                    isDeleting = false;
                    toolbar.setTag("default");
                    toolbar.setNavigationIcon(R.drawable.back_btn);
                    itemAdapter.refrashRemoveList();
                }else if(toolbar.getTag().equals("search_cancel")){
                    editText.setVisibility(View.GONE);
                    toolbar.setNavigationIcon(R.drawable.back_btn);
                    itemAdapter.isSearching = false;
                    itemAdapter.notifyDataSetChanged();
                }else{
                    onBackPressed();
                }
                break;
            case R.id.menu_switch_layout:
                switchLayout();
                break;
            case R.id.menu_search_layout:
                editText.setVisibility(View.VISIBLE);
                itemAdapter.isSearching = true;
                itemAdapter.notifyDataSetChanged();
                break;
            case R.id.menu_delete_layout:
                Set<Integer> set = itemAdapter.getRemoveList();
                if(set.size()>0 && !set.contains(-2)){
                    deleteDialog.setContentText(set.toString()).show();
                }else{
                    Toast.makeText(NoteActivity.this,"未选中任意项",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_choiceall_layout:
                if(isAllChoiced){
                    isAllChoiced = false;
                    itemAdapter.addWillRemoveItem(-2);
                }else{
                    isAllChoiced = true;
                    itemAdapter.addWillRemoveItem(-1);
                }
                break;
            default:
                break;
        }
        invalidateOptionsMenu();
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
}