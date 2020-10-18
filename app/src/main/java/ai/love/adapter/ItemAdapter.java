package ai.love.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ai.love.R;
import ai.love.model.NoteEnity;
import ai.love.utils.ClickListenerUtil;

/**
 * Created by gjz on 16/01/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{
    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_THREE = 3;

    private static final int VIEW_TYPE_SMALL = 1;
    private static final int VIEW_TYPE_BIG = 2;

    private Context mContext;
    private List<NoteEnity> mItems;
    private GridLayoutManager mLayoutManager;
    private OnItemClickListener mOnItemClickListener;//声明接口

    public ItemAdapter(Context context,List<NoteEnity> items, GridLayoutManager layoutManager) {
        mContext = context;
        mItems = items;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        NoteEnity item = mItems.get(position % 4);
        holder.id.setText(item.getId().toString());
        holder.title.setText(item.getTitle());
        Picasso.get().load("file://"+item.getImgResUrl().trim()).into(holder.iv);
        if (getItemViewType(position) == VIEW_TYPE_BIG && item.getTime()!= null) {
            holder.info.setText(item.getId()+":"+item.getTime().toString());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        ImageView iv;
        TextView title;
        TextView info;

        ItemViewHolder(View itemView, int viewType, final OnItemClickListener listener) {
            super(itemView);
            id = itemView.findViewById(R.id.note_id);
            if (viewType == VIEW_TYPE_BIG) {
                iv = (ImageView) itemView.findViewById(R.id.image_big);
                title = (TextView) itemView.findViewById(R.id.title_big);
                info = (TextView) itemView.findViewById(R.id.tv_info);
            } else {
                iv = (ImageView) itemView.findViewById(R.id.image_small);
                title = (TextView) itemView.findViewById(R.id.title_small);
            }
            itemView.setOnClickListener(new ClickListenerUtil() {
                @Override
                public void onMultiClick(View view) {
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
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Long id, int position);
    }
}
