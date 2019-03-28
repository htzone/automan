package com.redpig.common.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;


/**
 * @describe HttpClient请求封装类
 * @author hetao
 * @date 2016年11月10日 下午4:27:46
 */
public class HttpClientManager {
	
	/**
	 * @test
	 * @param args
	 */
	public static void main(String[] args){
		String url = "htt://news.baidu.com/ns?from=news&cl=2&bt=1479312000&y0=2016&m0=11&d0=17&y1=2016&m1=11&d1=17&et=1479398399&q1=tcl&submit=%B0%D9%B6%C8%D2%BB%CF%C2&q3=&q4=&mt=0&lm=&s=2&begin_date=2016-11-17&end_date=2016-11-17&tn=newsdy&ct1=1&ct=1&rn=20&q6=";
		String content = null;
		try {
			content = HttpClientManager.instance(url).getContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("result:" + content);
	}
	
	private final static Logger log = LoggerFactory.getLogger(HttpClientManager.class);
	
	/**
	 * get请求，post请求
	 */
	public static final String METHOD_GET = "GET", METHOD_POST = "POST";
	/**
	 * 用户代理
	 */
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.99 Safari/537.36";
	/**
	 * 重试间隔时间
	 */
//	private static final int RETRY_INTERVAL_TIME = 2000;
	/**
	 * HttpClient
	 */
	private static CloseableHttpClient client;
	/**
	 * 编码格式
	 */
	private String encoding = "UTF-8";
	/**
	 * 请求开始时间
	 */
	private long startTime;
	/**
	 * 请求尝试次数
	 */
	private int tryTimes = 3;
	/**
	 * 请求构造器
	 */
	private RequestBuilder requestBuilder;
	/**
	 * 响应结果
	 */
	private CloseableHttpResponse response;
	/**
	 * 请求url
	 */
	private String url;
	/**
	 * 返回实体
	 */
	private HttpEntity respEntity;
	/**
	 * 返回状态码
	 */
	private Integer respState;
	
	//构建HttpClient
	static {
		RequestConfig defaultRequestConfig = RequestConfig.custom()
        .setSocketTimeout(10000)
        .setConnectTimeout(10000)
        .setConnectionRequestTimeout(10000)
        .build();
		HttpClientBuilder clientBuilder = HttpClients.custom()
		.setUserAgent(HttpClientManager.USER_AGENT)
		.setMaxConnTotal(800)
    	.setMaxConnPerRoute(200)
    	.setDefaultRequestConfig(defaultRequestConfig)
    	.evictIdleConnections(1, TimeUnit.MINUTES)
    	.setDefaultCookieStore(new BasicCookieStore());
		client = clientBuilder.build();
	}
	
	/**
	 * 构造方法
	 * @param url 需要请求的url
	 * @param method 请求的方法
	 */
	private HttpClientManager(String url, String method){
		this.url = url;
		if(METHOD_GET.equalsIgnoreCase(method)){
			this.requestBuilder = RequestBuilder.get(url);
		}else if(METHOD_POST.equalsIgnoreCase(method)){
			this.requestBuilder = RequestBuilder.post(url);
		}
		else{
			log.error("请指定请求方法！");
		}
	}
	
	/**
	 * 获取实例
	 * @param url 需要请求的url
	 * @param method 请求的方法
	 * @return
	 */
	public static HttpClientManager instance(String url, String method){
		return new HttpClientManager(url, method);
	}

	/**
	 * 获取实例
	 * @param url 需要请求的url
	 * @return
	 */
	public static HttpClientManager instance(String url){
		return new HttpClientManager(url, METHOD_GET);
	}
	
	/**
	 * 添加请求头信息
	 * @param name 键名
	 * @param value 键值
	 * @return
	 */
	public HttpClientManager addHeader(String name, String value) {
        this.requestBuilder.addHeader(name, value);
        return this;
    }
	
	/**
	 * 添加请求参数
	 * @param name 键名
	 * @param value 键值
	 * @return
	 */
	public HttpClientManager addParam(String name, String value) {
        this.requestBuilder.addParameter(name, value);
        return this;
    }
	
	/**
	 * 设置请求配置
	 * @param config
	 * @return
	 */
	public HttpClientManager setRequestConfig(RequestConfig config){
		this.requestBuilder.setConfig(config);
		return this;
	}
	
	/**
	 * 设置代理
	 * @param ip
	 * @param port
	 * @return
	 */
	public HttpClientManager setProxy(String ip, int port){
		this.setRequestConfig(RequestConfig.custom().setProxy(new HttpHost(ip, port)).build());
		return this;
	}

	/**
	 * 设置请求实体
	 * @param entity 请求实体
	 * @return 
	 */
	public HttpClientManager setEntity(HttpEntity entity) {
        this.requestBuilder.setEntity(entity);
        return this;
    }
	
	/**
	 * 设置请求连接超时时间
	 * @param millseconds 设置毫秒
	 * @return
	 */
	public HttpClientManager setConnectTimeout(int millseconds) {
		this.setRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(millseconds).build());
        return this;
    }
	
