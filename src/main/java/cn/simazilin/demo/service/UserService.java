package cn.simazilin.demo.service;

import cn.simazilin.demo.controller.object.form.UserLoginForm;
import cn.simazilin.demo.controller.object.result.UserLoginResult;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-25 18:28
 * @Project: 阳光物业V1.0
 * @Description:
 */
public interface UserService {


    /**
     * 用户登陆
     * @param form
     * @return
     */
    public UserLoginResult getLogin(UserLoginForm form);
}
