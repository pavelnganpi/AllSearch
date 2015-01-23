package com.paveynganpi.allsearch;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by paveynganpi on 1/22/15.
 */
public class ImagesGridAdapter extends ArrayAdapter {

    protected static final String TAG = ImagesGridAdapter.class.getSimpleName();
    protected Context mContext;
    protected ArrayList<String> mPicUrls;

    public ImagesGridAdapter(Context context, ArrayList<String> picUrls) {
        super(context, R.layout.images_view_grid_layout,picUrls);
        mContext = context;
        mPicUrls = picUrls;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.images_view_grid_layout,null);
            holder = new ViewHolder();
            holder.photoImageView = (ImageView)convertView.findViewById(R.id.googleImageView);
            convertView.setTag(holder);//makes us return to initial state of listview after viewing the content

        }
        else{
            holder =(ViewHolder)convertView.getTag();//gets the view holder that was already created
            //if tag is no set as above, error will result due to the fact we are trying to retrieve a tag
            //that is no longer available


        }

        //holder.authorImageView.setI(mTitle_authors.get(position).getAuthor());
        Picasso.with(mContext).
                load(mPicUrls.get(position))
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .resize(300, 300)
                .into(holder.photoImageView);
        //Log.d(TAG,"title is ....." + mTitle_authors.get(0).getTitle());


        return convertView;
    }


    public static class ViewHolder{

        ImageView photoImageView;

    }

}
