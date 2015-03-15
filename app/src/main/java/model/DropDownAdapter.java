package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import bingyan.net.demondict.R;

/**
 * Created by Demon on 2015/2/8.
 * 自定义adapter，实现
 * 1、Item之间有明显的实线分割
 * 2、getView方法的缓存优化
 */
public class DropDownAdapter extends ArrayAdapter<String> {
    private  int resourcedId;


    public DropDownAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context,textViewResourceId,objects);
        this.resourcedId = textViewResourceId;
    }


    @Override
    public View getView(final int position,View convertView,ViewGroup parent){
        String str = getItem(position);

        View view;
        ViewHolder viewHolder;

        if(null == convertView) {
            view = LayoutInflater.from(getContext()).inflate(resourcedId, null);
            viewHolder = new ViewHolder();
            viewHolder.divideLine = (ImageView) view.findViewById(R.id.divideLine);
            viewHolder.wordContent = (TextView) view.findViewById(R.id.wordContent);
            view.setTag(viewHolder);
        }

        else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.wordContent.setText(str);

        return view;
    }


    //用ViewHolder优化
    class ViewHolder{
        ImageView divideLine;
        TextView wordContent;
    }
}
