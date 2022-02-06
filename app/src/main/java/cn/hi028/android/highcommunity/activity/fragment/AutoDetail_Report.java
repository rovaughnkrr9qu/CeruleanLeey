package cn.hi028.android.highcommunity.activity.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.don.tools.BpiHttpHandler;
import com.don.view.CircleImageView;

import net.duohuo.dhroid.activity.BaseFragment;
import net.duohuo.dhroid.util.ImageLoaderUtil;
import net.duohuo.dhroid.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.hi028.android.highcommunity.HighCommunityApplication;
import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.adapter.Auto_ReportDetailAdapter;
import cn.hi028.android.highcommunity.bean.Autonomous.Auto_ReportDetailBean;
import cn.hi028.android.highcommunity.utils.Constacts;
import cn.hi028.android.highcommunity.utils.HTTPHelper;
import cn.hi028.android.highcommunity.utils.HighCommunityUtils;
import cn.hi028.android.highcommunity.utils.TimeUtil;

/**
 * @功能：自治大厅 汇报详情Frag<br>
 * @作者： Lee_yting<br>
 * @时间：2016/10/11<br>
 */

public class AutoDetail_Report extends BaseFragment {
    public static final String Tag = "~~~AutoDetail_Report~~~";
    public static final String FRAGMENTTAG = "AutoDetail_Report";
    @Bind(R.id.reportdetail_spokerImage)
    CircleImageView mSpeakerImage;
    @Bind(R.id.reportdetail_spokerContent)
    EditText mSpeakerContent;
    @Bind(R.id.reportdetail_spokerButton)
    ImageView mSpeakerButton;
    @Bind(R.id.reportdetail_commentListview)
    ListView mCommentListview;
    @Bind(R.id.pg_progress)
    ProgressBar mProgress;
    @Bind(R.id.auto_reportDetails_noData)
    TextView mNoData;
    List<Auto_ReportDetailBean.ReportDetailDataEntity.ReportDetailReplyEntity> mList;
    @Bind(R.id.reportdetail_spokerLayout)
    LinearLayout mSpokerLayout;
    @Bind(R.id.loading_message)
    TextView loadingMessage;
    Auto_ReportDetailBean.ReportDetailDataEntity mBean = new Auto_ReportDetailBean.ReportDetailDataEntity();
    public boolean isReplay = false;
    /**
     * 被评论人id
     ***/
    String toid = null;
    /**
     * 评论id   对监督评论传0，对评论回复传id
     ****/
    String ParentId = null;
    /**
     * 监督id  可以intent得到 且data 数组返回
     **/
    String watch_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d(Tag + "onCreateView");
        View view = inflater.inflate(R.layout.frag_auto_detail_report, null);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        watch_id = bundle.getString("reportDetail_id");
        Log.d(Tag, "reportDetail_id:" + watch_id);
//        watch_id = getActivity().getIntent().getStringExtra("reportDetail_id");

