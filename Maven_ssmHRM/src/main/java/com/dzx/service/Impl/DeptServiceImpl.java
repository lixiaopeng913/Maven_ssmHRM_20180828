package com.dzx.service.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dzx.dao.DeptDao;
import com.dzx.domain.Dept;
import com.dzx.service.DeptService;
@Service
public class DeptServiceImpl implements DeptService {
	@Autowired
	private DeptDao deptDao;
	

	@Transactional(readOnly = false)
	@Override
	public int addDept(Dept dept) {
		// TODO Auto-generated method stub
		return deptDao.addDept(dept);
	}

	// 判断部门名称是否重复
	@Override
	public List<Dept> findDeptName(Dept dept) {
		// TODO Auto-generated method stub
		return deptDao.findDeptName(dept);
	}

	@Override
	public List<Dept> selectAllDept(Dept dept) {
		// TODO Auto-generated method stub
		return deptDao.selectAllDept(dept);
	}

	@Override
	public List<Dept> selectDept(Dept dept) {
		// TODO Auto-generated method stub
		if (dept!=null) {
			int totalRecord = deptDao.selectDeptCount(dept);
			dept.setTotalRecord(totalRecord);
			return deptDao.selectDept(dept);
		} else {
			return new ArrayList<>();
		}
		
	}

	// 删除部门
	@Transactional(readOnly = false)
	@Override
	public int deleteDept(Integer[] caption) {
		// TODO Auto-generated method stub
		if (caption != null) {
			for (Integer integer : caption) {
				deptDao.deleteDept(integer);
			}
		}
		return 0;
	}

	// 更改部门
	@Transactional(readOnly = false)
	@Override
	public int updateDept(Dept dept) {
		// TODO Auto-generated method stub
		return deptDao.updateDept(dept);
	}

	// 根据ID查找部门
	@Override
	public Dept findDeptById(int deptId) {
		// TODO Auto-generated method stub
		return deptDao.findDeptById(deptId);
	}

	@Override
	public Boolean importExcel(String[] thTitle, List<Dept> listValue, String tableTitle, String path) {
		// 创建Excelworkbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 创建一个工作表sheet
		XSSFSheet sheet = workbook.createSheet();

		// 创建表头样式
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();// 字体
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 14);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);// 居中
		cellStyle.setFont(font);// 字体
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, thTitle.length - 1)); // 合并单元格

		// 创建表格样式
		XSSFCellStyle cellStyle2 = workbook.createCellStyle();
		cellStyle2.setAlignment(HorizontalAlignment.CENTER);// 居中
		// 创建第一行
		XSSFRow row0 = sheet.createRow(0);
		XSSFCell cell = null;
		cell = row0.createCell(0);
		cell.setCellValue(tableTitle);// 设置标题值
		cell.setCellStyle(cellStyle);
		row0.setHeightInPoints((short) 20);// 设置行高

		// 创建第二行
		XSSFRow row2 = sheet.createRow(1);
		// 插入第二行的数据
		for (int i = 0; i < thTitle.length; i++) {
			cell = row2.createCell(i);
			cell.setCellValue(thTitle[i]);
			cell.setCellStyle(cellStyle2);
		}
		// 第三行,追加数据
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");// 格式化日期
		for (int i = 0; i < listValue.size(); i++) {
			XSSFRow nextRow = sheet.createRow(i + 2);
			XSSFCell cell2 = nextRow.createCell(0);
			cell2.setCellValue(listValue.get(i).getId().toString());
			cell.setCellStyle(cellStyle2);
			
			cell2 = nextRow.createCell(1);
			cell2.setCellValue(listValue.get(i).getName());
			cell.setCellStyle(cellStyle2);
			
			cell2 = nextRow.createCell(2);
			cell2.setCellValue(listValue.get(i).getRemark());
			cell.setCellStyle(cellStyle2);
			
			cell2 = nextRow.createCell(3);
			if (listValue.get(i).getDept_sum()==null) {
				cell2.setCellValue(0);
				cell.setCellStyle(cellStyle2);
			}else {
				cell2.setCellValue(listValue.get(i).getDept_sum());
				cell.setCellStyle(cellStyle2);
			}
			
			
			cell2 = nextRow.createCell(4);
			String dateString = format.format(listValue.get(i).getCreate_date());
			cell2.setCellValue(dateString);
			cell2.setCellStyle(cellStyle2);
			
//			设置列高,(第几列，像素)
			sheet.setColumnWidth((short) i, (short) 3000);
		}
		// 创建一个文件
		File file = new File(path);
		try {
			file.createNewFile();
			// 将excel的内容写入到流中
			FileOutputStream stream = FileUtils.openOutputStream(file);
			workbook.write(stream);
			stream.close();
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
