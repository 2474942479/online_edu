package edu.zsq.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.zsq.eduservice.entity.EduBanner;
import edu.zsq.eduservice.service.EduBannerService;
import edu.zsq.utils.result.MyResultUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author zsq
 * @since 2020-08-24
 */
@RestController
@RequestMapping("/eduService/banner")
public class EduBannerController {

    @Autowired
    private EduBannerService bannerService;


    @ApiOperation(value = "获取Banner分页列表")
    @GetMapping("pageBanner/{page}/{size}")
    public MyResultUtils pageBanner(@PathVariable long page, @PathVariable long size) {

        Page<EduBanner> pageBanner = new Page<>(page, size);
        bannerService.page(pageBanner, null);
        return MyResultUtils.ok().data("items", pageBanner.getRecords()).data("total", pageBanner.getTotal());
    }

    @ApiOperation(value = "获取Banner")
    @GetMapping("get/{id}")
    public MyResultUtils get(@PathVariable String id) {
        EduBanner banner = bannerService.getById(id);
        return MyResultUtils.ok().data("item", banner);
    }


    @ApiOperation(value = "新增Banner")
    @PostMapping("addBanner")
    public MyResultUtils addBanner(@RequestBody EduBanner EduBanner) {
        bannerService.save(EduBanner);
        return MyResultUtils.ok();
    }

    @ApiOperation(value = "修改Banner")
    @PutMapping("update")
    public MyResultUtils updateById(@RequestBody EduBanner banner) {
        bannerService.updateById(banner);
        return MyResultUtils.ok();
    }

    @ApiOperation(value = "删除Banner")
    @DeleteMapping("remove/{id}")
    public MyResultUtils remove(@PathVariable String id) {
        bannerService.removeById(id);
        return MyResultUtils.ok();
    }

}

