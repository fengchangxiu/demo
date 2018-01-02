package cn.simazilin.demo.common.exception;


/**
 * 错误码常量 code为int   1000-9999
 * <p>
 * Created by ICE on 2017/5/27.
 */
public class ErrorCode {

    public static final Integer REQUEST_ERROR_CODE = 9999;
    public static final String REQUEST_ERROR_MSG = "请求失败";

    /**
     * 基本校验
     */
    public static class Valid{

        public static final Integer PARAM_ERROR_CODE = 1000;
        public static final String PARAM_ERROR_MSG = "请求参数校验异常";

        public static final Integer GET_STORE_ERROR_CODE = 1006;
        public static final String GET_STORE_ERROR_MSG = "店铺信息获取失败";
    }




    public static final class Excel{
        public static final Integer IMPORT_ERROR_CODE = 1301;
        public static final String IMPORT_TEMPLATE_MISMATCH_MSG = "模板导入头部格式错误";
        public static final Integer HEADROW_IS_EMPTY_CODE = 1302;
        public static final String HEADROW_IS_EMPTY_MSG = "导入Excel文件为空";
        public static final Integer TEMPLET_IS_WRONG_CODE = 1303;
        public static final String TEMPLET_IS_WRONG_MSG = "导入Excel文件不符合要求";
        public static final Integer IMPORT_QUANTITY_CODE = 1304;
        public static final String IMPORT_QUANTITY_MSG = "无法处理大于5000条的数据";
        public static final Integer GET_EXCEL_ERROR_CODE = 1305;
        public static final String GET_EXCEL_ERROR_MSG = "文件导入格式有误！";
        public static final Integer EXPORT_EXCEL_IS_NULL_CODE = 1306;
        public static final String EXPORT_EXCEL_IS_NULL_MSG = "导出excel数据为空！";
        public static final Integer READ_EXCEL_ERROR_CODE = 1307;
        public static final String READ_EXCEL_ERROR_MSG = "读取excel失败！";
    }

    public static final class Back{
        public static final Integer CREDIT_RULE_IS_NULL_CODE = 1401;
        public static final String CREDIT_RULE_IS_NULL_MSG = "积分规则不存在";

        public static final Integer CREDIT_RULE_IS_EXIT_CODE = 1402;
        public static final String CREDIT_RULE_IS_EXIT_MSG = "积分规则已经存在";

        public static final Integer LEVEL_RULE_ADD_ERROR_CODE = 1403;
        public static final String LEVEL_RULE_ADD_ERROR_MSG = "不能跳级添加会员等级信息";

        public static final Integer LEVEL_RULE_IS_EXIT_CODE = 1404;

        public static final Integer LEVEL_RULE_IS_NULL_CODE = 1405;
        public static final String LEVEL_RULE_IS_NULL_MSG = "会员等级信息不存在";

        public static final Integer LEVEL_RULE_IS_RUNNING_CODE = 1406;
        public static final String LEVEL_RULE_IS_RUNNING_MSG = "会员等级运行中不能编辑";
    }

    public static final class Coupon{

        public static final Integer COUPON_IF_ALLOW_EDIT_CODE = 1501;
        public static final String COUPON_IF_ALLOW_EDIT_MSG = "当前优惠券状态不能编辑,请刷新后操作";

        public static final Integer GOODS_LIST_IS_EMPTY_CODE = 1502;
        public static final String GOODS_LIST_IS_EMPTY_MSG = "指定商品列表为空";

        public static final Integer TIME_ERROR_CODE = 1503;
        public static final String TIME_ERROR_MSG = "结束时间不得小于开始时间";

        public static final Integer NAME_ALREADY_CODE = 1504;
        public static final String NAME_ALREADY_MSG = "活动名称已存在";

        public static final Integer SUBJECT_NO_ALLOW_EDIT_CODE = 1505;
        public static final String SUBJECT_NO_ALLOW_EDIT_MSG = "当前状态不能编辑";

        public static final Integer SUBJECT_IF_ALLOW_CHANGE_STATUS_CODE = 1506;
        public static final String SUBJECT_IF_ALLOW_CHANGE_STATUS_MSG = "当前活动状态已更改,请刷新后操作";

        public static final Integer CITY_STORE_LIST_IS_EMPTY_CODE = 1507;
        public static final String CITY_STORE_LIST_IS_EMPTY_MSG = "选择门店列表为空";

        public static final Integer SEND_NUM_BEYOND_LIMIT_CODE = 1508;
        public static final String SEND_NUM_BEYOND_LIMIT_MSG = "该优惠券发放数量超出门店每天限制的最大数量";

        public static final Integer SELECT_COUPON_OVERDUE_CODE = 1509;
        public static final String SELECT_COUPON_OVERDUE_MSG = "优惠券在活动开始前已结束";

        public static final Integer SUBJECT_IF_ALLOW_EDIT_CODE = 1510;
        public static final String SUBJECT_IF_ALLOW_EDIT_MSG = "当前活动状态不能编辑,请刷新后操作";

    }

