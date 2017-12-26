package cn.simazilin.demo.controller;

import cn.simazilin.demo.controller.object.form.TestOneForm;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-21 18:45
 * @Project: 阳光物业V1.0
 * @Description:
 */
@RestController
@RequestMapping("/first")
public class TestController {

    @RequestMapping(value = "/testone",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getOne(HttpServletRequest request, HttpServletResponse response,TestOneForm form){
        System.out.println(form.getStoreId());
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(30*60);
        session.setAttribute("name","fgihxq");
        return "1233";
    }
}