	/**
	 * 设置socket读写超时时间
	 * @param millseconds 设置毫秒
	 * @return
	 */
	public HttpClientManager setSocketTimeout(int millseconds) {
		this.setRequestConfig(RequestConfig.custom().setSocketTimeout(millseconds).build());
        return this;
    }
	
	/**
	 * 设置IP
	 * @param hostName
	 * @return
	 */
	public HttpClientManager setLocalAddress(String hostName) {
		if(hostName != null && !"".equals(hostName)){
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(hostName);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			this.setRequestConfig(RequestConfig.custom().setLocalAddress(inetAddress).build());
		}
		return this;
	}
	
	/**
	 * 设置cookie
	 * @param cookieStr cookie字符串
	 * @return
	 */
	public HttpClientManager setCookie(String cookieStr){
		return this.addHeader("Cookie", cookieStr);
	}
	
	/**
	 * 设置Referer
	 * @param referer 来源链接
	 * @return
	 */
	public HttpClientManager setRefere(String referer){
		return this.addHeader("Referer", referer);
	}
	
	/**
	 * 设置编码格式
	 * @param encoding 编码格式
	 * @return
	 */
	public HttpClientManager setEncoding(String encoding){
		this.encoding = encoding;
		return this;
	}
	
	/**
	 * 设置尝试请求次数
	 * @param tryTimes 请求尝试次数
	 * @return
	 */
	public HttpClientManager setTryTimes(int tryTimes) {
        this.tryTimes = tryTimes;
        return this;
    }
	
	/**
	 * @return 获取返回结果码，没有为-1
	 */
	public Integer getState(){
		return this.respState != null ? this.respState : -1;
	}
	
	/**
	 * 获取返回实体，没有为null
	 * @return
	 */
	public HttpEntity getEntity(){
		return this.respEntity != null ? this.respEntity : null;
	}
	
	/**
	 * 调用请求
	 * @return 结果实体
	 * @throws Exception
	 */
	public HttpEntity call() throws Exception{
		try {
			this.response = HttpClientManager.client.execute(this.requestBuilder.build());
			this.respState = this.response.getStatusLine().getStatusCode();
			if (this.response != null && this.respState == 200) { //请求成功
				return this.response.getEntity();
			}
			log.error("请求URL:" + this.url + " 失败，" + (this.response != null ? "状态码为：" + this.response.getStatusLine().getStatusCode() : "返回结果为空..."));
			return (this.response != null ? this.response.getEntity() : null);
		} catch (Exception e){
			if(this.response != null){
				this.response.close();
				this.response = null;
			}
            throw e;
		}
	}
	
	/**
	 * 获取请求结果，文本形式返回
	 * @return
	 */
	public String getContent() throws Exception{
		startTime = System.currentTimeMillis();
		String content = null;
		try{
			this.respEntity = call();
			if(this.respEntity != null){
				content = EntityUtils.toString(this.respEntity, "UTF-8");
			}
		} catch(Exception e){
			throw e;
		} finally{
			callOver();
		}
		return content;
	}

	/**
	 * 获取请求结果，文本形式返回
	 * @return
	 */
	public String getContent(String charset) throws Exception{
		startTime = System.currentTimeMillis();
		String content = null;
		try{
			this.respEntity = call();
			if(this.respEntity != null){
				content = EntityUtils.toString(this.respEntity, charset);
			}
		} catch(Exception e){
			throw e;
		} finally{
			callOver();
		}
		return content;
	}

	/**
	 * 获取请求结果，字节形式返回
	 * @return
	 */
	public byte[] getBytes() throws Exception{
		startTime = System.currentTimeMillis();
		byte[] bytes = null;
		try {
			this.respEntity = call();
			if(this.respEntity != null){
				bytes = EntityUtils.toByteArray(this.respEntity);
			}
		} catch (Exception e) {
			throw e;
		} finally{
			callOver();
		}
		return bytes;
    }
	
	/**
	 * 请求结束操作
	 */
	public void callOver(){
		log.debug("请求URL：" + this.url + "共耗时" + (float)(System.currentTimeMillis() - this.startTime)/1000 + "秒");
		EntityUtils.consumeQuietly(this.respEntity);
		if(this.response != null){
			try {
				this.response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭客户端
	 * @return
	 */
	public HttpClientManager shutDown(){
		try {
			HttpClientManager.client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
}
