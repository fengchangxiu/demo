//package com.horsv.tom.vip.common.util;
//
//import com.horsv.tom.vip.common.exception.BaseException;
//import com.horsv.tom.vip.common.exception.ErrorCode;
//import org.apache.commons.beanutils.PropertyUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.POIXMLDocument;
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.hssf.usermodel.HSSFFont;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.openxml4j.opc.OPCPackage;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.beans.PropertyDescriptor;
//import java.io.*;
//import java.lang.annotation.*;
//import java.lang.reflect.Field;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.sql.Timestamp;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * Excel工具类：
// * 1.excel文件解析
// * 2.excel文件生成
// */
//public class ExcelUtils {
//
//    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
//
//    private static final int DEFAULT_EXCEL_ROW_NUM = 20000;//每一个sheet的最大可识别行数
//    private static final int DEFAULT_CELL_WIDTH = 25;//默认单元格宽度
//    private static final int DEFAULT_ROW_HEIGHT = 20 * 25;//默认行高
//    private static final int DEFAULT_CELL_WIDTH_MULTIPLE = 256;//默认列宽的倍数
//    private static final String DEFAULT_SERIAL_NUM = "序号";//默认序号列title名称
//    private static final String DEFAULT_ROW_FIELD = "row";//默认序号列字段
//    private static final String DATE_FORMAT = "yyyy/MM/dd";//标准的日期格式化字符串
//    private static final String DATETIME_FORMAT = "yyyy-MM-dd hh:mm:ss";//标准的日期时间格式化字符串
//    private static final String DIGIT_LETTER_REGEX = "[0-9a-zA-Z]+";//数字+字母正则
//
//    /**
//     * 通过controller获取{@link MultipartFile} 对象
//     * 通过{@link MultipartFile}获取数据流
//     * @param file 通过{@link MultipartFile}获取数据流
//     * @param clazz 数据对象
//     * @return 返回数据集
//     * @throws Exception 异常
//     */
//    public static <T> List<T> loadFromExcel(MultipartFile file, Class<T> clazz) throws Exception {
//        InputStream in = file.getInputStream();
//        return loadFromExcel(in, clazz);
//
//    }
//
//    /**
//     * 通过controller获取{@link MultipartFile} 对象
//     * 通过{@link MultipartFile}获取数据流
//     * 增加是否使用扩展列的判断
//     * @param file {@link MultipartFile 对象}
//     * @param clazz 数据对象
//     * @param useExtendColumn 是否使用扩展字段
//     * @return 返回数据集
//     * @throws Exception 异常
//     */
//    public static <T> List<T> loadFromExcel(MultipartFile file, Class<T> clazz, boolean useExtendColumn ) throws Exception {
//        InputStream in = file.getInputStream();
//        return loadFromExcel(in, clazz,useExtendColumn);
//
//    }
//
//    /**
//     * 通过文件读取excel
//     *
//     * @param filePath 文件路径
//     * @param clazz 数据类
//     * @return 返回数据集
//     * @throws Exception 异常
//     */
//    public static <T> List<T> loadFromExcel(String filePath, Class<T> clazz) throws Exception {
//        FileInputStream in = new FileInputStream(new File(filePath));
//        return loadFromExcel(in, clazz);
//    }
//
//    /**
//     * 通过数据流读取excel
//     * 不使用扩展字段
//     * @param in 输入流
//     * @param clazz 数据类
//     * @param <T> 数据对象
//     * @return 返回数据集
//     * @throws Exception 异常
//     */
//    public static <T> List<T> loadFromExcel(InputStream in, Class<T> clazz) throws Exception {
//        return loadFromExcel(in, clazz,false);
//    }
//
//    /**
//     * 通过数据流读取excel
//     * 使用扩展字段
//     * @param in 输入流
//     * @param clazz 数据类
//     * @param useExtendColumn 是否使用扩展字段
//     * @param <T> 数据对象
//     * @return 返回数据集
//     * @throws Exception 异常
//     */
//    public static <T> List<T> loadFromExcel(InputStream in, Class<T> clazz,boolean useExtendColumn) throws Exception {
//
//        Map<String, String> headerNames = new HashMap<String, String>();
//        Map<String, String> extendColumns = new HashMap<String, String>();
//        /**根据clazz获取excel需要的表头**/
//        getTitles(in, clazz, useExtendColumn, headerNames, extendColumns);
//
//        /**根据数据流新建excel对象**/
//        Workbook xwb = newExcelByInputStream(in);
//
//        /**获取xwb中数据对象**/
//        Sheet sheet = xwb.getSheetAt(0);
//        Row headerRow = getHeaderRow(sheet);
//
//        /**根据clazz注解文字和excel表头比较,校验表头是否一致**/
//        validExcelFormat(headerRow, headerNames);
//
//        /**转移数据，从excel到list**/
//        List<T> list = transferExcelData(clazz, headerNames, extendColumns, sheet, headerRow);
//
//        return list;
//    }
//
//    /**转移数据，从excel到list**/
//    private static <T> List<T> transferExcelData(Class<T> clazz, Map<String, String> headerNames, Map<String, String> extendColumns, Sheet sheet, Row headerRow) throws Exception {
//        List<T> list = new ArrayList<>();
//        boolean hasOneRight = false;
//        for (int i = sheet.getFirstRowNum() + 1; i <= 5000; i++) {
//
//            Row row = sheet.getRow(i);
//            if( null == row ){ break; }
//            T t = clazz.newInstance();
//
//            //原来逻辑:--整行为空,则跳出循环
//            //现在的逻辑:--整行为空时,结束本层循环读取下一行
//            /**设置每一行的数据**/
//            if( setExcelLineData(headerNames, headerRow, row, t) ){
//                continue;
//            }
//            /**扩展字段赋值**/
//            setExtendColumnData(extendColumns, i, t);
//            list.add(t);
//
//            hasOneRight = true;
//        }
//        /**至少有一行数据**/
//        if( !hasOneRight ){
//            throw new BaseException(ErrorCode.Excel.IMPORT_ERROR_CODE,ErrorCode.Excel.IMPORT_TEMPLATE_MISMATCH_MSG);
//        }
//        if( list.size() > 5000 ){
//            throw new BaseException(ErrorCode.Excel.IMPORT_ROW_LIMIT_CODE,ErrorCode.Excel.IMPORT_ROW_LIMIT_MSG);
//        }
//        return list;
//    }
//
//    /**设置每一行的数据**/
//    private static <T> boolean setExcelLineData(Map<String, String> headerNames, Row headerRow, Row row, T t) throws Exception {
//        boolean isAllBlank = true;
//        for (int j = 0; j < row.getLastCellNum(); j++) {
//            Cell cell = row.getCell(j);
//            //获取首行 对应列的字段
//            String propertyName = headerNames.get(getCellStringValue(headerRow.getCell(j),String.class));
//
//            if (StringUtils.isNotBlank(propertyName)) {
//                //获取当前列对应的bean字段
//                PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(t, propertyName);
//                //获取bean字段的数据类型
//                Class<?> type = descriptor.getPropertyType();
//
//                String cellValue = getCellStringValue(cell,type);
//                if( !StringUtils.isBlank(cellValue)){
//                    isAllBlank = false;
//                }
//
//                //给当前行对应的bean对象的某个字段赋值
//                PropertyUtils.setSimpleProperty(t, propertyName, stringToObject(type, cellValue));
//            }
//        }
//        return isAllBlank;
//    }
//
//    /**扩展字段赋值**/
//    private static <T> void setExtendColumnData(Map<String, String> extendColumns, int i, T t) throws Exception {
//        if( extendColumns.size() > 0 ){
//            for (Map.Entry<String, String> entry : extendColumns.entrySet()) {
//                String value = entry.getValue();
//                if( DEFAULT_ROW_FIELD.equals(value) ){
//                    PropertyUtils.setSimpleProperty(t, value, i);
//                }else{
//                    PropertyUtils.setSimpleProperty(t, value, null);
//                }
//            }
//        }
//    }
//
//    /**根据clazz注解文字和excel表头比较,校验是否符合**/
//    private static void validExcelFormat(Row headerRow, Map<String, String> headerNames) throws BaseException {
//        boolean flag = false;
//        Set<String> keySet = headerNames.keySet();
//        int rowNum = headerRow.getLastCellNum();
//        if(keySet.size()!=0 && rowNum>0) {
//            for (int i = 0; i < rowNum; i++) {
//                String cell = headerRow.getCell(i).getStringCellValue();
//                if (!keySet.contains(cell)) {
//                    flag=true;
//                }
//                if(flag){
//                    throw new BaseException(ErrorCode.Excel.TEMPLET_IS_WRONG_CODE,ErrorCode.Excel.TEMPLET_IS_WRONG_MSG);
//                }
//            }
//        }
//    }
//
//    /**
//     * 获取第一行
//     * @param sheet excel - sheet对象
//     * @return 返回行对象
//     * @throws BaseException 异常
//     */
//    private static Row getHeaderRow(Sheet sheet) throws BaseException {
//        Row headerRow = sheet.getRow(sheet.getFirstRowNum());
//
//        //导入Excel文件为空
//        if(headerRow==null){
//            throw new BaseException(ErrorCode.Excel.HEADROW_IS_EMPTY_CODE,ErrorCode.Excel.HEADROW_IS_EMPTY_MSG);
//        }
//        return headerRow;
//    }
//
//    /**
//     * 数据写出到excel文件
//     * @param out 输出流
//     * @param clazz 数据类
//     * @param list 数据集
//     * @param <T> 数据对象
//     * @return 返回成功或失败
//     * @throws Exception 异常
//     */
//    public static <T> boolean writeToExcel(OutputStream out, Class<T> clazz, List<T> list) throws Exception {
//        return writeToExcel(out, clazz, list,false);
//    }
//
//    /**
//     * 数据写出到excel文件
//     * @param out 输出流
//     * @param clazz 数据类
//     * @param list 数据列表
//     * @param useExtendColumn 是否使用扩展字段
//     * @param <T> 数据列中-对应的数据对象
//     * @return 返回成功或失败
//     * @throws Exception
//     */
//    public static <T> boolean writeToExcel(OutputStream out, Class<T> clazz, List<T> list,boolean useExtendColumn) throws Exception {
//
//        if (list == null) {
//            throw new RuntimeException("导出的内容为空");
//        }
//        if (list.isEmpty()) {
//            throw new RuntimeException("导出的内容为空");
//        }
//        Workbook workbook = writeToWorkbook(clazz, list, useExtendColumn);
//        if( null == workbook ){
//            return false;
//        }
//        boolean returnVal = false;
//        try {
//            workbook.write(out);
//            returnVal = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return returnVal;
//    }
//
//    /**
//     * 写入数据到excel对象中
//     * @param clazz 数据类
//     * @param list 数据列表
//     * @param useExtendColumn 是否使用扩展字段
//     * @param <T> 数据对象泛型
//     * @return 返回workbook
//     * @throws Exception
//     */
//    public static <T> Workbook writeToWorkbook(Class<T> clazz, List<T> list,boolean useExtendColumn) throws Exception {
//
//        Map<String, String> headerTitles = new HashMap<>();
//        Field[] fields = clazz.getDeclaredFields();
//        List<String> headerNames = getTitles(headerTitles, fields,useExtendColumn);
//        if (headerTitles.isEmpty()) {
//            return null;
//        }
//
//        Workbook workbook = new HSSFWorkbook();
//        Sheet sheet = null;
//        CellStyle headerStyle = newTableHeaderStyle(workbook);
//        CellStyle evenStyle = newTableEvenCellStyle(workbook);
//        CellStyle oddStyle = newTableOddCellStyle(workbook);
//
//        Row row;
//        int rowNumber = 0;
//        //默认行高
//        Short rowHeight = DEFAULT_ROW_HEIGHT;
//        Map<Integer,Integer> columnWidths = new LinkedHashMap<>();
//        for (int i = 0, length = list.size(); i < length; i++) {
//            if (rowNumber == 0 || rowNumber % DEFAULT_EXCEL_ROW_NUM == 0) {
//                sheet = setExcelTitleDatas(headerNames, workbook, headerStyle, rowHeight, columnWidths);
//                rowNumber = 1;
//            }
//
//            row = sheet.createRow(rowNumber % DEFAULT_EXCEL_ROW_NUM);
//            row.setHeight(rowHeight);
//            for (int j = 0; j < headerNames.size(); j++) {
//                setExcelCellData(list, headerTitles, headerNames, evenStyle, oddStyle, row, rowNumber, columnWidths, i, j);
//            }
//
//            rowNumber++;
//            setColumnsWidth(sheet, columnWidths);
//        }
//        return workbook;
//    }
//
//    private static <T> void setExcelCellData(List<T> list, Map<String, String> map, List<String> headerNames, CellStyle evenStyle, CellStyle oddStyle, Row row, int rowNumber, Map<Integer, Integer> columnWidths, int i, int j) throws Exception {
//        Cell cell = row.createCell(j);
//        if( i % 2 == 0 ){
//            cell.setCellStyle(oddStyle);
//        }else{
//            cell.setCellStyle(evenStyle);
//        }
//        String headerName = headerNames.get(j);
//        if (headerName.equals(DEFAULT_SERIAL_NUM)) {
//            setCellValueByType(cell, rowNumber % DEFAULT_EXCEL_ROW_NUM);
//        } else {
//            T obj = list.get(i);
//            Object value = PropertyUtils.getSimpleProperty(obj, map.get(headerName));
//            gatherColumnsWidth(columnWidths, headerName, String.valueOf(value), j);
//            setCellValueByType(cell, value);
//        }
//    }
//
//    /**
//     * 设置表头
//     * @param headerNames 表头数据
//     * @param workbook excel对象
//     * @param headerStyle 表头单元格
//     * @param rowHeight 行高数据
//     * @param columnWidths 列宽数据
//     * @return
//     */
//    private static Sheet setExcelTitleDatas(List<String> headerNames, Workbook workbook, CellStyle headerStyle, Short rowHeight, Map<Integer, Integer> columnWidths) {
//        Sheet sheet;
//        Row row;
//        sheet = workbook.createSheet();
//        row = sheet.createRow(0);
//        row.setHeight(rowHeight);
//        for (int j = 0; j < headerNames.size(); j++) {
//            Cell headCell = row.createCell(j);
//            headCell.setCellStyle(headerStyle);
//            String headerName = headerNames.get(j);
//            gatherColumnsWidth(columnWidths, headerName, headerName, j);
//            setCellValueByType(headCell, headerNames.get(j));
//        }
//        return sheet;
//    }
//
//    /**
//     * 写数据到WorkBook中
//     * @param clazz 数据集中对象类型
//     * @param list 数据集
//     * @param useExtendColumn 扩展列
//     * @param <T> 数据集对象
//     * @return 返回excel-workbook
//     * @throws Exception 异常
//     */
//    public static <T> Workbook writeToWorkbook2(Class<T> clazz, List<T> list,boolean useExtendColumn) throws Exception {
//
//        Map<String, String> headerTitles = new HashMap<>();
//        Field[] fields = clazz.getDeclaredFields();
//        List<String> headerNames = getTitles(headerTitles, fields,useExtendColumn);
//        if (headerTitles.isEmpty()) {
//            return null;
//        }
//        Workbook workbook = new HSSFWorkbook();
//        CellStyle headerStyle = newTableHeaderStyle(workbook);
//        CellStyle evenStyle = newTableEvenCellStyle(workbook);
//        CellStyle oddStyle = newTableOddCellStyle(workbook);
//
//        Row row;
//        int rowNumber = 0;
//        Sheet sheet = null;
//        Short rowHeight = DEFAULT_ROW_HEIGHT;
//        Map<Integer,Integer> columnWidths = new LinkedHashMap<>();
//        for (int i = 0, length = list.size(); i < length; i++) {
//
//            if (rowNumber == 0 || rowNumber % DEFAULT_EXCEL_ROW_NUM == 0) {
//                sheet = setExcelTitleDatas(headerNames, workbook, headerStyle, rowHeight, columnWidths);
//                rowNumber = 1;
//            }
//
//            row = sheet.createRow(rowNumber % DEFAULT_EXCEL_ROW_NUM);
//            row.setHeight(rowHeight);
//            for (int j = 0; j < headerNames.size(); j++) {
//                setExcelCellData2(list, headerTitles, headerNames, evenStyle, oddStyle, sheet, row, rowNumber, columnWidths, i, j);
//            }
//
//            rowNumber++;
//        }
//        return workbook;
//    }
//
//    /**设置excel单元格数据**/
//    private static <T> void setExcelCellData2(List<T> list, Map<String, String> map, List<String> headerNames, CellStyle evenStyle, CellStyle oddStyle, Sheet sheet, Row row, int rowNumber, Map<Integer, Integer> columnWidths, int i, int j) throws Exception {
//        Cell cell = row.createCell(j);
//        if( i % 2 == 0 ){
//            cell.setCellStyle(oddStyle);
//        }else{
//            cell.setCellStyle(evenStyle);
//        }
//        String headerName = headerNames.get(j);
//        if (headerName.equals(DEFAULT_SERIAL_NUM)) {
//            setCellValueByType(cell, rowNumber % DEFAULT_EXCEL_ROW_NUM);
//        } else {
//            T obj = list.get(i);
//            Object value = PropertyUtils.getSimpleProperty(obj, map.get(headerName));
//            /**收集列宽度数据**/
//            gatherColumnsWidth(columnWidths, headerName, String.valueOf(value), j);
//            /**在这里直接对列宽度进行设置**/
//            Integer width = columnWidths.get(j);
//            sheet.setColumnWidth(j ,(width)* DEFAULT_CELL_WIDTH_MULTIPLE);
//            setCellValueByType(cell, value);
//        }
//    }
//
//    /**根据数据流新建excel对象**/
//    private static Workbook newExcelByInputStream(InputStream inp) throws IOException, InvalidFormatException {
//        if (!inp.markSupported()) {
//            inp = new PushbackInputStream(inp, 8);
//        }
//        //操作Excel2003以前（包括2003）的版本，扩展名是.xls
//        if (POIFSFileSystem.hasPOIFSHeader(inp)) {
//            return new HSSFWorkbook(inp);
//        }
//        //操作Excel2007的版本，扩展名是.xlsx
//        if (POIXMLDocument.hasOOXMLHeader(inp)) {
//            return new XSSFWorkbook(OPCPackage.open(inp));
//        }
//        throw new IllegalArgumentException("Excel文件格式错误,请使用2003版本的Excel上传！");
//    }
//
//    /**
//     * 获取单元格的值
//     * 单元格类型：
//     * Cell.CELL_TYPE_BLANK 空白
//     * Cell.CELL_TYPE_NUMERIC 数字类型
//     * Cell.CELL_TYPE_STRING 字符类型
//     * Cell.CELL_TYPE_FORMULA 公式类型
//     * Cell.CELL_TYPE_BOOLEAN BOOL类型
//     * Cell.CELL_TYPE_ERROR 单元格格式错误类型
//     * @param cell 单元格对象
//     * @param clazz 单元格数据类型
//     * @return 返回转换之后的字符串值
//     */
//    private static String getCellStringValue(Cell cell,Class clazz) {
//        try {
//            if (null == cell) {
//                return "";
//            }
//            if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
//                return "";
//            }
//            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                if(HSSFDateUtil.isCellDateFormatted(cell)){
//                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
//                    return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
//                }
//                DecimalFormat df = new DecimalFormat("0");
//                if(clazz.equals(BigDecimal.class)){
//                    df = new DecimalFormat("0.00");
//                }
//                //TODO BigDecimal类型数据转换时,不会四舍五入
//                df.setRoundingMode(RoundingMode.DOWN);
//                return df.format(cell.getNumericCellValue());
//            }
//            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
//                return cell.getStringCellValue();
//            }
//            if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
//                return cell.getCellFormula();
//            }
//            if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
//                return String.valueOf(cell.getBooleanCellValue());
//            }
//            if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
//                return String.valueOf(cell.getErrorCellValue());
//            }
//            return cell.getStringCellValue();
//        } catch (RuntimeException e) {
//            logger.error("单元格字符值获取,错误信息{}",e.getMessage());
//        }
//        return "";
//    }
//
//    /**
//     * 字符串转对象
//     * @param clazz 对象类
//     * @param str 字符串内容
//     * @return 返回转换之后的对象值
//     * @throws Exception 异常
//     */
//    private static Object stringToObject(Class<?> clazz, String str) throws Exception {
//        Object o = str;
//        if (clazz == BigDecimal.class) {
//            if( StringUtils.isBlank(str) ){
//                o = BigDecimal.valueOf(0);
//            }else{
//                o = new BigDecimal(str);
//            }
//        } else if (clazz == Long.class) {
//            if( StringUtils.isBlank(str) ){
//                o = 0;
//            }else{
//                o = new Long(str);
//            }
//        } else if (clazz == Integer.class) {
//            if( StringUtils.isBlank(str) ){
//                o = 0;
//            }else{
//                o = new Integer(str);
//            }
//        } else if (clazz == int.class) {
//            o = Integer.parseInt(str);
//        } else if (clazz == float.class) {
//            o = Float.parseFloat(str);
//        } else if (clazz == boolean.class) {
//            o = Boolean.parseBoolean(str);
//        } else if (clazz == byte.class) {
//            o = Byte.parseByte(str);
//        }
//        return o;
//    }
//
//    /**根据clazz获取excel需要的表头**/
//    private static <T> void getTitles(InputStream in, Class<T> clazz, boolean useExtendColumns, Map<String, String> headerNames, Map<String, String> extendColumns) throws IOException, BaseException {
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field m : fields) {
//            if (m.isAnnotationPresent(Excel.class)) {
//                // 获取该字段的注解对象
//                Excel anno = m.getAnnotation(Excel.class);
//                headerNames.put(anno.headerName(), m.getName());
//            }
//            if( useExtendColumns ){
//                if (m.isAnnotationPresent(ExcelExtend.class)) {
//                    // 获取该字段的注解对象
//                    ExcelExtend anno = m.getAnnotation(ExcelExtend.class);
//                    extendColumns.put(anno.headerName(), m.getName());
//                }
//            }
//        }
//        if (headerNames.isEmpty()) {
//            in.close();
//            throw new BaseException(ErrorCode.Excel.IMPORT_ERROR_CODE,ErrorCode.Excel.IMPORT_TEMPLATE_MISMATCH_MSG);
//        }
//    }
//
//    /**
//     * 从对象中获取Excel表头
//     * @param headerTitles title名称和字段名称的对应关系
//     * @param fields 对象对应的字段
//     * @param useExtendColumn 是否使用扩展字段列
//     * @return 返回Title名称
//     */
//    private static List<String> getTitles(Map<String, String> headerTitles, Field[] fields, boolean useExtendColumn) {
//        List<String> headerNames = new ArrayList<>();
//        for (Field m : fields) {
//            if (m.isAnnotationPresent(Excel.class)) {
//                Excel anno = m.getAnnotation(Excel.class);
//                headerTitles.put(anno.headerName(), m.getName());
//                headerNames.add(anno.headerName());
//            }
//            if( useExtendColumn ){
//                if (m.isAnnotationPresent(ExcelExtend.class)) {
//                    ExcelExtend anno = m.getAnnotation(ExcelExtend.class);
//                    headerTitles.put(anno.headerName(), m.getName());
//                    headerNames.add(anno.headerName());
//                }
//            }
//        }
//        return headerNames;
//    }
//
//    /**
//     * 根据单元格类型，设置单元格的值
//     * @param cell 单元格对象
//     * @param value 单元格值
//     */
//    private static void setCellValueByType(Cell cell, Object value) {
//        if (null == value) {
//            return;
//        }
//        if (value instanceof BigDecimal) {
//            cell.setCellValue(String.valueOf(value));
//        } else if (value instanceof Long) {
//            cell.setCellValue(((Long) value));
//        } else if (value instanceof Integer) {
//            cell.setCellValue(((Integer) value));
//        } else if (value instanceof Float) {
//            cell.setCellValue(String.valueOf(value));
//        } else if (value instanceof Double) {
//            cell.setCellValue(String.valueOf(value));
//        } else if (value instanceof Boolean) {
//            cell.setCellValue((Boolean) value);
//        } else if (value instanceof Byte) {
//            cell.setCellValue(((Byte) value));
//        } else if (value instanceof String) {
//            cell.setCellValue(String.valueOf(value));
//        } else if (value instanceof Timestamp) {
//            SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_FORMAT);
//            cell.setCellValue(dateFormat.format(value));
//        }
//    }
//
//    /**
//     * 新建表头单元格样式
//     * @param wb workbook对象
//     * @return 返回单元格格式
//     */
//    private static CellStyle newTableHeaderStyle(Workbook wb) {
//        CellStyle style = wb.createCellStyle();
//        style.setBorderRight(CellStyle.BORDER_THIN);
//        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderBottom(CellStyle.BORDER_THIN);
//        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderLeft(CellStyle.BORDER_THIN);
//        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderTop(CellStyle.BORDER_THIN);
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
//        style.setWrapText(true);
//        Font font = wb.createFont();
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//        style.setFont(font);
//        return style;
//    }
//
//    /**
//     * 新建偶数行单元格样式
//     * @param wb workbook对象
//     * @return 返回单元格格式
//     */
//    private static CellStyle newTableEvenCellStyle(Workbook wb) {
//        CellStyle style = wb.createCellStyle();
//        style.setBorderRight(CellStyle.BORDER_THIN);
//        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderBottom(CellStyle.BORDER_THIN);
//        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderLeft(CellStyle.BORDER_THIN);
//        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderTop(CellStyle.BORDER_THIN);
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//        style.setWrapText(true);
//        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        return style;
//    }
//
//    /**
//     * 新建奇数行单元格样式
//     * @param wb workbook对象
//     * @return 返回单元格格式
//     */
//    private static CellStyle newTableOddCellStyle(Workbook wb) {
//        CellStyle style = wb.createCellStyle();
//        style.setBorderRight(CellStyle.BORDER_THIN);
//        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderBottom(CellStyle.BORDER_THIN);
//        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderLeft(CellStyle.BORDER_THIN);
//        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderTop(CellStyle.BORDER_THIN);
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//        style.setWrapText(true);
//        return style;
//    }
//
//    /**
//     * 收集列宽数据
//     * @param columnWidths 列宽数据列表
//     * @param headerName 表头名称
//     * @param content 单元格内容
//     * @param j 列编号
//     */
//    private static void gatherColumnsWidth(Map<Integer, Integer> columnWidths, String headerName, String content, int j) {
//        int length = content.getBytes().length;
//        if( headerName.equalsIgnoreCase(content) ){
//            if( headerName.indexOf("（")>0 ){
//                headerName = headerName.replace("（", "\r\n（");
//                String lengthStr = headerName.substring(0, headerName.indexOf("（"));
//                length = lengthStr.getBytes().length;
//            }
//            columnWidths.put(j,length);
//        }else{
//            if( content.matches(DIGIT_LETTER_REGEX) ){
//                length = length * 2;
//            }
//            int oldLength = null==columnWidths.get(j)?0:columnWidths.get(j);
//            if( length > DEFAULT_CELL_WIDTH){
//                oldLength = oldLength > DEFAULT_CELL_WIDTH ?oldLength: DEFAULT_CELL_WIDTH;
//            }else{
//                oldLength = oldLength > length?oldLength:length;
//            }
//            columnWidths.put(j,oldLength);
//        }
//
//    }
//
//    /**
//     * 统一设置列宽
//     * @param sheet
//     * @param columnWidths
//     */
//    private static void setColumnsWidth(Sheet sheet, Map<Integer, Integer> columnWidths) {
//        for( Map.Entry<Integer,Integer> entry: columnWidths.entrySet() ){
//            sheet.setColumnWidth(entry.getKey(), (entry.getValue())*256);
//        }
//    }
//
//    @Documented
//    @Target(ElementType.FIELD)
//    @Retention(RetentionPolicy.RUNTIME)
//    public @interface Excel {
//        String headerName() default "";
//    }
//
//    @Documented
//    @Target(ElementType.FIELD)
//    @Retention(RetentionPolicy.RUNTIME)
//    public @interface ExcelExtend {
//        String headerName() default "";
//    }
//
//}
