package cn.simazilin.demo.controller.object.form;

import java.io.Serializable;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-26 16:31
 * @Project: 阳光物业V1.0
 * @Description:
 */
public class UserSaveForm implements Serializable{

    private static final long serialVersionUID = 2789619498354007245L;
    private String userName;

    private String password;

    private String mobile;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
