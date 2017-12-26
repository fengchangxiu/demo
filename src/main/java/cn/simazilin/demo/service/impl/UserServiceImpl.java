package cn.simazilin.demo.service.impl;

import cn.simazilin.demo.controller.object.form.UserLoginForm;
import cn.simazilin.demo.controller.object.result.UserLoginResult;
import cn.simazilin.demo.entity.User;
import cn.simazilin.demo.repository.UserRepository;
import cn.simazilin.demo.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-25 18:28
 * @Project: 阳光物业V1.0
 * @Description:
 */

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserLoginResult getLogin(UserLoginForm form) {
        UserLoginResult result = new UserLoginResult();
        User user = userRepository.getByUserNameAndPassword(form.getUserName(),form.getPassword());
        BeanUtils.copyProperties(user,result);
        return result;
    }
}
