/*
 * 12306-hunter: Java Swing C/S�汾12306��Ʊ����
 * ��������ȫ����Դ���룬����Ϊ����ѧϰ����֮�ã����������κ���ҵ��;
 */
package lab.ticket.service;

import java.util.List;

import javax.swing.JOptionPane;

import lab.ticket.TicketMainFrame;
import lab.ticket.model.SingleTrainOrderVO;
import lab.ticket.model.TicketData;
import lab.ticket.model.TrainData;
import lab.ticket.model.TrainQueryInfo;
import lab.ticket.model.UserData;
import lab.ticket.view.RandomCodeDialog;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ÿ����¼�û�һ������ˢƱ�߳�
 */
public class TicketUserThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(TicketUserThread.class);

	//����������Ŀǰ��֪����0.1��϶�ʧ��
	private static final float SLEEP_SECONDS = 0.5f;

	private HttpClientService httpClientService;

	private TicketMainFrame ticketMainFrame;

	private TicketData ticketData;

	private UserData userData;

	private boolean success = false;

	private boolean terminalSign = false;

	private String submitCode;

	private int count = 0;

	public TicketUserThread(TicketMainFrame ticketMainFrame, HttpClientService httpClientService,
			TicketData ticketData, UserData userData) {
		this.ticketMainFrame = ticketMainFrame;
		this.httpClientService = httpClientService;
		this.ticketData = ticketData;
		this.userData = userData;
	}

	@Override
	public void run() {
		String title = "[�̣߳� " + this.getId() + ", ��¼�û���" + userData.getLoginUser() + "] ";
		TicketMainFrame.appendMessage(title + "ˢƱ����˳���б�");
		List<String> trainDates = ticketData.getTrainDateList();
		for (String trainDate : trainDates) {
			TicketMainFrame.appendMessage(title + " - " + trainDate);
		}
		while (!success) {
			try {
				if (terminalSign) {
					TicketMainFrame.appendMessage(title + "��ֹ�˳�");
					return;
				}

				TicketMainFrame.appendMessage(title + "�� " + (++count) + " ��ˢƱ");

				SingleTrainOrderVO singleTrainOrderVO = null;

				for (String trainDate : trainDates) {
					TicketMainFrame.appendMessage(title + "��ѯ��" + trainDate + "����Ʊ��Ϣ��Ӧ:");
					List<TrainQueryInfo> trainQueryInfos = httpClientService
							.queryTrain(ticketData, userData, trainDate);
					for (TrainQueryInfo trainQueryInfo : trainQueryInfos) {
						if (!trainQueryInfo.isValidForPurchase()) {
							TicketMainFrame.appendMessage(title + " - :( ��ǰ���ɹ������Ʊ���Σ�" + trainDate + trainQueryInfo);
						} else {

							TicketMainFrame.appendMessage(title + " - :) �ɹ��򳵴Σ�" + trainDate + trainQueryInfo);
						}
					}

					// �Ӳ�ѯ���������ݰ������ȼ��ҵ���ƥ��ĳ��κ���λ����
					// --��û�и�ϯ��*��δ����ʼʱ�䣻�У��в����������㣻���֣��е��������ޣ��ޣ�������
					List<TrainData> trainDatas = userData.getTrainDatas();
					for (TrainData trainData : trainDatas) {
						for (TrainQueryInfo trainQueryInfo : trainQueryInfos) {
							if (!trainQueryInfo.isValidForPurchase()) {
								continue;
							}
							if (trainQueryInfo.getTrainNo().equals(trainData.getTrainNo())) {
								String seatTypeValue = trainQueryInfo.getSeatDatas().get(trainData.getSeatType());
								if (seatTypeValue.equals("��")) {
									continue;
								}
								TicketMainFrame.appendMessage(title + ":) Ԥ��������Ʊ��" + trainData.getTrainNo() + ",ϯ��"
										+ trainData.getSeatType() + ",���ڣ�" + trainDate + ",��Ʊ��" + seatTypeValue);

								singleTrainOrderVO = new SingleTrainOrderVO();
								singleTrainOrderVO.setTrainQueryInfo(trainQueryInfo);
								singleTrainOrderVO.setLoginUser(userData.getLoginUser());
								singleTrainOrderVO.setCookieData(userData.getCookieData());
								singleTrainOrderVO.setSeatType(trainData.getSeatType());
								singleTrainOrderVO.setTrainDate(trainDate);
								singleTrainOrderVO.setTrainNo(trainData.getTrainNo());
								break;
							}
						}
						if (singleTrainOrderVO != null) {
							break;
						}
						TicketMainFrame.appendMessage(title + ":( Ԥ��������Ʊ��" + trainData.getTrainNo() + ",ϯ��"
								+ trainData.getSeatType() + ",���ڣ�" + trainDate);
					}

					if (singleTrainOrderVO != null) {
						break;
					}
				}

				if (singleTrainOrderVO == null) {
					TicketMainFrame.appendMessage(title + "���ƣ�����ָ�����ڳ���Ŀǰ���޷���Ʊ����Ʊ����ͣ " + SLEEP_SECONDS + " ���������ˢƱ...");
					Thread.sleep(Float.valueOf(SLEEP_SECONDS * 1000).intValue());
				} else {

					TicketMainFrame.appendMessage(title + "��ͣ " + SLEEP_SECONDS + " ������...");
					Thread.sleep(Float.valueOf(SLEEP_SECONDS * 1000).intValue());
					httpClientService.submitOrderRequest(singleTrainOrderVO);

					String responseBody = null;
					do {
						synchronized (this) {
							new RandomCodeDialog(singleTrainOrderVO, this);
							this.wait();
						}
						if (StringUtils.isBlank(this.submitCode)) {
							TicketMainFrame.appendMessage(title + "��ȡ�������µ���֤������");
							break;
						}
						responseBody = httpClientService.confirmSingleForQueueOrder(ticketData, singleTrainOrderVO,
								this.submitCode, true);
						if (responseBody.contains("��֤��")) {
							TicketMainFrame.appendMessage(title + "�µ���֤���������������");
						}
					} while (responseBody.contains("��֤��"));

					//���errMsg��ΪY���ʾ���ʴ��󣬼�����һ��ѭ��
					if (responseBody == null || responseBody.indexOf("\"errMsg\":\"Y\"") == -1) {
						continue;
					}

					//�µ�ȷ��ǰ����Ʊ�����������
					TicketMainFrame.appendMessage(title + "��ͣ " + SLEEP_SECONDS + " ������...");
					Thread.sleep(Float.valueOf(SLEEP_SECONDS * 1000).intValue());
					responseBody = httpClientService.getQueueCount(ticketData, singleTrainOrderVO);
					//�˴��ɿ�����Ӽ���ж��߼���ֻ�����·�������Ʊ�ż��������ύ����

					// �ύ����
					TicketMainFrame.appendMessage(title + "��ͣ " + SLEEP_SECONDS + " ������...");
					Thread.sleep(Float.valueOf(SLEEP_SECONDS * 1000).intValue());
					responseBody = httpClientService.confirmSingleForQueueOrder(ticketData, singleTrainOrderVO,
							this.submitCode, false);

					if (responseBody.indexOf("\"errMsg\":\"Y\"") > -1) {
						String msg = title + "ò���ѳɹ��µ����Ͽ����������¼���˺ŷ��ʡ�δ��ɶ������в鿴ȷ�ϼ����к����������";
						JOptionPane.showMessageDialog(ticketMainFrame, msg);
						TicketMainFrame.appendMessage(msg);
						TicketMainFrame.appendMessage(title + "����ֹ��ǰ�û�ˢƱ�̣߳������û�ˢƱ�̼߳�������");
						success = true;
						break;
					}
				}

			} catch (InterruptedException e) {
				TicketMainFrame.appendMessage(title + "�쳣��" + e.getMessage());
				logger.error("Error at thread " + this.getId() + " for user " + userData.getLoginUser(), e);
			}
		}
	}

	public void sendTerminalSign() {
		String title = "[�̣߳� " + this.getId() + ", ��¼�û���" + userData.getLoginUser() + "] ";
		TicketMainFrame.appendMessage(title + "�յ���ֹ�ź�");
		terminalSign = true;
	}

	public void setSubmitCode(String submitCode) {
		this.submitCode = submitCode;
	}

	public int getCount() {
		return count;
	}
}
