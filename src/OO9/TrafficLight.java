package OO9;

import java.util.Random;

public class TrafficLight extends Thread{
	//Overview:horizontal is the horizontal light; vertical is the vertical light.
	
	//��ʾ����:int horizontal, int vertical
	
	//������:AF(c)=() where horizontal is Random.nextInt(2), vertical=1-horizontal
	
	//����ʽ:0<=horizontal<=1 && horizontal+vertical==1
	//1Ϊ�̵� 0Ϊ���
	private int horizontal;//����
	private int vertical;//����
	public boolean repOK(){
		//Effects: returns true if the rep variant holds for this, otherwise returns false.
		if(horizontal < 0 || horizontal > 1)
			return false;
		if(horizontal + vertical != 1)
			return false;
		return true;
	}
	//�������
	public TrafficLight(){
		Random ra = new Random();
		horizontal = ra.nextInt(2);
		vertical = 1 - horizontal;
//		System.out.println(horizontal + "," + vertical);
	}
	//���²���
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
	//�۲����
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
