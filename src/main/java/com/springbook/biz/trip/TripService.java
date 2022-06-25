package com.springbook.biz.trip;

import java.util.List;
import java.util.Map;

public interface TripService {
	// CRUD ����� �޼ҵ� ����
	// ����
	void insertTrip(TripVO vo);

	// �� ����
//	void updateTrip(TripVO vo);

	// �� ����
	void deleteTrip(TripVO vo);

	// ���� �� ��ȸ
	TripVO getTrip(TripVO vo);

	// �� ��� ��ȸ
	List<TripVO> getTripList(TripVO vo);

	// �� ��� �� ��� �� �Ϸù�ȣ ȹ��
	int getTripSeq();

	// �Խñ� ������ �ش� �Խñ��� ÷������ ��� ����
	void deleteFileList(int seq);

	// ����ɹ� ���
	void insertTripMembers(TripMemberVO vo);

	// ����� ���� �ο��� ��ȸ
	int countMember(TripMemberVO vo);

	// ����� �ο� �ߺ� �˻�
	int checkContain(TripMemberVO vo);

	// ���� �ɹ� ��� ��ȸ
	List<TripMemberVO> getTripMemberList(TripVO vo);

	// ����ɹ� ���(host����)
	void insertTripMemberH(TripMemberVO vo);

	// ���� �ɹ� ���� ��ȸ
	TripMemberVO getTripMember(TripMemberVO vo);

	// ���� ���� ���
	void exitTrip(TripMemberVO vo);

	// ���� ���� ����
	void changeTripStatus(TripVO vo);

	// ���� ���� ���
	void insertReview(ReviewVO vo);

	// ���� �ߺ��˻�
	int checkReview(ReviewVO vo);

	// �Ű� ����� ACCOUNT_STATUS + 1
	void updateMemberStatus(ReviewVO vo);

	// ���� ���� ����Ʈ �ҷ����� (12.12 ����ȣ �߰�)
	List<ReviewVO> getmyReviewList(ReviewVO vo);

	// ######################## ���ִ� ���� ####################################

	// ��� �ҷ�����
	List<CommentVO> readComment(int seq);

	// ��� �ۼ��ϱ�
	void insertComment(CommentVO vo);

	// ������ ���� ��������
	List<ScheduleVO> getscheduleList(ScheduleVO vo);

	// �ٽ� ��õ�ϱ� ������ ���� delete insert
	void reinsertschedule(TripVO vo);

	// Ŭ����� ������ ����Ʈ
	List<Map<String, String>> getClearedscheduleList(ScheduleVO vo);

	// Ŭ���� �ȵȰ� �߿� ���� ������ ������ ��������
	Map<String, String> addGameModeSchedule(ScheduleVO vo);

	void updateClearTrschedule(ScheduleVO vo);

	void updateTrStatus(ScheduleVO vo);

	// Mytriplist

	List<TripVO> getMyTripList(int mSeq);

	List<TripVO> getMyTripMember(TripVO vo);

	List<TripVO> getMyTripPlace(TripVO vo);

}
