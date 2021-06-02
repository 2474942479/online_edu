package edu.zsq.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.zsq.order.entity.Order;
import edu.zsq.order.entity.dto.OrderDTO;
import edu.zsq.order.entity.dto.OrderQueryDTO;
import edu.zsq.order.service.OrderService;
import edu.zsq.order.utils.OrderNumberUtil;
import edu.zsq.service_order_api.entity.OrderVO;
import edu.zsq.utils.exception.servicexception.MyException;
import edu.zsq.utils.page.PageData;
import edu.zsq.utils.result.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author zsq
 * @since 2021-04-18
 */
@RestController
@RequestMapping("/orderService/order")
public class OrderController {

    @Resource
    private OrderService orderService;


    @PostMapping("/createOrder")
    @ApiOperation(value = "根据课程id和用户id创建订单，返回订单id")
    public JsonResult<String> createOrder(@RequestBody OrderDTO orderDTO) {
        return JsonResult.success(orderService.createOrder(orderDTO));
    }

    @PostMapping("/getOrderList")
    @ApiOperation(value = "根据订单id查询订单")
    public JsonResult<PageData<OrderVO>> getOrderList(@RequestBody OrderQueryDTO orderQueryDTO){
        return JsonResult.success(orderService.getOrderList(orderQueryDTO));
    }

    @GetMapping("/getOrderInfo/{orderNumber}")
    @ApiOperation(value = "根据订单id查询订单")
    public JsonResult<OrderVO> getOrderInfo(@PathVariable String orderNumber){
        return JsonResult.success(orderService.getOrderById(orderNumber));
    }

    @GetMapping("/isBuyCourse/{userId}/{courseId}")
    @ApiOperation(value = "判断用户是否购买课程")
    public JsonResult<Boolean> isBuyCourse(@PathVariable String userId, @PathVariable String courseId){
        return JsonResult.success(orderService.isBuyCourse(userId,courseId));
    }

    @DeleteMapping("/removeOrder/{orderNumber}")
    @ApiOperation(value = "取消订单")
    public JsonResult removeOrder(@PathVariable String orderNumber){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_number",orderNumber);
        boolean remove = orderService.remove(wrapper);
        if (remove){
            return JsonResult.success().message("已取消订单");
        }else {
            return JsonResult.failure("取消订单失败");
        }
    }


}

