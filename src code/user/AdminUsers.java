package user;

import java.util.Vector;

public class AdminUsers {
	public static User admin = new User(); //管理员

	public static Vector<Student> s = new Vector<>(); //学生
	public static Vector<Teacher> t = new Vector<>(); //学生
	
	public static void showAllStudent() {
		if (s!=null && s.size()!=0) {
			System.out.println("学号  姓名  班级");
			for (Student s : AdminUsers.s)
				s.showInfo();
		} else
			System.out.println("暂无学生信息...");
	}
	
	public static void showAllTcher() {
		if (t!=null && t.size()!=0) {
			System.out.println("工号  姓名  职位");
			for (Teacher t : AdminUsers.t)
				t.showInfo();
		} else
			System.out.println("暂无教师信息...");
	}
	
	public AdminUsers() {
	}

}