    /**
     * 储值相关
     */
    public static final class Recharge{
        public static final Integer RECHARGE_RULE_NAME_PARAM_ERROR_CODE = 1601;
        public static final String RECHARGE_RULE_NAME_PARAM_ERROR_MSG = "充值规则名称数据错误";

        public static final Integer RECHARGE_STORE_LIST_PARAM_ERROR_CODE = 1602;
        public static final String RECHARGE_STORE_LIST_PARAM_ERROR_MSG = "门店列表数据错误";

        public static final Integer RECHARGE_FIRST_REWARD_PARAM_ERROR_CODE = 1603;
        public static final String RECHARGE_FIRST_REWARD_PARAM_ERROR_MSG = "首次充值奖励规则数据错误";


        public static final Integer RECHARGE_EVERY_REWARD_PARAM_ERROR_CODE = 1604;
        public static final String RECHARGE_EVERY_REWARD_PARAM_ERROR_MSG = "每次充值奖励规则数据错误";

        public static final Integer RECHARGE_PAY_TYPE_PARAM_ERROR_CODE = 1605;
        public static final String RECHARGE_PAY_TYPE_PARAM_ERROR_MSG = "充值方式数据错误";

        public static final Integer RULE_IS_NOT_EXIT_CODE = 1606;
        public static final String RULE_IS_NOT_EXIT_MSG = "规则已经不存在";

        public static final Integer RECHARGE_STORE_USE_ERROR_CODE = 1607;
        public static final String RECHARGE_STORE_USE_ERROR_MSG = "选中门店已被其他规则占用,请重新选中门店";

        public static final Integer RECHARGE_RULE_NAME_REPEAT_ERROR_CODE = 1608;
        public static final String RECHARGE_RULE_NAME_REPEAT_ERROR_MSG = "充值规则名称已存在";

        public static final Integer RECHARGE_BALANCE_CHANGE_ERROR_CODE = 1609;
        public static final String RECHARGE_BALANCE_CHANGE_ERROR_MSG = "会员账户金额变动,请稍后再试";

        public static final Integer RECHARGE_LOW_AMOUNW_ERROR_CODE = 1610;
        public static final String RECHARGE_LOW_AMOUNW_ERROR_MSG = "充值金额不得小于等于0元";

        public static final String DATA_ERROR_MSG = "数据错误";

        public static final Integer RECHARGE_FIRST_REWARD_RECHARGE_AMOUNT_PARAM_ERROR_CODE = 1611;
        public static final String RECHARGE_FIRST_REWARD_RECHARGE_AMOUNT_PARAM_ERROR_MSG = "首次充值奖励规则充值金额";

        public static final Integer RECHARGE_FIRST_REWARD_GIFT_AMOUNT_PARAM_ERROR_CODE = 1612;
        public static final String RECHARGE_FIRST_REWARD_GIFT_AMOUNT_PARAM_ERROR_MSG = "首次充值奖励规则赠送金额";

        public static final Integer RECHARGE_FIRST_REWARD_GIFT_CREDIT_PARAM_ERROR_CODE = 1613;
        public static final String RECHARGE_FIRST_REWARD_GIFT_CREDIT_PARAM_ERROR_MSG = "首次充值奖励规则赠送积分";

        public static final Integer RECHARGE_EVERY_REWARD_RECHARGE_AMOUNT_PARAM_ERROR_CODE = 1614;
        public static final String RECHARGE_EVERY_REWARD_RECHARGE_AMOUNT_PARAM_ERROR_MSG = "每次充值奖励规则充值金额";

        public static final Integer RECHARGE_EVERY_REWARD_GIFT_AMOUNT_PARAM_ERROR_CODE = 1615;
        public static final String RECHARGE_EVERY_REWARD_GIFT_AMOUNT_PARAM_ERROR_MSG = "每次充值奖励规则赠送金额";

        public static final Integer RECHARGE_EVERY_REWARD_GIFT_CREDIT_PARAM_ERROR_CODE = 1616;
        public static final String RECHARGE_EVERY_REWARD_GIFT_CREDIT_PARAM_ERROR_MSG = "每次充值奖励规则赠送积分";

    }

    /**
     * 会员相关错误代码
     */
    public static class Mem{
        public static final Integer USER_IS_NULL_CODE = 1701;
        public static final String USER_IS_NULL_MSG = "用户不存在";

        public static final Integer USER_CREATE_ERROR_CODE = 1702;

        public static final Integer USER_IS_EXIT_CODE = 1703;
        public static final String USER_IS_EXIT_MSG = "用户已经存在";

        public static final Integer USER_MOBILE_EXIT_CODE = 1704;

        public static final Integer USER_PAY_PWD_NOT_MATCH_EXIT_CODE = 1705;
        public static final String USER_PAY_PWD_NOT_MATCH_EXIT_MSG = "支付密码不匹配";

