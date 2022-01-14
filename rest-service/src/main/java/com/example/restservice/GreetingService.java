package com.example.restservice;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    public Map<String, String> prepareSendMsg(String number, String phone) {
        String[] argsTest = new String[]{"-n",number,"-phone",phone};
        Map<String,String> sendMsgResult = startSendMsg(argsTest);
        return sendMsgResult;
    }
    private static Map<String,String> startSendMsg(String[] args) {
        Map<String,String> result = new HashMap<>();
        String error = "发生错误";
        if (args == null || args.length == 0){
            String errorMsg = "未输入任何运行参数，请重新输入。";
            result.put(error,errorMsg);
            return result;
        }
        int phoneIndex = -1;
        int numberIndex = -1;
        int number = 1;

        boolean breakFlag = false;
        for (int i = 0;i<args.length;i++) {
            if ("-phone".equals(args[i])){
                phoneIndex = i;
            }
            if("-n".equals(args[i])){
                numberIndex = i;
            }
        }
        /*判断手机号码运行参数*/
        if (phoneIndex == -1){
            String errorMsg = "未按格式输入手机号码的运行参数之后，请重新输入。";
            result.put(error,errorMsg);
            return result;
        }else if(phoneIndex+1>= args.length){
            String errorMsg = "未输入手机号，请重新输入";
            result.put(error,errorMsg);
            return result;
        }
        if (!isPhoneNumber(args[phoneIndex+1])){
            String errorMsg = "未输入符合格式的手机号码或者手机号运行参数之后未输入手机号，请重新输入";
            result.put(error,errorMsg);
            return result;
        }


        /*判断发送短信数目,限定取值是1-Integer.MAX_VALUE*/
        //1.没输入-n或者输入了-n但没有输入数值，默认值1
        //2.numberIndex为0或2的其他值，说明没有规范输入
        if(numberIndex==1||numberIndex==3){
            String errorMsg = "没有规范输入发送短信次数的运行参数，请重新输入";
            result.put(error,errorMsg);
            return result;
        }
        if(numberIndex!=-1 && numberIndex != args.length-1&&args[numberIndex+1]!="-phone"){//说明直接没有给值，取默认值1

            if(!isPositiveInteger(args[numberIndex+1])){
                String errorMsg = "未输入符合格式的正整数或整数值超过限定值，请重新输入";
                result.put(error,errorMsg);
                return result;
            }
            number = Integer.valueOf(args[numberIndex+1]);
        }


        return SendMsg(args[phoneIndex+1],number);
    }

    private static boolean isPositiveInteger(String str) {

        char[] chars = str.trim().toCharArray();

        int res = 0;
        for(int i = 0; i<chars.length; i++){
            if(chars[i]>'9' || chars[i]<'0')
                return false;//说明有一个字符不属于0-9
            //判断溢出
            if(res > Integer.MAX_VALUE/10) {
                return false;
            }else if(res == Integer.MAX_VALUE/10&&(chars[i]-'0')>7){
                return false;
            }
            res = res*10 + (chars[i]-'0');
        }
        return true;

    }


    public static boolean isPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || 0==phoneNumber.length()) {
            return false;
        }

        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-8])|(18[0-9])|(19[1、5、8、9]))\\d{8}$";
        if (phoneNumber.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phoneNumber);
            return m.matches();
        }
    }

    private static Map<String, String> SendMsg(String mobileNumber, int number) {
        String error = "发生错误";
        String errorMsg = null;
        Map<String,String> res = new HashMap<>();
        res.put("向号码"+mobileNumber+"发信","发信次数："+number);
        String result = null;
        //确定目标地址——这个一天只能用一次
        String url="https://user.daojia.com/mobile/getcode?mobile="+mobileNumber;
        //创建httpclient客户端对象
        CloseableHttpClient httpClient= HttpClients.createDefault();
        //创建请求类型
        HttpGet httpGet=new HttpGet(url);
        HttpResponse httpResponse = null;
        String key = null;
        String valuePre = null;
        StringBuilder value = null;
        //发起http请求
        try {
            for (int i = 1;i<=number;i++){
                httpResponse=httpClient.execute(httpGet);
                //判断状态码是否为200
                System.out.printf("第%d次发送，", i);
                key = "第"+i+"次发送";//字符串拼接可以改良
                System.out.println("响应状态码为:"+httpResponse.getStatusLine().getStatusCode());
                value = new StringBuilder("响应状态码为:"+httpResponse.getStatusLine().getStatusCode());
                HttpEntity entity = httpResponse.getEntity();
                valuePre = "响应内容为:" + EntityUtils.toString(entity);
                System.out.println(valuePre);
                value.append(String.format("\t"+valuePre));
//                Thread.sleep(1000);
                res.put(key, value.toString());
            }
        } catch (IOException e) {
            System.out.println("http请求执行错误或线程中断异常");
            errorMsg = "http请求执行错误或线程中断异常";
            res.put(error, errorMsg);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                System.out.println("httpClient关闭发生错误");
                errorMsg = "httpClient关闭发生错误";
                res.put(error, errorMsg);
            }
        }
        return res;
    }

}
