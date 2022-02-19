/***************************************************************************
 * Copyright (c) by raythinks.com, Inc. All Rights Reserved
 **************************************************************************/

package cn.hi028.android.highcommunity.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.don.tools.BpiHttpHandler;
import com.don.tools.BpiUniveralImage;
import com.don.tools.GeneratedClassUtils;
import com.don.view.CircleImageView;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import net.duohuo.dhroid.util.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

import cn.hi028.android.highcommunity.HighCommunityApplication;
import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.activity.ActiveAct;
import cn.hi028.android.highcommunity.activity.BaseFragmentActivity;
import cn.hi028.android.highcommunity.activity.CommunityDetailAct;
import cn.hi028.android.highcommunity.activity.GroupDataAct;
import cn.hi028.android.highcommunity.activity.MenuLeftAct;
import cn.hi028.android.highcommunity.activity.fragment.CommunityDetilsFrag;
import cn.hi028.android.highcommunity.activity.fragment.CommunityFrag;
import cn.hi028.android.highcommunity.bean.CommunityBean;
import cn.hi028.android.highcommunity.bean.CommunityListBean;
import cn.hi028.android.highcommunity.bean.OperateBean;
import cn.hi028.android.highcommunity.utils.Constacts;
import cn.hi028.android.highcommunity.utils.HTTPHelper;
import cn.hi028.android.highcommunity.utils.HighCommunityUtils;
import cn.hi028.android.highcommunity.utils.TimeUtil;

/**
 * @功能：社区列表<br>
 * @作者： 赵海<br>
 * @版本：1.0<br>
 * @时间：2015-12-08<br>
 */
public class CommunityAdapter extends BaseFragmentAdapter {
    BaseFragmentActivity mContext;
    List<CommunityBean> mList = new ArrayList<CommunityBean>();
    CommunityListBean mBean;
    private PopupWindow mWindow, mWaitingWindow;

