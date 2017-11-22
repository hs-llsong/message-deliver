package hs.ontheroadstore.message.deliver.tools;

import com.aliyun.openservices.shade.org.apache.commons.codec.digest.DigestUtils;
import okhttp3.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/22.
 */
public class HsHttpClient {
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient okHttpClient;
    private final Logger logger = Logger.getLogger(HsHttpClient.class);

    public HsHttpClient() {
        this.okHttpClient = new OkHttpClient();
    }

    public HsHttpClient(boolean isHsApiAuthorize,String appkey,String appSecret) {
        OkHttpClient.Builder okHttpBuiler = new OkHttpClient.Builder();
        if(isHsApiAuthorize) {
            okHttpBuiler = hsApiAuthorizeInterceptor(okHttpBuiler,true,appkey,appSecret);
        }
        this.okHttpClient = okHttpBuiler.build();
    }

    private OkHttpClient.Builder hsApiAuthorizeInterceptor(OkHttpClient.Builder builder, boolean enable, String appkey, String appSecret) {
        if(enable) {
            builder.networkInterceptors().add(chain -> {
                Request orginal = chain.request();
                String curtime = DateTimeTools.getNowTimeStamp();
                String nonce = RandomStringUtils.randomAlphanumeric(16).toUpperCase();
                String checksum = DigestUtils.sha1Hex(appSecret+nonce+curtime);
                Request.Builder requestBuilder = orginal.newBuilder()
                        .addHeader("appkey",appkey)
                        .addHeader("nonce",nonce)
                        .addHeader("curtime",curtime)
                        .addHeader("checksum",checksum);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
        }
        return builder;
    }


    private String getResponseBody (Response response) throws IOException {
        if(response == null) {
            throw new IOException("Null response exception code");
        }
        if (response.isSuccessful()) {
            try {
                return response.body().string();
            } catch (java.lang.NullPointerException e){
                throw new IOException("Null pinter exception code" + response);
            }
        } else {
            throw new IOException("Unexpected code" + response);
        }
    }
    public String Get(String url, Map<String,String> params) throws IOException {

        HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for(Map.Entry<String,String> param:params.entrySet()) {
                httpBuider.addQueryParameter(param.getKey(),param.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(httpBuider.build())
                .build();
        Response response = this.okHttpClient.newCall(request).execute();
        if(!response.isSuccessful()) return null;
        return this.getResponseBody(response);

    }

    public String postJson(String url,String jsonParams) throws IOException{
        RequestBody requestBody = RequestBody.create(JSON,jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = this.okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            return null;
        }
        return this.getResponseBody(response);
    }


}
