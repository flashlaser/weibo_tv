package com.example.xinyu10.newir;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xinyu10 on 2015/8/26.
 */
public class TucaoInter extends Fragment {

    static SimpleAdapter adapter;
    static private List<Map<String, Object>> listData;
    private ListView listView;
    private View thisView;
    private Spinner spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.tucao_inter, container, false);
        listView = (ListView) thisView.findViewById(R.id.tucao_list);
        spinner = (Spinner) thisView.findViewById(R.id.show_selector);
        ArrayAdapter<String> show_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,new String[]{"爸爸去哪儿","中国好声音"});
        show_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(show_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TucaoImages.setImagesPool(((CheckedTextView) view).getText().toString());
                resetItems();
                setTopVideo();
                setTopImage();
                updateItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listData = new ArrayList<Map<String, Object>>();
        adapter = new SimpleAdapter(getActivity(), listData,
                R.layout.tucao_item, new String[]{"tucao_item","video_item"}, new int[]{R.id.tucao_item});
        adapter.setViewBinder(binder);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listData.get(position).containsKey("video_item")){
                    String uri_s;
                    switch (getTucaoTV()){
                        case "爸爸去哪儿":
                            uri_s = "http://gslb.miaopai.com/stream/ktHk3SUhg-RLW7ez09WD1w__.mp4?yx=&refer=weibo_app";
                            break;
                        case "中国好声音":
                            uri_s = "http://gslb.miaopai.com/stream/XfUq0jk6E73mGVQ0bBEltw__.mp4?yx=&refer=weibo_app";
                            break;
                        default:
                            uri_s = "";
                            break;
                    }
                    Uri uri = Uri.parse(uri_s);
                    //Uri uri = Uri.parse("http://video.weibo.com/show?fid=1034:95bc31616eb91bf5a698b7e41c3a3165");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "video/mp4");
                    startActivity(intent);
                }else if(listData.get(position).containsKey("gif_item")){
                    Uri uri = Uri.parse("http://10.235.24.45:8086/webvr/static/video/baba.mp4");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "video/mp4");
                    startActivity(intent);
                }else if(listData.get(position).containsKey("top_image")){

                }else{
                    ((MainActivity) getActivity()).reviewTucao((int) listData.get(position).get("tucao_item"));
                    //Toast.makeText(getActivity(),String.valueOf(TucaoImages.getAllOnShow((int) listData.get(position).get("tucao_item")).size()), Toast.LENGTH_SHORT).show();
                }
            }
        });

        thisView.findViewById(R.id.tucao_change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).callTVSender(FeedToTV.getTVNum(spinner.getSelectedItem().toString()));
            }
        });

        thisView.findViewById(R.id.tucao_yuyue_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2016, 9, 28, 20, 30);
                Calendar endTime = Calendar.getInstance();
                endTime.set(2016, 9, 28, 21, 30);
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, spinner.getSelectedItem().toString())
                        .putExtra(CalendarContract.Events.DESCRIPTION, spinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        //thisView.findViewById(R.id.tucao_video_view).setBackgroundResource(R.drawable.babalogo);
        //thisView.findViewById(R.id.tucao_video_view).setBackgroundResource(R.drawable.babalogo);

        //thisView.findViewById(R.id.tucao_video_view).setBackground(getResources().getDrawable(R.drawable.babalogo,getApplicationContext().getTheme()));
//        thisView.findViewById(R.id.tucao_video_view).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Uri uri = Uri.parse("http://gslb.miaopai.com/stream/ktHk3SUhg-RLW7ez09WD1w__.mp4?yx=&refer=weibo_app");
//                Uri uri = Uri.parse("http://120.52.41.130/221/53/7/bcloud/100774/ver_00_22-1055486732-avc-200358-aac-32023-233080-7028417-92bdf876cb492409e4826c3386293bd7-1468593070381.mp4?crypt=82aa7f2e208&b=241&nlh=4096&nlt=60&bf=69&p2p=1&video_type=mp4&termid=2&tss=no&platid=2&splatid=206&its=0&qos=5&fcheck=0&amltag=700&mltag=700&proxy=2031258465,2008840771,2008840770&uid=1032296580.rp&keyitem=GOw_33YJAAbXYE-cnQwpfLlv_b2zAkYctFVqe5bsXQpaGNn3T1-vhw..&ntm=1476795000&nkey=dbcc60e9636e14ce26d7214f2a77bbbc&nkey2=8614786b12c64f90d888953ed6efa534&geo=CN-1-9-2&mmsid=60337134&tm=1476776653865&key=cab9daba43d15868221ac4c104fe1d20&payff=0&cuid=100774&vtype=21&dur=233&p1=3&p2=31&p3=310&cf=h5-android&p=101&playid=0&tag=mobile&sign=bcloud_100774&pay=0&ostype=android&hwtype=un&errc=0&gn=1158&vrtmcd=102&buss=700&cips=61.135.152.132");
//                //调用系统自带的播放器
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(uri, "video/mp4");
//                startActivity(intent);
//            }
//        });
        return thisView;
    }

    public String getTucaoTV(){
        return spinner.getSelectedItem().toString();
    }

    SimpleAdapter.ViewBinder binder = new SimpleAdapter.ViewBinder() {

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if (data != null) {
                view.setBackgroundResource((int) data);
                ((ViewGroup)view).removeAllViews();
                if(TucaoImages.isOnShow((int) data)){
                    List<Map<String,Object>> tags = TucaoImages.getLatestOnShow((int) data);
                    for(int i = 0;i<tags.size();i++){
                        //((RelativeLayout.LayoutParams)((PictureTagView)map.get("view")).getLayoutParams()).leftMargin;
                        PictureTagView old_view = (PictureTagView) tags.get(i).get("view");
                        RelativeLayout.LayoutParams new_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        new_params.leftMargin = ((RelativeLayout.LayoutParams)old_view.getLayoutParams()).leftMargin;
                        new_params.topMargin = ((RelativeLayout.LayoutParams)old_view.getLayoutParams()).topMargin;
                        PictureTagView new_view = new PictureTagView(getActivity(), old_view.getDirection());
                        new_view.setShow(old_view.getText());
                        ((ViewGroup)view).addView(new_view, new_params);
                    }
                }

            }
            return true;
        }
    };

    public void addItem() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("tucao_item", TucaoImages.resetTucaoImage());
        listData.add(0, map);
        adapter.notifyDataSetChanged();
    }

    public void updateItems(){
        Set items = TucaoImages.getOnShowImages();
        for (Object image: items) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("tucao_item", (int)image);
            listData.add(map);
        }
    }

    public void resetItems(){
        listData.clear();
        adapter.notifyDataSetChanged();
    }

    public void setTopVideo(){
        int top_image;
        switch (getTucaoTV()){
            case "爸爸去哪儿":
                top_image = R.drawable.baba_video;
                break;
            case "中国好声音":
                top_image = R.drawable.voice_video;
                break;
            default:
                top_image = R.drawable.baba_video;
                break;
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("tucao_item",top_image);
        map.put("video_item",true);
        listData.add(0, map);
        adapter.notifyDataSetChanged();
    }

    public void setTopImage(){
        if(getTucaoTV() == "爸爸去哪儿"){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("tucao_item",R.drawable.vshow);
            map.put("top_image",true);
            listData.add(0, map);
            adapter.notifyDataSetChanged();
        }
    }

    static public void setTailGif(){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("tucao_item",R.drawable.gif_video);
        map.put("gif_item",true);
        listData.add(0, map);
        adapter.notifyDataSetChanged();
    }
}