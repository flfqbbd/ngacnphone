package gov.pianzong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import gov.pianzong.bean.CommentInfo;
import gov.pianzong.holocnbeta.R;

/**
 * Created by Administrator on 13-7-2.
 */
public class CommentListAdapter extends BaseAdapter {
    private  List<CommentInfo>list;
    private Context context;

    public CommentListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return  list == null ? 0:list.size();

    }

    @Override
    public Object getItem(int i) {
       return  list == null ? null:list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return  list == null ?0:list.get(i).getTid();
    }
    private  static  class ViewHolder{
        public TextView comment;
        public TextView name;
        public TextView votes;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder  = null;
        if(context == null)
            context = viewGroup.getContext();
        if(view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.comment_item,viewGroup,false);
            holder = new ViewHolder();
            holder.comment = (TextView) view.findViewById(R.id.comment_text_view);
            holder.name = (TextView) view.findViewById(R.id.name_text_view);
            holder.votes = (TextView) view.findViewById(R.id.vote_text_view);

            view.setTag(holder);
        }
        else
        {

            holder = (ViewHolder) view.getTag();
        }
        CommentInfo info = list.get(i);
        holder.comment.setText(info.getComment());
        holder.name.setText(info.getName());
        String votes = context.getString(R.string.votes_support, info.getSupport())
                +  context.getString(R.string.votes_against, info.getAgainst());
        holder.votes.setText(votes);
        return view;
    }
    
    public void setList(List<CommentInfo> list){
        if(list.size() ==0){
            Toast.makeText(context,R.string.nocomment,Toast.LENGTH_SHORT).show();
        }
        this.list = list;
    }

}
