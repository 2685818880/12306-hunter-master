/*
 * 12306-hunter: Java Swing C/S�汾12306��Ʊ����
 * ��������ȫ����Դ���룬����Ϊ����ѧϰ����֮�ã����������κ���ҵ��;
 */
package lab.ticket.model;

import java.util.HashMap;
import java.util.Map;

import lab.ticket.model.UserData.SeatType;
import lab.ticket.util.TicketUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * �г���Ϣʵ����
 */
public class TrainQueryInfo {

	private String trainCode;// ���
	private String trainNo; // ����
	private String fromStation;// ��վ
	private String fromStationName; // ��վ���Ļ�վ
	private String fromStationCode; // ��վcode
	private String startTime;// ��ʱ
	private String toStation;// ��վ
	private String toStationName;// ��վ���Ļ�վ
	private String toStationCode;// ��վcode
	private String endTime; // ��ʱ
	private String locationCode; // λ��code
	private String takeTime;// ��ʱ
	private String formStationNo; // ��վ���
	private String toStationNo; // ��վ���

	// --��û�и�ϯ��*��δ����ʼʱ�䣻�У��в����������㣻���֣��е��������ޣ��ޣ�������
	private Map<SeatType, String> seatDatas = new HashMap<SeatType, String>();

	private String mmStr;// mmString
	private String trainno4;// trainno4
	private String ypInfoDetail;// ypInfoDetail
	private String single_round_type = "1"; // single_round_type

	private boolean validForPurchase;

	public String getTrainCode() {
		return trainCode;
	}

	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getFromStation() {
		return fromStation;
	}

	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}

	public String getFromStationCode() {
		return fromStationCode;
	}

	public void setFromStationCode(String fromStationCode) {
		this.fromStationCode = fromStationCode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getToStation() {
		return toStation;
	}

	public void setToStation(String toStation) {
		this.toStation = toStation;
	}

	public String getToStationCode() {
		return toStationCode;
	}

	public void setToStationCode(String toStationCode) {
		this.toStationCode = toStationCode;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getTakeTime() {
		return takeTime;
	}

	public void setTakeTime(String takeTime) {
		this.takeTime = takeTime;
	}

	public String getFormStationNo() {
		return formStationNo;
	}

	public void setFormStationNo(String formStationNo) {
		this.formStationNo = formStationNo;
	}

	public String getToStationNo() {
		return toStationNo;
	}

	public void setToStationNo(String toStationNo) {
		this.toStationNo = toStationNo;
	}

	public String getMmStr() {
		return mmStr;
	}

	public void setMmStr(String mmStr) {
		this.mmStr = mmStr;
	}

	public String getTrainno4() {
		return trainno4;
	}

	public void setTrainno4(String trainno4) {
		this.trainno4 = trainno4;
	}

	public String getYpInfoDetail() {
		return ypInfoDetail;
	}

	public void setYpInfoDetail(String ypInfoDetail) {
		this.ypInfoDetail = ypInfoDetail;
	}

	public String getSingle_round_type() {
		return single_round_type;
	}

	public void setSingle_round_type(String single_round_type) {
		this.single_round_type = single_round_type;
	}

	public String getFromStationName() {
		return fromStationName;
	}

	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}

	public String getToStationName() {
		return toStationName;
	}

	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append("����=");
		builder.append(trainNo);
		builder.append(", ����ʱ��=");
		builder.append(startTime);
		builder.append(", ��վʱ��=");
		builder.append(endTime);
		builder.append(", �˳�ʱ��=");
		builder.append(takeTime);
		builder.append(", ��Ʊ��Ϣ=");
		Map<SeatType, String> displaysSeatDatas = new HashMap<UserData.SeatType, String>();
		for (Map.Entry<SeatType, String> me : seatDatas.entrySet()) {
			if (!me.getValue().equals("--")) {
				displaysSeatDatas.put(me.getKey(), me.getValue());
			}
		}
		builder.append(displaysSeatDatas.toString());
		builder.append("]");
		return builder.toString();
	}

	public Map<SeatType, String> getSeatDatas() {
		return seatDatas;
	}

	public void setSeatDatas(Map<SeatType, String> seatDatas) {
		this.seatDatas = seatDatas;
	}

	public boolean isValidForPurchase() {
		return validForPurchase;
	}

	public void setValidForPurchase(boolean validForPurchase) {
		this.validForPurchase = validForPurchase;
	}

	/**
	 * �������ĳ�����λ�����Ƿ���Ч
	 * @param trainData
	 * @return
	 */
	public boolean validateTrainData(TrainData trainData) {
		if (!trainData.getTrainNo().equalsIgnoreCase(this.trainNo)) {
			return false;
		}
		String st = seatDatas.get(trainData.getSeatType());
		if (StringUtils.isBlank(st) || st.equals(TicketUtil.INVALID_SEAT_TYPE)) {
			return false;
		}
		return true;
	}

}
