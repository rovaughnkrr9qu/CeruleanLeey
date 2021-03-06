/***************************************************************************
 * Copyright (c) by raythinks.com, Inc. All Rights Reserved
 **************************************************************************/

package cn.hi028.android.highcommunity.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.activity.fragment.HuiChipsOrderFrag;
import cn.hi028.android.highcommunity.activity.fragment.HuiChipsOrderFrag_;
import cn.hi028.android.highcommunity.activity.fragment.MessageCenterFrag;
import cn.hi028.android.highcommunity.activity.fragment.MessageCenterFrag_;
import cn.hi028.android.highcommunity.activity.fragment.MyCarftsFrag;
import cn.hi028.android.highcommunity.activity.fragment.MyCarftsFrag_;
import cn.hi028.android.highcommunity.activity.fragment.MyCollectionSwitchFrag;
import cn.hi028.android.highcommunity.activity.fragment.MyCollectionSwitchFrag_;
import cn.hi028.android.highcommunity.activity.fragment.MyMessageFrag;
import cn.hi028.android.highcommunity.activity.fragment.MyTopicFrag;
import cn.hi028.android.highcommunity.activity.fragment.NewHuiGdCarFrag;
import cn.hi028.android.highcommunity.activity.fragment.NewHuiOrderFrag;
import cn.hi028.android.highcommunity.activity.fragment.NewHuiOrderFrag_;
import cn.hi028.android.highcommunity.activity.fragment.PersonalAuthFrag;
import cn.hi028.android.highcommunity.activity.fragment.ServicePaymentFrag;
import cn.hi028.android.highcommunity.activity.fragment.SettingFrag;
import cn.hi028.android.highcommunity.activity.fragment.SettingFrag_;
import cn.hi028.android.highcommunity.activity.fragment.SysMessageFrag;
import cn.hi028.android.highcommunity.activity.fragment.TenementBillFrag;
import cn.hi028.android.highcommunity.activity.fragment.TenementBillFrag_;
import cn.hi028.android.highcommunity.activity.fragment.UserInfoFrag;
import cn.hi028.android.highcommunity.activity.fragment.UserInfoFrag_;
import cn.hi028.android.highcommunity.activity.fragment.WalletSwitchFrag;
import cn.hi028.android.highcommunity.activity.fragment.WalletSwitchFrag_;
import cn.hi028.android.highcommunity.utils.Constacts;

/**
 * @????????????????????????????????????????????????<br> ????????????????????????????????????????????????????????????   ??????????????????????????????
 * @????????? ??????<br>
 * @?????????1.0<br>
 * @?????????2015/12/28<br>
 */
