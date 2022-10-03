package first;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import first.bean.Page;
import first.bean.Student;
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
	//init()方法在执行构造函数
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

					//如果姓名最后一个字符是*，则是女孩
					String gender=name.charAt(name.length()-1)=='*'?"女孩":"男孩";
					record.put("gender", gender);
					if("女孩".equals(gender)){
						record.put("name", name.substring(0,name.length()-1));
					}else if("男孩".equals(gender)){
						record.put("name",name);
					}

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
		// PrintWriter out = response.getWriter();
		List<Student> res=new ArrayList<>();

		//遍历每个学生
		for(Map<String, Object> map:contacts){
			// if(map.containsValue(param)){
			// 	//检索的结果包括学生的学号，姓名，性别，班级，手机号码，邮件地址。
			// 	String id=(String)map.get("id");
			// 	String name=(String)map.get("name");
			// 	String gender=(String)map.get("gender");
			// 	String strClass=(String)map.get("class");
			// 	String mobile=(String)map.get("mobile");
			// 	String email=(String)map.get("email");
			// 	out.write("id: "+id+"<br>");
			// 	out.write("name: "+name+"<br>");
			// 	out.write("gender: "+gender+"<br>");
			// 	out.write("strClass: "+strClass+"<br>");
			// 	out.write("mobile: "+mobile+"<br>");
			// 	out.write("email: "+email+"<br>");
			// }
			//遍历每个学生的每个属性
			for(Object val:map.values()){
				String sv=(String)val;
				int i = sv.indexOf(param);
				if(i!=-1){
					String id=(String)map.get("id");
					String name=(String)map.get("name");
					String gender=(String)map.get("gender");
					String strClass=(String)map.get("class");
					String mobile=(String)map.get("mobile");
					String email=(String)map.get("email");
					Student student = new Student(id, name, gender, strClass, mobile, email);
					res.add(student);
				}
			}
		}
		// out.close();

		//将集合转为数组，方便使用foreach进行循环
		Student [] res2 = res.toArray(new Student[0]);
		int currentPage=0;
		int count=10;
		//获取连接中的当前页数和一共页数
		String current = request.getParameter("currentPage");
		String countString = request.getParameter("count");
		if(current!=null){
			currentPage=Integer.parseInt(current);
		}
		if(countString!=null){
			count=Integer.parseInt(countString);
		}
		currentPage =currentPage>=0?currentPage:0;
		count =count>=0?count:10;


		Page page = new Page(res2.length,currentPage,count,currentPage+count-1);
		//将结果写入到request请求域
		request.setAttribute("res",res2);
		request.setAttribute("page",page);

		//转发
		request.getRequestDispatcher("/page.jsp").forward(request,response);


	}

}
