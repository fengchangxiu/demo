package cn.simazilin.demo.controller;

import cn.simazilin.demo.common.config.Conf;
import cn.simazilin.demo.common.config.redis.RedisUtils;
import cn.simazilin.demo.common.util.DateUtils;
import cn.simazilin.demo.common.util.ExcelUtil;
import cn.simazilin.demo.common.util.JSONUtil;
import cn.simazilin.demo.controller.object.form.UserLoginForm;
import cn.simazilin.demo.controller.object.form.UserSaveForm;
import cn.simazilin.demo.controller.object.result.ExportResult;
import cn.simazilin.demo.controller.object.result.UserLoginResult;
import cn.simazilin.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-25 18:23
 * @Project: 阳光物业V1.0
 * @Description:
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private Conf conf;
    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public Object getLogin(@Valid UserLoginForm form) {
        UserLoginResult result = userService.getLogin(form);
        logger.debug("环境： 【{}】", conf.getEnv());
        logger.debug("返回参数：[{}]", JSONUtil.objToStr(result));
        return result;
    }

    @RequestMapping(value = "/save", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public Object save(@Valid UserSaveForm form) {

        return userService.save(form);
    }

    @RequestMapping(value = "/export", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ExportResult> results = new ArrayList<>();
        //redis测试
        redisUtils.set("demo_question", "redis_question");
        redisUtils.set("demo_result", "redis_result");
        if (Objects.nonNull(redisUtils.get("demo_question")) && Objects.nonNull(redisUtils.get("demo_result"))) {
            ExportResult redisResult = new ExportResult();
            redisResult.setQuestion(redisUtils.get("demo_question").toString());
            redisResult.setResult(redisUtils.get("demo_result").toString());
            results.add(redisResult);
            System.out.println("reids存在");
        }

        ExportResult result = new ExportResult();
        result.setQuestion("123question");
        result.setResult("123result");
        results.add(result);
        try {
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            //2.设置文件头：
            // 转码中文
            response.setContentType("application/form-data");
            String fileName = new String("会员数据".getBytes("utf-8"), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName + DateUtils.fromTimestampToString(DateUtils.getCurrentTimeSecond()) + ".xls\"");
            // 输出excel
            ExcelUtil.writeToExcel(response.getOutputStream(), ExportResult.class, results);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
