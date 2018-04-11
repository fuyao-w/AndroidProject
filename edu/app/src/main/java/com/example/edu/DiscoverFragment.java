package com.example.edu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import com.example.edu.animation.AniUtils;
import com.example.edu.animation.CustomProgressDialog;
import com.example.edu.service.UserService;
import com.example.edu.utils.BaseUiListener;
import com.example.edu.utils.DialogViewHolder;
import com.example.edu.utils.FileUtils;
import com.example.edu.utils.Search_Result;

import com.example.edu.utils.ToastUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DiscoverFragment extends Fragment {
    private View FragmentView;
    private PopupWindow popWindow;
    int po, lvindext;
    private int itemPosition;
    private static int ii;
    private int height, tyear, tmonth, tdayOfMonth;

    private Toolbar toolbar1;
    private SearchView searchview;
    private ListView queryresult;
    private static ListViewAdapter adapter;
    private static Integer currentPage = 1;
    private final Integer linesize = 10;
    private static Integer allRecorders = 0;
    private static Integer pagesize = 1;
    private static Integer lastItem = 0;
    private static String querys = null;
    private LinearLayout loadlayout;
    private List<Search_Result> addlist;
    private TextView loadinfo;
    private Tencent mTencent;
    private static Search_Result data1;
    private static int flag = 0;
    private TextView detailedname, detailednumber, detailedclass, detailedtimes, tx;
    private static View myview;
    public static String mAppid = "1106184205";
    private static String Home = "http://192.168.191.1:8081/edupatrol/SearchServlet";
    private static String Home1 = "http://192.168.191.1:8081/edupatrol/SearchServlet2";
    private static List<Search_Result> list;
    private String bitName;
    private static View MyView, MyView1;
    private SwipeToLoadLayout swipeToLoadLayout;
    protected  RelativeLayout relative;
    private DiscoverReceiver discoverReceiver;
    protected static DiscoverFragment discoverFragment;

    private boolean Flag = true;
    String picname = "//" + FileUtils.getPhotoFileName();
    String imageurl = Environment.getExternalStorageDirectory().toString() + "/MotieReader" + picname;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (FragmentView == null)
            FragmentView = inflater.inflate(R.layout.activity_search, container, false);
        discoverFragment = this;
        mTencent = Tencent.createInstance("1106184205",this.getContext());
        relative = (RelativeLayout) FragmentView.findViewById(R.id.relative);

        toolbar1 = (Toolbar) FragmentView.findViewById(R.id.toolbar1);
        relative = (RelativeLayout) FragmentView.findViewById(R.id.relative);
        searchview = (SearchView) FragmentView.findViewById(R.id.searchview);
        queryresult = (ListView) FragmentView.findViewById(R.id.swipe_target);
        swipeToLoadLayout = (SwipeToLoadLayout) FragmentView.findViewById(R.id.swipeToLoadLayout);
        if (Flag) {
            toolbar1.inflateMenu(R.menu.dis_item);
            Flag = false;
        }
        registerReceiver();
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar1);

        setHasOptionsMenu(true);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                String s = searchview.getQuery().toString();

                if (!s.equals(""))
                    getdata(querys);

                swipeToLoadLayout.setRefreshing(false);//收头
            }
        });
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                String s = searchview.getQuery().toString();
                if (!s.equals("") && currentPage <= pagesize) {
                    DiscoverFragment.this.currentPage++;
                    appendData(querys);
                }
                swipeToLoadLayout.setLoadingMore(false);
            }
        });


        SpannableString spanText = new SpannableString("学号或姓名");
        spanText.setSpan(new ForegroundColorSpan(Color.WHITE), 0,
                spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        searchview.setQueryHint(spanText);
        searchview.setOnQueryTextListener(queryTextListener);
        queryresult.setOnScrollListener(scrollListener);


        DiscoverFragment.this.queryresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, int position, long id) {


                Search_Result data = (Search_Result) adapter.getItem(position);

                LayoutInflater factory = LayoutInflater.from(getContext());
                MyView = factory.inflate(R.layout.search_detailed, null);

                detailedname = (TextView) MyView.findViewById(R.id.detailedname);
                detailednumber = (TextView) MyView.findViewById(R.id.detailednumber);
                detailedclass = (TextView) MyView.findViewById(R.id.detailedclass);
                detailedtimes = (TextView) MyView.findViewById(R.id.detailedtime);
                tx = (TextView) MyView.findViewById(R.id.detailed);

                detailedname.setText(data.getStu_name());
                detailednumber.setText(data.getStu_number());
                detailedclass.setText(data.getClass_name());
                detailedtimes.setText(data.getMes_time());
                tx.setText(data.getMessage());


                Dialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle(data.getStu_name())
                        .setView(MyView)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        }).setNeutralButton("分享", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Bitmap b;
                                layoutView(MyView, 900, 900);

//
                                int viewWidth = MyView.getMeasuredWidth();
                                int viewHeight = MyView.getMeasuredHeight();
                                if (viewWidth > 0 && viewHeight > 0) {
                                    b = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
                                    Canvas cvs = new Canvas(b);
                                    MyView.draw(cvs);
                                    saveBitmap(b);
                                }
                                onClickShareToQQ();


                            }
                        }).create();

                dialog.show();


            }

        });


        if (MainActivity.roleType == 3)
            DiscoverFragment.this.queryresult.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                    final Search_Result data = (Search_Result) adapter.getItem(position);
                    itemPosition = position;
                    Log.e("位置", String.valueOf(itemPosition));


                    Dialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle(data.getStu_name())

                            .setPositiveButton("取消", null)
                            .setNeutralButton("修改", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    showPopWindow(getContext(), view, data);

                                }
                            }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Thread t1 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(getContext(), UserService.class);
                                            i.putExtra("action", "delete");

                                            i.putExtra("data", data.getRecord_id());

                                            getContext().startService(i);

                                        }
                                    });
                                    t1.start();

                                }
                            }).create();

                    dialog.show();

                    return true;
                }
            });

        toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.disclear) {
                    if (list != null) {
                        list.clear();
                        adapter.refresh(list);
                        queryresult.setAdapter(adapter);
                        queryresult.invalidate();
                        swipeToLoadLayout.setVisibility(View.INVISIBLE);
                    }

                    return true;
                }
                return true;
            }
        });

        return FragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.dis_item, menu);


    }

    private void layoutView(View v, int width, int height) {

        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    private void showPopWindow(Context context, View parent, final Search_Result data) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopWindow = inflater.inflate(R.layout.patroller_chan_or_dele, null, false);
        //宽300 高300

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        float density = dm.density;


        popWindow = new PopupWindow(vPopWindow, width, dip2px(getContext(), 500), true);
        Button sub = (Button) vPopWindow.findViewById(R.id.sub);
        Button databutton = (Button) vPopWindow.findViewById(R.id.databutton);
        detailedname = (TextView) vPopWindow.findViewById(R.id.detailedname);
        detailednumber = (TextView) vPopWindow.findViewById(R.id.detailednumber);
        detailedclass = (TextView) vPopWindow.findViewById(R.id.detailedclass);
        detailedtimes = (TextView) vPopWindow.findViewById(R.id.detailedtime);
        tx = (TextView) vPopWindow.findViewById(R.id.detailed);
        detailedname.setText(data.getStu_name());
        detailednumber.setText(data.getStu_number());
        detailedclass.setText(data.getClass_name());
        detailedtimes.setText(data.getMes_time());
        tx.setText(data.getMessage());
        databutton.setOnClickListener(dataSlect);
//         BindButton.setOnClickListener(poplistener);
//         binreturn.setOnClickListener(poplistener);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data1 = new Search_Result();
                data1.setStu_name(detailedname.getText().toString());
                data1.setStu_number(detailednumber.getText().toString());
                data1.setClass_name(detailedclass.getText().toString());
                data1.setMes_time(detailedtimes.getText().toString());
                data1.setMessage(tx.getText().toString());
                data1.setRecord_id(data.getRecord_id());

                Intent i = new Intent(getContext(), UserService.class);
                i.putExtra("action", "change");
                Bundle b = new Bundle();
                b.putParcelable("data", data1);
                i.putExtra("data", b);

                getContext().startService(i);
            }
        });
        popWindow.showAtLocation(parent, Gravity.TOP, dip2px(getContext(), 0),
                dip2px(getContext(), 0));

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private AnimationSet outAnimation() {
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(500);
        ScaleAnimation scale = new ScaleAnimation(
                1, 1,
                0.0f, 1,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );
        scale.setDuration(500);
        set.addAnimation(alpha);
        set.addAnimation(scale);
        return set;
    }

    public void DataUpdata() {

        Pattern pat = Pattern.compile("[0-9]+");
        Matcher mat = pat.matcher(detailedtimes.getText());

        while (mat.find()) {

            if ((flag++) == 4)
                break;
            else {
                switch (flag) {
                    case 1:
                        tyear = Integer.parseInt(mat.group());
                        break;
                    case 2:
                        tmonth = (Integer.parseInt(mat.group())) - 1;
                        break;
                    case 3:
                        tdayOfMonth = Integer.parseInt(mat.group());
                        break;
                    default:
                        break;


                }
            }
            System.out.println(tyear + " " + tmonth + " " + tdayOfMonth);

        }
        flag = 0;

    }


    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {


            if (list != null) {
                list.clear();
            }

            DiscoverFragment.querys = query;
            getdata(query);

            RequestParams params = new RequestParams(Home1);
            params.addQueryStringParameter("key", "tjpu");
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    //解析result
                    JSONObject jsonObject = JSON.parseObject(result);
                    allRecorders = jsonObject.getInteger("allrecords");
                    pagesize = (allRecorders + currentPage - 1) / linesize;
                    if (allRecorders - pagesize * linesize < linesize && allRecorders - pagesize * linesize > 0)
                        pagesize++;


                }

                //请求异常后的回调方法
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                }

                //主动调用取消请求的回调方法
                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                }
            });


            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }


    };

    private void getdata(String query) {
        currentPage = 1;
        allRecorders = 0;
        pagesize = 1;
        lastItem = 0;

        list = new ArrayList<Search_Result>();
        RequestParams params = new RequestParams(Home);

        params.addQueryStringParameter("currentPage", Integer.toString(currentPage));
        params.addQueryStringParameter("linesize", Integer.toString(linesize));
        params.addQueryStringParameter("searchMessage", query);

        params.setCacheMaxAge(5000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                swipeToLoadLayout.setVisibility(View.VISIBLE);
                queryresult.setAnimation(outAnimation());
                JSONObject jsonObject = JSON.parseObject(result);


                JSONArray jsonArray = jsonObject.getJSONArray("result");
                if (jsonArray.isEmpty()) {
                    Toast.makeText(getContext(), "当前没有记录信息", Toast.LENGTH_SHORT).show();
                    return;
                }


                for (Object object : jsonArray) {
                    JSONObject object1 = (JSONObject) object;

                    Search_Result search_result = new Search_Result();
                    search_result.setStu_name(object1.get("stu_name").toString());

                    search_result.setStu_number(object1.get("stu_number").toString());
                    search_result.setMessage(object1.get("message").toString());
                    search_result.setClass_name(object1.get("class_name").toString());
                    search_result.setMes_time(object1.get("mes_time").toString());
                    search_result.setRecord_id(object1.get("record_id").toString());
                    search_result.setStu_portrait(object1.get("stu_portrait").toString());
                    list.add(search_result);
                }


                if (adapter == null) {
                    adapter = new ListViewAdapter(getContext(), list);

                }
                queryresult.setAdapter(adapter);
                adapter.refresh(list);


            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getContext(), "获取信息失败", Toast.LENGTH_SHORT).show();
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });

        searchview.clearFocus();


    }


    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            DiscoverFragment.this.lastItem = firstVisibleItem + visibleItemCount - 1;

        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    DiscoverFragment.this.lastItem == DiscoverFragment.this.adapter.getCount() &&
                    DiscoverFragment.this.currentPage < DiscoverFragment.this.pagesize) {

                DiscoverFragment.this.queryresult.setSelection(DiscoverFragment.this.lastItem);

            }

            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    if (MainActivity.navigation.getVisibility() == View.VISIBLE) {

                    } else {

                    }

                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    lvindext = view.getLastVisiblePosition();


                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    int scrolled = view.getLastVisiblePosition();
                    if (scrolled > lvindext) {
                        if (MainActivity.navigation.getVisibility() == View.VISIBLE) {
                            MainActivity.navigation.startAnimation(AniUtils.xAnimation());
                        }
                        MainActivity.navigation.setVisibility(View.GONE);
                    } else if (scrolled < lvindext) {
                        if (MainActivity.navigation.getVisibility() == View.GONE) {
                            MainActivity.navigation.startAnimation(AniUtils.cAnimation());
                        }
                        MainActivity.navigation.setVisibility(View.VISIBLE);
                    }

            }


        }
    };


    private void appendData(String query) {
        addlist = new ArrayList<Search_Result>();
        RequestParams params = new RequestParams(Home);


        params.addQueryStringParameter("currentPage", Integer.toString(currentPage));
        params.addQueryStringParameter("linesize", Integer.toString(linesize));

        params.addQueryStringParameter("searchMessage", query);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result


                JSONObject jsonObject = JSON.parseObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (Object object : jsonArray) {
                    JSONObject object1 = (JSONObject) object;
                    Search_Result search_result = new Search_Result();
                    search_result.setStu_name(object1.get("stu_name").toString());

                    search_result.setStu_number(object1.get("stu_number").toString());
                    search_result.setMessage(object1.get("message").toString());
                    search_result.setClass_name(object1.get("class_name").toString());
                    search_result.setMes_time(object1.get("mes_time").toString());
                    search_result.setRecord_id(object1.get("record_id").toString());
                    search_result.setStu_portrait(object1.get("stu_portrait").toString());
                    addlist.add(search_result);
                }
                list.addAll(addlist);

                adapter.refresh(list);


            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });


    }


    private void onClickShareToQQ() {
        Bundle shareParams = new Bundle();
        shareParams.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        shareParams.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,
                imageurl);
        shareParams.putInt(QQShare.SHARE_TO_QQ_EXT_INT,
                QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        doShareToQQ(shareParams);
    }

    private void doShareToQQ(Bundle params) {
        mTencent.shareToQQ(getActivity(), params, new BaseUiListener() {
            protected void doComplete(org.json.JSONObject values) {
                Toast.makeText(getContext(), "成功",
                        Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(UiError e) {
            }

            @Override
            public void onCancel() {
            }
        });
    }

    public void saveBitmap(Bitmap bm) {


        File d = new File(Environment.getExternalStorageDirectory().toString() + "/MotieReader");
        if (!d.isDirectory()) {
            d.delete();
            d.mkdirs();
        }
        if (!d.exists()) {
            d.mkdirs();
        }
        writeBitmap(d.getPath(), picname, bm);

    }

    public static void writeBitmap(String path, String name, Bitmap bitmap) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        File _file = new File(path + name);
        if (_file.exists()) {
            _file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(_file);
            if (name != null && !"".equals(name)) {
                int index = name.lastIndexOf(".");
                if (index != -1 && (index + 1) < name.length()) {
                    String extension = name.substring(index + 1).toLowerCase();
                    if ("png".equals(extension)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    } else if ("jpg".equals(extension)
                            || "jpeg".equals(extension)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    protected void registerReceiver() {
        discoverReceiver = new DiscoverReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.discover");
        getContext().registerReceiver(discoverReceiver, intentFilter);


    }


    View.OnClickListener dataSlect = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DataUpdata();
            Dialog ddialog = new DatePickerDialog(getContext(), new DatePickerDialog
                    .OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    detailedtimes.setText(year + "-" + (month + 1) + "-" + dayOfMonth);

                }
            }, tyear, tmonth, tdayOfMonth);
            ddialog.show();
        }
    };

    @Override
    public void onDestroyView() {

        if (null != FragmentView) {
            ((ViewGroup) FragmentView.getParent()).removeView(FragmentView);
        }
        getContext().unregisterReceiver(discoverReceiver);
        super.onDestroyView();
    }


    class DiscoverReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.discover")) {
                String action = intent.getStringExtra("code");
                if (action.equals("del200")) {


                    list.remove(itemPosition);
                    adapter.refresh(list);
                    queryresult.setAdapter(adapter);
                    queryresult.invalidate();
                } else if (action.equals("cha200")) {
                    list.get(itemPosition).setClass_name(data1.getClass_name());
                    list.get(itemPosition).setStu_name(data1.getStu_name());
                    list.get(itemPosition).setStu_number(data1.getStu_number());
                    list.get(itemPosition).setMes_time(data1.getMes_time());
                    list.get(itemPosition).setMessage(data1.getMessage());
                    adapter.refresh(list);

                    queryresult.setAdapter(adapter);
                    queryresult.invalidate();


                }

            }


        }
    }
}
