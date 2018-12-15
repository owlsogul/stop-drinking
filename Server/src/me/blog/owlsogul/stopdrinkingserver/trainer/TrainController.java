package me.blog.owlsogul.stopdrinkingserver.trainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class TrainController implements Runnable{

	public static final String TensorFlowPath = "C:\\Users\\start\\Anaconda3\\python.exe";
	
	private TrainController() {init();}
	private static TrainController instance;
	public static TrainController getInstance() {
		if (instance == null) instance = new TrainController();
		return instance;
	}
	
	private Thread myThread;
	private LinkedList<String> currentTrainMembers; // 동시에 여러번 train하는 것을 막기 위해
	private volatile boolean doTrain = true;
	private void init() {
		currentTrainMembers = new LinkedList<>();
		myThread = new Thread(this);
		myThread.start();
	}
	
	public boolean trainModel(String member) {
		if (currentTrainMembers.contains(member)) {
			System.out.printf("%s는 이미 학습 대기열에 추가되어있습니다.\n", member);
			return false;
		}
		else {
			currentTrainMembers.add(member);
			System.out.printf("%s가 학습 대기열에 추가되었습니다.\n", member);
			return true;
		}
	}
	
	public double useModel(String member, int tension, int drinkingYesterday, int sleepHour, int drinkness) {
		if (currentTrainMembers.contains(member)) {
			System.out.printf("%s는 현재 학습 대기 중 입니다.\n", member);
		} else {
			try {
				Process process = new ProcessBuilder(TensorFlowPath, "model_use.py", member, String.valueOf(tension), String.valueOf(drinkingYesterday), String.valueOf(sleepHour), String.valueOf(drinkness)).start();
				BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String str = null;
				System.out.printf("%s 모델 사용을 시작합니다.\n", member);
				while ((str = stdOut.readLine())!= null) {
					System.out.println(str);
					if (str.startsWith("result:")) {
						str = str.replace("result:", "").replace("[array([[", "").replace("]])]", "");
						double val = Double.parseDouble(str);
						return val;
					}
					if (str.equals("jobs-done")) {
						System.out.printf("%s의 예측 완료되었습니다 . 50%%\n", member);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Double.MIN_VALUE;
	}

	@Override
	public void run() {
		while(doTrain) {
			
			try {
				Iterator<String> memberIter = currentTrainMembers.iterator();
				for (;memberIter.hasNext();) {
					String memberId = memberIter.next();
					try {
						System.out.printf("%s의 학습이 시작됩니다.\n", memberId);
						Process process = new ProcessBuilder(TensorFlowPath, "train.py", memberId).start();
						BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
						String str = null;
						while ((str = stdOut.readLine())!= null) {
							System.out.println(str);
							if (str.contains("train-done")) {
								System.out.printf("%s의 학습이 완료되었습니다 . 50%%\n", memberId);
							}
							if (str.contains("save-done")) {
								System.out.printf("%s의 학습 결과 저장이 완료되었습니다 . 100%%\n", memberId);
							}
						}
						memberIter.remove();
						System.out.printf("%s의 학습이 완료되어 학습 대기열에서 제거됩니다. (현재 대기:%d)\n", memberId, currentTrainMembers.size());
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}
