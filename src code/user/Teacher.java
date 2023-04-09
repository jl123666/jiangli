package user;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import course.Course;
import inputErr.UsrInput;

public class Teacher extends User {
	private int workId;
	private String occupation; //职位
	
	public static Map<Integer, Vector<Integer>> allTchCourses = new HashMap<>();
	
	public Teacher(String name, String pwd, int workId, String occupation) {
		this.name = name;
		this.pwd = pwd;
		this.workId = workId;
		this.occupation = occupation;
	}
	
	public Teacher() {}	

	public void showInfo() {
		System.out.println(workId+" "+name+" "+occupation);
	}

	public Teacher login() {
		System.out.print("输入工号:");
		int workId = UsrInput.getInt();
		
		System.out.print("输入密码:");
		String pwd = UsrInput.getString();
		
		for (Teacher t : AdminUsers.t)
			if (t.workId==workId && t.pwd.equals(pwd)) {
				System.out.println(t.name+" "+t.occupation+", 您好!");
				return t;
			}

		System.out.println("用户名或密码登陆失败...");
		return null;
	}

	public int getWorkId() {
		return workId;
	}

	public String toString() {
		return workId+"  "+name+"  "+occupation+"  "+pwd;
	}
	
	public static void tchMenu(Teacher t) {
		while (true) {
			System.out.println("选择功能:\n1.修改密码\n2.查看所授课程\n3.查看课程学生\n0.退出");
			int choice = UsrInput.getInt();
			switch(choice) {
				case 1: t.changePwd(); break;
				case 2: Teacher.updateTchCourses(t); Teacher.viewTchCourses(t); break;
				case 3: Teacher.viewCourseStu(t); break;
				case 0: return; //保存退出
				default: break;
			}
		}
	}
	
	public static void viewTchCourses(Teacher t) {
		System.out.println("课程号  课程名  类型  教师  选课人数  学分或最大选课人数");

		Course.updateRequiredCourseStuNum();
		boolean haveClasses = false;
		for (Integer workId : Teacher.allTchCourses.keySet())
			if (workId==t.workId)
				for (Integer cid : Teacher.allTchCourses.get(workId))
					for (Course c_ : Course.courseSet)
						if (c_.id==cid) {
							haveClasses = true;
							c_.show();
						}
		
		if (haveClasses==false)
			System.out.println("暂无课程...");
	}

	public static void viewCourseStu(Teacher t) {
		System.out.print("输入课程编号:");

		int cid_ = UsrInput.getInt();
		boolean anyOneSelect = false;
		
		// 是否存在该课程
		for (Integer cid : Teacher.allTchCourses.get(t.workId))
			if (cid_==cid) {

				if (Course.getCourseById(cid).tp==1) {
					System.out.println("您无权限进行此操作...");
					return;
				}

				System.out.println("选修该门课程学生列表:");
				
				for (Student s : AdminUsers.s)
					if (Student.isSelectedSomeCourse(s, Course.getCourseById(cid))) {
						anyOneSelect = true;
						s.showInfo();
					}
				if (!anyOneSelect)
					System.out.println("暂无学生数据...");
				
				return;
			}
		System.out.println("课程编号有误或您并未教授该门课程...");
	}
	
	/**
	 * 从课程列表中更新教授得课程
	 */
	public static void updateTchCourses(Teacher t) {
		Teacher.allTchCourses.clear();
		for (Course c_ : Course.courseSet)
			if (c_.teacher.equals(t.name)) {
				if (Teacher.allTchCourses.containsKey(t.workId)==false)
					Teacher.allTchCourses.put(t.workId, new Vector<Integer>());
				Teacher.allTchCourses.get(t.workId).add(c_.id);
			}
	}
	
}