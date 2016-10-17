package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class test2 {
//	private static String appKey="替换为自己的app_key";
//	private static String appSecret="替换为自己的app_secret";
	
	private static String appKey="c53b8ddf4250e742594d24c55e73c0df";
	private static String appSecret="533cfc090d81b4ee58358d0dcf7956ac";
	
	public static void main(String[] args) throws Exception {
		try{
			//初始化请求参数
			Map<String, Object> parmsMap=new HashMap<String,Object>(){{
		    	put("puid","99058641,99058644");
				put("time_sign",Long.toString(System.currentTimeMillis()/1000));
				put("app_key",appKey);
		    }};
		    
			parmsMap.put("api_token",getToken(parmsMap));
			
			String interfaceUrl="http://www.ganji.com/car/api/get.post.info";
			String queryString=toQueryString(parmsMap);
			
			String getRes=sendGet(interfaceUrl,queryString);
			System.out.println(getRes);
			
		}catch(Exception exception){
			throw exception;
		}
	}
	
	
	public static String getToken(Map<String, Object> parmsMap) throws UnsupportedEncodingException{
		List<Map.Entry<String, Object>> parmsList= new ArrayList<Map.Entry<String, Object>>(parmsMap.entrySet());
		
		//对参数进行正序排序
		parmsList.sort(new Comparator<Map.Entry<String, Object>>() {   
		    public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {    
		        return (o1.getKey()).toString().compareTo(o2.getKey());
		    }
		});

		//=号连接 编码过的key value 并用 &号 拼接起来
		String temp="";
		for(Map.Entry<String,Object> item:parmsList){
			temp+=String.format("%s=%s&", URLEncoder.encode(item.getKey(),java.nio.charset.StandardCharsets.UTF_8.toString()),
					URLEncoder.encode(item.getValue().toString(),java.nio.charset.StandardCharsets.UTF_8.toString()));
		}
		temp=temp.substring(0, temp.length()-1);
		
		//追加 "&"喝app_secret
		temp+="&"+appSecret;
		
//		System.out.println(temp);
		return SHA1(temp);
	}
	
	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	public static String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException {
	    StringBuffer queryString = new StringBuffer();

	    for (Entry<?, ?> pair : data.entrySet()) {
	        queryString.append ( URLEncoder.encode ( (String) pair.getKey (), "UTF-8" ) + "=" );
	        queryString.append ( URLEncoder.encode ( (String) pair.getValue (), "UTF-8" ) + "&" );
	    }

	    if (queryString.length () > 0) {
	        queryString.deleteCharAt ( queryString.length () - 1 );
	    }

		return queryString.toString ();
	}
	
	 /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


}
