package user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import course.Course;
import inputErr.UsrInput;

public class User {
	public String name; //用户名
	public String pwd;  //密码
	
	public User() {}
	
	/**
	 * 显示用户信息
	 */
	public void showInfo() {
		System.out.println(name+":"+pwd);
	}

	/**
	 * 设置用户密码
	 */
	public void changePwd() {
		System.out.print("输入原密码:");
		
		String originalPwd = UsrInput.getString();
		if (originalPwd.equals(this.pwd))
			while(true) {
				System.out.print("输入新的密码(6-12位):");
				String pwd = UsrInput.getString();
				
				System.out.print("确认密码:");
				String pwd2 = UsrInput.getString();
				
				if (pwd.length()>=6 && pwd.length()<=12 && pwd.equals(pwd2)) {
					this.pwd = pwd;
					System.out.println("密码修改成功...");
					break;
				} else {
					continue;
				}
			}
		else {
			System.out.println("原密码不正确...");
		}
		
	}
	
	public User login() {
		// get admin pwd for .txt file
		User.getAdminInfo();

		System.out.print("输入管理员密码:");
		String pwd = UsrInput.getString();

		if (pwd.equals(AdminUsers.admin.pwd)) {
			System.out.println(AdminUsers.admin.name + ", 欢迎您!");
			return AdminUsers.admin; //成功
		} else {
			System.out.println("登陆失败，密码错误...");
			return null; //失败
		}
	}
	
