package cn.hi028.android.highcommunity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.don.tools.BpiHttpHandler;
import com.don.tools.SaveBitmap;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hi028.android.highcommunity.HighCommunityApplication;
import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.activity.fragment.CommunityFrag;
import cn.hi028.android.highcommunity.adapter.GridAdapter;
import cn.hi028.android.highcommunity.adapter.LabelGrideAdapter;
import cn.hi028.android.highcommunity.bean.LabelBean;
import cn.hi028.android.highcommunity.bean.UserCenterBean;
import cn.hi028.android.highcommunity.utils.Constacts;
import cn.hi028.android.highcommunity.utils.HTTPHelper;
import cn.hi028.android.highcommunity.utils.HighCommunityUtils;
import cn.hi028.android.highcommunity.view.ECAlertDialog;
import photo.activity.AlbumActivity;
import photo.activity.GalleryActivity;
import photo.util.Bimp;
import photo.util.ImageItem;

/**
 * @?????????????????????????????????<br>
 * @????????? ?????????<br>
 * @?????????1.0<br>
 * @?????????2015/12/16<br>
 */

public class LabelAct extends BaseFragmentActivity implements ShowLocationListAct.MyAddressChangerListener {
    static final String TAG = "LabelAct--->";
    public static final String ACTIVITYTAG = "LabelAct";
    public static final String INTENTTAG = "LabelActIntent";
    LabelGrideAdapter mAdapter;
    GridAdapter mPostAdapter;
    LabelBean LastBean, CurrentBean;
    ECAlertDialog mDialog;
    boolean clicked = false;
    int requesetPhoto = 0x000001, requestFile = 0x000002,
            requestCropImage = 0x000003;
    PopupWindow mPhotoPopupWindow = null, mWaitingWindow;
    Uri mPhotoUri = null;
    List<String> mImages = new ArrayList<String>();
    String mLabelId = null;
    String mGid = "";
    boolean isDelStatus = false;
    LabelBean temp = null;
    int type = 0;
    @Bind(R.id.iv_label_back)
    ImageView mImgBack;
    @Bind(R.id.tv_label_title)
    TextView mTitle;
    @Bind(R.id.ptrgv_label_PostImage)
    PullToRefreshGridView mPostImage;
    @Bind(R.id.tv_label_Postlocation)
    TextView mLocation;
    @Bind(R.id.iv_label_gridview)
    PullToRefreshGridView mGridView;
    @Bind(R.id.tv_label_RightnMenu)
    TextView mPublish;
    @Bind(R.id.tv_label_PostContent)
    EditText mContent;
    private PopupWindow mWindow;
    String allDetailAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_label);
        ButterKnife.bind(this);

        initView();
    }

    void initView() {
        type = getIntent().getIntExtra(INTENTTAG, 0);
        mGid = getIntent().getStringExtra(ACTIVITYTAG);
        System.out.println("xxxddd:mGid:" + mGid);
        mAdapter = new LabelGrideAdapter(this);
        mPostAdapter = new GridAdapter(this);
        mGridView.setAdapter(mAdapter);
        mGridView.setMode(PullToRefreshBase.Mode.DISABLED);
        mGridView.setOnItemClickListener(mItemListener);
        mPostImage.setAdapter(mPostAdapter);
        mPostImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                  if (mPostAdapter.getItem(i).startsWith("drawable://")) {
                                                      if (mPhotoPopupWindow == null) {
                                                          mPhotoPopupWindow = HighCommunityUtils.GetInstantiation()
                                                                  .ShowPhotoPopupWindow(LabelAct.this, mPhoto, mFile);
                                                      }
                                                      mPhotoPopupWindow.showAtLocation(mContent, Gravity.BOTTOM, 0, HighCommunityApplication.SoftKeyHight);
                                                  } else {
                                                      Intent intent = new Intent(LabelAct.this,
                                                              GalleryActivity.class);
                                                      intent.putExtra("position", "1");
                                                      intent.putExtra("ID", i);
                                                      startActivity(intent);
                                                  }
                                              }
                                          }

        );
        HighCommunityUtils.InitLabelList(6);
        HTTPHelper.getUserCenter(mLocationIbpi, HighCommunityApplication.mUserInfo.getId() + "");

        //??????init
        Log.e(TAG, "????????????");
        mLocationClient = new LocationClient(this); //??????LocationClie
        Log.e(TAG, "????????????2");
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        //????????????GPS
        option.setCoorType("bd09ll");       //?????????????????????????????????
        option.setAddrType("all");//???????????????????????????????????????
        option.setPriority(LocationClientOption.NetWorkFirst);  //?????????????????????
        option.setProdName("LocationDemo"); //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        option.disableCache(false);//????????????????????????
        option.setIsNeedLocationPoiList(true);
        option.setIsNeedAddress(true);// ???????????????????????????????????????
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS????????????
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// ????????????????????????
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// ????????????
                    sb.append("\ndirection : ");
                    sb.append(location.getDirection());// ?????????
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    sb.append("\ndescribe : ");
                    sb.append("gps????????????");

                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// ??????????????????
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());

                    allDetailAdress = location.getAddrStr();
                    //???????????????
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("??????????????????");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// ??????????????????
                    sb.append("\ndescribe : ");
                    sb.append("??????????????????????????????????????????????????????");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("??????????????????????????????????????????IMEI???????????????????????????loc-bugs@baidu.com????????????????????????");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("????????????????????????????????????????????????????????????");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                }
                sb.append("\nlocationdescribe : ");
                sb.append(location.getLocationDescribe());// ?????????????????????
                List<Poi> list = location.getPoiList();// POI??????
                if (list != null) {
                    sb.append("\npoilist size = : ");
                    sb.append(list.size());
                    for (Poi p : list) {
                        sb.append("\npoi= : ");
                        sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                    }
                }
                Log.i("BaiduLocationApiDem", sb.toString());
                Log.i("BaiduLocationApiDem", "???????????????" + location.getCity() + " " + location.getDistrict() + " " + location.getStreet() + " " + location.getAddress());
                Log.i("BaiduLocationApiDem", "???????????? setui");
                if (allDetailAdress != null && !isLocationClicked) {
                    if (allDetailAdress.contains("???")) {
                        String[] split = allDetailAdress.split("???");
                        mLocation.setText(split[1]);
                    } else {
                        mLocation.setText(location.getCity() + "" + location.getDistrict() + "" + location.getStreet());
                    }
                }
                if (isLocationClicked) {
                    Log.i("BaiduLocationApiDem", "???????????? setui2");
                    isLocationClicked = !isLocationClicked;
                    Intent mModify = new Intent(LabelAct.this, ShowLocationListAct.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("BDLocation", location);
                    mModify.putExtras(mBundle);
//                    startActivity(mModify);
                    startActivityForResult(mModify, 8);
                }
            }
        });//??????????????????
        Log.e(TAG, "??????3");
        mLocationClient.start();
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocationClicked = true;
                int requestLocation = -1;
                if (mLocationClient.isStarted()) {
                    Log.e(TAG, "isStarted--->");
                    mLocationClient.stop();
                    mLocationClient.start();
                    requestLocation = mLocationClient.requestLocation();
                } else {
                    mLocationClient.start();
                    requestLocation = mLocationClient.requestLocation();
                }
                Log.e(TAG, "requestLocation--->" + requestLocation);
            }
        });

    }

    // ??????????????????
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    boolean isLocationClicked = false;
    public static LocationClient mLocationClient = null;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mGridView.getLeft() <= ev.getX() && ev.getX() <= mGridView.getRight()
                    && mGridView.getTop() < ev.getY() && ev.getY() < mGridView.getBottom()) {
            } else {
                if (isDelStatus) {
                    isDelStatus = false;
                    if (temp != null && temp.isDel() == true) {
                        temp.setDel(false);
                        mAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        setHeight();
        super.onResume();

    }


    public void setHeight() {
        HighCommunityUtils.GetInstantiation().setGridViewHeightBasedOnChildren(mPostImage, mPostAdapter, 4);
    }

    AdapterView.OnItemClickListener mItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            LabelBean mCurrent = mAdapter.getItem(i);
            if (mCurrent.getId() == "-1") {
                HighCommunityUtils.GetInstantiation().ShowInput(LabelAct.this, mContent, mBack, "?????????????????????");
                return;
            } else if (mCurrent.getId() == "-2") {
                if (mDialog == null) {
                    mDialog = ECAlertDialog.buildAlert(LabelAct.this, "?????????????????????????", "??????", "??????"
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mWaitingWindow = HighCommunityUtils.GetInstantiation().ShowWaittingPopupWindow(LabelAct.this, mContent, Gravity.CENTER);
                                    HTTPHelper.Dellabel(new BpiHttpHandler.IBpiHttpHandler() {
                                        @Override
                                        public void onError(int id, String message) {

                                        }

                                        @Override
                                        public void onSuccess(Object message) {
                                            HighCommunityUtils.GetInstantiation().ShowToast(message.toString(), 0);
                                            mAdapter.remove(mLabelId);
                                            mAdapter.notifyDataSetChanged();
                                            mLabelId = "";
                                            mWaitingWindow.dismiss();
                                        }

                                        @Override
                                        public Object onResolve(String result) {
                                            return null;
                                        }

                                        @Override
                                        public void setAsyncTask(AsyncTask asyncTask) {

                                        }

                                        @Override
                                        public void cancleAsyncTask() {
                                            mWaitingWindow.dismiss();
                                        }

                                        @Override
                                        public void shouldLogin(boolean isShouldLogin) {

                                        }

                                        @Override
                                        public void shouldLoginAgain(boolean isShouldLogin, String msg) {
                                            if (isShouldLogin){
                                                HighCommunityUtils.GetInstantiation().ShowToast(msg, 0);
                                                HighCommunityApplication.toLoginAgain(LabelAct.this);
                                            }
                                        }
                                    }, mLabelId);
                                }
                            }, null);
                    mDialog.setTitle("??????");
                }
                if (mLabelId == null || mLabelId.equals("")) {
                    HighCommunityUtils.GetInstantiation().ShowToast("??????????????????????????????", 0);
                    return;
                } else if (1 <= Integer.parseInt(mLabelId) && Integer.parseInt(mLabelId) <= 8) {
                    HighCommunityUtils.GetInstantiation().ShowToast("??????????????????????????????", 0);
                    return;
                }
                mDialog.show();
                return;
            }
            if (LastBean == null) {
                LastBean = mAdapter.getItem(i);
                LastBean.setClicked(true);
                mLabelId = LastBean.getId();
            } else {
                CurrentBean = mAdapter.getItem(i);
                LastBean.setClicked(false);
                CurrentBean.setClicked(true);
                mLabelId = CurrentBean.getId();

                LastBean = CurrentBean;
                CurrentBean = null;
            }
            if (TextUtils.isEmpty(LastBean.getPic())) {
                mAdapter.CanDelete(true);
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    HighCommunityUtils.InputCallBack mBack = new HighCommunityUtils.InputCallBack() {
        @Override
        public void onInput(String input) {
            if (input.length() > 4) {
                HighCommunityUtils.GetInstantiation().ShowToast("????????????????????????4??????", 0);
            } else {
                HTTPHelper.Addlabel(mLabelIbpi, HighCommunityApplication.mUserInfo.getId() + "", input);
            }

        }
    };

    BpiHttpHandler.IBpiHttpHandler mLabelIbpi = new BpiHttpHandler.IBpiHttpHandler() {
        @Override
        public void onError(int id, String message) {

        }

        @Override
        public void onSuccess(Object message) {
            if (null == message)
                return;
            LabelBean mLabel = (LabelBean) message;
            Constacts.mCustomLabel.add(0, mLabel);
            mAdapter.AddNewData(mLabel);
        }

        @Override
        public Object onResolve(String result) {
            return HTTPHelper.ResolveLabel(result);
        }

        @Override
        public void setAsyncTask(AsyncTask asyncTask) {

        }

        @Override
        public void cancleAsyncTask() {

        }

        @Override
        public void shouldLogin(boolean isShouldLogin) {

        }

        @Override
        public void shouldLoginAgain(boolean isShouldLogin, String msg) {
            if (isShouldLogin){
                HighCommunityUtils.GetInstantiation().ShowToast(msg, 0);
                HighCommunityApplication.toLoginAgain(LabelAct.this);
            }
        }
    };

    BpiHttpHandler.IBpiHttpHandler mLocationIbpi = new BpiHttpHandler.IBpiHttpHandler() {
        @Override
        public void onError(int id, String message) {
            HighCommunityUtils.GetInstantiation().ShowToast(message, 0);
        }

        @Override
        public void onSuccess(Object message) {
            if (null == message)
                return;
            Constacts.mUserCenter = (UserCenterBean) message;
        }

        @Override
        public Object onResolve(String result) {
            return HTTPHelper.ResolveUserCenter(result);
        }

        @Override
        public void setAsyncTask(AsyncTask asyncTask) {

        }

        @Override
        public void cancleAsyncTask() {

        }

        @Override
        public void shouldLogin(boolean isShouldLogin) {

        }

        @Override
        public void shouldLoginAgain(boolean isShouldLogin, String msg) {
            if (isShouldLogin){
                HighCommunityUtils.GetInstantiation().ShowToast(msg, 0);
                HighCommunityApplication.toLoginAgain(LabelAct.this);
            }
        }
    };

    void back() {
        this.finish();
    }

    void publish() {
        if (clicked) {
            return;
        }
        String content = mContent.getText().toString().trim();
        if (mImages != null && mImages.size() > 0) {
            mImages.clear();
        }
        for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
            if (!Bimp.tempSelectBitmap.get(i).getImagePath().startsWith("drawable://"))
                mImages.add(Bimp.tempSelectBitmap.get(i).getImagePath());
        }
        if (TextUtils.isEmpty(mLabelId)) {
            HighCommunityUtils.GetInstantiation().ShowToast("???????????????", 0);
            clicked = false;
            return;
        } else if (TextUtils.isEmpty(content)) {
            HighCommunityUtils.GetInstantiation().ShowToast("???????????????", 0);
            clicked = false;
            return;
        } else if (TextUtils.isEmpty(HighCommunityApplication.mUserInfo.getToken())) {
            HighCommunityUtils.GetInstantiation().ShowToast("????????????,?????????", 0);
            clicked = false;
            return;
        }
        mWaitingWindow = HighCommunityUtils.GetInstantiation().ShowWaittingPopupWindow(this, mGridView, Gravity.CENTER);
        HTTPHelper.PostingMsg2(mIbpi, mLabelId, content, HighCommunityApplication.mUserInfo.getId() + "",
                mImages, mGid, HighCommunityApplication.mUserInfo.getToken(),mLocation.getText().toString());
    }

    /**
     * ??????
     **/
    View.OnClickListener mPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPhotoPopupWindow != null)
                mPhotoPopupWindow.dismiss();
            mPhotoUri = Uri.fromFile(new File(SaveBitmap.getTheNewUrl()));
            Intent imageCaptureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            if (SaveBitmap.isHaveSD) {
                imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,
                        requesetPhoto);
            }
            startActivityForResult(imageCaptureIntent, 1);
        }
    };
    /**
     * ?????????
     **/
    View.OnClickListener mFile = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mPhotoPopupWindow != null)
                mPhotoPopupWindow.dismiss();
            Intent intent = new Intent(LabelAct.this,
                    AlbumActivity.class);
            startActivity(intent);
            LabelAct.this.overridePendingTransition(com.jeremyfeinstein.slidingmenu.lib.R.anim.activity_translate_in,
                    com.jeremyfeinstein.slidingmenu.lib.R.anim.activity_translate_out);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requesetPhoto) {
            // ????????????
            if (resultCode != RESULT_OK) {
                return;
            }
//            RequestCropImage(mPhotoUri);
            ImageItem mItem = new ImageItem();
            mItem.setImagePath(mPhotoUri.getPath());
            Bimp.tempSelectBitmap.add(0, mItem);
            mPostAdapter.notifyDataSetChanged();
        } else if (requestCode == requestFile) {
            // ????????????
            if (data != null) {
                // ???????????????Uri,?????????????????????????????????????????????Uri??????????????????????????????????????????Uri?????????????????????????????????
                Uri mImageCaptureUri = data.getData();
                if (mImageCaptureUri != null) {
                } else {
                    // ???????????????????????????????????????????????????Bundle??????????????????????????????????????????Bitmap??????
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap image = extras.getParcelable("data");
                        if (image != null) {
                            mImageCaptureUri = SaveBitmap.SaveBitmap(image);
                        }
                    }
                }
                RequestCropImage(mImageCaptureUri);
            }
        } else if (resultCode == 8) {
            Log.e(TAG, "requestCode==8:" + resultCode);
            if (requestCode != 8) {
                Log.e(TAG, "requestCode:" + requestCode);

                return;
            }
            Log.e(TAG, "requestCode2:" + requestCode);

            String address2 = data.getStringExtra("address");

            this.mLocation.setText(address2);
        }
    }

    /**
     * ??????
     **/
    private void RequestCropImage(Uri uri) {
        final Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("circleCrop", new String(""));
        mPhotoUri = Uri.fromFile(new File(SaveBitmap.getTheNewUrl()));
        intent.putExtra("output", mPhotoUri);
        startActivityForResult(intent, requestCropImage);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPhotoPopupWindow != null && mPhotoPopupWindow.isShowing()) {
                mPhotoPopupWindow.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    BpiHttpHandler.IBpiHttpHandler mIbpi = new BpiHttpHandler.IBpiHttpHandler() {
        @Override
        public void onError(int id, String message) {
            if (null != mWaitingWindow && mWaitingWindow.isShowing())
                mWaitingWindow.dismiss();
            HighCommunityUtils.GetInstantiation().ShowToast(message.toString(), 0);
        }

        @Override
        public void onSuccess(Object message) {
            if (null != mWaitingWindow && mWaitingWindow.isShowing())
                mWaitingWindow.dismiss();
            if (null == message)
                return;
            CommunityFrag.isNeedRefresh = true;
            HighCommunityUtils.GetInstantiation().ShowToast(message.toString(), 0);
            Intent goMainIntent = new Intent(LabelAct.this, MainActivity.class);
            goMainIntent.putExtra("communityFlag", 0x222);
            startActivity(goMainIntent);
            LabelAct.this.finish();
        }

        @Override
        public Object onResolve(String result) {
            return result;
        }

        @Override
        public void setAsyncTask(AsyncTask asyncTask) {

        }

        @Override
        public void cancleAsyncTask() {

        }

        @Override
        public void shouldLogin(boolean isShouldLogin) {

        }

        @Override
        public void shouldLoginAgain(boolean isShouldLogin, String msg) {
            if (isShouldLogin){
                HighCommunityUtils.GetInstantiation().ShowToast(msg, 0);
                HighCommunityApplication.toLoginAgain(LabelAct.this);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onAddressChange(String address) {
        if (this.mLocation == null) {
            Log.e(TAG, "mLocation null  address:" + address);
        } else {
            Log.e(TAG, "mLocation  ??????  address:" + address);
            this.mLocation.setText(address);
        }
    }

    @OnClick({R.id.iv_label_back, R.id.tv_label_RightnMenu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_label_back:
                onBack(view);
                break;
            case R.id.tv_label_RightnMenu:
                publish();
                break;
        }
    }
}
