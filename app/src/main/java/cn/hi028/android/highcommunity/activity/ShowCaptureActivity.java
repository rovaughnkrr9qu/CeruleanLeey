package cn.hi028.android.highcommunity.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.don.tools.BpiHttpHandler;
import com.don.tools.BpiUniveralImage;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hi028.android.highcommunity.HighCommunityApplication;
import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.bean.ActGoodsBean;
import cn.hi028.android.highcommunity.bean.WpayBean;
import cn.hi028.android.highcommunity.utils.HTTPHelper;
import cn.hi028.android.highcommunity.utils.HighCommunityUtils;
import cn.hi028.android.highcommunity.utils.alipay.AlipayUtils;
import cn.hi028.android.highcommunity.utils.alipay.PayResult;
import cn.hi028.android.highcommunity.utils.wchatpay.WchatPayUtils;
import cn.hi028.android.highcommunity.view.ECAlertDialog;
import cn.hi028.android.highcommunity.wxapi.WXPayEntryActivity;
import sun.misc.BASE64Decoder;

public class ShowCaptureActivity extends BaseFragmentActivity {
static final String Tag="ShowCaptureActivity:";
    @Bind(R.id.tv)
    TextView tv;
    String captureStr;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_secondtitle_name)
    TextView mTitle;
    @Bind(R.id.img_goods_pic)
    ImageView imgGoodsPic;
    @Bind(R.id.tv_goods_name)
    TextView tvGoodsName;
    @Bind(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @Bind(R.id.tv_goods_total)
    TextView tvGoodsTotal;
    @Bind(R.id.tv_goods_reduce)
    TextView tvGoodsReduce;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.tv_goods_add)
    TextView tvGoodsAdd;
    @Bind(R.id.ll_goods_num)
    LinearLayout llGoodsNum;
    @Bind(R.id.rb_pay_wx)
    RadioButton rbPayWx;
    @Bind(R.id.rb_pay_ipay)
    RadioButton rbPayIpay;
    @Bind(R.id.rg_huil_ife)
    RadioGroup rgHuilIfe;
    int payType = -1;
    PopupWindow waitPop = null;

    static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcapture);
        ButterKnife.bind(this);
        act=this;
        captureStr = getIntent().getStringExtra("result");
        tv.setVisibility(View.GONE);
        Log.e(Tag,"captureStr---"+captureStr.toString());
        String s=decode(captureStr);
        Log.e(Tag,"decode---"+s.toString());
        mTitle.setText("????????????");
        tv.setText(s);
        WchatPayUtils.getInstance().init(ShowCaptureActivity.this);
        handlerCaptureStr(s);
        initView();

    }

    public static String getFromBASE64(String s) {
        if (s == null) return null;
        sun.misc.BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }
    // ??????
    public String decode(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {

                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    Map mMap;
    private void handlerCaptureStr(String captureStr) {
        if (!captureStr.contains("&")){
          return;
        }
        mMap=new HashMap();
        String[] split2 = captureStr.split("&");
        Log.e(Tag,"2---"+split2.toString()+"---"+split2.length);
        for (int i = 0; i < split2.length; i++) {
            String[] split3 = split2[i].split("=");
            mMap.put(split3[0],split3[1]);
        }
setView(mMap);

    }

    private void setView(Map mMap) {
        tvGoodsName.setText(mMap.get("name")+"");
        tvGoodsPrice.setText("????????????"+mMap.get("price"));
        finaltotalPrice=Float.parseFloat(mMap.get("price").toString());
        tvGoodsTotal.setText("???????????????"+mMap.get("price"));
        if (mMap.get("pic") == null || mMap.get("pic").toString().equals("")) {
            BpiUniveralImage.displayImage("drawable://" + R.mipmap.default_no_pic, imgGoodsPic);
        } else {
            BpiUniveralImage.displayImage(mMap.get("pic").toString(), imgGoodsPic);
        }
        if (mMap.get("limit").toString()!=null&&mMap.get("limit").toString()!=""){

            limit=Integer.parseInt(mMap.get("limit").toString());
        }
    }

    private void initView() {




    }
int limit=-1;

float finaltotalPrice=0;int finalnum=1;
    @OnClick({R.id.img_back, R.id.tv_goods_reduce, R.id.tv_goods_add, R.id.rb_pay_wx, R.id.rb_pay_ipay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                this.finish();
                break;
            case R.id.tv_goods_reduce:
                Log.e(Tag,"Text().toString():"+tvGoodsNum.getText().toString()+",parseInt:"+Integer.parseInt(tvGoodsNum.getText().toString()));
                if (Integer.parseInt(tvGoodsNum.getText().toString()) > 1) {
                    int nowNUm=Integer.parseInt(tvGoodsNum.getText().toString())- 1;
                    finalnum=nowNUm;
                    tvGoodsNum.setText(nowNUm+"");
             float nowTotalPrice=(float)Math.round(nowNUm*Float.parseFloat(mMap.get("price").toString())*100)/100;
                    finaltotalPrice=nowTotalPrice;
                    tvGoodsTotal.setText("???????????????"+nowTotalPrice);
                } else {
                    HighCommunityUtils.GetInstantiation().ShowToast("???????????????0", 0);
                }
                break;
            case R.id.tv_goods_add:
                Log.e(Tag,"Text().toString():"+tvGoodsNum.getText().toString()+",parseInt:"+Integer.parseInt(tvGoodsNum.getText().toString()));
               if (limit==-1){
                   Log.e(Tag,"limit"+limit);
                   limit=3;
               }
                if (Integer.parseInt(tvGoodsNum.getText().toString()) < limit||limit==0) {
                    int nowNUm=Integer.parseInt(tvGoodsNum.getText().toString())+1;
                    finalnum=nowNUm;
                    tvGoodsNum.setText(nowNUm+"");
                    float nowTotalPrice=(float)Math.round(nowNUm*Float.parseFloat(mMap.get("price").toString())*100)/100;
                    finaltotalPrice=nowTotalPrice;
                    tvGoodsTotal.setText("???????????????"+nowTotalPrice);
                } else {
                    HighCommunityUtils.GetInstantiation().ShowToast("??????????????????"+limit+"???", 0);
                }
                break;
            case R.id.rb_pay_wx:
                payType = 1;
                rbPayIpay.setChecked(false);
                rbPayWx.setChecked(true);
                waitPop = HighCommunityUtils.GetInstantiation().ShowWaittingPopupWindow(ShowCaptureActivity.this, rbPayWx, Gravity.CENTER);
               Log.e(Tag,"rb_pay_wx");
                HTTPHelper.submitBuyHotGoodsWX(mIbpiWPaySubOrder,mMap.get("gid").toString(),finalnum+"", finaltotalPrice+"",payType+"");
                break;
            case R.id.rb_pay_ipay:
                payType = 0;
                rbPayIpay.setChecked(true);
                rbPayWx.setChecked(false);
                waitPop = HighCommunityUtils.GetInstantiation().ShowWaittingPopupWindow(ShowCaptureActivity.this, rbPayIpay, Gravity.CENTER);
                HTTPHelper.submitBuyHotGoods(mIbpiOrder,mMap.get("gid").toString(),finalnum+"", finaltotalPrice+"",payType+"");

                break;
        }
    }
    BpiHttpHandler.IBpiHttpHandler mIbpiOrder = new BpiHttpHandler.IBpiHttpHandler() {
        @Override
        public void onError(int id, String message) {
            waitPop.dismiss();
            HighCommunityUtils.GetInstantiation().ShowToast(message, 0);
        }

        @Override
        public void onSuccess(Object message) {
            waitPop.dismiss();
            if (null == message)
                return;
            ActGoodsBean.ActGoodsDataEntity mBean = (ActGoodsBean.ActGoodsDataEntity) message;
            if (payType==-1)return;
            if (payType == 0) {
                AlipayUtils.getInstance().payGoods(ShowCaptureActivity.this, "????????????","   ", mBean.getTotal_fee(), mBean.getOut_trade_no(), "0",
                        mBean.getTicket_value()+"", mBean.getNotify_url(), new AlipayUtils.onPayListener() {

                            @Override
                            public void onPay(PayResult result) {

                                String resultStatus = result.getResultStatus();
                                Log.e(Tag,"resultStatus:"+resultStatus);
                                // ??????resultStatus ??????9000???????????????????????????????????????????????????????????????????????????
                                if (TextUtils.equals(resultStatus, "9000")) {
                                    Toast.makeText(ShowCaptureActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                    showMsgDialog(1);
                                } else {
                                    // ??????resultStatus ??????"9000"???????????????????????????
                                    // "8000"????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                                    if (TextUtils.equals(resultStatus, "8000")) {
                                        Toast.makeText(ShowCaptureActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // ??????????????????????????????????????????????????????????????????????????????????????????????????????
//                                        showMsgDialog(2);
                                            Toast.makeText(ShowCaptureActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            } else if (payType == 1) {


            }
        }

        @Override
        public Object onResolve(String result) {
            return HTTPHelper.ResolveHotGoodsOrder(result);
        }

        @Override
        public void setAsyncTask(AsyncTask asyncTask) {

        }

        @Override
        public void cancleAsyncTask() {
            waitPop.dismiss();
        }

        @Override
        public void shouldLogin(boolean isShouldLogin) {

        }

        @Override
        public void shouldLoginAgain(boolean isShouldLogin, String msg) {

            if (isShouldLogin){
                HighCommunityUtils.GetInstantiation().ShowToast(msg, 0);
                HighCommunityApplication.toLoginAgain(ShowCaptureActivity.this);
            }
        }
    };
    BpiHttpHandler.IBpiHttpHandler mIbpiWPaySubOrder = new BpiHttpHandler.IBpiHttpHandler() {
        @Override
        public void onError(int id, String message) {
            waitPop.dismiss();
            HighCommunityUtils.GetInstantiation().ShowToast(message, 0);
        }
        @Override
        public void onSuccess(Object message) {
            waitPop.dismiss();
            if (null == message) {
                return;
            }
            WXPayEntryActivity.toFrag = 3;
            WpayBean mBean = (WpayBean) message;
            Log.e(Tag,"WpayBean:"+mBean.toString());
            WchatPayUtils.getInstance().apay(ShowCaptureActivity.this, mBean.getAppid(), mBean.getPartnerid(), mBean.getPrepayid(), mBean.getNoncestr(), mBean.getPackages(), mBean.getSign(), mBean.getTimestamp());

        }

        @Override
        public Object onResolve(String result) {
            return HTTPHelper.ResolveHotGoodsWXOrder(result);
        }

        @Override
        public void setAsyncTask(AsyncTask asyncTask) {

        }

        @Override
        public void cancleAsyncTask() {
            waitPop.dismiss();
        }

        @Override
        public void shouldLogin(boolean isShouldLogin) {

        }

        @Override
        public void shouldLoginAgain(boolean isShouldLogin, String msg) {
            if (isShouldLogin){
                HighCommunityUtils.GetInstantiation().ShowToast(msg, 0);
                HighCommunityApplication.toLoginAgain(ShowCaptureActivity.this);
            }
        }
    };
    public void showMsgDialog(int x) {
        String msg;
        if (x==1){
            msg="????????????";
        }else {
            msg="????????????";
        }
        ECAlertDialog dialog2 = ECAlertDialog.buildAlert(ShowCaptureActivity.this, msg,"????????????", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    ShowCaptureActivity.this.finish();
                }
            });
        dialog2.setTitle("??????");
        dialog2.show();
    }
    public static void finishThisAct() {
        act.finish();
    }
}