        public static final Integer USER_PAY_PWD_FORMAT_ERROR_CODE = 1706;
        public static final String USER_PAY_PWD_FORMAT_ERROR_MSG = "支付密码格式错误";

    }

    /**
     * 登录
     */
    public static class Login {
        public static final Integer LOGIN_FAIL_CODE = 2000;
        public static final String LOGIN_FAIL_MSG = "登录失效, 请重新登录";
    }

    /**
     * 营销管理活动
     */
    public static final class Activity{
        public static final Integer NAME_ALREADY_CODE = 3000;
        public static final String NAME_ALREADY_MSG = "活动名称已存在";

        public static final Integer TIME_ERROR_CODE = 3001;
        public static final String TIME_ERROR_MSG = "结束时间不得小于开始时间";

        public static final Integer GOODS_LIST_IS_EMPTY_CODE = 3002;
        public static final String GOODS_LIST_IS_EMPTY_MSG = "指定商品列表为空";

        public static final Integer CATEGORY_LIST_IS_EMPTY_CODE = 3003;
        public static final String CATEGORY_LIST_IS_EMPTY_MSG = "指定分类列表为空";

        public static final Integer ACTIVITY_INFO_IS_NULL_CODE = 3004;
        public static final String ACTIVITY_INFO_IS_NULL_MSG = "活动信息不存在";

        public static final Integer GOODS_DELETE_CODE=3005;
        public static final String GOODS_DELETE_MSG="商品已被删除";

        public static final Integer GOODS_PRICE_MODIFY_CODE=3006;
        public static final String GOODS_PRICE_MODIFY_MSG="商品价格已被修改，并低于设定的促销价";

        public static final Integer CITY_STORE_LIST_IS_EMPTY_CODE = 3007;
        public static final String CITY_STORE_LIST_IS_EMPTY_MSG = "选择门店列表为空";


        public static final Integer STORE_NO_ALLOW_RESTART_CODE = 3008;
        public static final String STORE_NO_ALLOW_RESTART_MSG = "店铺活动不准许重启";

        public static final Integer STORE_NO_ALLOW_EDIT_CODE = 3009;
        public static final String STORE_NO_ALLOW_EDIT_MSG = "当前状态不能编辑";

        public static final Integer SAVE_ACTIVITY_IS_FAIL_CODE = 3010;
        public static final String SAVE_ACTIVITY_IS_FAIL_MSG = "新建活动失败";

        public static final Integer STORE_ACTIVITY_IF_ALLOW_CHANGE_STATUS_CODE = 3011;
        public static final String STORE_ACTIVITY_IF_ALLOW_CHANGE_STATUS_MSG = "当前活动状态已更改,请刷新后操作";

        public static final Integer STORE_ACTIVITY_IF_ALLOW_EDIT_CODE = 3012;
        public static final String STORE_ACTIVITY_IF_ALLOW_EDIT_MSG = "当前活动状态不能编辑,请刷新后操作";


    }

    public static final class Order {
        public static final Integer ORDER_INFO_IS_NULL_CODE = 4000;
        public static final String ORDER__INFO_IS_NULL_MSG = "订单信息不存在";
    }

    /**
     * 余额规则相关
     */
    public static final class BalanceRule{
        public static final Integer BALANCE_RULE_NAME_PARAM_ERROR_CODE = 5001;
        public static final String BALANCE_RULE_NAME_PARAM_ERROR_MSG = "充值规则名称数据错误";

        public static final Integer BALANCE_STORE_LIST_PARAM_ERROR_CODE = 5002;
        public static final String BALANCE_STORE_LIST_PARAM_ERROR_MSG = "门店列表数据错误";

        public static final Integer RULE_IS_NOT_EXIT_CODE = 5003;
        public static final String RULE_IS_NOT_EXIT_MSG = "规则不存在";

        public static final Integer BALANCE_STORE_USE_ERROR_CODE = 5004;
        public static final String BALANCE_STORE_USE_ERROR_MSG = "选中门店已被其他规则占用,请重新选中门店";

        public static final Integer BALANCE_MINIMUMAMOUNT_PARAM_ERROR_CODE = 5005;
        public static final String BALANCE_MINIMUMAMOUNT_PARAM_ERROR_MSG = "请输入正确的最低消费金额";


        public static final Integer BALANCE_RULE_NAME_REPEAT_ERROR_CODE = 5006;
        public static final String BALANCE_RULE_NAME_REPEAT_ERROR_MSG = "余额规则名称已存在";

    }

    public static final class Balance{
        public static final Integer BALANCE_EXCEED_MINIMUMAMOUNT_ERROR_CODE = 6001;
        public static final String BALANCE_EXCEED_MINIMUMAMOUNT_ERROR_MSG = "余额支付最低消费0.01元";

        public static final Integer BALANCE_NOT_ENOUGH_SALE_ERROR_CODE = 6002;
        public static final String BALANCE_NOT_ENOUGH_SALE_ERROR_MSG = "余额不足,请充值后重试";
    }
}
