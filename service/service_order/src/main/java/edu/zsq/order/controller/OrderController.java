package edu.zsq.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.zsq.order.entity.Order;
import edu.zsq.order.service.OrderService;
import edu.zsq.order.utils.OrderNumberUtil;
import edu.zsq.utils.exception.servicexception.MyException;
import edu.zsq.utils.result.MyResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author zsq
 * @since 2020-08-25
 */
@RestController
@RequestMapping("/orderService/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    /**
     * 根据课程id和用户id创建订单，返回订单id
     * @param orderInfo
     * @return
     */
    @PostMapping("/createOrder")
    public MyResultUtils createOrder(@RequestBody Order orderInfo) {
        String userId = orderInfo.getUserId();
        if (userId ==null || "".equals(userId)){
            throw new MyException(28004,"您未登录，三秒后跳转登录页面");
        }
        String orderNumber = OrderNumberUtil.getOrderNumber();
        orderInfo.setOrderNumber(orderNumber);
//        支付类型（1：微信 2：支付宝）
        orderInfo.setPayType(1);
//        订单状态（0：未支付 1：已支付）
        orderInfo.setStatus(0);

        boolean b = orderService.saveOrUpdate(orderInfo);
        if (b){
            return MyResultUtils.ok().message("订单添加成功").data("orderNumber", orderNumber);

        }else {
            return MyResultUtils.error().message("订单添加失败");

        }

    }


    /**
     * 根据订单id查询订单
     * @param orderNumber
     * @return
     */
    @GetMapping("/getOrderInfo/{orderNumber}")
    public MyResultUtils getOrderInfo(@PathVariable String orderNumber){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_number",orderNumber);
        Order order = orderService.getOne(wrapper);
        return MyResultUtils.ok().data("orderInfo",order).message("订单创建成功，请确认！");
    }

    /**
     * 判断用户是否购买课程
     * @param userId 用户id
     * @param courseId  课程id
     * @return
     */
    @GetMapping("/isBuyCourse/{userId}/{courseId}")
    public Boolean isBuyCourse(@PathVariable String userId, @PathVariable String courseId){
        boolean isBuy = orderService.isBuyCourse(userId,courseId);
        return isBuy;
    }

    /**
     * 取消订单
     * @param orderNumber
     * @return
     */
    @DeleteMapping("/removeOrder/{orderNumber}")
    public MyResultUtils removeOrder(@PathVariable String orderNumber){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_number",orderNumber);
        boolean remove = orderService.remove(wrapper);
        if (remove){
            return MyResultUtils.ok().message("已取消订单");
        }else {
            return MyResultUtils.error().message("取消订单失败");
        }
    }


}

