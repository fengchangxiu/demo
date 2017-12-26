package cn.simazilin.demo.controller;

import cn.simazilin.demo.common.util.JSONUtil;
import cn.simazilin.demo.controller.object.form.UserLoginForm;
import cn.simazilin.demo.controller.object.result.UserLoginResult;
import cn.simazilin.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-25 18:23
 * @Project: 阳光物业V1.0
 * @Description:
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/login",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,method = RequestMethod.GET)
    public Object getLogin(@Valid UserLoginForm form){
        UserLoginResult result = userService.getLogin(form);
        logger.debug("返回参数：[{}]", JSONUtil.objToStr(result));
        return result;
    }
}
