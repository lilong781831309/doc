package org.xinhua.example.spring.mybatisplus.service;


import org.xinhua.example.spring.mybatisplus.model.dto.AccessToken;
import org.xinhua.example.spring.mybatisplus.model.dto.ListMediaResponse;
import org.xinhua.example.spring.mybatisplus.model.dto.getMediaLinkResponse;

public interface WechatService {


    /**
     * 属性	             类型	必填      	说明
     * grant_type	    string	是	    填写 client_credential
     * appid	        string	是	    小程序唯一凭证，即 AppID，可在「微信公众平台 - 设置 - 开发设置」页中获得。（需要已经成为开发者，且帐号没有异常状态）
     * appsecret	    string	是	    小程序唯一凭证密钥，即 AppSecret，获取方式同 appid
     * <p>
     * 请求方式 :     GET
     *
     * @return
     */
    AccessToken getAccessToken(String appId, String appSecret);

    /**
     * 属性	             类型	必填      	说明
     * access_token	    string	 是	    接口调用凭证，该参数为 URL 参数，非 Body 参数。使用 getAccessToken 或者 authorizer_access_token
     * drama_id	        number	 否	    根据剧目id获取剧集信息。
     * media_name	    string	 否	    媒资文件名，模糊匹配。
     * start_time	    number	 否	    媒资上传时间>=start_time。
     * end_time	        number	 否	    媒资上传时间<end_time。
     * limit	        number	 否	    分页拉取的最大返回结果数。默认值：100；最大值：100。
     * offset	        number	 否	    分页拉取的起始偏移量。默认值：0。
     * <p>
     * 请求方式         :    POST
     * Content-Type    :    application/json
     *
     * @return
     */
    ListMediaResponse listMedia(String accessToken, Long dramaId, String mediaName, Long startTime, Long endTime, Long limit, Long offset);

    /**
     * 属性	             类型	必填      	说明
     * access_token	    string	是	    接口调用凭证，该参数为 URL 参数，非 Body 参数。使用 getAccessToken 或者 authorizer_access_token
     * media_id	        number	是	    媒资文件id。
     * t	            number	是	    播放地址的过期时间戳。有效的时间最长不能超过2小时后。
     * us	            string	否	    链接标识。平台默认会生成一个仅包含小写字母和数字的字符串用于增强链接的唯一性(如us=647488c4792c15185b8fd2a6)。如开发者需要增加自己的标识，比如区分播放的渠道，可使用该参数，该参数最终的值是"开发者标识-平台标识"（如开发者传入abcd，则最终的临时链接中us=abcd-647488c4792c15185b8fd2a6）
     * expr	            number	否	    试看时长，单位：秒，最大值不能超过视频长度
     * rlimit	        number	否	    最多允许多少个不同 IP 的终端播放，以十进制表示，最大值为9，不填表示不做限制。当限制 URL 只能被1个人播放时，建议 rlimit 不要严格限制成1（例如可设置为3），因为移动端断网后重连 IP 可能改变。
     * whref	        string	否	    允许访问的域名列表，支持1条 - 10条，用半角逗号分隔。域名前不要带协议名（http://和https://），域名为前缀匹配（如填写 abc.com，则 abc.com/123 和 abc.com.cn也会匹配），且支持通配符（如 *.abc.com）。
     * bkref	        string	否	    禁止访问的域名列表，支持1条 - 10条，用半角逗号分隔。域名前不要带协议名（http://和https://），域名为前缀匹配（如填写 abc.com，则 abc.com/123 和 abc.com.cn也会匹配），且支持通配符（如 *.abc.com）。
     * <p>
     * 请求方式         :    POST
     * Content-Type    :    application/json
     *
     * @return
     */
    getMediaLinkResponse getMediaLink(String accessToken, Long mediaId, Integer t, String us, Integer expr, Integer rlimit, String whref, String bkref);

}
