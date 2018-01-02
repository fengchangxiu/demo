package cn.simazilin.demo.common.util;

import cn.simazilin.demo.common.exception.BaseException;
import cn.simazilin.demo.common.exception.ErrorCode;
import cn.simazilin.demo.common.util.annotation.Excel;
import cn.simazilin.demo.common.util.annotation.ExcelExtend;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author simazilin
 */

public class ExcelUtil {

    private static final Integer MAX_CELL_WIDTH = 25;

    /**
     * 数据导入
     *
     * @param file  1
     * @param clazz 1
     * @return 1
     * @throws Exception 1
     */
    public static <T> List<T> loadFromExcel(MultipartFile file, Class<T> clazz) throws Exception {

        InputStream in = file.getInputStream();
        return loadFromExcel(in, clazz);

    }

    /**
     * 数据导入
     *
     * @param file
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> loadFromExcel(MultipartFile file, Class<T> clazz, boolean useExtendColumns) throws Exception {

        InputStream in = file.getInputStream();
        return loadFromExcel(in, clazz, useExtendColumns);

    }

    /**
     * 数据导入
     *
     * @param filePath
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> loadFromExcel(String filePath, Class<T> clazz) throws Exception {

        FileInputStream in = new FileInputStream(new File(filePath));
        return loadFromExcel(in, clazz);

    }

    public static <T> List<T> loadFromExcel(InputStream in, Class<T> clazz) throws Exception {
        return loadFromExcel(in, clazz, false);
    }

    /**
     * 数据导入
     *
     * @param in
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> loadFromExcel(InputStream in, Class<T> clazz, boolean useExtendColumns) throws Exception {

        Map<String, String> headerNames = new HashMap<String, String>();
        Map<String, String> extendColumns = new HashMap<String, String>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field m : fields) {
            if (m.isAnnotationPresent(Excel.class)) {
                // 获取该字段的注解对象
                Excel anno = m.getAnnotation(Excel.class);
                headerNames.put(anno.headerName(), m.getName());
            }
            if (useExtendColumns) {
                if (m.isAnnotationPresent(ExcelExtend.class)) {
                    // 获取该字段的注解对象
                    ExcelExtend anno = m.getAnnotation(ExcelExtend.class);
                    extendColumns.put(anno.headerName(), m.getName());
                }
            }
        }
        if (headerNames.isEmpty()) {
            in.close();
            throw new BaseException(ErrorCode.Excel.IMPORT_ERROR_CODE, ErrorCode.Excel.IMPORT_TEMPLATE_MISMATCH_MSG);
        }

        List<T> list = new ArrayList<T>();

        // HSSFWorkbook xwb = new HSSFWorkbook(in);

        //创建一个Workbook
        Workbook xwb = newExcelByInputStream(in);

        Sheet sheet = xwb.getSheetAt(0);

        Row headerRow = sheet.getRow(sheet.getFirstRowNum());

        //导入Excel文件为空
        if (headerRow == null) {
            throw new BaseException(ErrorCode.Excel.HEADROW_IS_EMPTY_CODE, ErrorCode.Excel.HEADROW_IS_EMPTY_MSG);
        }
        //headRow和headerNames比较
        Set<String> keySet = headerNames.keySet();
        int rowNum = headerRow.getLastCellNum();
        boolean flag = false;
        //判断上传的文件是否相符
        if (keySet.size() != 0 && rowNum > 0) {
            for (int i = 0; i < rowNum; i++) {
                String cell = headerRow.getCell(i).getStringCellValue();
                if (!keySet.contains(cell)) {
                    flag = true;
                }
                if (flag) {
                    throw new BaseException(ErrorCode.Excel.TEMPLET_IS_WRONG_CODE, ErrorCode.Excel.TEMPLET_IS_WRONG_MSG);
                }
            }
        }
        boolean hasOneRight = false;
        for (int i = sheet.getFirstRowNum() + 1; i <= 5000; i++) {
            Row row = sheet.getRow(i);
            if (null == row) {
                break;
            }
            //初始化一个bean对象,用于存放当前行数据,其中,每一列的数据对应这个bean对象的某个字段
            T t = clazz.newInstance();
            boolean isAllBlank = true;
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                //获取首行 对应列的字段
                String propertyName = headerNames.get(getCellStringValue(headerRow.getCell(j), String.class));

                if (StringUtils.isNotBlank(propertyName)) {
                    hasOneRight = true;
                    //获取当前列对应的bean字段
                    PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(t, propertyName);
                    //获取bean字段的数据类型
                    Class<?> type = descriptor.getPropertyType();

                    //TODO BigDecimal类型数据转换时,会四舍五入
                    String cellValue = getCellStringValue(cell, type);
                    if (!StringUtils.isBlank(cellValue)) {
                        isAllBlank = false;
                    }

                    //给当前行对应的bean对象的某个字段赋值
                    PropertyUtils.setSimpleProperty(t, propertyName, stringToObject(type, cellValue));
                }
            }
            // TODO
            //原来逻辑:--整行为空,则跳出循环
            //现在的逻辑:--整行为空时,结束本层循环读取下一行
            if (isAllBlank) {
                continue;
            }

            //
            if (extendColumns.size() > 0) {
                for (Map.Entry<String, String> entry : extendColumns.entrySet()) {
                    String value = entry.getValue();
                    if ("row".equals(value)) {
                        PropertyUtils.setSimpleProperty(t, value, i);
                    } else {
                        PropertyUtils.setSimpleProperty(t, value, null);
                    }
                }
            }
            list.add(t);
        }

        if (!hasOneRight) {
            throw new BaseException(ErrorCode.Excel.IMPORT_ERROR_CODE, ErrorCode.Excel.IMPORT_TEMPLATE_MISMATCH_MSG);
        }
        if (list.size() > 5000) {
            throw new BaseException(ErrorCode.Excel.IMPORT_QUANTITY_CODE, ErrorCode.Excel.IMPORT_QUANTITY_MSG);
        }

        return list;
    }

    /**
     * 数据导入
     * 适用于标题行不在文件第一行的的情况
     * 例如：
     *
     * @param in
     * @param clazz
     * @param index 标题所在行 例如：标题在第二行 设置index=2；
     * @param max   支持处理最大记录数量，超过抛出异常
     * @return
     * @throws Exception
     */
    public static <T> List<T> loadFromExcel(InputStream in, Class<T> clazz, int index, int max) throws Exception {
        Map<String, String> headerNames = new HashMap<String, String>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field m : fields) {
            if (m.isAnnotationPresent(Excel.class)) {
                // 获取该字段的注解对象
                Excel anno = m.getAnnotation(Excel.class);
                headerNames.put(anno.headerName(), m.getName());
            }
        }
        if (headerNames.isEmpty()) {
            in.close();
            throw new BaseException(ErrorCode.Excel.IMPORT_ERROR_CODE, ErrorCode.Excel.IMPORT_TEMPLATE_MISMATCH_MSG);
        }
        List<T> list = new ArrayList<T>();
        // 创建一个Workbook
        Workbook xwb = newExcelByInputStream(in);
        Sheet sheet = xwb.getSheetAt(0);
        Row headerRow = sheet.getRow(index - 1);
        // 导入Excel文件为空
        if (headerRow == null) {
            throw new BaseException(ErrorCode.Excel.HEADROW_IS_EMPTY_CODE, ErrorCode.Excel.HEADROW_IS_EMPTY_MSG);
        }
        // 数据量超过限制
        if (sheet.getLastRowNum() - index > max) {
            throw new BaseException(ErrorCode.Excel.IMPORT_QUANTITY_CODE, ErrorCode.Excel.IMPORT_QUANTITY_MSG);
        }
        // headRow和headerNames比较
        Set<String> keySet = headerNames.keySet();
        int rowNum = headerRow.getLastCellNum();
        // 判断上传的文件是否相符
        if (keySet.size() != 0 && rowNum > 0) {
            for (int i = 0; i < rowNum; i++) {
                String cell = headerRow.getCell(i).getStringCellValue();
                if (!keySet.contains(cell)) {
                    throw new BaseException(ErrorCode.Excel.TEMPLET_IS_WRONG_CODE, ErrorCode.Excel.TEMPLET_IS_WRONG_MSG);
                }
            }
        }
        for (int i = index; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            // 初始化一个bean对象,用于存放当前行数据,其中,每一列的数据对应这个bean对象的某个字段
            T t = clazz.newInstance();
            boolean isAllBlank = true;
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                // 获取首行 对应列的字段
                String propertyName = headerNames.get(getCellStringValue(headerRow.getCell(j), String.class));
                if (StringUtils.isNotBlank(propertyName)) {
                    // 获取当前列对应的bean字段
                    PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(t, propertyName);
                    // 获取bean字段的数据类型
                    Class<?> type = descriptor.getPropertyType();
                    // BigDecimal类型数据转换时,会四舍五入
                    String cellValue = getCellStringValue(cell, type);
                    if (!StringUtils.isBlank(cellValue)) {
                        isAllBlank = false;
                    }
                    // 给当前行对应的bean对象的某个字段赋值
                    PropertyUtils.setSimpleProperty(t, propertyName, stringToObject(type, cellValue));
                }
            }
            // 原来逻辑:--整行为空,则跳出循环
            // 现在的逻辑:--整行为空时,结束本层循环读取下一行
            if (isAllBlank) {
                continue;
            }
            list.add(t);
        }
        return list;
    }

    public static <T> boolean writeToExcel(OutputStream out, Class<T> clazz, List<T> list) throws Exception {
        return writeToExcel(out, clazz, list, false);
    }

    /**
     * 数据导出
     *
     * @param out
     * @param clazz
     * @return
     * @throws Exception
     */

    public static <T> boolean writeToExcel(OutputStream out, Class<T> clazz, List<T> list, boolean useExtendColumn) throws Exception {

        if (list == null) {
            throw new RuntimeException("导出的内容为空");
        }
        if (list.isEmpty()) {
            throw new RuntimeException("导出的内容为空");
        }

        Workbook workbook = writeToWorkbook(clazz, list, useExtendColumn);
        boolean returnVal = false;
        try {
            workbook.write(out);
            returnVal = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnVal;
    }

    public static <T> Workbook writeToWorkbook(Class<T> clazz, List<T> list, boolean useExtendColumn)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> map = new HashMap<String, String>();
        Field[] fields = clazz.getDeclaredFields();
        List<String> headerNames = fetchTitleList(map, fields, useExtendColumn);
        if (map.isEmpty()) {
            return null;
        }
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = null;
        Row row = null;
//        CellStyle headerCell = newTableHeaderStyle(workbook);
//        CellStyle evenStyle = newTableEvenCellStyle(workbook);
//        CellStyle oddStyle = newTableOddCellStyle(workbook);
        int rowNumber = 0;
        Short rowHeight = 20 * 25;
        Map<Integer, Integer> columnWidths = new LinkedHashMap<Integer, Integer>();
        for (int i = 0, length = list.size(); i < length; i++) {
            if (rowNumber == 0) {
                sheet = workbook.createSheet();
                row = sheet.createRow(0);
                row.setHeight(rowHeight);
                for (int j = 0; j < headerNames.size(); j++) {
                    Cell headCell = row.createCell(j);
//                    headCell.setCellStyle(headerCell);
                    String headerName = headerNames.get(j);
                    gatherColumnWidths(columnWidths, headerName, headerName, j);
                    setCellValueByType(headCell, headerNames.get(j));
                }
                rowNumber = 1;
            } else if (rowNumber % 20000 == 0) {
                sheet = workbook.createSheet();
                row = sheet.createRow(0);
                row.setHeight(rowHeight);
                for (int j = 0; j < headerNames.size(); j++) {
                    Cell headCell = row.createCell(j);
//                    headCell.setCellStyle(headerCell);
                    String headerName = headerNames.get(j);
                    gatherColumnWidths(columnWidths, headerName, headerName, j);
                    setCellValueByType(headCell, headerName);
                }
                rowNumber = 1;
            }
            // for( int k = 0 ; k <list.size() ; k++ ){
            row = sheet.createRow(rowNumber % 20000);
            row.setHeight(rowHeight);
            for (int j = 0; j < headerNames.size(); j++) {
                Cell cell = row.createCell(j);
//                if( i % 2 == 0 ){
//                    cell.setCellStyle(oddStyle);
//                }else{
//                    cell.setCellStyle(evenStyle);
//                }
                String headerName = headerNames.get(j);
                if (headerName.equals("序号")) {
                    setCellValueByType(cell, rowNumber % 20000);
                } else {
                    T obj = list.get(i);
                    Object value = PropertyUtils.getSimpleProperty(obj, map.get(headerName));
                    gatherColumnWidths(columnWidths, headerName, String.valueOf(value), j);
                    setCellValueByType(cell, value);
                }
            }

            rowNumber++;
            // }
            setColumnWidth(sheet, columnWidths);
        }
        return workbook;
    }

    public static <T> void writeToExcel(HttpServletResponse response, Class<T> clazz, List<T> list, String fileNamePrefix) {
        try {
            response.setContentType("application/form-data");
            String fileName = new String(fileNamePrefix.getBytes("utf-8"), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + DateUtils.format(new Date(), DateUtils.PATTERN_DATE_1) + ".xls");
            Workbook workbook = ExcelUtil.writeToWorkbook2(clazz, list, false);

            if (null != workbook) {
                workbook.write(response.getOutputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写数据到WorkBook中
     *
     * @param clazz
     * @param list
     * @param useExtendColumn
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static <T> Workbook writeToWorkbook2(Class<T> clazz, List<T> list, boolean useExtendColumn)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Map<String, String> map = new HashMap<String, String>();
        Field[] fields = clazz.getDeclaredFields();
        List<String> headerNames = fetchTitleList(map, fields, useExtendColumn);
        if (map.isEmpty()) {
            return null;
        }
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = null;
        Row row = null;
//        CellStyle headerCell = newTableHeaderStyle(workbook);
//        CellStyle evenStyle = newTableEvenCellStyle(workbook);
//        CellStyle oddStyle = newTableOddCellStyle(workbook);
        int rowNumber = 0;
        Short rowHeight = 20 * 25;
        Map<Integer, Integer> columnWidths = new LinkedHashMap<Integer, Integer>();

        //空数据时，只导出表头
        if (list == null || list.size() <= 0) {
            sheet = workbook.createSheet();
            row = sheet.createRow(0);
            row.setHeight(rowHeight);
            for (int j = 0; j < headerNames.size(); j++) {
                Cell headCell = row.createCell(j);
//                    headCell.setCellStyle(headerCell);
                String headerName = headerNames.get(j);
                //给列单元格设置宽度 TODO 待优化
                gatherColumnWidths(columnWidths, headerName, headerName, j);
                setCellValueByType(headCell, headerNames.get(j));
            }
            rowNumber = 1;

        } else {
            //遍历数据,每行每行的进行写数据
            for (int i = 0, length = list.size(); i < length; i++) {
                //初始化Sheet 第一行或达到20000行时,创建新的Sheet,并进行初始化
                if (rowNumber == 0 || rowNumber % 20000 == 0) {
                    sheet = workbook.createSheet();
                    row = sheet.createRow(0);
                    row.setHeight(rowHeight);
                    for (int j = 0; j < headerNames.size(); j++) {
                        Cell headCell = row.createCell(j);
//                    headCell.setCellStyle(headerCell);
                        String headerName = headerNames.get(j);
                        //给列单元格设置宽度 TODO 待优化
                        gatherColumnWidths(columnWidths, headerName, headerName, j);
                        setCellValueByType(headCell, headerNames.get(j));
                    }
                    rowNumber = 1;
                }

                //创建row对象,进行数据封装
                row = sheet.createRow(rowNumber % 20000);
                row.setHeight(rowHeight);
                for (int j = 0; j < headerNames.size(); j++) {
                    Cell cell = row.createCell(j);
//                if( i % 2 == 0 ){
//                    cell.setCellStyle(oddStyle);
//                }else{
//                    cell.setCellStyle(evenStyle);
//                }
                    String headerName = headerNames.get(j);
//                    if (headerName.equals("序号")) {
                    if (false) {
                        setCellValueByType(cell, rowNumber % 20000);
                    } else {
                        T obj = list.get(i);
                        Object value = PropertyUtils.getSimpleProperty(obj, map.get(headerName));
                        //给列单元格设置宽度
                        gatherColumnWidths(columnWidths, headerName, String.valueOf(value), j);
                        //在这里直接对列宽度进行设置
                        Integer width = columnWidths.get(j);
                        sheet.setColumnWidth(j, (width) * 256);
                        setCellValueByType(cell, value);
                    }
                }
                //行号自增
                rowNumber++;
                // }
                //遍历Sheet,对列设置宽度    TODO 放在前面进行
//            setColumnWidth(sheet, columnWidths);
            }
        }
        return workbook;
    }


    private static Workbook newExcelByInputStream(InputStream inp) throws IOException, InvalidFormatException {
        if (!inp.markSupported()) {
            inp = new PushbackInputStream(inp, 8);
        }
        //操作Excel2003以前（包括2003）的版本，扩展名是.xls
        if (POIFSFileSystem.hasPOIFSHeader(inp)) {
            return new HSSFWorkbook(inp);
        }
        //操作Excel2007的版本，扩展名是.xlsx
        if (POIXMLDocument.hasOOXMLHeader(inp)) {
            return new XSSFWorkbook(OPCPackage.open(inp));
        }
        throw new IllegalArgumentException("Excel文件格式错误,请使用2003版本的Excel上传！");
    }

    /**
     * 获取cell的alue
     *
     * @param cell
     * @param clazz 指定字段类型时,可以根据类型做出处理,当前只针对BigDecimal进行单独处理
     * @return
     */
    private static String getCellStringValue(Cell cell, Class clazz) {
        try {
            if (null == cell) {
                return "";
            }
            /**
             * Cell.CELL_TYPE_BLANK Cell.CELL_TYPE_NUMERIC Cell.CELL_TYPE_STRING
             * Cell.CELL_TYPE_FORMULA Cell.CELL_TYPE_BOOLEAN
             * Cell.CELL_TYPE_ERROR
             */
            if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                return "";
            }
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                }
                //数字格式化
                DecimalFormat df = new DecimalFormat("0");
                //TODO 没找到好的类型比对方法
                if (clazz == BigDecimal.class) {
                    df = new DecimalFormat("0.00");
                }
                df.setRoundingMode(RoundingMode.DOWN);
                return df.format(cell.getNumericCellValue());
            }
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                return cell.getStringCellValue();
            }
            if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                return cell.getCellFormula();
            }
            if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            }
            if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
                return String.valueOf(cell.getErrorCellValue());
            }
            return cell.getStringCellValue();
        } catch (RuntimeException e) {
            // logger.error("单元格字符值获取,错误信息{}",e.getMessage());
        }
        return "";
    }

    /*
     * 字段类型转换
     */
    private static Object stringToObject(Class<?> clazz, String str) throws Exception {
        Object o = str;
        if (clazz == BigDecimal.class) {
            if (StringUtils.isBlank(str)) {
                o = new BigDecimal(0);
            } else {
                o = new BigDecimal(str);
            }
        } else if (clazz == Long.class) {
            if (StringUtils.isBlank(str)) {
                o = new Long(0);
            } else {
                o = new Long(str);
            }
        } else if (clazz == Integer.class) {
            if (StringUtils.isBlank(str)) {
                o = new Integer(0);
            } else {
                o = new Integer(str);
            }
        } else if (clazz == int.class) {
            o = Integer.parseInt(str);
        } else if (clazz == float.class) {
            o = Float.parseFloat(str);
        } else if (clazz == boolean.class) {
            o = Boolean.parseBoolean(str);
        } else if (clazz == byte.class) {
            o = Byte.parseByte(str);
        }
        return o;
    }

    /**
     * 从对象中获取Excel表头
     *
     * @param map             title名称和字段名称的对应关系
     * @param fields          对象对应的字段
     * @param useExtendColumn
     * @return 返回Title名称
     */
    private static List<String> fetchTitleList(Map<String, String> map, Field[] fields, boolean useExtendColumn) {
        List<String> headerNames = new ArrayList<String>();
        for (Field m : fields) {
            if (m.isAnnotationPresent(Excel.class)) {
                // 获取该字段的注解对象
                Excel anno = m.getAnnotation(Excel.class);
                map.put(anno.headerName(), m.getName());
                headerNames.add(anno.headerName());
            }
            if (useExtendColumn) {
                if (m.isAnnotationPresent(ExcelExtend.class)) {
                    // 获取该字段的注解对象
                    ExcelExtend anno = m.getAnnotation(ExcelExtend.class);
                    map.put(anno.headerName(), m.getName());
                    headerNames.add(anno.headerName());
                }
            }
        }
        return headerNames;
    }

    private static void setCellValueByType(Cell cell, Object value) {
        if (null == value) {
            return;
        }
        if (value instanceof BigDecimal) {
            // cell.setCellValue(((BigDecimal)value).doubleValue());
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof Long) {
            cell.setCellValue(((Long) value).longValue());
        } else if (value instanceof Integer) {
            cell.setCellValue(((Integer) value).intValue());
        } else if (value instanceof Float) {
            // cell.setCellValue(Math.floor(((Float)value).floatValue()));
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof Double) {
            // cell.setCellValue(Math.floor(((Double)value).doubleValue()));
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Byte) {
            cell.setCellValue(((Byte) value));
        } else if (value instanceof String) {
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof Timestamp) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            cell.setCellValue(dateFormat.format(value));
        }
    }

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

    private static void gatherColumnWidths(Map<Integer, Integer> columnWidths, String headerName, String content, int j) {
        int length = content.getBytes().length;
        if (headerName.equalsIgnoreCase(content)) {
            if (headerName.indexOf("（") > 0) {
                headerName = headerName.replace("（", "\r\n（");
                String lengthStr = headerName.substring(0, headerName.indexOf("（"));
                length = lengthStr.getBytes().length;
            }
            columnWidths.put(j, length);
        } else {
            if (content.matches("[0-9a-zA-Z]+")) {
                length = length * 2;
            }
            int oldLength = null == columnWidths.get(j) ? 0 : columnWidths.get(j);
            if (length > MAX_CELL_WIDTH) {
                oldLength = oldLength > MAX_CELL_WIDTH ? oldLength : MAX_CELL_WIDTH;
            } else {
                oldLength = oldLength > length ? oldLength : length;
            }
            columnWidths.put(j, oldLength);
        }

    }

    private static void setColumnWidth(Sheet sheet, Map<Integer, Integer> columnWidths) {
        for (Map.Entry<Integer, Integer> entry : columnWidths.entrySet()) {
            sheet.setColumnWidth(entry.getKey(), (entry.getValue()) * 256);
        }
    }

    /**
     * 导出excel 单sheet
     *
     * @param outputStream
     * @param list         第一行为表头
     * @throws IOException
     */
    public static void exportExcel(OutputStream outputStream, List<List<Object>> list) throws IOException {
        exportExcel(outputStream, ImmutableMap.of("sheet1", list));
    }

    /**
     * 导出excel 单sheet
     *
     * @param outputStream
     * @param list         第一行为表头
     * @throws IOException
     */
    public static void exportExcel(OutputStream outputStream, List<List<Object>> list, String remark) throws IOException {
        exportExcel(outputStream, ImmutableMap.of("sheet1", list), remark);
    }

    /**
     * 导出excel 多sheet
     *
     * @param outputStream
     * @param map
     * @throws IOException
     */
    public static void exportExcel(OutputStream outputStream, Map<String, List<List<Object>>> map) throws IOException {
        exportExcel(outputStream, map, null);
    }

    /**
     * 导出excel 多sheet
     *
     * @param outputStream
     * @param map          数据内容
     * @param remark       注释, 放到第一行
     * @throws IOException
     */
    public static void exportExcel(OutputStream outputStream, Map<String, List<List<Object>>> map, String remark) throws IOException {
        if (Objects.isNull(map) || map.isEmpty()) {
            throw new RuntimeException("导出的内容为空");
        }
        Workbook workbook = new HSSFWorkbook();
        for (String key : map.keySet()) {
            Sheet sheet = workbook.createSheet(key);
            List<List<Object>> rowList = map.get(key);
            if (Objects.isNull(rowList) || rowList.isEmpty()) {
                continue;
            }
            // 样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
            CellStyle contentStyle = workbook.createCellStyle();
            contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
            contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            if (Objects.isNull(rowList.get(0))) {
                throw new RuntimeException("数据格式错误");
            }
            // 列数
            int columnSize = rowList.get(0).size();
            // 表头注释内容
            if (Objects.nonNull(remark)) {
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnSize - 1));
                Row remarkRow = sheet.createRow(0);
                remarkRow.createCell(0).setCellValue(remark);
                remarkRow.setHeight((short) 500);
            }
            // 内容
            for (int i = 0; i < rowList.size(); i++) {
                Row row = sheet.createRow(remark == null ? i : i + 1);
                List<Object> cellList = rowList.get(i);
                if (Objects.isNull(cellList) || cellList.isEmpty()) {
                    continue;
                }
                for (int j = 0; j < cellList.size(); j++) {
                    row.setHeight((short) 500);
                    Cell cell = row.createCell(j);
                    cell.setCellValue(String.valueOf(cellList.get(j)));
                    cell.setCellStyle(i == 0 ? headerStyle : contentStyle);
                }
            }
            // 自动调整列宽度
            for (int x = 0; x < columnSize; x++) {
                sheet.autoSizeColumn(x);
            }
        }
        workbook.write(outputStream);
    }

}
