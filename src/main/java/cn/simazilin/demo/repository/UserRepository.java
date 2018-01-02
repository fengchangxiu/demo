package cn.simazilin.demo.repository;

import cn.simazilin.demo.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-25 17:53
 * @Project: 阳光物业V1.0
 * @Description:
 */
@Repository
public interface UserRepository extends CrudRepository<User,Integer>{

    @Query(value = "select * from t_user where user_name=?1 and password = ?2",nativeQuery = true)
    User getByUserNameAndPassword(String userName, String password);

    /**
     * 更新用户名
     * @param userName 新用户名
     * @param oldName  旧用户名
     * @return 返回
     */
    @Modifying
    @Query(value = "UPDATE t_user SET user_name = ?2 where user_name=?1",nativeQuery = true)
    int update(String userName,String oldName);
}
