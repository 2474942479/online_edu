package edu.zsq.service_order_api.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 张
 */
@Data
@Builder
public class OrderVO {

    @ApiModelProperty(value = "订单号")
    private String orderNumber;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "课程名称")
    private String courseTitle;

    @ApiModelProperty(value = "课程封面")
    private String courseCover;

    @ApiModelProperty(value = "讲师名称")
    private String teacherName;

    @ApiModelProperty(value = "会员id")
    private String userId;

    @ApiModelProperty(value = "会员昵称")
    private String nickname;

    @ApiModelProperty(value = "会员手机")
    private String mobile;

    @ApiModelProperty(value = "课程原价（分）")
    private BigDecimal courseMoney;

    @ApiModelProperty(value = "减免金额（分）")
    private BigDecimal reductionMoney;

    @ApiModelProperty(value = "应付金额（分）")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "订单状态（0：未支付 1：已支付）")
    private Integer status;
}