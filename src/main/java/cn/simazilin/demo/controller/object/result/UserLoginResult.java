package cn.simazilin.demo.controller.object.result;

import java.io.Serializable;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-25 18:29
 * @Project: 阳光物业V1.0
 * @Description:
 */
public class UserLoginResult implements Serializable{

    private static final long serialVersionUID = -6774270539875112556L;
    private String userName;

    private String mobile;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
