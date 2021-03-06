package edu.zsq.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import edu.zsq.utils.exception.servicexception.MyException;
import edu.zsq.utils.properties.ReadPropertiesUtil;
import edu.zsq.vod.service.VodService;
import edu.zsq.vod.utils.InitVodClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author 张
 */
@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadVideo(MultipartFile file) {

        /**
         * title:上传之后显示名称
         * fileName:上传文件原始名称
         * inputStream:上传文件流
         */

        String videoId = null;

        try {

            String fileName = file.getOriginalFilename();
            String title = fileName.substring(0, fileName.lastIndexOf("."));

            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ReadPropertiesUtil.ACCESS_KEY_ID, ReadPropertiesUtil.ACCESS_KEY_SECRET, title, fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            //请求视频点播服务的请求ID
            if (response.isSuccess()) {
                videoId = response.getVideoId();
                System.out.print("VideoId=" + response.getVideoId() + "\n");
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
            return videoId;
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(20001, "上传视频失败");
        }


    }


    /**
     * 通过视频资源Id删除阿里云中的视频
     *
     * @param videoSourceId
     * @return
     */
    @Override
    public boolean removeVod(String videoSourceId) {

        try {
            //        初始化对象

            DefaultAcsClient client = InitVodClient.initVodClient(ReadPropertiesUtil.ACCESS_KEY_ID, ReadPropertiesUtil.ACCESS_KEY_SECRET);
//            创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoSourceId);
//            调用初始化对象的方法实现删除
            client.getAcsResponse(request);

            return true;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new MyException(20001, "删除视频失败");
        }


    }


    /**
     * 通过多个id批量删除视频
     *
     * @param vodIdList
     * @return
     */
    @Override
    public boolean removeVodList(List vodIdList) {
        try {
            //        初始化对象

            DefaultAcsClient client = InitVodClient.initVodClient(ReadPropertiesUtil.ACCESS_KEY_ID, ReadPropertiesUtil.ACCESS_KEY_SECRET);
//            创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            String videoSourceIds = StringUtils.join(vodIdList.toArray(), ",");
            request.setVideoIds(videoSourceIds);
//            调用初始化对象的方法实现删除
            client.getAcsResponse(request);

            return true;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new MyException(20001, "删除视频失败");
        }
    }

    @Override
    public String getPlayAuth(String videoId) {
        try {
//          初始化对象

            DefaultAcsClient client = InitVodClient.initVodClient(ReadPropertiesUtil.ACCESS_KEY_ID, ReadPropertiesUtil.ACCESS_KEY_SECRET);
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return playAuth;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new MyException(20001,"获取视频凭证出错了");
        }

    }
}
