package ai.love.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.love.R;
import ai.love.controllor.NoteEnityControllor;
import ai.love.model.NoteEnity;
import ai.love.utils.TimeFarmatUtil;

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
    private LinkedList<NoteEnity> mDataList;
    private Set<Integer> mRemoveList;
    private List<String> mIndexList;
    private int startIndex;
    private int endIndex;
    private Matcher matcher;
    private GridLayoutManager mLayoutManager;
    private OnItemClickListener mOnItemClickListener;//声明接口
    private List<NoteEnity> mFilterList;

    public ItemAdapter(Context context, LinkedList<NoteEnity> items, GridLayoutManager layoutManager) {
        mContext = context;
        mDataList = items;
        mRemoveList = new HashSet<>();
        mFilterList = new ArrayList<>();
        mIndexList = new LinkedList<>();
        mLayoutManager = layoutManager;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_BIG) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_big, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_small, parent, false);
        }
        return new ItemViewHolder(view, viewType, mOnItemClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        NoteEnity item;
        ForegroundColorSpan colorSpan;
        if(isSearching){
            if(mFilterList.size()>0){
                item = mFilterList.get(position);
                colorSpan = new ForegroundColorSpan(Color.parseColor("#29B6F6"));
            }else{
                return;
            }
        }else{
            item = mDataList.get(position);
            colorSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
            int type = getItemViewType(position);
            if (type == VIEW_TYPE_BIG && item.getTime() != null) {
                holder.info.setText(TimeFarmatUtil.dateToString(item.getTime(),"yyyy, MM-dd"));
            }
            setItemRemoveStatue(holder, position, type);
        }
        String title = item.getTitle();
        SpannableStringBuilder spannable = new SpannableStringBuilder(title);
        if(mIndexList.size()>0){
            String[] indexs = mIndexList.get(position).split("@");
            spannable.setSpan(colorSpan,Integer.parseInt(indexs[0]),Integer.parseInt(indexs[1]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.id.setText(item.getId().toString());
        holder.title.setText(spannable);
        String img_url = item.getImgResUrl() == null ? "NONE" : item.getImgResUrl();
        Picasso.get().load("file://" + img_url.trim()).into(holder.iv);

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
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String str = constraint.toString();
                mIndexList.clear();
                if(str.isEmpty()){
                    mFilterList.clear();
                }else{
                    List<NoteEnity> filterList = new ArrayList<>();
                    for(NoteEnity enity : mDataList){
                        if(enity.getTitle().contains(str)){
                            matcher = Pattern.compile(constraint.toString()).matcher(enity.getTitle());
                            if(matcher.find()){
                                startIndex = matcher.start();
                                endIndex = startIndex+constraint.toString().length();
                                String index = startIndex+"@"+endIndex;
                                mIndexList.add(index);
                            }
                            filterList.add(enity);
                        }else{
                            break;
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

    public void deleteItem(NoteEnityControllor controllor){
        List<NoteEnity> deleteList = new LinkedList<>();
        NoteEnity enity;
        for(int position: mRemoveList){
            enity = mDataList.get(position);
            notifyItemRemoved(position);
            notifyItemChanged(position);
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


    public void addItem(NoteEnityControllor controllor,long id){
        mDataList.add(0,controllor.searchById(id));
        notifyItemInserted(0);
        notifyItemRangeChanged(0,mDataList.size());
        //notifyDataSetChanged();
    }

    private void sortByTime(List<NoteEnity> newList, List<NoteEnity> oldList) {
        for(NoteEnity enity: oldList){
            newList.add(0,enity);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public Set<Integer> getRemoveList() {
        return mRemoveList;
    }

    public LinkedList<NoteEnity> getmDataList(){
        return mDataList;
    }

    public void refreshList(List<NoteEnity> searchAll) {
        mDataList.clear();
        mDataList.addAll(searchAll);
        notifyDataSetChanged();
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
