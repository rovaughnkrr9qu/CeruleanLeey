//package net.duohuo.dhroid.activity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.webkit.ValueCallback;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.don.tools.Debug;
//import com.don.tools.GeneratedClassUtils;
//import com.tamic.jswebview.browse.CallBackFunction;
//import com.tamic.jswebview.browse.JsWeb.CustomWebViewClient;
//import com.tamic.jswebview.browse.JsWeb.JavaCallHandler;
//import com.tamic.jswebview.browse.JsWeb.JsHandler;
//import com.tamic.jswebview.view.ProgressBarWebView;
//
//import net.duohuo.dhroid.util.ParamsContacts;
//
//import org.androidannotations.annotations.Click;
//
//import java.util.ArrayList;
//import java.util.Map;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.hi028.android.highcommunity.R;
//import cn.hi028.android.highcommunity.activity.BaseFragmentActivity;
//
//
//public class BrowseActivity2 extends BaseFragmentActivity {
//    static final String Tag = "BrowseActivity--->";
//    @Bind(R.id.img_back)
//    ImageView img_back;
//    @Bind(R.id.tv_secondtitle_name)
//    TextView tv_title_name;
//    @Bind(R.id.pw_browse)
//    ProgressBarWebView mProgressBarWebView;
//
//    private String url, title;
//    // UI references.
//    private ArrayList<String> mHandlers = new ArrayList<>();
//    ValueCallback<Uri> mUploadMessage;
//    private static CallBackFunction mfunction;
//    int RESULT_CODE = 0;
//
//    public static void toBrowseActivity(Context activity, String title,
//                                        String url) {
//        Intent intent = new Intent(activity, GeneratedClassUtils.get(BrowseActivity2.class));
//        intent.putExtra(ParamsContacts.BROWSE_TITLE, title);
//        intent.putExtra(ParamsContacts.BROWSE_URL, url);
//        activity.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_browse2);
//        ButterKnife.bind(this);
//
////         ????????????
//        url = getIntent().getStringExtra(ParamsContacts.BROWSE_URL);
//        title = getIntent().getStringExtra(ParamsContacts.BROWSE_TITLE);
//        tv_title_name.setText(title);
//        img_back.setVisibility(View.VISIBLE);
//        Log.e(Tag, "url--->" + url);
//        initWebView();
//        Debug.info("Browse url---->", url);
//    }
//
//    private void initWebView() {
//        mProgressBarWebView.setWebViewClient(new CustomWebViewClient(mProgressBarWebView.getWebView()) {
//
//
//            @Override
//            public String onPageError(String url) {
//                //??????????????????????????????????????????
//                return "file:///android_asset/error.html";
//            }
//
//            @Override
//            public Map<String, String> onPageHeaders(String url) {
//
//                // ????????????header
//
//                return null;
//            }
//
//            @SuppressWarnings("unused")
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
//                this.openFileChooser(uploadMsg);
//            }
//
//            @SuppressWarnings("unused")
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
//                this.openFileChooser(uploadMsg);
//            }
//
//            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                mUploadMessage = uploadMsg;
//                pickFile();
//            }
//        });
//
//        // ????????????????????????????????????url
//        mProgressBarWebView.loadUrl("file:///android_asset/demo.html");
//
//        mHandlers.add("login");
//        mHandlers.add("callNative");
//        mHandlers.add("callJs");
//        mHandlers.add("open");
//
//        //??????js?????????
//        mProgressBarWebView.registerHandlers(mHandlers, new JsHandler() {
//            @Override
//            public void OnHandler(String handlerName, String responseData, CallBackFunction function) {
//
//                if (handlerName.equals("login")) {
//
//                    Toast.makeText(BrowseActivity2.this, responseData, Toast.LENGTH_SHORT).show();
//
//
//                } else if (handlerName.equals("callNative")) {
//
//                    Toast.makeText(BrowseActivity2.this, responseData, Toast.LENGTH_SHORT).show();
//
//                    function.onCallBack("????????????");
//
//                } else if (handlerName.equals("callJs")) {
//
//                    Toast.makeText(BrowseActivity2.this, responseData, Toast.LENGTH_SHORT).show();
//
//                    // ????????????????????????
//
//                    function.onCallBack("?????? ?????????????????? ???xxxxxxx");
//
//
//                }
//                if (handlerName.equals("open")) {
//
//                    mfunction = function;
//
//                    pickFile();
//
//                }
//
//            }
//        });
//
//        // ??????js
//
//        mProgressBarWebView.callHandler("callNative", "hello H5, ??????java", new JavaCallHandler() {
//            @Override
//            public void OnHandler(String handlerName, String jsResponseData) {
//                Toast.makeText(BrowseActivity2.this, "h5??????????????????" + jsResponseData, Toast.LENGTH_SHORT).show();
//            }
//        });
//        //???????????????js
//        mProgressBarWebView.send("??????", new CallBackFunction() {
//            @Override
//            public void onCallBack(String data) {
//
//                Toast.makeText(BrowseActivity2.this, data, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//
//    public void pickFile() {
//        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        chooserIntent.setType("image/*");
//        startActivityForResult(chooserIntent, RESULT_CODE);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == RESULT_CODE) {
//            if (null == mUploadMessage) {
//                return;
//            }
//            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
//            mUploadMessage.onReceiveValue(result);
//            mUploadMessage = null;
//
//            mfunction.onCallBack(intent.getData().toString());
//
//        }
//
//    }
//
////
////
////
////    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        if (KeyEvent.KEYCODE_BACK == keyCode && webview.canGoBack()) {
////            Log.e(Tag,"??????goback");
////            webview.goBack();
////            return true;
////        }
////        return super.onKeyDown(keyCode, event);
////    }
//
//    @Click(R.id.img_back)
//    void back() {
//        this.finish();
//    }
//
//
//    @OnClick(R.id.img_back)
//    public void onClick() {
//        this.finish();
//
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mProgressBarWebView.getWebView() != null) {
//            mProgressBarWebView.getWebView().destroy();
//        }
//    }
//}
