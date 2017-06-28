package OO9;

import java.util.Random;

public class TrafficLight extends Thread{
	//Overview:horizontal is the horizontal light; vertical is the vertical light.
	
	//表示对象:int horizontal, int vertical
	
	//抽象函数:AF(c)=() where horizontal is Random.nextInt(2), vertical=1-horizontal
	
	//不定式:0<=horizontal<=1 && horizontal+vertical==1
	//1为绿灯 0为红灯
	private int horizontal;//横向
	private int vertical;//纵向
	public boolean repOK(){
		//Effects: returns true if the rep variant holds for this, otherwise returns false.
		if(horizontal < 0 || horizontal > 1)
			return false;
		if(horizontal + vertical != 1)
			return false;
		return true;
	}
	//构造操作
	public TrafficLight(){
		Random ra = new Random();
		horizontal = ra.nextInt(2);
		vertical = 1 - horizontal;
//		System.out.println(horizontal + "," + vertical);
	}
	//更新操作
	public void run(){
		while(true){
			try {
				sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println(horizontal + "," + vertical);
			synchronized (this) {
				horizontal = 1 - horizontal;
				vertical = 1 - horizontal;
			}
		}
	}
	//观察操作
	public synchronized int getHorizontal(){
		//Requires:None
		//Modifies:None
		//Effects:return horizontal
		return horizontal;
	}
	public synchronized int getVertical(){
		//Requires:None
		//Modifies:None
		//Effects:return horizontal
		return vertical;
	}
}