	public static void getAdminInfo() {
		File f = new File("admin pwd.txt");
		try {
			if (!f.exists())
				f.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			
			String info = null;
			
			if ((info=br.readLine())==null) {
				AdminUsers.admin.name = "admin"; //管理员初始名称
				AdminUsers.admin.pwd = "123456"; //管理员初始密码123456
			} else {
				String info_[] = info.split("  ");
				AdminUsers.admin.name = info_[0];
				AdminUsers.admin.pwd = info_[1];
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void saveAdminInfo() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("admin pwd.txt"));
			
			out.write(AdminUsers.admin.name + "  " + AdminUsers.admin.pwd); // write password

			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void adminMenu() {
		int choice=0, innerChoice=0;
		while (true) {
			System.out.println("1.管理课程\n2.管理教师\n3.管理学生\n4.修改管理员密码\n5.修改管理员名称\n0.退出");
			choice = UsrInput.getInt();
			switch(choice) {
				case 1:
					do {
						innerChoice=User.adminCourseMenu();
						if (innerChoice!=0)
							User.funcDistribute(innerChoice);
					} while (innerChoice!=0);
					break;
				case 2:
					do {
						innerChoice=User.adminTchMenu();
						if (innerChoice!=0)
							User.funcDistribute(innerChoice);
					} while (innerChoice!=0);
					break;
				case 3:
					do {
						innerChoice=User.adminStuMenu();
						if (innerChoice!=0)
							User.funcDistribute(innerChoice);
					} while (innerChoice!=0);
					break;
				case 4:
					AdminUsers.admin.changePwd();
					break;
				case 5:
					User.changeAdminName();
					break;
				case 0:
					Course.saveTofile(); // 保存所有课程信息
					User.saveStuInfo();
					User.saveTchInfo();
					return;
				default: break;
			}
		}
	}
	
	public static void changeAdminName() {
		System.out.print("输入新的管理员名称:");
		String newAdminName = UsrInput.getString();
		
		AdminUsers.admin.name = newAdminName;
		System.out.println("名称修改成功...");
	}
	
	public static void funcDistribute(int funcId) {
			switch (funcId) {
			case 1: Course.addCourses(); 			break;
			case 2: Course.deleteCourses();         break;
			case 3: Course.setCourseTeacher();      break;
			case 4: Course.showAllCourses(); 		break;
			case 5: Course.sortCourses();    		break;
			case 6: User.addTeachers();   			break;
			case 7: User.deleteTeachers(); 			break;
			case 8: Course.searchCourseByTeacher(); break;
			case 9: AdminUsers.showAllTcher(); 		break;
			case 10: User.resetPwd(1); 				break;
			case 11: User.addStudents(); 			break;
			case 12: User.deleteStudents(); 		break;
			case 13: AdminUsers.showAllStudent(); 	break;
			case 14: User.resetPwd(0); 				break;
			default:
			}
	}
	
	public static int adminCourseMenu() {
		System.out.println("选择功能:\n1.添加课程\n2.删除课程\n"
				+ "3.设置课程教师\n4.显示课程列表\n5.按选课人数排序\n"
				+ "0.退出");
		while (true) {
			int choice = UsrInput.getInt();

			if (choice==0) return 0;
			else if (choice>=1 && choice<=5)
				return choice;
		}
	}

	public static int adminTchMenu() {
		System.out.println("选择功能:\n1.添加教师\n2.删除教师\n"
				+ "3.查看教师所授课程\n4.显示教师列表\n"
				+ "5.教师密码恢复\n0.退出");
		while (true) {
			int choice = UsrInput.getInt();
			if (choice==0) return 0;
			else if (choice>=1 && choice<=5)
				return choice+5;
		}
	}

	public static int adminStuMenu() {
		System.out.println("选择功能:\n1.添加学生\n2.删除学生\n"
				+ "3.查看学生列表\n"
				+ "4.学生密码恢复\n0.退出");
		while (true) {
			int choice = UsrInput.getInt();

			if (choice==0) return 0;
			else if (choice>=1 && choice<=4)
				return choice+10;
		}
	}

	public static void addTeacher() {
		System.out.print("姓名:");
		String name = UsrInput.getString();
		System.out.print("工号:");
		int workId = UsrInput.getInt();
		System.out.print("职位:");
		String occupation = UsrInput.getString();
		
		AdminUsers.t.add(new Teacher(name, "123456", workId, occupation));
	}
	
	public static void addTeachers() {
		while(true) {
			User.addTeacher();
			
			System.out.print("教师信息添加成功，是否继续添加？（y/n）");
			if (UsrInput.getString().equals("y"))
				continue;
			else
				break;
		}
	}

	public static boolean deleteTeacher() {
		System.out.print("输入要删除的教师信息工号:");
		int objTeacher = UsrInput.getInt();

		Teacher tmp = null;
		for (int i=0; i<AdminUsers.t.size(); ++i)
			if ((tmp=AdminUsers.t.get(i)).getWorkId()==objTeacher) {
				AdminUsers.t.remove(i); // 删除教师信息

				//首先级联置空其教授的课程
				for (Course c : Course.courseSet)
					if (c.teacher.equals(tmp.name))
						c.teacher = "---";
				return true;
			}
		return false;
	}

	public static void deleteTeachers() {
		if (AdminUsers.t==null || AdminUsers.t.size()==0) {
			System.out.println("暂无任何教师信息...");
			return; //空
		}

		while(true) {
			if (User.deleteTeacher())
				System.out.print("教师信息删除成功，是否继续删除？（y/n）");
			else
				System.out.print("删除失败，无匹配的教师信息...\n输入y继续删除，输入n退出:");
			if (UsrInput.getString().equals("y")) {
				continue;
			} else
				break;
		}
	}
	
	public static void addStudent() {
		System.out.print("姓名:");
		String name = UsrInput.getString();
		System.out.print("学号:");
		int stuId = UsrInput.getInt();
		System.out.print("班级:");
		String class_ = UsrInput.getString();
		
		AdminUsers.s.add(new Student(name, "123456", stuId, class_));
	}
	
	public static void addStudents() {
		while(true) {
			User.addStudent();
			
			System.out.print("学生信息添加成功，是否继续添加？（y/n）");
			if (UsrInput.getString().equals("y"))
				continue;
			else
				break;
		}
	}
	
	public static boolean deleteStudent() {
		System.out.print("输入要删除的学生信息学号:");
		int objStu = UsrInput.getInt();

		Student tmp = null;
		for (int i=0; i<AdminUsers.s.size(); ++i)
			if (objStu==(tmp=AdminUsers.s.get(i)).getStuId()) {
				AdminUsers.s.remove(i);
				
				// 该学生选修的课程人数减一
				for (Integer cid : Student.allStuCourses.get(tmp.getStuId()))
					for (Course c : Course.courseSet)
						if (c.id==cid.intValue())
							--c.stuNum;
				return true;
			}
		return false;
	}

	public static void deleteStudents() {
		if (AdminUsers.s==null || AdminUsers.s.size()==0) {
			System.out.println("暂无任何学生信息...");
			return; //空
		}

		Scanner cin = new Scanner(System.in);
		
		while(true) {
			if (User.deleteStudent())
				System.out.print("学生信息删除成功，是否继续删除？（y/n）");
			else
				System.out.print("删除失败，无匹配的学生信息...\n输入y继续删除，输入n退出:");
			if (cin.next().equals("y")) {
				continue;
			} else
				break;
		}
	}
	
	public static void saveStuInfo() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("student info.txt"));
			
			for (Student s : AdminUsers.s)
				out.write(s.toString()+"\r\n");
			
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readStuInfo() {
		File f = new File("student info.txt");
		try {
			if (!f.exists()) f.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			
			String info = null;
			
			while ((info = br.readLine()) != null) {
				String[] info_ = info.split("  ");
				
				int stuId = Integer.parseInt(info_[0]);
				String name = info_[1];
				String class_ = info_[2];
				String pwd = info_[3];
				
				AdminUsers.s.add(new Student(name, pwd, stuId, class_));
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveTchInfo() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("teacher info.txt"));
			
			for (Teacher t : AdminUsers.t)
				out.write(t.toString()+"\r\n");
			
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readTchInfo() {
		File f = new File("teacher info.txt");
		try {
			if (!f.exists()) f.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			
			String info = null;
			
			while ((info = br.readLine()) != null) {
				String[] info_ = info.split("  ");
				
				int workId = Integer.parseInt(info_[0]);
				String name = info_[1];
				String occupation = info_[2];
				String pwd = info_[3];
				
				AdminUsers.t.add(new Teacher(name, pwd, workId, occupation));
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void resetPwd(int flag) { //1 tch| 0 stu
		int Id=0, pos=0;
		Teacher u=null; Student s=null;
		do {
			System.out.print("输入" +(flag==1?"教师":"学生")+"id:");
			Id = UsrInput.getInt();
			
			pos=User.findPosById(Id, flag);
			if (pos!=-1) {
				if (flag==1) {
					u = AdminUsers.t.get(pos);
					u.pwd = "123456";
					AdminUsers.t.set(pos, u);
				} else {
					s = AdminUsers.s.get(pos);
					s.pwd = "123456";
					AdminUsers.s.set(pos, s);
				}
				
				System.out.print("修改成功,");
			} else
				System.out.println("未搜索到此" +(flag==1?"教师":"学生")+"...");
			
			System.out.print("是否继续修改?(y/n)");
		} while (UsrInput.getString().equals("y"));
	}

	public static int findPosById(int id, int flag) {
		int pos = 0;
		if (flag==1) //教师
			for (Teacher t : AdminUsers.t)
				if (t.getWorkId()==id)
					return pos;
				else ++pos;
		else
			for (Student s : AdminUsers.s)
				if (s.getStuId()==id)
					return pos;
				else ++pos;
		
		return -1;
	}
}
