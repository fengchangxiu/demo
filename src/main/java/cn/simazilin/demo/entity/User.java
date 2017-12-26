package cn.simazilin.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-25 17:55
 * @Project: 阳光物业V1.0
 * @Description:
 */
@Entity
@Table(name = "t_user",indexes = {@Index(name = "MOBILE_INDEX",columnList = "mobile",unique = true)})
public class User implements Serializable{

    private static final long serialVersionUID = -7632444519642112136L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20, nullable = false, columnDefinition = "INT(20) NOT NULL COMMENT '主键'")
    private Integer id;

    @Column(name = "user_name", length = 20, nullable = false, columnDefinition = "VARCHAR(64) NOT NULL COMMENT '用户名'")
    private String userName;

    @Column(name = "password", length = 20, nullable = false, columnDefinition = "VARCHAR(128)  COMMENT '密码'")
    private String password;

    @Column(name = "mobile", length = 20, nullable = false, columnDefinition = "VARCHAR(11)  COMMENT '手机号'")
    private String mobile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
