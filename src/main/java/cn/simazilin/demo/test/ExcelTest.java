package cn.simazilin.demo.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

/**
 * @北京联合倍全电子商务有限公司
 * @author: simazilin  @Email: fgihxq@163.com
 * @Date: 2017-12-31 12:18
 * @Project:
 * @Description:
 */
public class ExcelTest {

    public static void main(String[] args) throws Exception {
        FileReader file = new FileReader("/Users/simazilin/Downloads/excel.txt");
        BufferedReader reader =new BufferedReader(file);
        ExcelTest excelTest = new ExcelTest();
        excelTest.getOne(reader);
    }

    private void getOne(BufferedReader reader) throws Exception {
        String line = null;
        while ((line = reader.readLine()) != null){
            String [] line2 = line.split("、");
            List<String> list = Arrays.asList(line2[1].split("_"));
            for (String st:list){

            }
        }
    }
}
