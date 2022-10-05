package com.team.prj.admin.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.team.prj.admin.service.ProfitService;
import com.team.prj.admin.service.ProfitVO;

@Controller
public class ProfitController {
	@Autowired
	private ProfitService ps;

	@RequestMapping("/manager/profit")
	public String salesCheck(Model model) {
		String start = null;
		String end = null;
		String key = null;

		List<Map<String, Object>> d = ps.dailyList(start);
		List<Map<String, Object>> w = ps.weeklyList(start);
		List<Map<String, Object>> m = ps.monthlyList(start);

		model.addAttribute("dailyList", d);
		model.addAttribute("weeklyList", w);
		model.addAttribute("monthlyList", m);

		return "admin/profit";
	}

	@RequestMapping("/exelDownload")
	public void excelDownload(HttpServletResponse response, @RequestParam("start") String start,
			@RequestParam("end") String end) throws IOException {
//        Workbook wb = new HSSFWorkbook();
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("첫번째 시트");
		Row row = null;
		Cell cell = null;
		int rowNum = 0;

		// 테이블 헤더용 스타일   
		CellStyle headStyle = wb.createCellStyle();
		// 가는 경계선   
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);
		// 배경색 노랑.   
		headStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		// 데이터 가운데 정렬   
		headStyle.setAlignment(HorizontalAlignment.CENTER);
		// 데이터용 경계 스타일 테두리만 지정   
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);

		// Header
		row = sheet.createRow(rowNum++);
		cell = row.createCell(0);
		cell.setCellStyle(headStyle);
		cell.setCellValue("번호");
		cell = row.createCell(1);
		cell.setCellStyle(headStyle);
		cell.setCellValue("정산일자"); // minus_dt
		cell = row.createCell(2);
		cell.setCellStyle(headStyle);
		cell.setCellValue("거래일자"); // order_dt
		cell = row.createCell(3);
		cell.setCellStyle(headStyle);
		cell.setCellValue("구매자"); // user_name
		cell = row.createCell(4);
		cell.setCellStyle(headStyle);
		cell.setCellValue("판매자"); // seller_name
		cell = row.createCell(5);
		cell.setCellStyle(headStyle);
		cell.setCellValue("거래품목"); // category_id
		cell = row.createCell(6);
		cell.setCellStyle(headStyle);
		cell.setCellValue("거래품명"); // name
		cell = row.createCell(7);
		cell.setCellStyle(headStyle);
		cell.setCellValue("거래금액"); // total_price
		cell = row.createCell(8);
		cell.setCellStyle(headStyle);
		cell.setCellValue("정산금액"); // minus_price
		cell = row.createCell(9);
		cell.setCellStyle(headStyle);
		cell.setCellValue("매출"); // profit

		// Body

		List<ProfitVO> pl = ps.profitList(start, end);
		for (int i = 0; i < pl.size(); i++) {
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(i + 1);
			cell = row.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(pl.get(i).getMinusDt());
			cell = row.createCell(2);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(pl.get(i).getOrderDt());
			cell = row.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(pl.get(i).getUserName());
			cell = row.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(pl.get(i).getSellerName());
			cell = row.createCell(5);
			cell.setCellStyle(bodyStyle);
			String category;
			if (pl.get(i).getCategoryId().toUpperCase().equals("S")) {
				category = "펫상품";
			} else {
				category = "펫클래스";
			}
			cell.setCellValue(category);
			cell = row.createCell(6);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(pl.get(i).getName());
			cell = row.createCell(7);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(pl.get(i).getTotalPrice());
			cell = row.createCell(8);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(pl.get(i).getMinusPrice());
			cell = row.createCell(9);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(pl.get(i).getProfit());
		}

		// 컬럼 너비 설정
		sheet.setColumnWidth(0, 1000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 3000);
		sheet.setColumnWidth(5, 3000);
		sheet.setColumnWidth(6, 10000);
		sheet.setColumnWidth(7, 3000);
		sheet.setColumnWidth(8, 3000);
		sheet.setColumnWidth(9, 3000);

		// 컨텐츠 타입과 파일명 지정
		response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
		response.setHeader("Content-Disposition", "attachment;filename=profit_list_" + start + "_" + end + ".xlsx");

		// Excel File Output
		wb.write(response.getOutputStream());
		wb.close();
	}
}
