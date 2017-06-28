package OO9;

class expHandler{
	public static void err(int code){
		//Requires:code is 1 or 2
		//Modifies:None
		//Effects:each code reflect one error
		if(code == 1)
			System.out.println("非连通图，程序退出");
		else if(code == 2)
			System.out.println("所提供的图错误");
		System.exit(0);
	}
}

public class Main {
	public static void main(String[] args){
		//Requires:None
		//Modifies:None
		//Effects:create and start all Threads
		Map map = new Map();
		Taxi[] TaxiList = new Taxi[100];
		for(int i = 0; i < 100; i++){
			TaxiList[i] = new Taxi(i,map);
			TaxiList[i].start();
		}
		SYSTEM system = new SYSTEM(TaxiList,map);
		Test test = new Test(system);
		system.start();
		test.start();
	}
}
