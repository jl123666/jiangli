package course;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import inputErr.UsrInput;
import user.AdminUsers;
import user.Student;

public class Course {
	public int id; //课程编号
	public String name;    //课程名称
	public int tp; //课程选修类型，0选修，1必修
	public String teacher; //上课教师
	public int stuNum; //选课人数
	
	public static Vector<Course> courseSet = new Vector<Course>(); // 全部课程集合
	
	public Course() {}

	public Course(int id, String name, int tp, String teacher, int stuNum) {
		this.id = id;
		this.name = name;
		this.tp = tp;
		this.teacher = teacher;
		this.stuNum = stuNum;
	}

	/**
	 * 显示课程信息
	 */
	public void show() {
		System.out.print(id + "  " + name+"  ");
		
		if (tp==0)
			System.out.print("选修");
		else if (tp==1)
			System.out.print("必修");
		else
			System.out.print("其他");

		System.out.println("  "+teacher+"  "+stuNum);
	}
	
	public static void addCourse() {
		Course c = null;
		
		System.out.print("输入课程ID:");
		int cid = UsrInput.getInt();
		System.out.print("输入课程名:");
		String cname = UsrInput.getString();
		System.out.print("输入课程类型(0选修，1必修):");
		int ctp = UsrInput.getInt();
		System.out.print("输入上课教师:");
		String ctcher = UsrInput.getString();
		System.out.print("输入选课人数:");
		int cstuNum = UsrInput.getInt();
		if (ctp==0) {
			System.out.print("输入课程最大人数:");
			int maxStuNum = UsrInput.getInt();
			c = new OptionalCourse(cid, cname, ctp, cstuNum, ctcher, maxStuNum); //添加到选修课程Vector中
		} else {
			System.out.print("输入课程学分:");
			float credit = UsrInput.getFloat();
			c = new RequiredCourse(cid, cname, ctp, ctcher, cstuNum, credit); //添加到必修课程Vector中
		}
		
		Course.courseSet.add(c); //上溯造型
	}
	
	public static void addCourses() {
		while(true) {
			Course.addCourse();
			
			System.out.print("课程添加成功，是否继续添加？（y/n）");
			if (UsrInput.getString().equals("y"))
				continue;
			else
				break;
		}
	}
	
	public static void showAllCourses() {
		if (isempty()) return;

		System.out.println("课程号  课程名  类型  教师  选课人数  学分或最大选课人数");
		Course.updateRequiredCourseStuNum();
		for (Course c : Course.courseSet)
			c.show();
	}
	
	public static void sortCourses() {
		if (isempty()) return; //为空则无需排序

		Course s1, s2;
		
		for (int i=Course.courseSet.size()-1; i>0; --i) //冒泡排序法
			for (int j=0; j<i; ++j) {
				s1=Course.courseSet.get(j);
				s2=Course.courseSet.get(j+1);
				if (s1.stuNum>s2.stuNum) { //大小交换
					Course.courseSet.set(j, s2);
					Course.courseSet.set(j+1, s1);
				}
			}
	}
	
	public static void searchCourseByTeacher() {
		if (isempty()) return;

		System.out.print("输入教师名称:");
		String teacherName = UsrInput.getString();
		
		for (Course c : Course.courseSet) {
			if (c.teacher.equals(teacherName))
				c.show();
		}
	}
	
	public static boolean deleteCourse() {
		System.out.print("输入要删除的课程名称:");
		String objCourse = UsrInput.getString();

		Course tmpC = null;
		for (int i=0; i<Course.courseSet.size(); ++i) {
			tmpC = Course.courseSet.get(i);
			if (tmpC.name.equals(objCourse)) {
				Course.courseSet.remove(i); //从课程Vector中删除
				
				// 如果是选修课则需要从学生选课中删除
				if (tmpC.tp==0)
					deleteCourseInStu(tmpC.id);
				return true;
			}
		}
		return false;
	}
	
	public static void deleteCourses() {
		if (isempty()) return;
		
		while(true) {
			if (Course.deleteCourse())
				System.out.print("课程删除成功，是否继续删除？（y/n）");
			else
				System.out.print("删除失败，无此项课程...\n输入y继续删除，输入n退出:");
			if (UsrInput.getString().equals("y")) {
				continue;
			} else
				break;
		}
	}
	
	public static void setCourseTeacher() {
		if (isempty()) return;

		System.out.print("输入课程名称");
		String courseName = UsrInput.getString();
		
		for (int i=0; i<Course.courseSet.size(); ++i)
			if (Course.courseSet.get(i).name.equals(courseName)) {
				System.out.println("查找到此课程:");
				Course tmp = Course.courseSet.get(i);
				tmp.show();
				System.out.print("输入更改后的教师名称:");
				String tcherName = UsrInput.getString();
				tmp.teacher = tcherName;
				Course.courseSet.set(i, tmp);
				System.out.println("更改成功:");
				tmp.show();
				
				return;
			}
		System.out.println("无此项课程...");
	}
	
	private static boolean isempty() {
		if (Course.courseSet.size()!=0)
			return false;
		else {
			System.out.println("操作失败，暂无任何课程...");
			return true;
		}
	}
	
	public String toString() {
		return id+"  "+name+"  "+tp+"  "+stuNum+"  "+teacher;
	}
	
	public static void saveTofile() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("course data.txt"));
			
			for (Course c : Course.courseSet)
				out.write(c.toString()+"\r\n");
			
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readFromFile() {
		File f = new File("course data.txt");
		try {
			if (!f.exists()) f.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			
			String info = null;
			
			while ((info = br.readLine()) != null) {
				String[] info_ = info.split("  ");
				
				int id = Integer.parseInt(info_[0]);
				String name = info_[1];
				int tp = Integer.parseInt(info_[2]);
				int stuNum = Integer.parseInt(info_[3]);
				String teacher = info_[4];
				
				if (tp==0) {
					int maxStuNum = Integer.parseInt(info_[5]);
					Course.courseSet.add(new OptionalCourse(id, name, tp, stuNum, teacher, maxStuNum));
				} else {
					float credit = Float.parseFloat(info_[5]);
					Course.courseSet.add(new RequiredCourse(id, name, tp, teacher, stuNum, credit));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void showAllRequiredCourses() {
		for (Course c : Course.courseSet)
			if (c.tp==1)
				c.show();
	}
	
	public static Course getCourseById(int cid) {
		for (Course c_ : Course.courseSet)
			if (c_.id==cid)
				return c_;
		
		return null;
	}
	
	public static void updateRequiredCourseStuNum() {
		for (Course c_ : Course.courseSet)
			if (c_.tp==1)
				c_.stuNum = AdminUsers.s.size();
	}
	
	public static void deleteCourseInStu(int cid) {
		int i=0;
		Vector<Integer> tmpM = null;
		for (Integer sid : Student.allStuCourses.keySet()) {
			tmpM = Student.allStuCourses.get(sid.intValue());
			for (i=0; i<tmpM.size(); ++i) //遍历该学生课程列表
				if (tmpM.get(i)==cid)
					Student.allStuCourses.get(sid.intValue()).remove(i);
		}
	}
}
