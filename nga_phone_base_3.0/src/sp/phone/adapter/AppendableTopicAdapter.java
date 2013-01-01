package sp.phone.adapter;

import java.util.ArrayList;
import java.util.List;

import sp.phone.bean.ThreadPageInfo;
import sp.phone.bean.TopicListInfo;
import android.content.Context;

public class AppendableTopicAdapter extends TopicListAdapter {
	final private List<TopicListInfo> infoList;
	public AppendableTopicAdapter(Context context) {
		super(context);
		infoList = new ArrayList<TopicListInfo>();

	}

	@Override
	protected ThreadPageInfo getEntry(int position) {
		for(int i=0; i< infoList.size(); i++){
			if(position < infoList.get(i).get__T__ROWS()){
				return infoList.get(i).getArticleEntryList().get(position);
			}
			position -= infoList.get(i).get__T__ROWS();
		}
		return null;
	}

	@Override
	public void jsonfinishLoad(TopicListInfo result) {
		infoList.add(result);
		count += result.get__T__ROWS();
		if(count != result.get__T__ROWS())
		{
			this.notifyDataSetChanged();
			
			//Toast.makeText(context, "finish load page:" + infoList.size(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void clear(){
		count = 0;
		infoList.clear();
		setSelected(-1);
	}
	
	public int getNextPage(){
		return infoList.size() + 1;
	}

}
