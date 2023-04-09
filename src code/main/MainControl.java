package main;

import course.Course;
import inputErr.UsrInput;
import user.AdminUsers;
import user.Student;
import user.Teacher;
import user.User;

public class MainControl {
	
	User u;
	Student s;
	Teacher t;
	
	public static void main(String[] args) {
		Course.readFromFile();		//读入所有课程信息
		
		User.readStuInfo(); 		//学生信息载入
		User.readTchInfo();			//教师信息载入
		Student.prepareStuCourse(); //读取学生选课信息建立映射关系，map<int, vector<int>>
		
		boolean isexit;
		do {
			isexit = menu(); 
		} while (!isexit);			//0退出
		
		User.saveStuInfo();			//保存学生信息
		User.saveTchInfo();			//保存教师信息
		Student.saveToFile();		//保存学生选课信息
		Course.saveTofile();		//保存课程信息
		User.saveAdminInfo();		//保存管理员信息

		System.out.print("\n再见...");
	}
	
	public static boolean menu() {
		System.out.println("选择登录端:\n1.管理员\n2.教师\n3.学生\n0.退出");
		
		int choice = UsrInput.getInt();
		switch(choice) {
		case 1:
			if (AdminUsers.admin.login()!=null) //管理员登录
				AdminUsers.admin.adminMenu();   //跳转到管理员菜单
			return false;
		case 2:
			Teacher tmp = new Teacher();
			Teacher t = tmp.login(); //教师登录
			if (t!=null)
				Teacher.tchMenu(t);  //跳转到教师菜单
			return false;
		case 3:
			Student tmp_ = new Student();
			Student s = tmp_.login();
			if (s!=null)
				Student.stuMenu(s);
			return false;
		case 0:
			return true;
		}
		
		return true;
	}
}
