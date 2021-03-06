package edu.zsq.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 张
 */
public interface VodService {

    /**
     * 上传视频到阿里云
     * @param file
     * @return
     */
    String uploadVideo(MultipartFile file);

    /**
     * 通过视频资源Id删除阿里云中的视频
     * @param videoSourceId
     * @return
     */
    boolean removeVod(String videoSourceId);

    /**
     * 通过多个id批量删除视频
     *
     * @param vodIdList
     * @return
     */
    boolean removeVodList(List vodIdList);

    /**
     * 根据视频id获取视频播放凭证
     * @param videoId
     * @return
     */
    String getPlayAuth(String videoId);
}
