package OO9;

import java.util.ArrayList;

public class Request {
	//Overview:startPlace_x and startPlace_y are the start place of request; endPlace_x and endPlace_y are the end place of request; list contains the taxis
	//which can get the request; waitTime is the waiting time of the request.
	
	//表示对象:int startPlace_x, int startPlace_y, int endPlace_x, int endPlace_y, ArrayList<Taxi> list, int waitTime
	
	//抽象函数:AF(c)=(x1,y1,x2,y2) where startPlace_x=c.x1, startPlace_y=c.y1, endPlace_x=c.x2, endPlace_y=c.y2
	
	//不定式:1<=startPlace_x,startPlace_y,endPlace_x,endPlace_y<=80 && list<>null && list[i]<>null when 0<=i<=list.size-1 && 0<=waitTime<=3000
	private int startPlace_x;
	private int startPlace_y;
	private int endPlace_x;
	private int endPlace_y;
	private ArrayList<Taxi> list;
	private int waitTime;
	
	public boolean repOK(){
		//Effects: returns true if the rep variant holds for this, otherwise returns false.
		if(startPlace_x < 1 || startPlace_x > 80)
			return false;
		if(startPlace_y < 1 || startPlace_y > 80)
			return false;
		if(endPlace_x < 1 || endPlace_x > 80)
			return false;
		if(endPlace_y < 1 || endPlace_y > 80)
			return false;
		for(int i = 0; i < list.size(); i++)
			if(list.get(i) == null)
				return false;
		if(waitTime < 0 || waitTime > 3000)
			return false;
		return true;
	}
	//构造操作
	public Request(int x1, int y1, int x2, int y2){
		startPlace_x = x1;
		startPlace_y = y1;
		endPlace_x = x2;
		endPlace_y = y2;
		list = new ArrayList<>();
		waitTime = 0;
	}
	//更新操作
	public void addTaxi(Taxi t){
		//Requires:None
		//Modifies:list
		//Effects:add taxi of list
		list.add(t);
	}
	public void addWaitTime(){
		//Requires:None
		//Modifies:waitTime+=100
		//Effects:add the wait time of request
		waitTime += 100;
	}
	//观察操作
	public boolean ifTimeUp(){
		//Requires:None
		//Modifies:None
		//Effects:check if the request time up
		if(waitTime >= 3000)
			return true;
		else
			return false;
	}
	public int getStart_x(){
		//Requires:None
		//Modifies:None
		//Effects:get the startPlace_x of request
		return startPlace_x;
	}
	public int getStart_y(){
		//Requires:None
		//Modifies:None
		//Effects:get the startPlace_y of request
		return startPlace_y;
	}
	public int getEnd_x(){
		//Requires:None
		//Modifies:None
		//Effects:get the endPlace_x of request
		return endPlace_x;
	}
	public int getEnd_y(){
		//Requires:None
		//Modifies:None
		//Effects:get the endPlace_y of request
		return endPlace_y;
	}
	public ArrayList<Taxi> getList(){
		//Requires:None
		//Modifies:None
		//Effects:get the taxi list of request
		return list;
	}
	public String toString(){
		//Requires:None
		//Modifies:None
		//Effects:get the String of request
		return "从(" + startPlace_x + "," + startPlace_y +")去往(" + endPlace_x + "," + endPlace_y + ")的请求";
	}
}
