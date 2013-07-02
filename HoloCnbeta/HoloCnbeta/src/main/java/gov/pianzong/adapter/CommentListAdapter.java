package gov.pianzong.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import gov.pianzong.bean.CommentInfo;
import gov.pianzong.holocnbeta.R;

/**
 * Created by Administrator on 13-7-2.
 */
public class CommentListAdapter extends BaseAdapter {
    private  List<CommentInfo>list;

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
        if(view == null)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item,viewGroup,false);
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
        holder.votes.setText(viewGroup.getContext().getString(R.string.votes_format,info.getSupport(),info.getAgainst()));
        return view;
    }
    
    public void setList(List<CommentInfo> list){
        this.list = list;
    }

}
