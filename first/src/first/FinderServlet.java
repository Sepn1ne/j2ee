package first;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class FinderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// contact table，存放所有excel表的所有行数据
	private List<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();
	//重写了GenericServlet的init方法
	public void init() throws ServletException {
		try {
			//读取xml文件中的init-param的内容
			String files = getInitParameter("contacts");
			//取出字符串前后空格
			files = files.trim();
			files = files.replace('，', ',');
			String[] file_name_array = files.split(",");
			//遍历初始化参数对应的xlsx文件
			for (int i = 0; i < file_name_array.length; i++) {
				String file_name = file_name_array[i];
				file_name = file_name.trim();
				if (file_name.length() == 0) {
					continue;
				}
				//Gets the real path corresponding to the given virtual path.
				File file = new File(getServletContext().getRealPath("/WEB-INF/contacts/" + file_name));

				FileInputStream fis = new FileInputStream(file);

				/**
				 * @description: High level representation of a Excel workbook.
				 * This is the first object most users will construct whether they are reading or writing a workbook.
				 * It is also the top level object for creating new sheets/etc
				 * @author Sepnine
				 * @date: 2022/9/15 20:30
				 */
				Workbook book = null;

				try {
					book = new XSSFWorkbook(fis);
				} catch (Exception ex) {
					book = new HSSFWorkbook(fis);
				}

				//Get the Sheet object at the given index.
				Sheet sheetAt = book.getSheetAt(0);
				//遍历该sheet的每一行
				for (Row row : sheetAt) {
					//Get row number this row represents
					//跳过第0行，因为第0行是列名
					if (row.getRowNum() == 0) {
						continue;
					}

					if (row == null) {
						break;
					}
					//获取第0个单元格
					Cell cell = row.getCell(0);

					if (cell == null) {
						break;
					}
					//获取第0个单元格的内容作为数字返回，如果不是数字，抛出异常
					double no = row.getCell(0).getNumericCellValue();
					String id = row.getCell(1).getStringCellValue();
					String name = row.getCell(2).getStringCellValue();
					String strClass = "";
					String mobile = "";
					String email = "";
					//班级字段
					cell = row.getCell(3);
					if (cell != null) {
						strClass = cell.getStringCellValue();
					}
					//电话字段
					cell = row.getCell(4);
					if (cell != null) {
						//设置该单元格的属性，已过时
						cell.setCellType(CellType.STRING);
						mobile = cell.getStringCellValue();
					}
					//邮箱字段
					cell = row.getCell(5);
					if (cell != null) {
						cell.setCellType(CellType.STRING);
						email = cell.getStringCellValue();
					}

					//将改行的所有元素放入到Map集合
					Map<String, Object> record = new HashMap<String, Object>();
					record.put("id", id);
					record.put("name", name);
					//如果姓名最后一个字符是*，则是女孩
					String gender=name.charAt(name.length()-1)=='*'?"女孩":"男孩";
					record.put("gender", gender);
					record.put("class", strClass);
					record.put("mobile", mobile);
					record.put("email", email);
					//将map集合加入到List集合
					contacts.add(record);

				}

				book.close();
				fis.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//设置表单提交的参数类型
		request.setCharacterEncoding("utf-8");
		//设置相应到浏览器的编码类型
		//使用utf-8的兼容性较好
		response.setContentType("text/html;charset=utf-8");
		//获取浏览器传参
		String param = request.getParameter("param");
		PrintWriter out = response.getWriter();
		//使用正则表达式判断输入的参数类型
		//姓名的正则表达式
		// String pName="^[\u4e00-\u9fa5]{2,6}$";


		for(Map<String, Object> map:contacts){
			if(map.containsValue(param)){
				//检索的结果包括学生的学号，姓名，性别，班级，手机号码，邮件地址。
				String id=(String)map.get("id");
				String name=(String)map.get("name");
				String gender=(String)map.get("gender");
				String styClass=(String)map.get("class");
				String mobile=(String)map.get("mobile");
				String email=(String)map.get("email");
				out.write("id: "+id+"<br>");
				out.write("name: "+name+"<br>");
				out.write("gender: "+gender+"<br>");
				out.write("styClass: "+styClass+"<br>");
				out.write("mobile: "+mobile+"<br>");
				out.write("email: "+email+"<br>");
			}
		}
		out.close();


	}

}
