package cn.simazilin.demo.controller.object.result;

import cn.simazilin.demo.common.util.annotation.Excel;

/**
 * @北京联合倍全电子商务有限公司
 * @Author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-09-06 22:23
 * @Project：母婴IPOS V1.0
 * Description:
 */
public class ExportResult {

    @Excel(headerName = "题干（必填）")
    private String question;
    @Excel(headerName = "答案1")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}