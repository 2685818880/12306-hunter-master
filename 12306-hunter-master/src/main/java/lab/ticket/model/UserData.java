/*
 * 12306-hunter: Java Swing C/S�汾12306��Ʊ����
 * ��������ȫ����Դ���룬����Ϊ����ѧϰ����֮�ã����������κ���ҵ��;
 */
package lab.ticket.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * ��¼�˺�����ģ��
 */
public class UserData implements Serializable {

	private static final long serialVersionUID = -8827235378622351629L;

	private String loginUser;

	private boolean loginSuccess = false;

	//��¼�û���Cookie���ݶ���
	private Map<String, String> cookieData;

	private List<TrainData> trainDatas = new ArrayList<TrainData>();


	public enum SeatType {
		BUSS_SEAT("������", "9"),
		BEST_SEAT("�ص���", "P"),
		ONE_SEAT("һ����", "M"),
		TWO_SEAT("������", "O"),
		VAG_SLEEPER("�߼�����", "6"),
		SOFT_SLEEPER("����", "4"),
		HARD_SLEEPER("Ӳ��", "3"),
		SOFT_SEAT("����", "2"),
		HARD_SEAT("Ӳ��", "1"),
		NONE_SEAT("����", "-1"),
		OTH_SEAT("����", "0");

		private String label;
		private String value;

		private SeatType(String label, String value) {
			this.label = label;
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

		public String toString() {
			return label;
		}
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public Map<String, String> getCookieData() {
		return cookieData;
	}

	public void setCookieData(Map<String, String> cookieData) {
		this.cookieData = cookieData;
	}

	public List<TrainData> getTrainDatas() {
		return trainDatas;
	}

	public void setTrainDatas(List<TrainData> trainDatas) {
		this.trainDatas = trainDatas;
	}

	public boolean isLoginSuccess() {
		return loginSuccess;
	}

	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}
}
