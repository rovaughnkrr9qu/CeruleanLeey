package cn.hi028.android.highcommunity.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.don.tools.BpiHttpHandler;
import com.don.tools.GeneratedClassUtils;
import com.don.view.CircleImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jaeger.library.StatusBarUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.activity.BaseFragment;
import net.duohuo.dhroid.util.ImageLoaderUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.hi028.android.highcommunity.HighCommunityApplication;
import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.activity.alliance.AllianceOrder;
import cn.hi028.android.highcommunity.activity.fragment.ActFrag;
import cn.hi028.android.highcommunity.activity.fragment.HuiLifeFrag;
import cn.hi028.android.highcommunity.activity.fragment.NeighborFrag;
import cn.hi028.android.highcommunity.activity.fragment.ServiceFrag;
import cn.hi028.android.highcommunity.bean.RongYunBean;
import cn.hi028.android.highcommunity.bean.UserCenterBean;
import cn.hi028.android.highcommunity.utils.Constacts;
import cn.hi028.android.highcommunity.utils.HTTPHelper;
import cn.hi028.android.highcommunity.utils.HighCommunityUtils;
import cn.hi028.android.highcommunity.utils.MyLocationListener;
import cn.hi028.android.highcommunity.utils.NToast;
import cn.hi028.android.highcommunity.utils.updateutil.UpdateUtil;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import photo.util.Res;