    public CommunityAdapter(BaseFragmentActivity mContext) {
        this.mContext = mContext;
        String SaveData = HighCommunityApplication.share.getString(HighCommunityApplication.mUserInfo.getId() + CommunityFrag.FRAGMENTTAG, "");
        if (!TextUtils.isEmpty(SaveData)) {
            mBean = HTTPHelper.gson.fromJson(SaveData, CommunityListBean.class);
            mList = mBean.getData();
        } else {
            mBean = new CommunityListBean();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CommunityBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_community, null);
            mViewHolder.mAvatar = (CircleImageView) convertView
                    .findViewById(R.id.civl_communityItem_avatar);
            mViewHolder.mName = (TextView) convertView
                    .findViewById(R.id.tv_communityItem_name);
            mViewHolder.mSex = (TextView) convertView
                    .findViewById(R.id.tv_communityItem_sex);
            mViewHolder.mMore = (ImageView) convertView
                    .findViewById(R.id.iv_communityItem_more);
            mViewHolder.mFrom = (TextView) convertView
                    .findViewById(R.id.tv_communityItem_from);
            mViewHolder.mContent = (TextView) convertView
                    .findViewById(R.id.tv_communityItem_content);
            mViewHolder.mTime = (TextView) convertView
                    .findViewById(R.id.tv_communityItem_time);
            mViewHolder.mLocation = (TextView) convertView
                    .findViewById(R.id.tv_communityItem_location);
            mViewHolder.mComment = (TextView) convertView
                    .findViewById(R.id.tv_communityItem_comment);
            mViewHolder.mAssist = (TextView) convertView
                    .findViewById(R.id.tv_communityItem_Assist);

            mViewHolder.tv_community = (TextView) convertView
                    .findViewById(R.id.tv_community);
            mViewHolder.img_community_pic = (ImageView) convertView
                    .findViewById(R.id.img_community_pic);
            mViewHolder.ll_act = (LinearLayout) convertView
                    .findViewById(R.id.ll_act);
            mViewHolder.ll_comm_loc = (LinearLayout) convertView
                    .findViewById(R.id.ll_comm_loc);
            mViewHolder.mGridView = (PullToRefreshGridView) convertView
                    .findViewById(R.id.ptrgv_communityItem_piclistView);
            mViewHolder.mImageAdapter = new CommunityImgGridAdapter(mContext);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final CommunityBean mBean = mList.get(position);
        mViewHolder.mAssist.setSelected(mBean.isPra());
        if (mBean.getType() == 2 || mBean.getType() == 3) {
            mViewHolder.ll_comm_loc.setVisibility(View.GONE);
            mViewHolder.mMore.setVisibility(View.INVISIBLE);
            mViewHolder.mGridView.setVisibility(View.GONE);
            SpannableString spanString = new SpannableString("     " + mBean.getLabel());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), 5, 5 + mBean.getLabel().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            mViewHolder.mContent.setText(spanString);
            mViewHolder.ll_act.setVisibility(View.VISIBLE);
            mViewHolder.mContent.append("  " + mBean.getWelcome());
            mViewHolder.tv_community.setText(mBean.getContent());
            ImageLoaderUtil.disPlay(Constacts.IMAGEHTTP + mBean.getAg_pic(), mViewHolder.img_community_pic);
            mViewHolder.mFrom.setVisibility(View.VISIBLE);
            mViewHolder.mFrom.setText("" + mBean.getTitle());

        } else {
            mViewHolder.ll_act.setVisibility(View.GONE);
            mViewHolder.mGridView.setVisibility(View.VISIBLE);
            mViewHolder.mMore.setVisibility(View.VISIBLE);
            mViewHolder.mImageAdapter.AddNewData(mBean.getPic());
            mViewHolder.mGridView.setAdapter(mViewHolder.mImageAdapter);
            HighCommunityUtils.GetInstantiation().setGridViewHeightBasedOnChildren(mViewHolder.mGridView, mViewHolder.mImageAdapter, 3);
            mViewHolder.mGridView.setClickable(false);
            mViewHolder.mLocation.setText(mBean.getSite());
            mViewHolder.mComment.setText(mBean.getD_count() + " 评论");
            mViewHolder.mAssist.setText(mBean.getP_count() + " 点赞");
            mViewHolder.ll_comm_loc.setVisibility(View.VISIBLE);
            SpannableString spanString = new SpannableString("     " + mBean.getTitle());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), 5, 5 + mBean.getTitle().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            mViewHolder.mContent.setText(spanString);
            mViewHolder.mContent.append("  " + mBean.getContent());
            if (!TextUtils.isEmpty(mBean.getG_name())) {
                mViewHolder.mFrom.setVisibility(View.VISIBLE);
                mViewHolder.mFrom.setText("来自" + mBean.getG_name());
            } else {
                mViewHolder.mFrom.setVisibility(View.GONE);
            }
        }
        if (mBean.getHead_pic() == null || mBean.getHead_pic().equals("")) {
            BpiUniveralImage.displayImage("drawable://" + R.mipmap.defult_avatar, mViewHolder.mAvatar);
        } else {
            BpiUniveralImage.displayImage(Constacts.IMAGEHTTP + mBean.getHead_pic(), mViewHolder.mAvatar);

        }
        mViewHolder.mName.setText(mBean.getNick());
        mViewHolder.mTime.setText(TimeUtil.getDescriptionTimeFromTimestamp(Long.parseLong(mBean.getCreate_time())));

        if (mBean.getSex() == 0) {
            mViewHolder.mSex.setSelected(false);
        } else {
            mViewHolder.mSex.setSelected(true);
        }
        mViewHolder.mSex.setText(mBean.getAge() + "");

        mViewHolder.mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(HighCommunityApplication.mUserInfo.getToken())) {
                    HighCommunityUtils.GetInstantiation().ShowToast("请登录后操作", 0);
                    return;
                }
                mWaitingWindow = HighCommunityUtils.GetInstantiation().ShowWaittingPopupWindow(mContext, mViewHolder.mAssist, Gravity.CENTER);
                HTTPHelper.GetPoerate(new BpiHttpHandler.IBpiHttpHandler() {
                    @Override
                    public void onError(int id, String message) {
                        if (mWaitingWindow != null && mWaitingWindow.isShowing())
                            mWaitingWindow.dismiss();
                    }

                    @Override
                    public void onSuccess(Object message) {
                        if (mWaitingWindow != null && mWaitingWindow.isShowing())
                            mWaitingWindow.dismiss();
                        if (message == null)
                            return;
                        final OperateBean mopeate = (OperateBean) message;
                        mWindow = HighCommunityUtils.GetInstantiation().ShowCommunityShare(mContext, mopeate, mBean.getMid(), mViewHolder.mAssist, new HighCommunityUtils.OnDeleteClick() {
                            @Override
                            public void OnClick() {
                                mList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                        mWindow.showAtLocation(mViewHolder.mAssist, Gravity.BOTTOM, 0, HighCommunityApplication.SoftKeyHight);
                    }

                    @Override
                    public Object onResolve(String result) {
                        return HTTPHelper.ResolveOperateBean(result);
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
                            HighCommunityApplication.toLoginAgain(mContext);
                        }
                    }
                }, HighCommunityApplication.mUserInfo.getId() + "", mBean.getMid() + "");
            }
        });
        mViewHolder.mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(HighCommunityApplication.mUserInfo.getToken())) {
                    HighCommunityUtils.GetInstantiation().ShowShouldLogin();
                    return;
                }
                Intent mCommunity = new Intent(mContext, GeneratedClassUtils.get(CommunityDetailAct.class));
                mCommunity.putExtra(CommunityDetailAct.ACTIVITYTAG, "Details");
                mCommunity.putExtra(CommunityDetailAct.INTENTTAG, mBean.getMid());
                mCommunity.putExtra(CommunityDetilsFrag.FRAGMENTTAG, true);
                mContext.startActivity(mCommunity);
            }
        });
        mViewHolder.mAssist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(HighCommunityApplication.mUserInfo.getToken())) {
                    HighCommunityUtils.GetInstantiation().ShowShouldLogin();
                    return;
                }
                HTTPHelper.AssistMessage(new BpiHttpHandler.IBpiHttpHandler() {
                    @Override
                    public void onError(int id, String message) {
                        HighCommunityUtils.GetInstantiation().ShowToast(message, 0);
                    }

                    @Override
                    public void onSuccess(Object message) {
                        mBean.setP_count(mBean.getP_count() + 1);
                        mBean.setPra(true);
                        notifyDataSetChanged();
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
                            HighCommunityApplication.toLoginAgain(mContext);
                        }
                    }
                }, HighCommunityApplication.mUserInfo.getId() + "", mBean.getMid() + "");
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBean.getType() == 2) {
                    Intent ActCreate = new Intent(mContext, GeneratedClassUtils.get(ActiveAct.class));
                    ActCreate.putExtra(ActiveAct.ACTIVITYTAG, Constacts.ACTIVITY_DETAILS);
                    ActCreate.putExtra(ActiveAct.INTENTTAG, mBean.getId() + "");
                    mContext.startActivity(ActCreate);

                } else if (mBean.getType() == 3) {
                    Intent mInt = new Intent(mContext, GeneratedClassUtils.get(GroupDataAct.class));
                    mInt.putExtra(GroupDataAct.ACTIVITYTAG, "Detils");
                    mInt.putExtra(GroupDataAct.INTENTTAG, mBean.getId() + "");
                    mContext.startActivity(mInt);

                } else {
                    Intent mCommunity = new Intent(mContext, GeneratedClassUtils.get(CommunityDetailAct.class));
                    mCommunity.putExtra(CommunityDetailAct.ACTIVITYTAG, "Details");
                    mCommunity.putExtra(CommunityDetailAct.INTENTTAG, mBean.getMid());
                    mContext.startActivity(mCommunity);
                }
            }
        });
        mViewHolder.mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mDetails = new Intent(mContext, GeneratedClassUtils.get(MenuLeftAct.class));
                mDetails.putExtra(MenuLeftAct.ACTIVITYTAG, Constacts.MENU_LEFT_USERINFO);
                mDetails.putExtra(MenuLeftAct.INTENTTAG, mBean.getUid() + "");
                mContext.startActivity(mDetails);
            }
        });
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mBean = new CommunityListBean();
        mBean.setData(mList);
        HighCommunityApplication.share.edit()
                .putString(HighCommunityApplication.mUserInfo.getId() + CommunityFrag.FRAGMENTTAG, HTTPHelper.gson.toJson(mBean)).commit();
    }

    public boolean onKeyDown() {
        if (mWindow != null && mWindow.isShowing()) {
            mWindow.dismiss();
            return true;
        }
        return false;
    }

    private class ViewHolder {
        CircleImageView mAvatar;
        TextView mName;
        TextView mSex;
        TextView mFrom;
        TextView mTime;
        ImageView mMore;
        TextView mContent;
        TextView mLocation;
        TextView mComment;
        TextView mAssist;
        TextView tv_community;
        LinearLayout ll_act, ll_comm_loc;
        ImageView img_community_pic;
        PullToRefreshGridView mGridView;
        CommunityImgGridAdapter mImageAdapter;
    }

    @Override
    public void RefreshData(Object mObject) {
        if (mObject instanceof List<?>)
            mList.addAll((List<CommunityBean>) mObject);
        notifyDataSetChanged();
        super.RefreshData(mObject);
    }

    public void SetData(Object mObject) {
        if (mObject instanceof List<?>) {
            mList.clear();
            mList = (List<CommunityBean>) mObject;
            notifyDataSetChanged();
        }
    }

    public void ClearData() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 用来添加新数据
     *
     * @param mObject
     */
    @Override
    public void AddNewData(Object mObject) {
        List<CommunityBean> temp;
        if (mObject instanceof List<?>) {
            temp = (List<CommunityBean>) mObject;
            if (mList.size() == 0) {
                mList = temp;
            } else {
                for (int i = temp.size(); i > 0; i--) {
                    mList.add(0, temp.get(i - 1));
                }
            }
        }
        notifyDataSetChanged();
    }
}
