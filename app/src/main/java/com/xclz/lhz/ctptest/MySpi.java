package com.xclz.lhz.ctptest;

import android.os.Handler;
import com.sfit.ctp.thostmduserapi.CThostFtdcMdSpi;
import com.sfit.ctp.thostmduserapi.CThostFtdcRspInfoField;
import com.sfit.ctp.thostmduserapi.CThostFtdcSpecificInstrumentField;
import com.sfit.ctp.thostmduserapi.CThostFtdcDepthMarketDataField;

public class MySpi extends CThostFtdcMdSpi{
    private Handler mHandler;
    private MainActivity mActivity;
    
    public MySpi(Handler handler, MainActivity activity) {
        super();
        mHandler = handler;
        mActivity = activity;
    }

    @Override
    public void OnFrontConnected() {
        // TODO: Implement this method
        mHandler.sendEmptyMessage(Globals.FRONT_CONNECT);
    }

    @Override
    public void OnFrontDisconnected(int nReason) {
        // TODO: Implement this method
        mHandler.sendMessage(mHandler.obtainMessage(Globals.FRONT_DISCONNECT, nReason, 0));
    }

    @Override
    public void OnRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        // TODO: Implement this method
        super.OnRspSubMarketData(pSpecificInstrument, pRspInfo, nRequestID, bIsLast);
        mHandler.sendMessage(mHandler.obtainMessage(Globals.SMDATA_RECEIVED, pRspInfo));
    }

    @Override
    public void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
        // TODO: Implement this method
        super.OnRtnDepthMarketData(pDepthMarketData);
        mHandler.sendMessage(mHandler.obtainMessage(Globals.DMDATA_RECEIVED, pDepthMarketData));
    }
    
}
