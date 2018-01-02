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
    private String resultOne;
    @Excel(headerName = "答案2")
    private String resultTwo;
    @Excel(headerName = "答案3")
    private String resultThree;
    @Excel(headerName = "答案4")
    private String resultFour;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResultOne() {
        return resultOne;
    }

    public void setResultOne(String resultOne) {
        this.resultOne = resultOne;
    }

    public String getResultTwo() {
        return resultTwo;
    }

    public void setResultTwo(String resultTwo) {
        this.resultTwo = resultTwo;
    }

    public String getResultThree() {
        return resultThree;
    }

    public void setResultThree(String resultThree) {
        this.resultThree = resultThree;
    }

    public String getResultFour() {
        return resultFour;
    }

    public void setResultFour(String resultFour) {
        this.resultFour = resultFour;
    }
}