        initView();
        return view;
    }

    Auto_ReportDetailAdapter mAdapter;

    String content;

    private void initView() {
        LogUtil.d(Tag + "initView");
        mProgress.setVisibility(View.VISIBLE);
        mCommentListview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mList = new ArrayList<Auto_ReportDetailBean.ReportDetailDataEntity.ReportDetailReplyEntity>();
        mAdapter = new Auto_ReportDetailAdapter(mList, getActivity(), this);
        initHeader();
        mCommentListview.setAdapter(mAdapter);
        if (HighCommunityApplication.mUserInfo.getId() == 0) {
            mSpokerLayout.setVisibility(View.GONE);
        } else {
            mSpokerLayout.setVisibility(View.VISIBLE);
            ImageLoaderUtil.disPlay(Constacts.IMAGEHTTP + HighCommunityApplication.mUserInfo.getHead_pic(), mSpeakerImage);
        }
        if (watch_id != null) {
            initDatas();
        }
        boolean flag = getActivity().getIntent().getBooleanExtra(FRAGMENTTAG, false);
        if (flag) {
            InputMethodManager mManager = (InputMethodManager) mSpeakerContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mManager.showSoftInput(mSpeakerContent, InputMethodManager.SHOW_FORCED);
        }
        initSpoker();
        mSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(HighCommunityApplication.mUserInfo.getToken())) {
                    LogUtil.d("");

                    HighCommunityUtils.GetInstantiation().ShowShouldLogin();
                    return;
                }
                content = mSpeakerContent.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    HTTPHelper.CommentReportDetail(mCommentIbpi, HighCommunityApplication.mUserInfo.getOwner_id(), toid, watch_id, ParentId, content);
                } else {
                    HighCommunityUtils.GetInstantiation().ShowToast("请输入内容", 0);
                }
            }
        });
    }

    TextView mTitle, mReportorName, mReportTime, mContent;
    LinearLayout mInforLayout;


    public void setText(String text, String to_id, String parentId, boolean isReplay) {
        this.isReplay = isReplay;
        mSpeakerContent.setHint(text);
        toid = to_id;
        ParentId = parentId;
        InputMethodManager mManager = (InputMethodManager) mSpeakerContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mManager.showSoftInput(mSpeakerContent, 0);
    }

    private void initDatas() {
        HTTPHelper.GetReportDetail(mIbpi, watch_id);
    }

    BpiHttpHandler.IBpiHttpHandler mIbpi = new BpiHttpHandler.IBpiHttpHandler() {
        @Override
        public void onError(int id, String message) {
            LogUtil.d(Tag + "---~~~onError");
            HighCommunityUtils.GetInstantiation().ShowToast(message, 0);
        }

        @Override
        public void onSuccess(Object message) {
            if (message == null) {
                return;
            }
            mBean = (Auto_ReportDetailBean.ReportDetailDataEntity) message;
            setHeadData();
            mList = mBean.getReply();
            mAdapter.AddNewData(mList);
            mCommentListview.setAdapter(mAdapter);
//            mList = (List<Auto_NoticeListBean.NoticeListDataEntity>) message;
//            mAdapter.AddNewData(mList);
//            mListview.setAdapter(mAdapter);


        }

        @Override
        public Object onResolve(String result) {
            LogUtil.d(Tag + " ~~~result" + result);
            return HTTPHelper.ResolveReportDetailDataEntity(result);
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
                HighCommunityApplication.toLoginAgain(getActivity());
            }
        }
    };

    private void setHeadData() {
        if (mBean.getTitle() == null || mBean.getTitle() == "") {
            mInforLayout.setVisibility(View.GONE);
        } else {
            mTitle.setText(mBean.getTitle());
            mReportorName.setText("汇报人:" + mBean.getName());
            mReportTime.setText(TimeUtil.getDayTime(Long.parseLong(mBean.getCreate_time())));
            String url = mBean.getContent();
            CharSequence charSequence = Html.fromHtml(url);
            mContent.setText("    " + charSequence);
        }


    }

    private void initHeader() {
        LinearLayout header = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.head_auto_report_detail, null);
        mTitle = (TextView) header.findViewById(R.id.reportdetail_title);
        mReportorName = (TextView) header.findViewById(R.id.reportdetail_name);
        mReportTime = (TextView) header.findViewById(R.id.reportdetail_time);
        mContent = (TextView) header.findViewById(R.id.reportdetail_content);
        mInforLayout = (LinearLayout) header.findViewById(R.id.inforLayout);
        mCommentListview.addHeaderView(header,null,false);
    }

    BpiHttpHandler.IBpiHttpHandler mCommentIbpi = new BpiHttpHandler.IBpiHttpHandler() {
        @Override
        public void onError(int id, String message) {

            HighCommunityUtils.GetInstantiation().ShowToast(message.toString(), 0);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSuccess(Object message) {
            HighCommunityUtils.GetInstantiation().ShowToast(message.toString(), 0);
            if (getActivity().getCurrentFocus() != null && getActivity().getCurrentFocus().getWindowToken() != null) {
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
//            if (isReplay) {
//                mAdapter.setNewData(isReplay, content, null);
//            } else {
//                mAdapter.setNewData(isReplay, content, message.toString());
//            }
            initSpoker();
            initDatas();
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
                HighCommunityApplication.toLoginAgain(getActivity());
            }
        }
    };

    private void initSpoker() {
//        CommunityFrag.isNeedRefresh = true;
        this.isReplay = false;
        mSpeakerContent.setHint("");
//对监督的评论
        toid = "0";
        ParentId = "0";
        mSpeakerContent.setText("");
    }

    public void finish() {
//        Intent result = new Intent();
//        result.putExtra("PinLun", mPraisesNum);
//        getActivity().setResult(getActivity().RESULT_OK, result);
    }


    /**********
     * --------------------------------------------------------------------
     **********/

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d(Tag + "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(Tag + "onResume");
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }




}
