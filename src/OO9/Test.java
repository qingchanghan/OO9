package OO9;

public class Test extends Thread{
	//Overview:system is the dispatcher system.
	
	//表示对象:SYSTEM system
	
	//抽象函数:AF(c)=(system) where system=c.system
	
	//不定式:system<>null
	private SYSTEM system;
	public boolean repOK(){
		//Effects: returns true if the rep variant holds for this, otherwise returns false.
		if(system == null)
			return false;
		return true;
	}
	//构造操作
	public Test(SYSTEM system){
		this.system = system;
	}
	//更新操作
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
			//下面三行代码用于输出此时刻所有出租车的信息
//			for (int i = 0; i < 100; i++) {
//				system.getTaxiInfo(i);
//			}
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println("数组越界");
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
}
