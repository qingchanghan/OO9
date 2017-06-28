package OO9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Map {
	//Overview:map is the input map.txt; matrix is the adjacency matrix of map; across is the input across.txt; trafficLight is the traffic light of the city;
	//num is the number of traffic light; vehicleFlowrate is the vehicle flow rate matrix; MAXROW is the constant:80;
	
	//表示对象:short[][] matrix, short[][] map, short[] across, TrafficLight[] trafficLight, int num, short[][] vehicleFlowrate
	
	//抽象函数:AF(c)=(),where map is from map.txt, across is from across.txt, matrix is from map, trafficLight is from matrix and across, vehicleFlowrate is from matrix.
	
	//不定式:map[i][j]=0,1,2,3 when 0<=i<=80 0<=j<=80 && matrix[i][j]=-1,0 or 1 when 0=i<=6400 0<=j<=6400 && 0<=across[i]<=6400 when 0<=i<=6400 && 0<=num<=6400 &&
	//trafficLight[i]<>null when 1<=i<=num && vehicleFlowrate[i][j]>=0 when 0<=i<=1 0<=j<=6320
	private short[][] matrix;
	private short[][] map;
	private short[] across;
	private TrafficLight[] trafficLight;//从1开始计
	private int num;
	private short[][] vehicleFlowrate;
	public static final int MAXROW = 80;
	public boolean repOK(){
		//Effects: returns true if the rep variant holds for this, otherwise returns false.
		int i,j;
		for(i = 0; i <= MAXROW; i++)
			for(j = 0; j <= MAXROW; j++)
				if(map[i][j] < 0 || map[i][j] > 3)
					return false;
		for(i = 0; i <= MAXROW*MAXROW; i++){
			for(j = 0; j <= MAXROW*MAXROW; j++)
				if(matrix[i][j] < -1 || matrix[i][j] > 1)
					return false;
			if(across[i] < 0 || across[i] > MAXROW*MAXROW)
				return false;
		}
		if(num < 0 || num > MAXROW*MAXROW)
			return false;
		for(i = 1; i <= num; i++)
			if(trafficLight[i] == null)
				return false;
		for(i = 0; i <= 1; i++)
			for(j = 0; j <= MAXROW*(MAXROW-1); j++)
				if(vehicleFlowrate[i][j] < 0)
					return false;
		return true;
	}

	//构造操作
	public Map(){
		matrix = new short[MAXROW*MAXROW+1][MAXROW*MAXROW+1];
		map = new short[MAXROW+1][MAXROW+1];
		across = new short[MAXROW*MAXROW+1];
		trafficLight = new TrafficLight[MAXROW*MAXROW];
		num = 0;
		vehicleFlowrate = new short[2][MAXROW*(MAXROW-1)+1];
		inputMap();
		inputAcross();
		toAdjacencyMatrix();
		ifConnected();
		setTrafficLight();
		startTrafficLight();
		toVehicleFlowrate();
		System.out.println("地图分析完成");
	}
	private void inputMap(){
		//Requires:None
		//Modifies:map
		//Effects:change map from map.txt if map.txt doesn't exits, print "文件未找到".
		try {
			//输入map
			File f = new File("D:\\map.txt");
			BufferedReader bw = new BufferedReader(new FileReader(f));
			int i = 1, j = 1, c = 0;
			while((c = bw.read()) != -1){
				if(c == '\n' || c == '\r' || c == ' ')
					continue;
				if(j < MAXROW){
					map[i][j++] = (short) (c - '0');
				}
				else if(j == MAXROW){
					map[i][j] = (short) (c - '0');
					i++;
					j = 1;
				}
			}
			bw.close();
			if(i != MAXROW+1 || j != 1)
				expHandler.err(2);
		} catch (FileNotFoundException e) {
			System.out.println("文件未找到");
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	private void inputAcross(){
		//Requires:None
		//Modifies:across
		//Effects:change across from across.txt if across.txt doesn't exits, print "文件未找到".
		try {
			File f = new File("D:\\across.txt");
			BufferedReader bw = new BufferedReader(new FileReader(f));
			int c = 0, i = 1;
			while((c = bw.read()) != -1){
				if(c == '\n' || c == '\r')
					continue;
				if(c == '0')
					across[i++] = 0;
				else if(c == '1')
					across[i++] = 1;
				else {
					expHandler.err(2);
				}
			}
			bw.close();
		} catch (FileNotFoundException e) {
			System.out.println("文件未找到");
		} catch (IOException e){
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e){
			expHandler.err(2);
		}
	}
	private void setTrafficLight(){
		//Requires:None
		//Modifies:trafficLight
		//Effects:change trafficLight according to matrix and across.
		int i;
		for(i = 1; i <= MAXROW*MAXROW; i++){
			if(i == 1 || i == MAXROW || i == MAXROW*(MAXROW-1)+1 || i == MAXROW*MAXROW){
				across[i] = 0;
				continue;
			}
			int k = 0;
			if(i > MAXROW && matrix[i][i-MAXROW] == 1)
				k++;
			if(i <= MAXROW*(MAXROW-1) && matrix[i][i+MAXROW] == 1)
				k++;
			if(matrix[i][i-1] == 1)
				k++;
			if(matrix[i][i+1] == 1)
				k++;
			if(k >= 3){
				trafficLight[++num] = new TrafficLight();
				across[i] = (short) num;
			}
			else {
				across[i] = 0;
			}	
		}
	}
	private void startTrafficLight(){
		//Requires:None
		//Modifies:None
		//Effects:start all TrafficLight Threads
		for(int i = 1; i <= num; i++){
			trafficLight[i].start();
		}
	}
	private void ifConnected(){
		//Requires:None
		//Modifies:None
		//Effects:check if the graph is connected
		Queue<Integer> queue = new LinkedList<Integer>();
		short[] visited = new short[MAXROW*MAXROW + 1];
		int i;
		for(i = 0; i <= MAXROW*MAXROW; i++)
			visited[i] = 0;
		visited[1] = 1;
		queue.offer(1);
		while(!queue.isEmpty()){
			int v = queue.remove();
			i = v - MAXROW;
			if(v > MAXROW){
				if(matrix[i][v] == 1){
					if(visited[i] == 0){
						visited[i] = 1;
						queue.offer(i);
					}
				}
			}
			i = v - 1;
			if(v % MAXROW != 1){
				if(matrix[i][v] == 1){
					if(visited[i] == 0){
						visited[i] = 1;
						queue.offer(i);
					}
				}
			}
			i = v + 1;
			if(v % MAXROW != 0){
				if(matrix[i][v] == 1){
					if(visited[i] == 0){
						visited[i] = 1;
						queue.offer(i);
					}
				}
			}
			i = v + MAXROW;
			if(v <= MAXROW * (MAXROW - 1)){
				if(matrix[i][v] == 1){
					if(visited[i] == 0){
						visited[i] = 1;
						queue.offer(i);
					}
				}
			}
		}
		for(i = 1; i <= MAXROW*MAXROW; i++){
			if(visited[i] == 0){
				System.out.println(i);
				expHandler.err(1);
			}
		}
	}
	private void toAdjacencyMatrix(){//将输入的地图转化为邻接矩阵
		//Requires:None
		//Modifies:matrix
		//Effects:make the map to Adjacency Matrix
		int i = 0, j = 0;
		for(i = 0; i <= MAXROW*MAXROW; i++)
			for(j = 0; j <= MAXROW*MAXROW; j++)
				if(i == j)
					matrix[i][j] = 0;
				else
					matrix[i][j] = -1;
		for(i = 1; i <= MAXROW; i++){
			for(j = 1; j <= MAXROW; j++){
				if(map[i][j] == 1){
					matrix[(i-1)*MAXROW+j][(i-1)*MAXROW+j+1] = 1;
					matrix[(i-1)*MAXROW+j+1][(i-1)*MAXROW+j] = 1;
				}
				else if(map[i][j] == 2){
					matrix[(i-1)*MAXROW+j][i*MAXROW+j] = 1;
					matrix[i*MAXROW+j][(i-1)*MAXROW+j] = 1;
				}
				else if(map[i][j] == 3){
					matrix[(i-1)*MAXROW+j][(i-1)*MAXROW+j+1] = 1;
					matrix[(i-1)*MAXROW+j+1][(i-1)*MAXROW+j] = 1;
					matrix[(i-1)*MAXROW+j][i*MAXROW+j] = 1;
					matrix[i*MAXROW+j][(i-1)*MAXROW+j] = 1;
				}
			}
		}
	}
	private void toVehicleFlowrate(){//将邻接矩阵中的信息输入到流量矩阵中
		//Requires:None
		//Modifies:vehicleFlowrate
		//Effects:use information of the Adjacency Matrix to create Vehicle Flow Rate Matrix
		for(int i = 1, j = i; i <= MAXROW*MAXROW; i++, j++){//[0]中存储的是横向边 [1]中存储的是纵向边 
			if(i % MAXROW == 0){//二者都是横着编号的 [0]中第一行是1-79 [1]中第一行是1-80
				j--;
				continue;
			}
			if(matrix[i][i+1] == 1)
				vehicleFlowrate[0][j] = 0;
			else
				vehicleFlowrate[0][j] = -1;
		}
		for(int i = 1; i <= MAXROW*(MAXROW-1); i++){//竖着也是一行一行来
			if(matrix[i][i+MAXROW] == 1)
				vehicleFlowrate[1][i] = 0;
			else
				vehicleFlowrate[1][i] = -1;
//			System.out.println("444");
		}
	}
	
	//更新操作
	public void addVehicleFlowrate(int x1, int y1, int x2, int y2){
		//Requires:x1,y1,x2,y2 is in the range of 1-80
		//Modifies:vehicleFlowrate
		//Effects:use information of these numbers to change the Vehicle Flow Rate Matrix
		if(x2 == x1 + 1 && y1 == y2){
			vehicleFlowrate[1][(x1-1)*MAXROW+y1]++;
		}
		else if(x2 == x1 - 1 && y1 == y2){
			vehicleFlowrate[1][(x2-1)*MAXROW+y2]++;
		}
		else if(y2 == y1 + 1 && x1 == x2){
			vehicleFlowrate[0][(x1-1)*(MAXROW-1)+y1]++;
		}
		else{
			vehicleFlowrate[1][(x2-1)*(MAXROW-1)+y2]++;
		}
	}
	public void decreaseVehicleFlowrate(int x1, int y1, int x2, int y2){
		//Requires:x1,y1,x2,y2 is in the range of 1-80
		//Modifies:vehicleFlowrate
		//Effects:use information of these numbers to change the Vehicle Flow Rate Matrix
		if(x2 == x1 + 1 && y1 == y2){
			vehicleFlowrate[1][(x1-1)*MAXROW+y1]--;
		}
		else if(x2 == x1 - 1 && y1 == y2){
			vehicleFlowrate[1][(x2-1)*MAXROW+y2]--;
		}
		else if(y2 == y1 + 1 && x1 == x2){
			vehicleFlowrate[0][(x1-1)*(MAXROW-1)+y1]--;
		}
		else{
			vehicleFlowrate[1][(x2-1)*(MAXROW-1)+y2]--;
		}
	}
	public void openRoad(int x1, int y1, int x2, int y2){
		//Requires:x1,y1.x2,y2 is in the range of 1-80
		//Modifies:matrix and vehicleFlowrate
		//Effects:open the road in Adjacency Matrix and Vehicle Flow Rate Matrix
		if(x1 <= 0 || x1 >= MAXROW || y1 <= 0 || y2 >= MAXROW || x2 <= 0 || x2 >= MAXROW || y2 <= 0 || y2 >= MAXROW){
			System.out.println("打开道路请求有误");
			return;
		}
		if(matrix[(x2-1)*MAXROW+y2][(x1-1)*MAXROW+y1] != -1){
			System.out.println("打开道路请求有误");
			return;
		}
		if(x1 == x2){
			if(y1 == y2 + 1){//(x2,y2)往右
				if(map[x2][y2] == 1 || map[x2][y2] == 3){
					vehicleFlowrate[0][(x2-1)*(MAXROW-1)+y2] = 0;
					matrix[(x2-1)*MAXROW+y2][(x1-1)*MAXROW+y1] = 1;
					matrix[(x1-1)*MAXROW+y1][(x2-1)*MAXROW+y2] = 1;
				}
				else{
					System.out.println("打开道路请求有误");
					return;
				}
			}
			else if(y2 == y1 + 1){//(x1,y1)往右
				if(map[x1][y1] == 1 || map[x1][y1] == 3){
					vehicleFlowrate[0][(x1-1)*(MAXROW-1)+y1] = 0;
					matrix[(x1-1)*MAXROW+y1][(x2-1)*MAXROW+y2] = 1;
					matrix[(x2-1)*MAXROW+y2][(x1-1)*MAXROW+y1] = 1;
//					System.out.println(vehicleFlowrate[0][(x1-1)*(MAXROW-1)+y1] + "," + x1 + "," + y1);
//					System.out.println(((x1-1)*MAXROW+y1) + "," + ((x2-1)*MAXROW+y2));
				}
				else{
					System.out.println("打开道路请求有误");
					return;
				}
			}
			else{
				System.out.println("打开道路请求有误");
				return;
			}
		}
		else if(y1 == y2){
			if(x1 == x2 + 1){//(x2,y2)往下
				if(map[x2][y2] == 2 || map[x2][y2] == 3){
					vehicleFlowrate[1][(x2-1)*MAXROW+y2] = 0;
					matrix[(x2-1)*MAXROW+y2][(x1-1)*MAXROW+y1] = 1;
					matrix[(x1-1)*MAXROW+y1][(x2-1)*MAXROW+y2] = 1;
				}
				else {
					System.out.println("打开道路请求有误");
					return;
				}
			}
			else if(x2 == x1 + 1){//(x1,y1)往下
				if(map[x1][y1] == 2 || map[x1][y1] == 3){
					vehicleFlowrate[1][(x1-1)*MAXROW+y1] = 0;
					matrix[(x2-1)*MAXROW+y2][(x1-1)*MAXROW+y1] = 1;
					matrix[(x1-1)*MAXROW+y1][(x2-1)*MAXROW+y2] = 1;
				}
				else {
					System.out.println("打开道路请求有误");
					return;
				}
			}
			else{
				System.out.println("打开道路请求有误");
				return;
			}
		}
		else{
			System.out.println("打开道路请求有误");
			return;
		}
	}
	public void closeRoad(int x1, int y1, int x2, int y2){
		//Requires:x1,y1.x2,y2 is in the range of 1-80
		//Modifies:matrix and vehicleFlowrate
		//Effects:close the road in Adjacency Matrix and Vehicle Flow Rate Matrix
		if(x1 <= 0 || x1 >= MAXROW || y1 <= 0 || y2 >= MAXROW || x2 <= 0 || x2 >= MAXROW || y2 <= 0 || y2 >= MAXROW){
			System.out.println("关闭道路请求有误");
			return;
		}
		if(matrix[(x1-1)*MAXROW+y1][(x2-1)*MAXROW+y2] != 1){
			System.out.println("关闭道路请求有误");
			return;
		}
		if(x1 == x2){
			if(y1 == y2 + 1){//(x2,y2)往右
				if(vehicleFlowrate[0][(x2-1)*(MAXROW-1)+y2] == 0){
					vehicleFlowrate[0][(x2-1)*(MAXROW-1)+y2] = -1;
					matrix[(x2-1)*MAXROW+y2][(x1-1)*MAXROW+y1] = -1;
					matrix[(x1-1)*MAXROW+y1][(x2-1)*MAXROW+y2] = -1;
				}
				else{
					System.out.println("关闭道路请求有误");
					return;
				}
			}
			else if(y2 == y1 + 1){//(x1,y1)往右
				if(vehicleFlowrate[0][(x1-1)*(MAXROW-1)+y1] == 0){
					vehicleFlowrate[0][(x1-1)*(MAXROW-1)+y1] = -1;
					matrix[(x2-1)*MAXROW+y2][(x1-1)*MAXROW+y1] = -1;
					matrix[(x1-1)*MAXROW+y1][(x2-1)*MAXROW+y2] = -1;
				}
				else{
					System.out.println("关闭道路请求有误");
					return;
				}
			}
			else{
				System.out.println("关闭道路请求有误");
				return;
			}
		}
		else if(y1 == y2){
			if(x1 == x2 + 1){//(x2,y2)往下
				if(vehicleFlowrate[1][(x2-1)*MAXROW+y2] == 0){
					vehicleFlowrate[1][(x2-1)*MAXROW+y2] = -1;
					matrix[(x2-1)*MAXROW+y2][(x1-1)*MAXROW+y1] = -1;
					matrix[(x1-1)*MAXROW+y1][(x2-1)*MAXROW+y2] = -1;
				}
				else{
					System.out.println("关闭道路请求有误");
					return;
				}
			}
			else if(x2 == x1 + 1){//(x1,y1)往下
				if(vehicleFlowrate[1][(x1-1)*MAXROW+y1] == 0){
					vehicleFlowrate[1][(x1-1)*MAXROW+y1] = -1;
					matrix[(x1-1)*MAXROW+y1][(x2-1)*MAXROW+y2] = -1;
					matrix[(x2-1)*MAXROW+y2][(x1-1)*MAXROW+y1] = -1;
				}
				else{
					System.out.println("关闭道路请求有误");
					return;
				}
			}
			else{
				System.out.println("关闭道路请求有误");
				return;
			}
		}
		else{
			System.out.println("关闭道路请求有误");
			return;
		}
	}
	
	//观察操作
	public int getDirection(int x1, int y1, int x2, int y2){
		//Requires:x1,y1,x2,y2 is in the range of 1-80
		//Modifies:None
		//Effects:get best direction on the basis of these numbers and these Matrixs
		int p1, p2, bestDirection = -1, minDistance = 0, minFlowrate = 0;
		p1 = (x1 - 1) * MAXROW + y1;
		p2 = (x2 - 1) * MAXROW + y2;
		int[] flowrate = new int[4];
		int[] distance = new int[4];
		int i;
		for(i = 0; i <= 3; i++){
			flowrate[i] = -1;
			distance[i] = -1;
		}
		if(matrix[p1][p1-1] == 1)//左
			flowrate[0] = vehicleFlowrate[0][(x1-1)*(MAXROW-1)+y1-1];
		if(p1 < MAXROW*MAXROW){
			if(matrix[p1][p1+1] == 1){//右
				flowrate[1] = vehicleFlowrate[0][(x1-1)*(MAXROW-1)+y1];
//				System.out.println(vehicleFlowrate[0][(x1-1)*(MAXROW-1)+y1] + "," + x1 + "," + y1);
			}
		}
		if(p1 > MAXROW){
			if(matrix[p1][p1-MAXROW] == 1){//上
				flowrate[2] = vehicleFlowrate[1][(x1-2)*MAXROW+y1];
			}
		}
		if(p1 <= MAXROW*(MAXROW-1)){
			if(matrix[p1][p1+MAXROW] == 1){//下
				flowrate[3] = vehicleFlowrate[1][(x1-1)*MAXROW+y1];
			}
		}
		if(flowrate[0] != -1)
			distance[0] = bfs(p1-1, p2);
		if(flowrate[1] != -1)
			distance[1] = bfs(p1+1, p2);
		if(flowrate[2] != -1)
			distance[2] = bfs(p1-MAXROW, p2);
		if(flowrate[3] != -1)
			distance[3] = bfs(p1+MAXROW, p2);
		for(i = 0; i <= 3; i++){
			if(flowrate[i] == -1)
				continue;
			if(bestDirection == -1){
				bestDirection = i;
				minDistance = distance[i];
				minFlowrate = flowrate[i];
			}
			else if(distance[i] < minDistance){
				bestDirection = i;
				minDistance = distance[i];
				minFlowrate = flowrate[i];
			}
			else if(distance[i] == minDistance){
				if(flowrate[i] < minFlowrate){
					bestDirection = i;
					minDistance = distance[i];
					minFlowrate = flowrate[i];
				}
			}
		}
//		System.out.println(flowrate[0] + "," + flowrate[1] + "," + flowrate[2] + "," + flowrate[3] + ",,," + minFlowrate);
//		System.out.println(distance[0] + "," + distance[1] + "," + distance[2] + "," + distance[3] + "..." + bestDirection);
		return bestDirection + 1;
	}
	private int bfs(int start, int end){
		//Requires:start,end is in the range of 1-6400
		//Modifies:None
		//Effects:get the shortest distance according to BFS
		if(start == end)
			return 0;
		Queue<Integer> queue = new LinkedList<Integer>();
		short[] visited = new short[MAXROW*MAXROW + 1];
		int[] distance = new int[MAXROW*MAXROW + 1];
		int i;
		for(i = 0; i <= MAXROW*MAXROW; i++)
			visited[i] = 0;
		visited[start] = 1;
		queue.offer(start);
		distance[start]= 0;
		while(!queue.isEmpty()){
			int v = queue.remove();
			i = v - MAXROW;
			if(v > MAXROW){
				if(matrix[i][v] == 1){
					if(visited[i] == 0){
						visited[i] = 1;
						queue.offer(i);
						distance[i] = distance[v] + 1;
						if(i == end)
							return distance[i];
					}
				}
			}
			i = v - 1;
			if(v % MAXROW != 1){
				if(matrix[i][v] == 1){
					if(visited[i] == 0){
						visited[i] = 1;
						queue.offer(i);
						distance[i] = distance[v] + 1;
						if(i == end)
							return distance[i];
					}
				}
			}
			i = v + 1;
			if(v % MAXROW != 0){
				if(matrix[i][v] == 1){
					if(visited[i] == 0){
						visited[i] = 1;
						queue.offer(i);
						distance[i] = distance[v] + 1;
						if(i == end)
							return distance[i];
					}
				}
			}
			i = v + MAXROW;
			if(v <= MAXROW * (MAXROW - 1)){
				if(matrix[i][v] == 1){
					if(visited[i] == 0){
						visited[i] = 1;
						queue.offer(i);
						distance[i] = distance[v] + 1;
						if(i == end)
							return distance[i];
					}
				}
			}
		}
		return 0;
	}
	public int getDistance(int x1, int y1, int x2, int y2){
		//Requires:x1,y1.x2,y2 is in the range of 1-80
		//Modifies:None
		//Effects:get the shortest distance according to bfs
		return bfs((x1-1)*MAXROW+y1, (x2-1)*MAXROW+y2);
	}
	public int getRandomDirection(int x, int y){
		//Requires:x,y is in the range of 1-80
		//Modifies:None
		//Effects:get random direction according to algorithm
		int[] availableDirection = new int[4];
		int i = 0, minFlowrate = Integer.MAX_VALUE;
		int num = (x - 1) * MAXROW + y;
		if(num > MAXROW){
			if(matrix[num][num-MAXROW] == 1){
				if(vehicleFlowrate[1][(x-2)*MAXROW+y] < minFlowrate){
					i = 0;
					availableDirection[i++] = 3;
					minFlowrate = vehicleFlowrate[1][(x-2)*MAXROW+y];
				}
				else if(vehicleFlowrate[1][(x-2)*MAXROW+y] == minFlowrate){
					availableDirection[i++] = 3;
				}
			}
		}
		if(num > 1){
			if(matrix[num][num-1] == 1){
				if(vehicleFlowrate[0][(x-1)*(MAXROW-1)+y-1] < minFlowrate){
					i = 0;
					availableDirection[i++] = 1;
					minFlowrate = vehicleFlowrate[0][(x-1)*(MAXROW-1)+y-1];
				}
				else if(vehicleFlowrate[0][(x-1)*(MAXROW-1)+y-1] == minFlowrate){
					availableDirection[i++] = 1;
				}
			}
		}
		if(num < MAXROW*MAXROW){
			if(matrix[num][num+1] == 1){
				if(vehicleFlowrate[0][(x-1)*(MAXROW-1)+y] < minFlowrate){
					i = 0;
					availableDirection[i++] = 2;
					minFlowrate = vehicleFlowrate[0][(x-1)*(MAXROW-1)+y];
				}
				else if(vehicleFlowrate[0][(x-1)*(MAXROW-1)+y] == minFlowrate){
					availableDirection[i++] = 2;
				}
			}
		}
		if(num <= (MAXROW-1)*MAXROW){
			if(matrix[num][num+MAXROW] == 1){
				if(vehicleFlowrate[1][num] < minFlowrate){
					i = 0;
					availableDirection[i++] = 4;
					minFlowrate = vehicleFlowrate[1][num];
				}
				else if(vehicleFlowrate[1][num] == minFlowrate){
					availableDirection[i++] = 4;
				}
			}
		}
		Random ra = new Random();
		try {
			return availableDirection[ra.nextInt(i)];
		} catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		return 0;
	}
	public boolean canRun(int lastDirection, int currentDirection, int x, int y){
		//Requires:0<=lastDirection<=4 1<=currentDirection<=4 1<=x<=80 1<=y<=80
		//Modifies:None
		//Effects:check if can run according to lastDirection, currentDirection, x, y, across and trafficLight.
		if(lastDirection == 0){//起步时
//			System.out.println("起步");
			return true;
		}
		if(across[(x-1)*MAXROW+y] == 0){//没有设置红绿灯
//			System.out.println("没有设置红绿灯");
			return true;
		}
		int horizontal = trafficLight[across[(x-1)*MAXROW+y]].getHorizontal();
		int vertical = trafficLight[across[(x-1)*MAXROW+y]].getVertical();
//		System.out.println("由红绿灯决定是否通行");
		if(lastDirection == 1){
			if((currentDirection == 1 || currentDirection == 2 || currentDirection == 4) && horizontal == 1)
				return true;
			if(currentDirection == 3)
				return true;
		}
		else if(lastDirection == 2){
			if((currentDirection == 1 || currentDirection == 2 || currentDirection == 3) && horizontal == 1)
				return true;
			if(currentDirection == 4)
				return true;
		}
		else if(lastDirection == 3){
			if((currentDirection == 1 || currentDirection == 3 || currentDirection == 4) && vertical == 1)
				return true;
			if(currentDirection == 2)
				return true;
		}
		else{
			if((currentDirection == 2 || currentDirection == 3 || currentDirection == 4) && vertical == 1)
				return true;
			if(currentDirection == 1)
				return true;
		}
		return false;
	}
}
