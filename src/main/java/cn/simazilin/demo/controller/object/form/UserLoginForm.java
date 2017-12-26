package cn.simazilin.demo.controller.object.form;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-25 18:26
 * @Project: 阳光物业V1.0
 * @Description:
 */
public class UserLoginForm implements Serializable{

    private static final long serialVersionUID = 6664819884572998670L;
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotBlank(message = "密码不能为空")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
