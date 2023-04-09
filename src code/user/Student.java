package user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import course.Course;
import course.OptionalCourse;
import inputErr.UsrInput;

public class Student extends User {
	private int stuId; //学号
	private String class_; //班级
	
	public static Map<Integer, Vector<Integer>> allStuCourses = null;

	public Student(String name, String pwd, int stuId, String class_) {
		this.name = name;
		this.pwd = pwd;
		this.stuId = stuId;
		this.class_ = class_;
	}
	
	public Student() {}

	public void showInfo() {
		System.out.println(stuId+" "+name+" "+class_);
	}
	
	public Student login() {
		System.out.print("输入学号:");

		int stuId = UsrInput.getInt();
		
		System.out.print("输入密码:");
		String pwd = UsrInput.getString();
		
		for (Student s : AdminUsers.s)
			if (s.stuId==stuId && s.pwd.equals(pwd)) {
				System.out.println(s.name+"同学, 你好!");
				return s;
			}
		System.out.println("用户名或密码错误, 登陆失败...");
		return null;
	}

	public int getStuId() {
		return stuId;
	}
	
	public String toString() {
		return stuId+"  "+name+"  "+class_+"  "+pwd;
	}
	
	public static void stuMenu(Student s) {
		Student.prepareStuCourse();
		while (true) {
			System.out.println("选择功能:\n1.修改密码\n2.查看所选课程\n3.选课\n0.退出");
			int choice = UsrInput.getInt();
			switch(choice) {
				case 1: s.changePwd(); break;
				case 2: Student.viewSelectedCourse(s); break;
				case 3: Student.selectCourses(s); break;
				case 0: return; //保存退出
				default: break;
			}
		}
	}
	
	public static void prepareStuCourse() {
		if (allStuCourses==null) {
			allStuCourses = new HashMap<Integer, Vector<Integer>>();
			Student.readFromFile();
		}
	}
	
	public static void viewSelectedCourse(Student s) {
		System.out.println("课程号  课程名  类型  教师  选课人数  学分或最大选课人数");

		Course.updateRequiredCourseStuNum();
		Course.showAllRequiredCourses();
		if (Student.allStuCourses.containsKey(s.stuId)==false)
			Student.allStuCourses.put(s.stuId, new Vector<>());
		
		Course tmpC = null;
		for (Integer cid : Student.allStuCourses.get(s.stuId))
			if ((tmpC=Course.getCourseById(cid.intValue()))!=null)
				tmpC.show();
	}
	
	public static void selectCourse(Student s) {
		System.out.print("输入课程编号:");
		int courseId = UsrInput.getInt();
		
		boolean haveSelected = false;
		for (int i=0; i<Course.courseSet.size(); ++i) {
			Course c_ = Course.courseSet.get(i);
			if (c_.id==courseId) { //存在此门课程
				// 判断是否已经选修此门课
				for (Integer cid : Student.allStuCourses.get(s.stuId))
					if (cid.intValue()==courseId)
						haveSelected = true;
				if (c_.tp==0 && haveSelected==false) {//选修课且尚未选修该门课程
					OptionalCourse ctmp = (OptionalCourse)c_; //下塑造型

					// 判断选修课人数是否已满
					if (ctmp.stuNum==ctmp.maxStuNum) {
						System.out.println("选课失败，该选修课人数已满...");
						return;
					}

					if (Student.allStuCourses.containsKey(s.stuId)==false)
						Student.allStuCourses.put(s.stuId, new Vector<>());
					Student.allStuCourses.get(s.stuId).add(c_.id);
					++ctmp.stuNum;
					Course.courseSet.set(i, ctmp); //上溯造型
				}
				else
					System.out.println("课程编号错误或你已选修该课程...");
				return;
			}
		}
		
		System.out.println("无该课程...");
	}

	public static void selectCourses(Student s) {
		if (Course.courseSet.size()!=0) {
			// 显示全部课程
			Course.showAllCourses();
			// 选课操作
			do {
				Student.selectCourse(s);
				
				System.out.print("是否继续选课?(y/n)");
			} while (UsrInput.getString().equals("y"));
		} else
			System.out.println("当前无任何课程信息...");
	}
	
	public static void saveToFile() {
		if (Student.allStuCourses!=null)
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter("student courses.txt"));
				
				for (Integer i : Student.allStuCourses.keySet()) {
					out.write(i.intValue()+"");
					for (Integer cid : Student.allStuCourses.get(i))
						out.write("  "+cid.intValue());
					out.write("\r\n");
				}
				
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void readFromFile() {
		File f = new File("student courses.txt");
		try {
			if (!f.exists()) f.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			
			String info = null;
			
			while ((info = br.readLine()) != null) {
				String[] info_ = info.split("  ");
				int stuId = Integer.parseInt(info_[0]);
				allStuCourses.put(stuId, new Vector<Integer>());
				
				for (int i=1; i< info_.length; ++i)
					allStuCourses.get(stuId).add(Integer.parseInt(info_[i]));
			}


			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isSelectedSomeCourse(Student s, Course co) {
		for (Integer sid : Student.allStuCourses.keySet())
			if (s.stuId==sid.intValue()) // 找到学生
				for (Integer cid : Student.allStuCourses.get(sid))
						if (co.id==cid.intValue())
							return true;
		return false;
	}
}
