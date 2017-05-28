package com.xclz.lhz.ctptest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.sfit.ctp.thostmduserapi.CThostFtdcDepthMarketDataField;
import com.sfit.ctp.thostmduserapi.CThostFtdcMdApi;
import com.sfit.ctp.thostmduserapi.CThostFtdcReqUserLoginField;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspInfoField;

public class MainActivity extends Activity {
    private Handler mHandler;
    private CThostFtdcMdApi mApi;
    private MySpi mSpi;

    private Button loginButton;
    private Button connectButton;
    private TextView textView;

    private String mBrokerId = "9999";
    private String mUserId = "081119";
    private String mPassword = "826117";

    static {
        System.loadLibrary("thostmduserapi");
        System.loadLibrary("thostmduserapi_wrap");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setHandler();
        initComponents();
    }

    private void initComponents() {
        loginButton = (Button) findViewById(R.id.login_button);
        connectButton = (Button) findViewById(R.id.connect_button);
        textView = (TextView) findViewById(R.id.mainTextView);
    }

    public void connectCTP(View v) {
        connectButton.setText("Connecting.");
        String sdpath = Environment.getExternalStorageDirectory().toString() + "/ctplog" + "/";

        mSpi = new MySpi(mHandler, this);
        mApi = CThostFtdcMdApi.CreateFtdcMdApi(sdpath);
        mApi.RegisterSpi(mSpi);
        mApi.RegisterFront("tcp://180.168.146.187:10031");
        mApi.Init();
    }
    public void loginCTP(View v) {
        CThostFtdcReqUserLoginField fields = new CThostFtdcReqUserLoginField();
        fields.setBrokerID(mBrokerId);
        fields.setUserID(mUserId);
        fields.setPassword(mPassword);

        mApi.ReqUserLogin(fields, Globals.getRequestId());
        mApi.SubscribeMarketData(new String[]{"rb1710"}, 1);
    }

    private void setHandler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int id = msg.what;
                switch(id) {
                    case Globals.FRONT_CONNECT:
                        connectButton.setText("Connected");
                        connectButton.setClickable(false);
                        connectButton.setTextColor(0xFFAAAAAA);
                        loginButton.setClickable(true);
                        loginButton.setTextColor(0xFF000000);
                        break;
                    case Globals.FRONT_DISCONNECT:
                        connectButton.setText("Failed");
                        connectButton.setTextColor(0xFFFF5555);
                        toast("DISCONNECTED: " + msg.arg1, 0);
                        break;
                    case Globals.SMDATA_RECEIVED:
                        CThostFtdcRspInfoField error = (CThostFtdcRspInfoField) msg.obj;
                        setTitle("Msg: " + error.getErrorMsg() + " ID:" + error.getErrorID());
                        break;
                    case Globals.DMDATA_RECEIVED:
                        CThostFtdcDepthMarketDataField fields = (CThostFtdcDepthMarketDataField) msg.obj;
                        String text = "InstrumentID:" + fields.getInstrumentID() + "\n"
                                    + "ActionDay:" + fields.getActionDay() + "\n" 
                                    + "AskPrice1:" + fields.getAskPrice1() + "   " 
                                    + "AskPrice2:" + fields.getAskPrice2() + "\n"
                                    + "AskPrice3:" + fields.getAskPrice3() + "   "
                                    + "AskPrice4:" + fields.getAskPrice4() + "\n"
                                    + "AskPrice5:" + fields.getAskPrice5() + "   "
                                    + "AskVolume1:" + fields.getAskVolume1() + "\n"
                                    + "AskVolume2:" + fields.getAskVolume2() + "   "
                                    + "AskVolume3:" + fields.getAskVolume3() + "\n"
                                    + "AskVolume4:" + fields.getAskVolume4() + "   "
                                    + "AskVolume5:" + fields.getAskVolume5() + "\n";
                        textView.setText(text);
                        break;
                }
            }
        };
    }

    public void toast(String text, int d) {
        Toast.makeText(this, text, d).show();
    }
}
