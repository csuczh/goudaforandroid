package com.dg.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.AppContext;
import com.dg.app.AppManager;
import com.dg.app.R;
import com.dg.app.api.remote.DGApi;
import com.dg.app.bean.City;
import com.dg.app.bean.Date_Response;
import com.dg.app.bean.Province;
import com.dg.app.bean.Walk_Response;
import com.dg.app.ui.dialog.DatePickerDialog;
import com.dg.app.ui.dialog.LostCustomDialog;
import com.dg.app.util.AddDayAndCurrent;
import com.dg.app.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.message.PushAgent;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public class SendXQInvite extends AppCompatActivity {

    //失效的时间
    TextView lostDays;
    //图标
    ImageView lostImage;
    ImageView xqImage;
    private TextView myDog;

    //遛狗信息的地理位置信息
    TextView geoAdress;
    //百度地图选择位置的入口
    ImageView baiduMap;

    //位置信息
    String address;
    //省
    String province;
    //市
    String city;
    //经度
    String lng;
    //维度
    String lat;
    int cityid=-1;

    int day;
    Activity activity;
    private static final int REQUEST_CODE_MAP=0;
    private static final int REQUEST_CODE_NAME=1;
    private String dogName;
    private EditText et_write_status;
    public static int MAX_LEN = 50;
    private TextView tweet_text_record;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xq_send_invite);
        PushAgent.getInstance(this).onAppStart();
        AppManager.getAppManager().addActivity(this);
        ActionBar actionBar =this.getSupportActionBar();
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        LayoutInflater inflater = (LayoutInflater) this.getLayoutInflater();
        View titleView = inflater.inflate(R.layout.actionbar_title_invite, null);
        actionBar.setCustomView(titleView, lp);
        actionBar.setDisplayShowHomeEnabled(false);//去掉导航
        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);

        ImageView imageView=(ImageView)this.findViewById(R.id.send_image_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView sendInvite=(TextView)this.findViewById(R.id.send_invite);
        sendInvite.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              String message = et_write_status.getText().toString();
                                              String lost = lostDays.getText().toString();
                                              String name=myDog.getText().toString();
                                              String place = geoAdress.getText().toString();
                                              if (message.replaceAll(" ","") == "") {
                                                  Toast.makeText(activity, "请输入邀请内容！", Toast.LENGTH_LONG).show();
                                                  return;
                                              }
                                              if (lost == "") {
                                                  Toast.makeText(activity, "失效时间必须写！", Toast.LENGTH_LONG).show();
                                                  return;
                                              }
                                              if (place == "") {
                                                  Toast.makeText(activity, "遛狗地点必须写！", Toast.LENGTH_LONG).show();
                                                  return;
                                              }
                                              if(name=="")
                                              {
                                                  Toast.makeText(activity, "狗狗的种类必须写！", Toast.LENGTH_LONG).show();

                                              }
                                              int day = Integer.parseInt(lost);
                                              String user_id=  AppContext.getInstance().getProperty("dgUser.userid");
                                              int int_user_Id=Integer.parseInt(user_id);
                                              String content = message;
                                              String dog_variety=dogName;
                                              String deadline = AddDayAndCurrent.add(day);
                                              int city_id = cityid;
                                              String pos = lng + "," + lat;
                                              DGApi.sendXQ("android", int_user_Id, content, dog_variety, deadline, city_id, pos, new AsyncHttpResponseHandler() {
                                                  @Override
                                                  public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                      Toast.makeText(activity, "发送成功", Toast.LENGTH_LONG).show();
                                                      Date_Response date_response=parseDate(new ByteArrayInputStream(responseBody));
                                                      Intent   mIntent=new Intent();
                                                      mIntent.putExtra("lng",lng+"");
                                                      mIntent.putExtra("lat",lat+"");
                                                      mIntent.putExtra("invite_id",String.valueOf(date_response.getDate_id()));
                                                      SendXQInvite.this.setResult(33,mIntent);
                                                      SendXQInvite.this.finish();
                                                      finish();
                                                  }

                                                  @Override
                                                  public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                  }
                                              });

                                          }
                                      }
        );



        lostDays=(TextView)this.findViewById(R.id.xq_lost_time);
        lostImage=(ImageView)this.findViewById(R.id.xq_lost_imageview);
        baiduMap=(ImageView)this.findViewById(R.id.ld_address_imageview);
        geoAdress=(TextView)this.findViewById(R.id.ld_address);
        xqImage=(ImageView)this.findViewById(R.id.xq_dog_image);
        myDog=(TextView)this.findViewById(R.id.xq_my_dog);
        // 输入框
        et_write_status = (EditText) findViewById(R.id.xq_content);
        tweet_text_record = (TextView) findViewById(R.id.tweet_text_record);
        et_write_status.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() >= MAX_LEN) {
                    tweet_text_record.setText("邀请超过字数啦");
                } else {
                    tweet_text_record.setText((MAX_LEN - s.length())
                            + " X");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > MAX_LEN) {
                    et_write_status.setText(s.subSequence(0, MAX_LEN));
                    CharSequence text = et_write_status.getText();
                    if (text instanceof Spannable) {
                        Selection.setSelection((Spannable) text, MAX_LEN);
                    }
                }
            }
        });


        activity=this;
        xqImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(activity, DogName.class), REQUEST_CODE_NAME);
            }
        });


        lostImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LostCustomDialog lostCustomDialog=new LostCustomDialog(activity, new LostCustomDialog.OnWhellSetDayData() {
                    @Override
                    public void setWhellData(String w1) {
                       lostDays.setText(w1+"");
                    }
                });

                //设置对话框的位置
                WindowManager m=getWindowManager();
                Window dialogWindow= lostCustomDialog.getWindow();
                WindowManager.LayoutParams lp=dialogWindow.getAttributes();
                Display d=m.getDefaultDisplay();
                lp.height=(int)(d.getHeight()*0.6);
                lp.width=(int)(d.getWidth());
                dialogWindow.setAttributes(lp);
                dialogWindow.setGravity(Gravity.BOTTOM);
                lostCustomDialog.show();
            }
        });

        baiduMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(activity, BaiduMapActivity.class), REQUEST_CODE_MAP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

             if (resultCode == RESULT_OK) {
                 if (requestCode == REQUEST_CODE_MAP) {
                     lng = data.getStringExtra("longitude");
                     lat = data.getStringExtra("latitude");
                     address = data.getStringExtra("address");

                     String temp[] = address.split(" ");
                     province = temp[0];
                     city = temp[1];
                     geoAdress.setText(address.replaceAll(" ", ""));
                     String temp_province = province.substring(0, province.length() - 1);
                     //Toast.makeText(activity, "temp_province" + temp_province, Toast.LENGTH_LONG).show();

                     DGApi.getCtyList("android", temp_province, mHandler);

                     //Toast.makeText(activity, lng + " " + lat + " " + address, Toast.LENGTH_LONG).show();
                 }
                 if(requestCode==REQUEST_CODE_NAME)
                 {
                      dogName=data.getStringExtra("name");
                      myDog.setText(dogName);
                 }
             }

    }
    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBytes) {
            String str=new String(responseBytes);
            try {
                Province province= parseList(new ByteArrayInputStream(responseBytes));
                List<City> cityList=province.getList();
                String temp_city= city.substring(0, city.length() - 1);
                cityid=-1;
                for(int i=0;i<cityList.size();i++)
                {
                    if(cityList.get(i).getName().equals(temp_city))
                    {
                        cityid=cityList.get(i).getCityid();
                        //Toast.makeText(activity,cityid+"city",Toast.LENGTH_LONG).show();

                        break;
                    }
                }
                if(cityid==-1)
                {
                    Toast.makeText(activity,"没有当前城市的id",Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            Toast.makeText(activity,"请求网络失败，检查网络是否正常！",Toast.LENGTH_LONG).show();
        }
    };
    protected Province parseList(InputStream is) throws Exception {
        Province list = null;
        try {
            list = XmlUtils.toBean(Province.class, is);
        } catch (NullPointerException e) {
            list = new Province();
        }
        return list;
    }
    //当接受邀请时把返回的值进行解析
    protected Date_Response parseDate(InputStream is)
    {
        Date_Response date_response;
        try {
            date_response = XmlUtils.toBean(Date_Response.class, is);
        }catch (NullPointerException e)
        {
            date_response=new Date_Response();
        }
        return date_response;
    }


}
