package OO9;

import java.util.ArrayList;

public class SYSTEM extends Thread{
	//Overview:TaxiList includes 100 taxis; RequestList includes all effective requests; map is the map of city.
	
	//��ʾ����:Taxi[] TaxiList, ArrayList<Request> RequestList, Map map
	
	//������:AF(c)=(TaxiList,map) where TaxiList=c.TaxiList, map=c.map, RequestList is from addRequest
	
	//����ʽ:TaxiLlist<>null && TaxiList[i]<>null when 0<=i<=99 && RequestList<>null && RequestList[i]<>null when 0<=i<=RequestList.size-1 && map<>null
	private Taxi[] TaxiList;
	private ArrayList<Request> RequestList;
	private Map map;
	
	public boolean repOK(){
		//Effects: returns true if the rep variant holds for this, otherwise returns false.
		if(TaxiList == null)
			return false;
		for(int i = 0; i <= 99; i++)
			if(TaxiList[i] == null)
				return false;
		if(RequestList == null)
			return false;
		for(int i = 0; i < RequestList.size(); i++)
			if(RequestList.get(i) == null)
				return false;
		if(map == null)
			return false;
		return true;
	}
	//�������
	public SYSTEM(Taxi[] TaxiList, Map map){
		this.TaxiList = TaxiList;
		RequestList = new ArrayList<>();
		this.map = map;
	}
	//���²���
	public void run(){
		while(true){
			synchronized (this) {
				for(int i = 0; i < RequestList.size(); i++){
					Request r = RequestList.get(i);
					if(r.ifTimeUp()){
						//ѡһ�����ŵĳ��⳵
						Taxi t = choice(i);
						if(t != null){
							t.PickUp(r.getStart_x(), r.getStart_y(), r.getEnd_x(), r.getEnd_y());
							System.out.println(r.toString() + " ���������" + t.getNo() + "�ų��⳵    ��ǰʱ��:" + System.currentTimeMillis());
						}
						else{
							System.out.println(r.toString() + " �޳���Ӧ����ʧЧ    ��ǰʱ��:" + System.currentTimeMillis());
//							for (int k = 0; k <= 99; k++) {
//								this.getTaxiInfo(k);
//							}
						}
						RequestList.remove(i);
						i--;
					}
					else{
						for(int j = 0; j <= 99; j++){
							if(r.getList().contains(TaxiList[j]))
								continue;
							if(IfInRange(TaxiList[j], r) && TaxiList[j].getCurrentState() == 0)
								r.addTaxi(TaxiList[j]);
						}
						r.addWaitTime();
					}
				}
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private Taxi choice(int num){
		//Requires:num is the number of the request in the list
		//Modifies:None
		//Effects:choice best taxi for the request
		ArrayList<Taxi> list = RequestList.get(num).getList();
		int i = 0;
		int d1 = 0, d2 = 0;
		Taxi t = null; 
		for(; i < list.size(); i++){
			if(t == null){
				if(list.get(i).getCurrentState() == 0 && list.get(i).getNextState() != 1)
					t = list.get(i);
			}
			else{
				if(list.get(i).getCurrentState() == 0 && list.get(i).getNextState() != 1){
					if(list.get(i).getCredit() > t.getCredit())
						t = list.get(i);
					else if(list.get(i).getCredit() < t.getCredit())
						continue;
					else{
						d1 = distance(list.get(i).getCurrentPlace_x(),list.get(i).getCurrentPlace_y(),RequestList.get(num).getStart_x(),RequestList.get(num).getStart_y());
						d2 = distance(t.getCurrentPlace_x(),t.getCurrentPlace_y(),RequestList.get(num).getStart_x(),RequestList.get(num).getStart_y());
						if(d1 < d2){
							t = list.get(i);
						}
						else {
							continue;
						}
					}
				}
			}
		}
		return t;
	}
	private boolean IfInRange(Taxi t, Request r){
		//Requires:t is a taxi, r is a request
		//Modifies:None
		//Effects:check if t is in the range of r
		if(t.getCurrentPlace_x() >= r.getStart_x() - 2 && t.getCurrentPlace_x() <= r.getStart_x() + 2 &&
			t.getCurrentPlace_y() >= r.getStart_y() - 2 && t.getCurrentPlace_y() <= r.getStart_y() + 2)
			return true;
		else {
			return false;
		}
	}
	public synchronized void addRequest(int x1, int y1, int x2, int y2){
		//Requires:x1,y1,x2,y2 is in the range of 1-80
		//Modifies:RequestList
		//Effects:add request for list
		Request r = new Request(x1, y1, x2, y2);
		if(x1 >= 1 && x1 <= 80 && y1 >= 1 && y1 <= 80 && x2 >= 1 && x2 <= 80 && y2 >= 1 && y2 <= 80 && !(x1 == x2 && y1 == y2)){
			synchronized (RequestList) {
				RequestList.add(r);
			}
		}
		else{
			System.out.println(r.toString() + "��Ч");
		}
	}
	public synchronized void openRoad(int x1, int y1, int x2, int y2){
		//Requires:x1,y1,x2,y2 is in the range of 1-80
		//Modifies:map
		//Effects:open road
		map.openRoad(x1, y1, x2, y2);
	}
	public synchronized void closeRoad(int x1, int y1, int x2, int y2){
		//Requires:x1,y1,x2,y2 is in the range of 1-80
		//Modifies:map
		//Effects:close road
		map.closeRoad(x1, y1, x2, y2);
	}
	//�۲����
	private int distance(int x1, int y1, int x2, int y2){
		//Requires:x1,y1,x2,y2 is in the range of 1-80
		//Modifies:None
		//Effects:get shortest distance between two places
		return map.getDistance(x1, y1, x2, y2);
	}
	public synchronized void getTaxiInfo(int i){
		//Requires:i is in the range of 0-99
		//Modifies:None
		//Effects:get the information of No.i taxi
		try {
			System.out.print(TaxiList[i].getNo() + "�ų��⳵ λ��:(" + TaxiList[i].getCurrentPlace_x() + "," + 
					TaxiList[i].getCurrentPlace_y() + ") ״̬:" + TaxiList[i].stateToString() + " ����:" + TaxiList[i].getCredit());
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(i + "����Խ��");
		}
		System.out.println("  ��ǰʱ��:" + System.currentTimeMillis());
	}
}
