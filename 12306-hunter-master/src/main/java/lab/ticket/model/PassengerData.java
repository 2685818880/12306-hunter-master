/*
 * 12306-hunter: Java Swing C/S鐗堟湰12306璁㈢エ鍔╂墜
 * 鏈▼搴忓畬鍏ㄥ紑鏀炬簮浠ｇ爜锛屼粎浣滀负鎶�湳瀛︿範浜ゆ祦涔嬬敤锛屼笉寰楃敤浜庝换浣曞晢涓氱敤閫� * 涓汉鍙嚜鐢卞厤璐逛娇鐢ㄦ垨浜屾寮�彂锛岃嚜琛屾壙鎷呬换浣曠浉鍏宠矗浠� */
package lab.ticket.model;

import java.io.Serializable;

import lab.ticket.model.UserData.SeatType;

/**
 * 涔樺鏁版嵁瀵硅薄
 */
public class PassengerData implements Serializable {

	private static final long serialVersionUID = -4047393628292653857L;

	private boolean selected = true;
	private String cardNo;
	private CardType cardType = CardType.T1;
	private String name;
	private String mobile;
	private TicketType ticketType = TicketType.T1;

	public enum TicketType {
		T1("1", "1"),
		T2("2", "2"),
		T3("3", "3"),
		T4("4", "4");

		private String label;
		private String value;

		private TicketType(String label, String value) {
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

	public enum CardType {
		T1("11", "1"),
		T2("22", "2"),
		T3("33", "C"),
		T4("44", "G"),
		T5("55", "B");

		private String label;
		private String value;

		private CardType(String label, String value) {
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public TicketType getTicketType() {
		return ticketType;
	}

	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getShortText() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(",").append(cardType.getValue()).append(",").append(cardNo);
		return builder.toString();
	}

	public String getLongText(SeatType seatType) {
		StringBuilder builder = new StringBuilder();
		builder.append(seatType.getValue()).append(",").append("0,").append(ticketType.getValue()).append(",")
				.append(getShortText()).append(",").append(mobile).append(",").append("Y");
		return builder.toString();
	}
}