@EActivity(resName = "act_menu_left")
public class MenuLeftAct extends BaseFragmentActivity {
    public static final String ACTIVITYTAG = "MenuLeftAct";
    public static final String INTENTTAG = "MenuLeftActIntent";
    public static final String Tag = "MenuLeftAct:";
    @ViewById(R.id.tv_secondtitle_name)
    TextView mTitle;
    @ViewById(R.id.title_menuleft_layout)
    View mTitleLayout;
    @ViewById(R.id.title_secondTitle_Hight)
    View mHight;
    @ViewById(R.id.tv_right_name)
    TextView tv_right_name;
    NewHuiGdCarFrag mGdCarment;
    FragmentManager fm;
    @AfterViews
    void initView() {
        Log.d(Tag, "------MenuLeftAct");
        if (!super.isVersionBiger()) {
            mHight.setVisibility(View.GONE);
        }
        int flag = getIntent().getIntExtra(ACTIVITYTAG, -1);
        int intentPage = getIntent().getIntExtra(INTENTTAG, -1);
        if (-1 == flag)
            return;
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (flag) {
            case Constacts.MENU_LEFT_USERINFO:
                mTitleLayout.setVisibility(View.GONE);
                UserInfoFrag mPersonal = (UserInfoFrag) new UserInfoFrag_();
                ft.replace(R.id.ll_menuleft_layout, mPersonal, PersonalAuthFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_LEFT_TOPIC:
                mTitle.setText("????????????");
                Log.d(Tag, "------goto???????????????");
                MyTopicFrag mMyTopicFrag = new MyTopicFrag();
                ft.replace(R.id.ll_menuleft_layout, mMyTopicFrag, MyTopicFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_LEFT_COLLECTION:
                mTitle.setText("????????????");
                Log.d(Tag, "------goto????????????");
                MyCollectionSwitchFrag mCollection = (MyCollectionSwitchFrag) new MyCollectionSwitchFrag_();
                ft.replace(R.id.ll_menuleft_layout, mCollection, MyCollectionSwitchFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_LEFT_WALLET:
                mTitle.setText("????????????");
                Log.d(Tag, "------goto????????????");
                WalletSwitchFrag mSwitch = (WalletSwitchFrag) new WalletSwitchFrag_();
                ft.replace(R.id.ll_menuleft_layout, mSwitch, WalletSwitchFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_LEFT_GDCAR://?????????
                mTitle.setText("?????????");
                Log.d(Tag, "------goto?????????");
                tv_right_name.setVisibility(View.VISIBLE);
                mGdCarment = new NewHuiGdCarFrag();
                ft.replace(R.id.ll_menuleft_layout, mGdCarment, ServicePaymentFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_LEFT_BILL:
                mTitle.setText("????????????");
                Log.d(Tag, "------goto????????????");
                TenementBillFrag mBill = (TenementBillFrag) new TenementBillFrag_();
                ft.replace(R.id.ll_menuleft_layout, mBill, TenementBillFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_LEFT_ORDER:
                mTitle.setText("????????????");
                Log.d(Tag, "------goto????????????");
                NewHuiOrderFrag mOrder = (NewHuiOrderFrag) new NewHuiOrderFrag_();
                if (intentPage != -1) {
                }
                ft.replace(R.id.ll_menuleft_layout, mOrder, NewHuiOrderFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_LEFT_ZHONGCOU:
                mTitle.setText("????????????");
                Log.d(Tag, "------goto????????????");
                HuiChipsOrderFrag mChipsOrder = (HuiChipsOrderFrag) new HuiChipsOrderFrag_();
                ft.replace(R.id.ll_menuleft_layout, mChipsOrder, HuiChipsOrderFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_LEFT_CARFTS:
                mTitle.setText("????????????");
                Log.d(Tag, "------goto????????????");
                MyCarftsFrag mCarfts = (MyCarftsFrag) new MyCarftsFrag_();
                ft.replace(R.id.ll_menuleft_layout, mCarfts, MyCarftsFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_LEFT_SETTING:
                mTitle.setText("??????");
                Log.d(Tag, "------goto??????");
                SettingFrag mSetting = (SettingFrag) new SettingFrag_();
                ft.replace(R.id.ll_menuleft_layout, mSetting, SettingFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_LEFT_MESSAGECENTER:
                mTitle.setText("????????????");
                Log.d(Tag, "------goto??????");
                MessageCenterFrag mMessage = (MessageCenterFrag) new MessageCenterFrag_();

                ft.replace(R.id.ll_menuleft_layout, mMessage, MessageCenterFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_MYMESSAGE:
                mTitle.setText("????????????");
                Log.d(Tag, "------goto????????????");
                MyMessageFrag mMyMessage = (MyMessageFrag) new MyMessageFrag();

                ft.replace(R.id.ll_menuleft_layout, mMyMessage, MyMessageFrag.FRAGMENTTAG);
                break;
            case Constacts.MENU_SYSMESSAGE:
                mTitle.setText("????????????");
                Log.d(Tag, "------goto????????????");
                SysMessageFrag mSysMessage = (SysMessageFrag) new SysMessageFrag();
                Log.d(Tag, "------goto????????????2");

                ft.replace(R.id.ll_menuleft_layout, mSysMessage, SysMessageFrag.FRAGMENTTAG);
                break;
        }
        ft.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0x22) {
            PersonalAuthFrag mFrag = (PersonalAuthFrag)
                    getSupportFragmentManager().findFragmentByTag(PersonalAuthFrag.FRAGMENTTAG);
            if (mFrag != null) {
                mFrag.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        NewHuiOrderFrag mNewHuiOrderFrag = (NewHuiOrderFrag) getSupportFragmentManager()
                .findFragmentByTag(NewHuiOrderFrag.FRAGMENTTAG);
        MyCollectionSwitchFrag mCollection = (MyCollectionSwitchFrag) getSupportFragmentManager()
                .findFragmentByTag(MyCollectionSwitchFrag.FRAGMENTTAG);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mNewHuiOrderFrag != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("actFlag", 0x66);
                startActivity(intent);
                MenuLeftAct.this.finish();
                return true;
            } else {
            }
            if (mCollection != null && mCollection.onKeyDown()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Click(R.id.img_back)
    void back() {
        NewHuiOrderFrag mNewHuiOrderFrag = (NewHuiOrderFrag) fm.findFragmentByTag(NewHuiOrderFrag.FRAGMENTTAG);
        if (mNewHuiOrderFrag != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("actFlag", 0x66);
            startActivity(intent);
            MenuLeftAct.this.finish();
        } else {
            this.finish();
        }

    }

    @Click(R.id.tv_right_name)
    public void onRight() {
        if (tv_right_name.getText().toString().trim().equals("??????")) {
            mGdCarment.btn_pay.setSelected(true);
            tv_right_name.setText("??????");
        } else {
            mGdCarment.btn_pay.setSelected(false);
            tv_right_name.setText("??????");
        }
        mGdCarment.adapter.notifyDataSetChanged();
    }
}
