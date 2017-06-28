package OO9;

import java.util.Random;

public class Taxi extends Thread{
	//Overview:no is the number of taxi; currentPlace_x and currentPlace_y are the place of taxi now;targetStartPlace_x and targetStartPlace_y 
	//are target start place of taxi, targetEndPlace_x and targetEndPlace_y are target end place of taxi; lastState is the last status of taxi,
	//currentState is the current status of taxi, nextState is the next status of taxi; waitTime is the waiting time when taxi is in WAITSERVICE,
	//stopTime is the stopping time when taxi is in STOP;credit is the credit of taxi; map is the map of city; in last, WAITSERVICE, COMINGSERVICE,
	//SERVICING, STOP are four constants.
	
	//��ʾ����:int no, int currentPlace_x, int currentPlace_y, int lastState, int currentState, int nextState, int targetStartPlace_x, int targetStartPlace_y,
	//int targetEndPlace_x, int targetEndPlace_y, int waitTime, int stopTime, int credit, Map map
	
	//������:AF(c)=(no,map), where no=c.no, map=c.map, currentPlace_x=Random.nextInt(80)+1, currentPlace_y=Random.nextInt(80)+1, lastState=nextState=currentState=0,
	//targetStartPlace_x=targetStartPlace_y=targetEndPlace_x=targetEndPlace_y=0,waitTime=stopTime=0,credit=0
	
	//����ʽ:1<=no<=100 && 1<=currentPlace_x<=80 && 1<=currentPlace_y<=80 && 0<=targetStartPlace_x<=80 && 0<=targerStartPlace_y<=80 && 0<=targetEndPlace_x<=80 &&
	//0<=targetEndPlace_y<=80 && 0<=lastState<=3 && 0<=currentState<=3 && 0<=nextState<=3 && waitTime=k*100(0<=k<=200) && stopTime=m*100(0<=m<=10) && credit>=0 && map<>null

	private int no;
	private int currentPlace_x;
	private int currentPlace_y;
	private int lastState;
	private int currentState;
	private int nextState;
	private int targetStartPlace_x;
	private int targetStartPlace_y;
	private int targetEndPlace_x;
	private int targetEndPlace_y;
	private int waitTime;//��λ��ms
	private int stopTime;
	private int credit;
	private Map map;
	private static final int WAITSERVICE = 0;
	private static final int COMINGSERVICE = 1;
	private static final int SERVICING = 2;
	private static final int STOP = 3;
	
	public boolean repOK(){
		//Effects: returns true if the rep variant holds for this, otherwise returns false.
		if(no < 1 || no > 100)
			return false;
		if(currentPlace_x < 1 || currentPlace_x > 80)
			return false;
		if(currentPlace_y < 1 || currentPlace_y > 80)
			return false;
		if(targetEndPlace_x < 0 || targetEndPlace_x > 80)
			return false;
		if(targetEndPlace_y < 0 || targetEndPlace_y > 80)
			return false;
		if(targetStartPlace_x < 0 || targetStartPlace_x > 80)
			return false;
		if(targetStartPlace_y < 0 || targetStartPlace_y > 80)
			return false;
		if(lastState < 0 || lastState > 3)
			return false;
		if(currentState < 0 || currentState > 3)
			return false;
		if(nextState < 0 || nextState > 3)
			return false;
		if(waitTime < 0 || waitTime > 20000)
			return false;
		if(stopTime < 0 || stopTime > 1000)
			return false;
		if(credit < 0)
			return false;
		if(map == null)
			return false;
		return true;
	}
	
	//�������
	public Taxi(int no, Map map){
		this.no = no + 1;
		Random ra = new Random();
		currentPlace_x = ra.nextInt(80) + 1;
		currentPlace_y = ra.nextInt(80) + 1;
		lastState = WAITSERVICE;
		currentState = WAITSERVICE;
		nextState = WAITSERVICE;
		targetStartPlace_x = 0;
		targetStartPlace_y = 0;
		targetEndPlace_x = 0;
		targetEndPlace_y = 0;
		waitTime = 0;
		stopTime = 0;
		credit = 0;
		this.map = map;
	}
	
