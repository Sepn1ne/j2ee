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

	// contact table���������excel�������������
	private List<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();
	//��д��GenericServlet��init����
	public void init() throws ServletException {
		try {
			//��ȡxml�ļ��е�init-param������
			String files = getInitParameter("contacts");
			//ȡ���ַ���ǰ��ո�
			files = files.trim();
			files = files.replace('��', ',');
			String[] file_name_array = files.split(",");
			//������ʼ��������Ӧ��xlsx�ļ�
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
				//������sheet��ÿһ��
				for (Row row : sheetAt) {
					//Get row number this row represents
					//������0�У���Ϊ��0��������
					if (row.getRowNum() == 0) {
						continue;
					}

					if (row == null) {
						break;
					}
					//��ȡ��0����Ԫ��
					Cell cell = row.getCell(0);

					if (cell == null) {
						break;
					}
					//��ȡ��0����Ԫ���������Ϊ���ַ��أ�����������֣��׳��쳣
					double no = row.getCell(0).getNumericCellValue();
					String id = row.getCell(1).getStringCellValue();
					String name = row.getCell(2).getStringCellValue();
					String strClass = "";
					String mobile = "";
					String email = "";
					//�༶�ֶ�
					cell = row.getCell(3);
					if (cell != null) {
						strClass = cell.getStringCellValue();
					}
					//�绰�ֶ�
					cell = row.getCell(4);
					if (cell != null) {
						//���øõ�Ԫ������ԣ��ѹ�ʱ
						cell.setCellType(CellType.STRING);
						mobile = cell.getStringCellValue();
					}
					//�����ֶ�
					cell = row.getCell(5);
					if (cell != null) {
						cell.setCellType(CellType.STRING);
						email = cell.getStringCellValue();
					}

					//�����е�����Ԫ�ط��뵽Map����
					Map<String, Object> record = new HashMap<String, Object>();
					record.put("id", id);
					record.put("name", name);
					//����������һ���ַ���*������Ů��
					String gender=name.charAt(name.length()-1)=='*'?"Ů��":"�к�";
					record.put("gender", gender);
					record.put("class", strClass);
					record.put("mobile", mobile);
					record.put("email", email);
					//��map���ϼ��뵽List����
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
		//���ñ��ύ�Ĳ�������
		request.setCharacterEncoding("utf-8");
		//������Ӧ��������ı�������
		//ʹ��utf-8�ļ����ԽϺ�
		response.setContentType("text/html;charset=utf-8");
		//��ȡ���������
		String param = request.getParameter("param");
		PrintWriter out = response.getWriter();
		//ʹ��������ʽ�ж�����Ĳ�������
		//������������ʽ
		// String pName="^[\u4e00-\u9fa5]{2,6}$";


		for(Map<String, Object> map:contacts){
			if(map.containsValue(param)){
				//�����Ľ������ѧ����ѧ�ţ��������Ա𣬰༶���ֻ����룬�ʼ���ַ��
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
