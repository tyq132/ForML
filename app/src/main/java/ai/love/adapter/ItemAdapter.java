package ai.love.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ai.love.R;
import ai.love.controllor.NoteEnityControllor;
import ai.love.model.NoteEnity;

/**
 * Created by gjz on 16/01/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements Filterable {
    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_THREE = 3;

    private static final int VIEW_TYPE_SMALL = 1;
    private static final int VIEW_TYPE_BIG = 2;
    public boolean isSearching;

    private Context mContext;
    private List<NoteEnity> mDataList;
    private Set<Integer> mRemoveList;
    private GridLayoutManager mLayoutManager;
    private OnItemClickListener mOnItemClickListener;//声明接口
    private List<NoteEnity> mFilterList;

    public ItemAdapter(Context context, List<NoteEnity> items, GridLayoutManager layoutManager) {
        mContext = context;
        mDataList = items;
        mRemoveList = new HashSet<>();
        mFilterList = new ArrayList<>();
        mLayoutManager = layoutManager;
    }

    @Override
    public int getItemViewType(int position) {
        int spanCount = mLayoutManager.getSpanCount();
        if (spanCount == SPAN_COUNT_ONE) {
            return VIEW_TYPE_BIG;
        } else {
            return VIEW_TYPE_SMALL;
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_BIG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small, parent, false);
        }
        return new ItemViewHolder(view, viewType, mOnItemClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        NoteEnity item;
        if(isSearching){
            if(mFilterList.size()>0){
                item = mFilterList.get(position);
            }else{
                return;
            }
        }else{
            item = mDataList.get(position % 4);
            int type = getItemViewType(position);
            if (type == VIEW_TYPE_BIG && item.getTime() != null) {
                holder.info.setText(item.getId() + ":" + item.getTime().toString());
            }
            setItemRemoveStatue(holder, position, type);
        }
        holder.id.setText(item.getId().toString());
        holder.title.setText(item.getTitle());
        String img_url = item.getImgResUrl() == null ? "NONE" : item.getImgResUrl();
        Picasso.get().load("file://" + img_url.trim()).into(holder.iv);

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String str = constraint.toString();
                if(str.isEmpty()){
                    mFilterList.clear();
                }else{
                    List<NoteEnity> filterList = new ArrayList<>();
                    for(NoteEnity enity : mDataList){
                        if(enity.getTitle().contains(str)){
                            filterList.add(enity);
                        }
                    }
                    mFilterList = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilterList = (List<NoteEnity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return isSearching? mFilterList.size():mDataList.size();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setItemRemoveStatue(ItemViewHolder holder, int position, int type) {
        if (mRemoveList.contains(position) || mRemoveList.size() == mDataList.size()) {
           changeChoiceStatue(holder,type,true);
        }else if(mRemoveList.size() == 0){
            changeChoiceStatue(holder,type,false);
        }else{
            changeChoiceStatue(holder,type,false);
        }
    }

    private void changeChoiceStatue(ItemViewHolder holder, int type, boolean checked) {
        switch (type){
            case VIEW_TYPE_BIG:
                holder.check.setVisibility(checked?View.VISIBLE:View.GONE);
                break;
            case VIEW_TYPE_SMALL:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.iv.setForeground(checked?mContext.getDrawable(R.drawable.shape_rectangle_radius):null);
                }
                break;
            default:
                break;
        }
    }

    public void deleteItem(Set<Integer> list, NoteEnityControllor controllor){
        List<NoteEnity> deleteList = new ArrayList<>(list.size());
        for(int position: list){
            NoteEnity enity = mDataList.get(position);
            deleteList.add(enity);
            controllor.delete(enity.getId());
        }
        mDataList.removeAll(deleteList);
        refrashRemoveList();
    }

    public void refrashRemoveList() {
        mRemoveList.clear();
        notifyDataSetChanged();
    }

    public synchronized void addWillRemoveItem(int position){
        if(position == -1){
            mRemoveList.clear();
            for (int i = 0; i < mDataList.size(); i++) {
                mRemoveList.add(i);
            }
            notifyItemRangeChanged(0,mDataList.size());
            return;
        }else if(position == -2){
            mRemoveList.clear();
            notifyItemRangeChanged(0,mDataList.size());
            return;
        }
        if(!mRemoveList.add(position)){
            mRemoveList.remove(position);
        }
        notifyDataSetChanged();
    }

    public void addItem(NoteEnity enity){
        mDataList.add(0,enity);
        notifyItemInserted(0);
        notifyDataSetChanged();
    }

    public void refreshList(List<NoteEnity> list){
        mDataList.clear();
        sortByTime(mDataList,list);
        notifyDataSetChanged();
    }

    private void sortByTime(List<NoteEnity> newList, List<NoteEnity> oldList) {
        for(NoteEnity enity: oldList){
            newList.add(0,enity);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void deleteItemByPosition(int position) {
        mDataList.remove(position);
        notifyDataSetChanged();
    }

    public Set<Integer> getRemoveList() {
        return mRemoveList;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        ImageView iv;
        TextView title;
        TextView info;
        LinearLayout rootView;
        LinearLayout check;

        ItemViewHolder(View itemView, int viewType, final OnItemClickListener listener) {
            super(itemView);
            id = itemView.findViewById(R.id.note_id);
            if (viewType == VIEW_TYPE_BIG) {
                rootView = itemView.findViewById(R.id.rootView_big);
                iv = (ImageView) itemView.findViewById(R.id.image_big);
                title = (TextView) itemView.findViewById(R.id.title_big);
                info = (TextView) itemView.findViewById(R.id.tv_info);
                check = itemView.findViewById(R.id.check_view_big);
            } else {
                rootView = itemView.findViewById(R.id.rootView_small);
                iv = (ImageView) itemView.findViewById(R.id.image_small);
                title = (TextView) itemView.findViewById(R.id.title_small);
            }
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        //确保position值有效
                        if (position != RecyclerView.NO_POSITION) {
                            /*设置点击后获取的数据，此处应为id*/
                            listener.onItemClick(Long.parseLong(id.getText().toString()), position);
                        }
                    }
                }
            });
            rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(Long.parseLong(id.getText().toString()), getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Long id, int position);

        void onItemLongClick(Long id, int position);
    }
}