	//���²���
	public void run(){
		int lastDirection = 0, lastDirection1 = 0;
		while(true){
			switch (currentState) {
			case WAITSERVICE:if(nextState == COMINGSERVICE){
							}
							else if(waitTime == 20000){
								synchronized (this) {
									nextState = STOP;
								}
								waitTime = 0;
							}
							else{
								waitTime += 100;
								//�ó������һ����λ Ȼ���¼λ�õı仯
								switch (map.getRandomDirection(currentPlace_x, currentPlace_y)) {
								case 1:
									while(!map.canRun(lastDirection1, 1, currentPlace_x, currentPlace_y)){
//											System.out.println("��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
//										System.out.println("��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_y--;
									}
									lastDirection = 1;
									lastDirection1 = 1;
									map.addVehicleFlowrate(currentPlace_x, currentPlace_y+1, currentPlace_x, currentPlace_y);
									break;
								case 2:
									while(!map.canRun(lastDirection1, 2, currentPlace_x, currentPlace_y)){
//											System.out.println("��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
//										System.out.println("��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_y++;
									}
									lastDirection = 2;
									lastDirection1 = 2;
									map.addVehicleFlowrate(currentPlace_x, currentPlace_y-1, currentPlace_x, currentPlace_y);
									break;
								case 3:
									while(!map.canRun(lastDirection1, 3, currentPlace_x, currentPlace_y)){
//											System.out.println("��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
//										System.out.println("��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_x--;
									}
									lastDirection = 3;
									lastDirection1 = 3;
									map.addVehicleFlowrate(currentPlace_x+1, currentPlace_y, currentPlace_x, currentPlace_y);
									break;
								case 4:
									while(!map.canRun(lastDirection1, 4, currentPlace_x, currentPlace_y)){
//											System.out.println("��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
//										System.out.println("��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_x++;
									}
									lastDirection = 4;
									lastDirection1 = 4;
									map.addVehicleFlowrate(currentPlace_x-1, currentPlace_y, currentPlace_x, currentPlace_y);
									break;
								default:
									break;
								}
							}
							break;
			case COMINGSERVICE:System.out.println(no + "�ų��⳵����λ��:(" + currentPlace_x + "," + currentPlace_y + ")");
							if(currentPlace_x == targetStartPlace_x && currentPlace_y == targetStartPlace_y){
								synchronized (this) {
									nextState = STOP;
								}
								System.out.print(no + "�ų��⳵�ѵ���(" + currentPlace_x + "," + currentPlace_y + ")�ӿ�");
								System.out.println(" ��������(" + targetEndPlace_x + "," + targetEndPlace_y + ")  ��ǰʱ��:" + System.currentTimeMillis());
							}
							else{
								//�ó���Ŀ�ĵ���һ����λ�����·���� ��¼λ�ñ仯
								switch (map.getDirection(currentPlace_x, currentPlace_y, targetStartPlace_x, targetStartPlace_y)) {
								case 1:while(!map.canRun(lastDirection1, 1, currentPlace_x, currentPlace_y)){
										System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_y--;
									}
									lastDirection = 1;
									lastDirection1 = 1;
									map.addVehicleFlowrate(currentPlace_x, currentPlace_y+1, currentPlace_x, currentPlace_y);
									break;
								case 2:while(!map.canRun(lastDirection1, 2, currentPlace_x, currentPlace_y)){
										System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_y++;
									}
									lastDirection = 2;
									lastDirection1 = 2;
									map.addVehicleFlowrate(currentPlace_x, currentPlace_y-1, currentPlace_x, currentPlace_y);
									break;
								case 3:while(!map.canRun(lastDirection1, 3, currentPlace_x, currentPlace_y)){
										System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_x--;
									}
									lastDirection = 3;
									lastDirection1 = 3;
									map.addVehicleFlowrate(currentPlace_x+1, currentPlace_y, currentPlace_x, currentPlace_y);
									break;
								case 4:while(!map.canRun(lastDirection1, 4, currentPlace_x, currentPlace_y)){
										System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_x++;
									}
									lastDirection = 4;
									lastDirection1 = 4;
									map.addVehicleFlowrate(currentPlace_x-1, currentPlace_y, currentPlace_x, currentPlace_y);
									break;
								default:
									break;
								}
							}
							break;
			case SERVICING:System.out.println(no + "�ų��⳵����λ��:(" + currentPlace_x + "," + currentPlace_y + ")");
							if(currentPlace_x == targetEndPlace_x && currentPlace_y == targetEndPlace_y){
								synchronized (this) {
									nextState = STOP;
									this.credit += 3;
								}
								System.out.println("��(" + targetStartPlace_x + "," + targetStartPlace_y +")ȥ��(" + targetEndPlace_x + "," + targetEndPlace_y + ")������ �ѱ�" + no + "�ų��⳵���    ��ǰʱ��:" + System.currentTimeMillis());
							}
							else{
								//�ó���Ŀ�ĵ���һ����λ�����·���� ��¼λ�ñ仯
								switch (map.getDirection(currentPlace_x, currentPlace_y, targetEndPlace_x, targetEndPlace_y)) {
								case 1:while(!map.canRun(lastDirection1, 1, currentPlace_x, currentPlace_y)){
										System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_y--;
									}
									lastDirection = 1;
									lastDirection1 = 1;
									map.addVehicleFlowrate(currentPlace_x, currentPlace_y+1, currentPlace_x, currentPlace_y);
									break;
								case 2:while(!map.canRun(lastDirection1, 2, currentPlace_x, currentPlace_y)){
										System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_y++;
									}
									lastDirection = 2;
									lastDirection1 = 2;
									map.addVehicleFlowrate(currentPlace_x, currentPlace_y-1, currentPlace_x, currentPlace_y);
									break;
								case 3:while(!map.canRun(lastDirection1, 3, currentPlace_x, currentPlace_y)){
										System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_x--;
									}
									lastDirection = 3;
									lastDirection1 = 3;
									map.addVehicleFlowrate(currentPlace_x+1, currentPlace_y, currentPlace_x, currentPlace_y);
									break;
								case 4:while(!map.canRun(lastDirection1, 4, currentPlace_x, currentPlace_y)){
										System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ���Ƶȴ�");
										try {
											sleep(50);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									System.out.println(no + "�ų��⳵��(" + currentPlace_x + "," + currentPlace_y + ")�����ߣ�ͨ��");
									synchronized (this) {
										currentPlace_x++;
									}
									lastDirection = 4;
									lastDirection1 = 4;
									map.addVehicleFlowrate(currentPlace_x-1, currentPlace_y, currentPlace_x, currentPlace_y);
									break;
								default:
									break;
								}
							}
							break;
			case STOP:if(stopTime == 1000){
						stopTime = 0;
						synchronized (this) {
							if(lastState == COMINGSERVICE){
								nextState = SERVICING;
							}
							else if(lastState == SERVICING){
								nextState = WAITSERVICE;
							}
							else if(lastState == WAITSERVICE){
								nextState = WAITSERVICE;
							}
						}
					}
					else{
						stopTime += 100;
					}
					break;
			default:
				break;
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(nextState != currentState){
				lastState = currentState;
				currentState = nextState;
			}
			if(lastDirection != 0){
				if(lastDirection == 1){
					map.decreaseVehicleFlowrate(currentPlace_x, currentPlace_y+1, currentPlace_x, currentPlace_y);
				}
				else if(lastDirection == 2){
					map.decreaseVehicleFlowrate(currentPlace_x, currentPlace_y-1, currentPlace_x, currentPlace_y);
				}
				else if(lastDirection == 3){
					map.decreaseVehicleFlowrate(currentPlace_x+1, currentPlace_y, currentPlace_x, currentPlace_y);
				}
				else{
					map.decreaseVehicleFlowrate(currentPlace_x-1, currentPlace_y, currentPlace_x, currentPlace_y);
				}
				lastDirection = 0;
			}
		}
	}
	public synchronized void PickUp(int targetStartPlace_X, int targetStartPlace_Y, int targetEndPlace_X, int targetEndPlace_Y){
		//Requires:targetStartPlace_X,targetStartPlace_Y,targetEndPlace,targetEndPlace is in the range of 1-80
		//Modifies:nextState, targetStartPlace_x, targetStartPlace_y, targetEndPlace_x, targetEndPlace_y, credit
		//Effects:change the next state of taxi
		nextState = COMINGSERVICE;
		targetStartPlace_x = targetStartPlace_X;
		targetStartPlace_y = targetStartPlace_Y;
		targetEndPlace_x = targetEndPlace_X;
		targetEndPlace_y = targetEndPlace_Y;
		credit++;
	}
	
	//�۲����
	public int getNo(){
		//Requires:None
		//Modifies:None
		//Effects:get the number of taxi
		return no;
	}
	public synchronized int getCurrentPlace_x(){
		//Requires:None
		//Modifies:None
		//Effects:get the CurrentPlace_x of taxi
		return currentPlace_x;
	}
	public synchronized int getCurrentPlace_y(){
		//Requires:None
		//Modifies:None
		//Effects:get the CurrentPlace_y of taxi
		return currentPlace_y;
	}
	public synchronized int getCredit(){
		//Requires:None
		//Modifies:None
		//Effects:get the credit of taxi
		return credit;
	}
	public synchronized int getCurrentState(){
		//Requires:None
		//Modifies:None
		//Effects:get the current state of taxi
		return currentState;
	}
	public synchronized int getNextState(){
		//Requires:None
		//Modifies:None
		//Effects:get the next state of taxi
		return nextState;
	}
	public synchronized String stateToString(){
		//Requires:None
		//Modifies:None
		//Effects:get the String of state of taxi
		switch (currentState) {
		case WAITSERVICE:return "WAITSERVICE";
		case COMINGSERVICE:return "COMINGSERVICE";
		case SERVICING:return "SERVICING";
		case STOP:return "STOP";
		default:
			return null;
		}
	}
}