/**
 * ?????????
 */
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener, NeighborFrag.MyChangeListener,HuiLifeFrag.MyChangeListener4HuiLife {
    static  final String Tag="MainActivity--->";
    public static String TAG = "MainActivity--->";

    /**
     * ??????tabs
     **/
    private final int[] tab_menu_text_ID = new int[]{R.id.main_tab_firth,
            R.id.main_tab_second, R.id.main_tab_fourth, R.id.main_tab_five};
    private View[] menu_tvs = null;
    private long exitTime = 0;
    /**
     * ??????tabs ??????
     **/
    private final int[] tab_menu_title = new int[]{R.string.tb_first, R.string.tb_second, R.string.tb_fourth, R.id.main_tab_five};
//    ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();// frags??????
    ArrayList<Fragment> fragments = new ArrayList<Fragment>();// frags??????

    BaseFragment  serviceFrag, huiLifeFrag, neighborFrag;
    Fragment actFrag;
    private ImageView MiddleButton;
    private RelativeLayout mUserinfo;
    private PullToRefreshScrollView mLeftLayout;
    public SlidingMenu menu;
    private LinearLayout mTopic, mCollection, mBill, mWallet, mSetting, mCart,
            mOrder, mAllianceOrder, mZhongCou, mCarft,mMyMsg,mSysMsg;;
    private TextView mTopicNum, mCollectionNum, mLeftBillNum, mLeftWalletNum,
            mSettingNum, mTitle, mLeftName, mLeftSex, mLeftLocation,
            mLeftCartNum, mLeftOrderNum, mLeftAllianceOrder, mLeftZhongCouNum,
            mLeftCarftNum;
    private CircleImageView mAvatar;
    /**??????????????????  ????????????0?????? 1??????**/
    private RadioGroup mGroup,mNewHuiLifeGroup;
    private LinearLayout mStatusHight;
    /**??????  ?????? ??????  ??????**/
    private RadioButton mLeftButton, mRightButton,mRightButton_right,mSupplyBut,mChipsBut;
    /**?????????????????????   ?????????????????????   ??????????????????????????????      ????????????????????????????????????????????????**/
    private ImageView mLeftMenu, mRightMenu, mRightTop, mLeftTop;
    LinearLayout mLeftLocation_layout;
    private int mTouchMode = -1;

    private PopupWindow mWindow;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//		??????labelact
        if (intent.getIntExtra("communityFlag", 0) == 0x22) {
//            tabSelector(1);
//			NeighborFrag mFrag = (NeighborFrag) getSupportFragmentManager()
//					.findFragmentByTag(NeighborFrag.FRAGMENTTAG);
            ServiceFrag mFrag = (ServiceFrag)
                    getSupportFragmentManager().findFragmentByTag(ServiceFrag.FRAGMENTTAG);
            if (null == mFrag)
                return;
//			mFrag.setCurrentPage(0);
        } else if (intent.getIntExtra("communityFlag", 0) == 0x222) {
            tabSelector(0);
            NeighborFrag mFrag = (NeighborFrag) getSupportFragmentManager()
                    .findFragmentByTag(NeighborFrag.FRAGMENTTAG);
            if (null == mFrag)
                return;
            mFrag.setCurrentPage(0);
        }else if (intent.getIntExtra("actFlag", 0) == 0x66) {
            tabSelector(3);
            ActFrag mFrag = (ActFrag) getSupportFragmentManager()
                    .findFragmentByTag(ActFrag.FRAGMENTTAG);
            if (null == mFrag)
                return;
//            mFrag.setCurrentPage(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityTack.getInstanse().clear();
        // ???????????????
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // // ???????????????
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // ???????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // //???????????????
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //????????????
        new UpdateUtil(MainActivity.this, getApplicationContext()).initUpdate();
        Res.init(this);
        //TODO ???????????????
        Log.d(TAG, "connect  1");
//??????   ????????????
        mHttpUtils = new HttpUtils();
        mHttpUtils.configCurrentHttpCacheExpiry(0);
        mHttpUtils.configSoTimeout(4000);
        mHttpUtils.configTimeout(4000);
        useHttp();
//        HTTPHelper.getRongToken(mRongIbpi);

//        connect("F2sMSChp+KkI6SSaEWUnITkc36aZPpJZqj81iEXN8zId1Zx5/yGTsbphE0dY6jOh96NnNqVdWJIYgvzyeiwnVg==");
        Log.d(TAG, "connect  3");

        initView();
        initLeftMenu();
        initMenu(savedInstanceState);

        if (HighCommunityUtils.isLogin()) {
            JPushInterface.init(getApplicationContext());
            setTag();
            setStyleCustom();
            registerMessageReceiver();
            Log.d("userinfor", "???????????????" + HighCommunityApplication.mUserInfo.toString());
        }
//        toTestLocaton();
    }

    public void setTag() {
        Log.d(Tag,"???????????????" + HighCommunityApplication.mUserInfo.getId());
        JPushInterface.setAliasAndTags(getApplicationContext(),
                HighCommunityApplication.mUserInfo.getId() + "", null,
                new TagAliasCallback() {

                    @Override
                    public void gotResult(int code, String alias,
                                          Set<String> tags) {
                        String logs;
                        switch (code) {
                            case 0:
                                logs = "Set tag and alias success";
                                Log.i(TAG, logs);
                                break;

                            case 6002:
                                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                                Log.i(TAG, logs);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setTag();
                                    }
                                }, 60 * 6000);
                                break;

                            default:
                                logs = "Failed with errorCode = " + code;
                                Log.e(TAG, logs);
                        }

                    }

                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int checkNum=getIntent().getIntExtra("checkupdata",-1);
        if (checkNum==6){
            boolean isUpdate=  new UpdateUtil(MainActivity.this,getApplicationContext()).checkIsToUpdate();
            if (isUpdate){
                new UpdateUtil(MainActivity.this, getApplicationContext()).initUpdate();
            }else{
                Toast.makeText(MainActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        if (HighCommunityUtils.isLogin()) {
            unregisterReceiver(mMessageReceiver);
        }
        super.onDestroy();
    }

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "cn.hi028.android.highcommunity.related";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void onHuiLifeChange(int i) {

        if (i==0) {//????????????
            mSupplyBut.setChecked(true);
            mChipsBut.setChecked(false);

        } else if (i==1){//????????????
            mSupplyBut.setChecked(false);
            mChipsBut.setChecked(true);
;
        }

    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                int extras = intent.getIntExtra(KEY_EXTRAS, 0);
                if (extras == 1) {
                    mRightTop.setVisibility(View.GONE);
                } else {
                    mRightTop.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * ????????????????????? - ???????????????Layout
     */
    private void setStyleCustom() {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(
                MainActivity.this, R.layout.customer_notitfication_layout,
                R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.drawable.ic_launcher;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (HighCommunityApplication.mUserInfo.getId() != 0) {
            HTTPHelper.getUserCenter(mIbpi,
                    HighCommunityApplication.mUserInfo.getId() + "");
        }
    }
    public void goneTitleBar(){
        Log.e(Tag, "goneTitleBar ");

        titleBar.setVisibility(View.GONE);
        Log.e(Tag, "goneTitleBar ok");


    }
    public void visibleTitleBar(){
        Log.e(Tag, "goneTitleBar ");

        titleBar.setVisibility(View.GONE);
        Log.e(Tag, "goneTitleBar ok");


    }
View titleBar;
    void initView() {
        titleBar =this.findViewById(R.id.title);
        mTitle = (TextView) this.findViewById(R.id.tv_mainlevel_title);
        mLeftMenu = (ImageView) this.findViewById(R.id.tv_mainlevel_LeftButton);
        mRightMenu = (ImageView) this.findViewById(R.id.iv_mainlevel_RightButton);
        mRightTop = (ImageView) this.findViewById(R.id.iv_mainlevel_RightNewMessage);
        mLeftTop = (ImageView) this.findViewById(R.id.iv_mainlevel_LeftNewMessage);
        mGroup = (RadioGroup) this.findViewById(R.id.rg_maintitle_group);
        mNewHuiLifeGroup = (RadioGroup) this.findViewById(R.id.rg_title_newHuilifegroup);
        mLeftButton = (RadioButton) this.findViewById(R.id.rb_maintitle_leftbutton);
        mRightButton = (RadioButton) this.findViewById(R.id.rb_maintitle_rightbutton);
        mRightButton_right = (RadioButton) this.findViewById(R.id.rb_maintitle_rightbutton_right);
        mSupplyBut = (RadioButton) this.findViewById(R.id.rb_title_supply);
        mChipsBut = (RadioButton) this.findViewById(R.id.rb_title_chips);
        MiddleButton = (ImageView) this.findViewById(R.id.main_tab_third);
        mGroup.setVisibility(View.VISIBLE);
        mNewHuiLifeGroup.setVisibility(View.VISIBLE);
        mRightMenu.setVisibility(View.VISIBLE);
        mLeftButton.setOnClickListener(mTitleListener);
        mRightButton.setOnClickListener(mTitleListener);
        mRightButton_right.setOnClickListener(mTitleListener);
        mSupplyBut.setOnClickListener(mTitleListener);
        mChipsBut.setOnClickListener(mTitleListener);
        mRightMenu.setOnClickListener(mTitleListener);
        goneTitleBar();
    }

    /**
     * ????????????????????????
     */
    private void initLeftMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//// ?????????????????????????????????
        menu.setFadeDegree(0.35f);// ??????????????????????????????
        menu.setEnabled(true);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);//???SlidingMenu?????????Activity???
        // ???????????????????????????
        menu.setMenu(R.layout.leftmenu);
        //?????????
        mLeftLayout = (PullToRefreshScrollView) menu.getMenu().findViewById(
                R.id.ptrsv_leftmenu_layout);
        mUserinfo = (RelativeLayout) menu.getMenu().findViewById(
                R.id.rl_leftmenu_userinfo);
        mTopic = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_topic);
        mCollection = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_collection);
        mCart = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_cart);
        mOrder = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_Order);
        mAllianceOrder = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_alliance_Order);
        mZhongCou = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_ZhongCou);
        mMyMsg = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_MyMsg);
        mSysMsg = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_SysMsg);
        mCarft = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_Carft);
        mBill = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_bill);
        mWallet = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_wallet);
        mSetting = (LinearLayout) menu.getMenu().findViewById(
                R.id.ll_leftMenu_setting);
        mAvatar = (CircleImageView) menu.getMenu().findViewById(
                R.id.img_LeftFrag_Avatar);
        mLeftLocation = (TextView) menu.getMenu().findViewById(
                R.id.tx_LeftFrag_userlocation);

        mLeftLocation_layout = (LinearLayout) menu.getMenu().findViewById(
                R.id.tx_LeftFrag_userlocation_layout);
        mLeftSex = (TextView) menu.getMenu().findViewById(
                R.id.tx_LeftFrag_userSex);
        mTopicNum = (TextView) menu.getMenu().findViewById(
                R.id.tv_leftMenu_topic_Num);
        mCollectionNum = (TextView) menu.getMenu().findViewById(
                R.id.tv_leftMenu_topic_Num);
        mLeftBillNum = (TextView) menu.getMenu().findViewById(
                R.id.tv_leftMenu_topic_Num);
        mLeftWalletNum = (TextView) menu.getMenu().findViewById(
                R.id.tv_leftMenu_wallet_Num);
        mSettingNum = (TextView) menu.getMenu().findViewById(
                R.id.tv_leftMenu_topic_Num);
        mLeftName = (TextView) menu.getMenu().findViewById(
                R.id.tx_LeftFrag_userName);
        mLeftCartNum = (TextView) menu.getMenu().findViewById(
                R.id.tv_leftMenu_Cart_Num);
        mLeftCarftNum = (TextView) menu.getMenu().findViewById(
                R.id.tv_leftMenu_Carft_Num);
        mLeftOrderNum = (TextView) menu.getMenu().findViewById(
                R.id.tv_leftMenu_Order_Num);
        mLeftAllianceOrder = (TextView) menu.getMenu().findViewById(
                R.id.tv_leftMenu_alliance_Order_Num);
        mLeftZhongCouNum = (TextView) menu.getMenu().findViewById(
                R.id.tv_leftMenu_ZhongCou_Num);

        mLeftLayout.setMode(PullToRefreshBase.Mode.DISABLED);
        mUserinfo.setOnClickListener(mLeftMenuListener);
        mTopic.setOnClickListener(mLeftMenuListener);
        mCollection.setOnClickListener(mLeftMenuListener);
        mBill.setOnClickListener(mLeftMenuListener);
        mWallet.setOnClickListener(mLeftMenuListener);
        mSetting.setOnClickListener(mLeftMenuListener);
        mCart.setOnClickListener(mLeftMenuListener);
        mLeftName.setOnClickListener(mLeftMenuListener);
        mAvatar.setOnClickListener(mLeftMenuListener);
        mCarft.setOnClickListener(mLeftMenuListener);
        mZhongCou.setOnClickListener(mLeftMenuListener);
        mOrder.setOnClickListener(mLeftMenuListener);
        mAllianceOrder.setOnClickListener(mLeftMenuListener);
        //
        mMyMsg.setOnClickListener(mLeftMenuListener);
        mSysMsg.setOnClickListener(mLeftMenuListener);
        if (HighCommunityApplication.mUserInfo.getId() != 0)
            HTTPHelper.getUserCenter(mIbpi,
                    HighCommunityApplication.mUserInfo.getId() + "");
        menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                if (HighCommunityApplication.mUserInfo.getId() != 0){
                }
            }
        });
    }

    BpiHttpHandler.IBpiHttpHandler mIbpi = new BpiHttpHandler.IBpiHttpHandler() {
        @Override
        public void onError(int id, String message) {
            HighCommunityUtils.GetInstantiation().ShowToast(message, 0);
        }

        @Override
        public void onSuccess(Object message) {
            if (null == message)
                return;
            Constacts.mUserCenter = (UserCenterBean) message;
            setleftData();
        }

        @Override
        public Object onResolve(String result) {
            Log.e("renk", result);
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
                HighCommunityApplication.toLoginAgain(MainActivity.this);
            }
        }
    };

    private void setleftData() {
        boolean leftFlag = false, rightFlag = false;
        if (Constacts.mUserCenter != null) {
            ImageLoaderUtil.disPlay(Constacts.IMAGEHTTP
                            + Constacts.mUserCenter.getUserInfo().getHead_pic(),
                    mAvatar);
            mLeftLocation.setText("????????????:"
                    + Constacts.mUserCenter.getUserInfo().getVillage());
            mLeftLocation_layout.setOnClickListener(this);

            mLeftName.setText(Constacts.mUserCenter.getUserInfo().getNick());
            mLeftSex.setText(Constacts.mUserCenter.getUserInfo().getAge());
            if (Constacts.mUserCenter.getUserInfo().getSex().equals("0")) {
                mLeftSex.setSelected(false);
            } else {
                mLeftSex.setSelected(true);
            }
            mTopicNum.setVisibility(View.GONE);
            if (Constacts.mUserCenter.getOrder() == 0) {
                mLeftOrderNum.setVisibility(View.GONE);
            } else {
                leftFlag = true;
                mLeftOrderNum.setVisibility(View.VISIBLE);
                mLeftOrderNum.setText(Constacts.mUserCenter.getOrder() + "");
            }
            if (Constacts.mUserCenter.getNearby() == 0) {
                mLeftAllianceOrder.setVisibility(View.GONE);
            } else {
                mLeftAllianceOrder.setVisibility(View.VISIBLE);
                mLeftAllianceOrder.setText(Constacts.mUserCenter.getNearby()
                        + "");

            }
            if (Constacts.mUserCenter.getCart() == 0) {
                mLeftCartNum.setVisibility(View.GONE);
            } else {
                leftFlag = true;
                mLeftCartNum.setVisibility(View.VISIBLE);
                mLeftCartNum.setText(Constacts.mUserCenter.getCart() + "");
            }
            if (Constacts.mUserCenter.getFee() == 0) {
                mLeftBillNum.setVisibility(View.GONE);
            } else {
                leftFlag = true;
                mLeftBillNum.setVisibility(View.VISIBLE);
                mLeftBillNum.setText(Constacts.mUserCenter.getFee() + "");
            }
            if (Constacts.mUserCenter.getCho() == 0) {
                mLeftZhongCouNum.setVisibility(View.GONE);
            } else {
                leftFlag = true;
                mLeftZhongCouNum.setVisibility(View.VISIBLE);
                mLeftZhongCouNum.setText(Constacts.mUserCenter.getCho() + "");
            }
            mLeftWalletNum.setVisibility(View.GONE);
            if (leftFlag) {
            } else {
                mLeftTop.setVisibility(View.GONE);
            }
        } else {

        }
    }

    /**
     * ???????????????????????????
     */
    View.OnClickListener mLeftMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent mLeftjump = new Intent(MainActivity.this,
                    GeneratedClassUtils.get(MenuLeftAct.class));
            if (view.getId() == R.id.ll_leftMenu_setting) {
                mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                        Constacts.MENU_LEFT_SETTING);
                MainActivity.this.startActivity(mLeftjump);
                return;
            }
            if (HighCommunityUtils.isLogin(MainActivity.this)) {
                switch (view.getId()) {
                    case R.id.rl_leftmenu_userinfo:
                    case R.id.img_LeftFrag_Avatar:
                    case R.id.tx_LeftFrag_userName:
                        mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                                Constacts.MENU_LEFT_USERINFO);
                        mLeftjump.putExtra(MenuLeftAct.INTENTTAG,
                                HighCommunityApplication.mUserInfo.getId() + "");
                        break;
                    case R.id.ll_leftMenu_topic:
                        mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                                Constacts.MENU_LEFT_TOPIC);
                        Constacts.mUserCenter.setMessage(0);
                        break;
                    case R.id.ll_leftMenu_collection:
                        mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                                Constacts.MENU_LEFT_COLLECTION);
                        break;
                    case R.id.ll_leftMenu_wallet:
                        mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                                Constacts.MENU_LEFT_WALLET);
                        break;
                    case R.id.ll_leftMenu_bill:
                        mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                                Constacts.MENU_LEFT_BILL);
                        Constacts.mUserCenter.setFee(0);
                        break;
                    case R.id.ll_leftMenu_Order:
                        mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                                Constacts.MENU_LEFT_ORDER);
                        mLeftjump.putExtra(MenuLeftAct.INTENTTAG, 0);
                        Constacts.mUserCenter.setOrder(0);
                        break;
                    case R.id.ll_leftMenu_alliance_Order:
                        Intent intent = new Intent(MainActivity.this,
                                AllianceOrder.class);
                        startActivity(intent);
                        return;
                    case R.id.ll_leftMenu_cart:
                        mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                                Constacts.MENU_LEFT_GDCAR);
                        // Constacts.mUserCenter.setCart(0);
                        break;
                    case R.id.ll_leftMenu_Carft:
                        mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                                Constacts.MENU_LEFT_CARFTS);
                        break;
                    case R.id.ll_leftMenu_ZhongCou:
                        mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                                Constacts.MENU_LEFT_ZHONGCOU);
                        Constacts.mUserCenter.setCho(0);
                        break;
                    case R.id.ll_leftMenu_MyMsg://????????????
                        Log.d(TAG,"??????????????????");
                        mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                                Constacts.MENU_LEFT_MESSAGECENTER);
                        break;
                    case R.id.ll_leftMenu_SysMsg://????????????
                        Log.d(TAG,"??????????????????");

                        mLeftjump.putExtra(MenuLeftAct.ACTIVITYTAG,
                                Constacts.MENU_SYSMESSAGE);
                        Constacts.mUserCenter.setCho(0);
                        break;
                }
                if (view.getId() != R.id.ll_leftMenu_setting) {
                    setleftData();
                    MainActivity.this.startActivity(mLeftjump);
                }
            }

        }
    };

    /**
     * ???????????????
     */
    private void initMenu(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStackImmediate(null, 1);
        }
        neighborFrag = new NeighborFrag();
        serviceFrag = new ServiceFrag();
        huiLifeFrag = new HuiLifeFrag();
        actFrag = new ActFrag();
        fragments.add(neighborFrag);
        fragments.add(serviceFrag);
        fragments.add(huiLifeFrag);
        fragments.add(actFrag);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_Content, neighborFrag, NeighborFrag.FRAGMENTTAG)
                .show(neighborFrag)
                .add(R.id.main_Content, huiLifeFrag, HuiLifeFrag.FRAGMENTTAG)
                .hide(huiLifeFrag)
                .add(R.id.main_Content, actFrag, ActFrag.FRAGMENTTAG)
                .hide(actFrag)
                .add(R.id.main_Content, serviceFrag, ServiceFrag.FRAGMENTTAG)
                .hide(serviceFrag).commit();
        menu_tvs = new TextView[tab_menu_text_ID.length];
        for (int i = 0; i < tab_menu_text_ID.length; i++) {
            menu_tvs[i] = (TextView) findViewById(tab_menu_text_ID[i]);
            menu_tvs[i].setOnClickListener(this);
        }
        MiddleButton.setOnClickListener(this);
        tabSelector(1);
        mTitle.setText("??????");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void getMsgNum() {
        HTTPHelper.getMsgNum(new BpiHttpHandler.IBpiHttpHandler() {
            @Override
            public void onError(int id, String message) {

            }

            @Override
            public void onSuccess(Object message) {
                if (message != null) {
                    Integer msgNum = (Integer) message;
                    if (msgNum == 0) {
                        mRightTop.setVisibility(View.GONE);
                    } else {
                        mRightTop.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public Object onResolve(String result) {
                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                } catch (JSONException e) {
                    json = new JSONObject();
                }
                return json.optInt("tips", 0);
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
                    HighCommunityApplication.toLoginAgain(MainActivity.this);
                }
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param position 0-?????? 1-?????? 2-????????? 3-??????
     */
    private void tabSelector(int position) {
        Log.d(Tag,"tabSelector:" + position);
        if (0 == position) {
//            mTitle.setVisibility(View.GONE);
//            mGroup.setVisibility(View.VISIBLE);
//            mNewHuiLifeGroup.setVisibility(View.GONE);

        } else if (position==2){
//            mTitle.setVisibility(View.GONE);
//            mGroup.setVisibility(View.GONE);
//            mNewHuiLifeGroup.setVisibility(View.VISIBLE);

        }else {
//            mGroup.setVisibility(View.GONE);
//            mTitle.setVisibility(View.VISIBLE);
//            mNewHuiLifeGroup.setVisibility(View.GONE);

        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < tab_menu_text_ID.length; i++) {//
            if (position == i) {
                menu_tvs[i].setSelected(true);
            } else {
                menu_tvs[i].setSelected(false);
            }
            if (i==position) {
                //TODO
                Log.e(Tag,"?????????position???"+i+",fragment"+fragments.get(position).getTag());
                ft.show(fragments.get(position));
//                ft.commit();

            } else {
                ft.hide(fragments.get(i));
            }
        }
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_tab_firth://
                tabSelector(0);
                break;
            case R.id.main_tab_second://
                tabSelector(1);
                mTitle.setText("??????");
                break;
            case R.id.main_tab_third://
                if (HighCommunityUtils.GetInstantiation().isLogin(MainActivity.this)) {
                    Intent mLabel = new Intent(this, LabelAct.class);
                    startActivity(mLabel);
                }
                break;
            case R.id.main_tab_fourth://
                mTitle.setText("?????????");
                tabSelector(2);
                break;
            case R.id.main_tab_five://
                mTitle.setText(" ");
                tabSelector(3);
                break;
            case R.id.tx_LeftFrag_userlocation_layout:
            case R.id.tx_LeftFrag_userlocation:
                VallageAct.toStartAct(MainActivity.this, 1, false);
                break;
            default:
                break;
        }
    }

    View.OnClickListener mTitleListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NeighborFrag mFrag = (NeighborFrag) getSupportFragmentManager().findFragmentByTag(NeighborFrag.FRAGMENTTAG);
            HuiLifeFrag mHuiLifeFrag = (HuiLifeFrag) getSupportFragmentManager().findFragmentByTag(HuiLifeFrag.FRAGMENTTAG);
            if (null == mFrag)
                return;
            switch (view.getId()) {
                case R.id.rb_maintitle_leftbutton:
                    mFrag.setCurrentPage(0);
                    break;
                case R.id.rb_maintitle_rightbutton:
                    mFrag.setCurrentPage(1);
                    break;
                case R.id.rb_maintitle_rightbutton_right:
                    mFrag.setCurrentPage(2);
                    break;
                case R.id.rb_title_supply://??????
                    mHuiLifeFrag.setCurrentPage(0);
                    break;
                case R.id.rb_title_chips://??????
                    mHuiLifeFrag.setCurrentPage(1);
                    break;
                case R.id.tv_mainlevel_LeftButton:
                    int requestLocation=-1;
                    Toast.makeText(getApplicationContext(),"???????????????",Toast.LENGTH_SHORT).show();
                    if (mLocationClient.isStarted()){
                        Log.e(TAG,"isStarted--->");

                        mLocationClient.stop();

                    }else{
                        mLocationClient.start();
                        mLocationClient.requestLocation();
                    }
                    Log.e(TAG,"requestLocation--->");
                    break;
                case R.id.iv_mainlevel_RightButton:
                    if (HighCommunityUtils.isLogin(MainActivity.this)) {

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, MipcaActivityCapture.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    break;
            }
        }
    };
    private final static int SCANNIN_GREQUEST_CODE = 1;
    public static LocationClient mLocationClient=null;
    public BDLocationListener myListener = new MyLocationListener();
    @Override
    public void onChange(int flag) {
        if (flag==0) {
            mLeftButton.setChecked(true);
            mRightButton.setChecked(false);
            mRightButton_right.setChecked(false);
        } else if (flag==1){
            mLeftButton.setChecked(false);
            mRightButton.setChecked(true);
            mRightButton_right.setChecked(false);
        }else if (flag==2){
            mRightButton_right.setChecked(true);
            mLeftButton.setChecked(false);
            mRightButton.setChecked(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (((NeighborFrag) fragments.get(0)).onKeyDown(keyCode, event)) {
                return true;
            } else {
                exit();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            HighCommunityUtils.GetInstantiation().ShowToast(
                    getResources().getString(R.string.toast_press_to_exit), 0);
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("renk", "onactivityresult");
        Log.e("renk", "onactivityresult");
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
//                    //????????????????????????
                    Log.e(TAG,"??????????????????:"+bundle.getString("result"));
//                    mTextView.setText(bundle.getString("result"));
//                    //??????
//                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }

    }
//    @Override
//    protected void setStatusBar() {
////        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this,0, null);
//        StatusBarUtil.setColor(this,0xffffffff);
//    }
@Override
protected void setStatusBar() {
    StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this,0, null);
}
    /**
     * <p>?????????????????????????????????????????????????????????????????????????????? {@link_init(Context)} ???????????????</p>
     * <p>??????????????????????????????????????????SDK ???????????????????????????????????????10?????????????????????1, 2, 4, 8, 16, 32, 64, 128, 256, 512?????????
     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????</p>
     *
     * @param token    ??????????????????????????????????????????Token??????
     * @param??????callback ???????????????
     * @return RongIM  ??????????????????????????????
     */
    private void connect(String token) {
        Log.d(Tag, "--connect" );

        if (getApplicationInfo().packageName.equals(HighCommunityApplication.getCurProcessName(getApplicationContext()))) {
            Log.d(Tag, "--connect  ===" );

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token ???????????????????????????????????? 1.  Token ??????????????????????????????????????? App Server ???????????????????????? Token
                 *                  2.  token ????????? appKey ????????????????????? appKey ????????????
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.d(Tag, "--onTokenIncorrect" );

                }

                /**
                 * ??????????????????
                 * @param userid ?????? token ??????????????? id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d(Tag, "--onSuccess" + userid);
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
                    NToast.shortToast(MainActivity.this,"??????????????????: userid"+userid);
                }

                /**
                 * ??????????????????
                 * @param errorCode ???????????????????????? ??????????????????????????????
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d(Tag, "--onError" + errorCode);
                    NToast.shortToast(MainActivity.this,"????????????fail: errorCode"+errorCode);

                }
            });
        }
        Log.d(Tag, "--connect  !===" );

    }

    private void toTestLocaton() {
        Log.e(TAG,"????????????");
        mLocationClient = new LocationClient(this); //??????LocationClie
        Log.e(TAG,"????????????2");
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        //????????????GPS
        option.setAddrType("all");//???????????????????????????????????????
        option.setCoorType("bd09ll");//???????????????????????????????????????,?????????gcj02
        option.setPriority(LocationClientOption.NetWorkFirst);  //?????????????????????
        option.setProdName("LocationDemo"); //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        option.disableCache(false);//????????????????????????
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                Log.i("BaiduLocationApiDem","1,"+ location.getAddress().address+"2,"+location.getAddress().city+"3,"+
                        location.getAddrStr()+"4,"+location.getCity()+"5,"+location.getCountry()+"6,"+location.getStreet()+"7,"+
                        location.hasAddr()
                );
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
                    //???????????????
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ntostring : ");
                    sb.append(location.toString());
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
//                Toast.makeText(MainActivity.this,"???????????????"+location.getCity()+" "+location.getDistrict()+" "+location.getStreet()+" "+location.getAddress(),Toast.LENGTH_SHORT).show();
            }
        });//??????????????????
    }
    private HttpUtils mHttpUtils;
    private void useHttp() {
        Log.e(Tag, "??????useHttp ");
        String url = "http://028hi.cn/rc/chat/get-token.html";
        RequestParams params = new RequestParams();
        params.addBodyParameter("token", HighCommunityApplication.mUserInfo.getToken());
        Log.e(Tag, "??????useHttp  3");

        mHttpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Log.e(Tag, "http ??????????????? arg1--->" + arg1.toString());
                HighCommunityUtils.GetInstantiation().ShowToast(arg1.toString(), 0);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String content = arg0.result;
                Log.e(Tag, "http ??????success??? content--->" + content);


                if (null == arg0){
                    Log.d(TAG+"rong", "message  return :");
                    return;
                }
                RongYunBean data = new Gson().fromJson(content, RongYunBean.class);
                Log.d(TAG+"rong", "message  data :"+data.toString());

                HighCommunityUtils.GetInstantiation().ShowToast(data.toString(), 0);
                connect(data.getToken());
                Log.d(TAG+"rong", "connect  2");


            }
        });
    }
    BpiHttpHandler.IBpiHttpHandler mRongIbpi = new BpiHttpHandler.IBpiHttpHandler() {
        @Override
        public void onError(int id, String message) {
            Log.d(TAG+"rong", "onError  :"+message);

            HighCommunityUtils.GetInstantiation().ShowToast(message, 0);
        }

        @Override
        public void onSuccess(Object message) {
            Log.d(TAG+"rong", "onSuccess  :");

            if (null == message){
                Log.d(TAG+"rong", "message  return :");

                return;
            }
            RongYunBean data = (RongYunBean) message;
            Log.d(TAG+"rong", "message  data :"+data.toString());

            HighCommunityUtils.GetInstantiation().ShowToast(data.toString(), 0);
            connect(data.getToken());
            Log.d(TAG+"rong", "connect  2");
        }

        @Override
        public Object onResolve(String result) {
            Log.e(Tag, result);
            return HTTPHelper.ResolveRongYunBean(result);
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
                HighCommunityApplication.toLoginAgain(MainActivity.this);
            }
        }
    };

}
