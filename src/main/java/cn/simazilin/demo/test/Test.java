package cn.simazilin.demo.test;

import cn.simazilin.demo.common.util.JSONUtil;
import cn.simazilin.demo.controller.object.result.UserLoginResult;

import java.util.TreeSet;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-27 20:19
 * @Project:
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        TreeSet<UserLoginResult> results = test.getList();
        for (UserLoginResult result: results){
            System.out.println(JSONUtil.objToStr(result));
        }
    }

    private TreeSet<UserLoginResult> getList(){
        TreeSet<UserLoginResult> results = new TreeSet<>();
        for (int i=0;i<10;i++){
            UserLoginResult result = new UserLoginResult();
            result.setMobile(new StringBuilder().append(i).toString());
            result.setUserName(new StringBuilder().append(i).toString());
            results.add(result);
        }
        return results;
    }
}
