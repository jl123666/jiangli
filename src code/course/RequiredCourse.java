package course;

public class RequiredCourse extends Course {
	private float credit;

	public RequiredCourse(int id, String name, int tp, String teacher, int stuNum, float credit) {
		this.id = id;
		this.name = name;
		this.tp = tp;
		this.teacher = teacher;
		this.stuNum = stuNum;
		this.credit = credit;
	}
	
	public void show() {
		System.out.println(id + "  " + name+"  必修  "+teacher+"  "+stuNum+"  "+credit);
	}
	
	
	public String toString() {
		return super.toString()+"  "+credit;
	}
}
