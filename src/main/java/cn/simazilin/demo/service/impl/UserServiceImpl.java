package cn.simazilin.demo.service.impl;

import cn.simazilin.demo.common.util.JSONUtil;
import cn.simazilin.demo.common.util.PasswordUtil;
import cn.simazilin.demo.controller.object.form.UserLoginForm;
import cn.simazilin.demo.controller.object.form.UserSaveForm;
import cn.simazilin.demo.controller.object.result.UserLoginResult;
import cn.simazilin.demo.entity.User;
import cn.simazilin.demo.repository.UserRepository;
import cn.simazilin.demo.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

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
        //writeStream();
        //write();
        //writeBuffer();
        return result;
    }

    @Override
    public Integer save(UserSaveForm form) {
        User user = new User();
        BeanUtils.copyProperties(form,user);
        user.setPassword(PasswordUtil.encrypt(form.getPassword()));
        user = userRepository.save(user);
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class,propagation = Propagation.REQUIRED)
    public Integer update() {
        User user = new User();
        int i= userRepository.update("123","345");
        System.out.println(i);
        return user.getId();
    }

    /**
     * 流操作
     */
    private void writeStream(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("log/demo/demo.txt");
            ObjectOutputStream os = new ObjectOutputStream(fileOutputStream);
            os.writeObject(123456);
            os.close();
        }catch (Exception e){
            System.out.println("数据错误");
        }
        try {
            FileInputStream fileInputStream = new FileInputStream("log/demo/demo.txt");
            ObjectInputStream os = new ObjectInputStream(fileInputStream);
            Object ob = os.readObject();
            Integer userLoginResult = (Integer)ob;
            System.out.print("读取流转化----------");
            System.out.println(JSONUtil.objToStr(userLoginResult));
            os.close();
        }catch (Exception e){
            System.out.println("数据错误");
        }
    }

    /**
     * 数据操作
     */
    private void write(){
        try {
            FileWriter fileWriter = new FileWriter("log/demo/demo1.txt");
            fileWriter.write("我是谁");
            fileWriter.close();
        }catch (Exception e){
            System.out.println("数据错误");
        }
        try {
            FileReader fileReader = new FileReader("log/demo/demo1.txt");
            Object ob = fileReader.read();
            String re = (String)ob;
            System.out.print("读取文件----------");
            System.out.println(re);
            fileReader.close();
        }catch (Exception e){
            System.out.println("数据错误");
        }
    }

    /**
     * 高效数据操作
     */
    private void writeBuffer(){

        try {
            File file = new File("log/demo/demo2.txt");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer =new BufferedWriter(fileWriter);
            writer.write("我是谁，我从哪里来");
            writer.close();
        }catch (Exception e){
            System.out.println("数据错误");
        }

        try {
            FileReader fileReader = new FileReader("log/demo/demo2.txt");
            BufferedReader reader =new BufferedReader(fileReader);
            String line = null;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
        }catch (Exception e){
            System.out.println("数据错误");
        }
    }

}
