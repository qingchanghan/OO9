package OO9;

public class Test extends Thread{
	//Overview:system is the dispatcher system.
	
	//��ʾ����:SYSTEM system
	
	//������:AF(c)=(system) where system=c.system
	
	//����ʽ:system<>null
	private SYSTEM system;
	public boolean repOK(){
		//Effects: returns true if the rep variant holds for this, otherwise returns false.
		if(system == null)
			return false;
		return true;
	}
	//�������
	public Test(SYSTEM system){
		this.system = system;
	}
	//���²���
	public void run(){
		try {
			system.addRequest(4, 4, 70, 70);
			system.closeRoad(16, 31, 16, 32);
			system.closeRoad(15, 32, 16, 32);
			sleep(100);
			system.addRequest(16, 32, 63, 42);
			system.openRoad(16, 31, 16, 32);
			sleep(100);
			system.addRequest(16, 32, 63, 42);
			sleep(30000);
			//�������д������������ʱ�����г��⳵����Ϣ
//			for (int i = 0; i < 100; i++) {
//				system.getTaxiInfo(i);
//			}
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println("����Խ��");
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
}
