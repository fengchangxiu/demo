package cn.simazilin.demo.controller.object.form;

import java.io.Serializable;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-22 11:13
 * @Project: 阳光物业V1.0
 * @Description:
 */
public class TestOneForm implements Serializable{

    private static final long serialVersionUID = -4244738186313066745L;
    private Integer storeId;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

}
