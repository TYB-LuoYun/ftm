package top.anets.utils.base;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class HttpClientUtil {

	private static final Log logeer = LogFactory.getLog(HttpClientUtil.class);



	public static String postFrom(String url, Map<String, Object> map){
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<>();

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String name = entry.getKey();
			String value = "";
			if( entry.getValue()!=null){
				value = entry.getValue()+"";
			}
			params.add(new BasicNameValuePair(name, value));
		}
		String result = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}

			if (entity != null) {
				String responseBody = EntityUtils.toString(entity);
				System.out.println("Response: " + responseBody);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}




	/**
	 * post请求
	 * @param url
	 * @param map
	 * @param charset
	 * @return
	 */
	public static String doPost(String url, Map<String, Object> map, String charset,int type) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			httpPost = new HttpPost(url);
            String json = JSON.toJSON(map).toString();
            StringEntity entity = new StringEntity(json,charset);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String doPost(String url, String str, String charset) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(str,charset);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String doPost(String url, String str, Header[] headers, String charset) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = HttpClientBuilder.create().build();
			httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(str,charset);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			httpPost.setHeaders(headers);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	/**
	 * get请求
	 * @param url
	 * @param map
	 * @param charset
	 * @return
	 */
	public static String doGet(String url, Map<String, Object> map, String charset,int type) {
		logeer .info("*****url:  "+url);
		String result = "";
		try {
			// 根据地址获取请求
			HttpGet request = new HttpGet(url);// 这里发送get请求
			// 获取当前客户端对象
			HttpClient httpClient = HttpClientBuilder.create().build();
			// 通过请求对象获取响应对象
			HttpResponse response = httpClient.execute(request);

			// 判断网络连接状态码是否正常(0--200都数正常)
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logeer .info("*****result:  "+result);
		return result;
	}

	/**
	 * get请求
	 * @param url
	 * @param map
	 * @param charset
	 * @return
	 */
	public static String doGet(String url, Map<String, Object> map, String charset) {
		url = url+ "?" + mapToUrlStr(map);
		logeer .info("*****url:  "+url);
		String result = "";
		try {
			// 根据地址获取请求
			HttpGet request = new HttpGet(url);// 这里发送get请求
			// 获取当前客户端对象
			HttpClient httpClient = HttpClientBuilder.create().build();
			// 通过请求对象获取响应对象
			HttpResponse response = httpClient.execute(request);

			// 判断网络连接状态码是否正常(0--200都数正常)
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logeer .info("*****result:  "+result);
		return result;
	}

	private static String mapToUrlStr(Map<String, Object> map){
		StringBuilder stringBuilder = new StringBuilder();
		Set<String> keys = map.keySet();
		for (String string : keys) {
			String value = map.get(string).toString();
			stringBuilder.append("&"+string+"="+value);
		}
		if(stringBuilder.toString().length()>0) {
			return stringBuilder.toString().substring(1);
		}
		return stringBuilder.toString();
	}


	/**
	 * 网上获取文件
	 *
	 * @param savepath 保存路径
	 * @param resurl  资源路径
	 * @param fileName  自定义资源名
	 * @return false:文件已存在 true：文件获取成功
	 */
	public static boolean getInternetRes(String savepath, String resurl, String fileName) {
		URL url = null;
		HttpURLConnection con = null;
		InputStream in = null;
		FileOutputStream out = null;
		try {
			url = new URL(resurl);
			//建立http连接，得到连接对象
			con = (HttpURLConnection) url.openConnection();
			//con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			in = con.getInputStream();
			byte[] data = getByteData(in);//转化为byte数组

			File file = new File(savepath);
			if (!file.exists()) {
				file.mkdirs();
			}

			File res = new File(file + File.separator + fileName);
			if (res.exists()) {
				return false;
			}
			out = new FileOutputStream(res);
			out.write(data);
			logeer .info("downloaded successfully!");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != out) {
					out.close();
				}
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return true;
	}


	/**
	 * 从输入流中获取字节数组
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static byte[] getByteData(InputStream in) throws IOException {
		byte[] b = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = 0;
		while ((len = in.read(b)) != -1) {
			bos.write(b, 0, len);
		}
		if(null!=bos){
			bos.close();
		}
		return bos.toByteArray();
	}

}
