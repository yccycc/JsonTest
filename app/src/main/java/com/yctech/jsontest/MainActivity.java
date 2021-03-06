package com.yctech.jsontest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject("{\"name\":\"ycc\"}");

            String name = jsonObject.getString("name");
            Log.i("name--",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        long tt = System.currentTimeMillis();
        Log.i("bitch--", System.currentTimeMillis() - tt + "");
        executorService.execute(new Runnable() {
            @Override
            public void run() {
              loginUid("http://10.0.3.2:8080/ewrrew/servlet/loginUid","","","");
            }
        });
    }

    /**
     *http://ip:port/eomfront/ loginUid.do? OPTCARDNO=xxx&BUSICARDNO =xxx&M1=xxx
     * @param path
     * @param optCardNo 操作员卡序列号
     * @param busiCardno 业务卡序列号
     * @param devSn    现场服务终端序列号
     */
    public String loginUid(String path,String optCardNo,String busiCardno,String devSn)
    {
        StringBuffer params = new StringBuffer();
        params.append("OPTCARDNO").append("=").append(optCardNo).append("&")
                .append("BUSICARDNO").append("=").append(busiCardno).append("&")
                .append("DEV_SN").append("=").append(devSn);
        return getResponseStr(path,params);
    }

    /**
     * http://ip:port/eomfront/ loginIdauth.do? UID =xxx&OPTCODE=xxx&M=xxx&MS=xxx
     * @param path
     * @param uid
     * @param optCode
     * @param m
     * @param ms
     * @return
     */
    public String loginIdauth(String path,String uid,String optCode,String m,String ms)
    {
        StringBuffer params = new StringBuffer();
        params.append("UID").append("=").append(uid).append("&")
                .append("OPTCODE").append("=").append(optCode).append("&")
                .append("M").append("=").append(m).append("&")
                .append("MS").append("=").append(ms);
        return getResponseStr(path,params);
    }
    //http://ip:port/eomfront/ logout.do? UID =xxx
    public String logout(String path,String uid) {
        StringBuffer params = new StringBuffer();
        params.append("UID").append("=").append(uid);
        return getResponseStr(path, params);
    }
    //http://ip:port/eomfront/ updateDev.do? UID =xxx
    public String updateDev(String path,String uid) {
        StringBuffer params = new StringBuffer();
        params.append("UID").append("=").append(uid);
        return getResponseStr(path, params);
    }
    //http://ip:port/eomfront/ cardContUpdate.do? UID=xxx
    public String cardContUpdate(String path,String uid) {
        StringBuffer params = new StringBuffer();
        params.append("UID").append("=").append(uid);
        return getResponseStr(path, params);
    }
    //http://ip:port/eomfront/ commParmUpdate.do? UID=xxx
    public String commParmUpdate(String path,String uid) {
        StringBuffer params = new StringBuffer();
        params.append("UID").append("=").append(uid);
        return getResponseStr(path, params);
    }
    //http://ip:port/eomfront/getAppNum.do? OPTCARDNO=xxx&OPTCODE=xxx
    public String getAppNum(String path,String optCardNo,String optCode) {
        StringBuffer params = new StringBuffer();
        params.append("OPTCARDNO").append("=").append(optCardNo).append("&")
                .append("OPTCODE").append("=").append(optCode);
        return getResponseStr(path, params);
    }
    //











    public String getResponseStr(String path, StringBuffer params)
    {
        String returnContent = "";
        HttpURLConnection conn = null;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            byte[] bypes = params.toString().getBytes();
            conn.getOutputStream().write(bypes);
            Log.i("dabitch", conn.getResponseCode() + "");
            if(200==conn.getResponseCode())
            {
                InputStream inStream=conn.getInputStream();
                returnContent = new String(readInputStream(inStream));
                Log.i("dabitch",returnContent);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
        return  returnContent;
    }
    public byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }
    //
}
