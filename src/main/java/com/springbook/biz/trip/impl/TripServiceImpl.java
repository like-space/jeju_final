package com.springbook.biz.trip.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbook.biz.trip.CommentVO;
import com.springbook.biz.trip.ReviewVO;
import com.springbook.biz.trip.ScheduleVO;
import com.springbook.biz.trip.TripMemberVO;
import com.springbook.biz.trip.TripService;
import com.springbook.biz.trip.TripVO;

@Service("tripService")
public class TripServiceImpl implements TripService {

	@Autowired
	private TripDAOMybatis tripDAO;

	@Override
	public void insertTrip(TripVO vo) {
		tripDAO.insertTrip(vo);
	}

	@Override
	public void deleteTrip(TripVO vo) {
		tripDAO.deleteTrip(vo);
	}

	// ���� �� ��ȸ
	@Override
	public TripVO getTrip(TripVO vo) {
		// TODO Auto-generated method stub
		return tripDAO.getTrip(vo);
	}

	@Override
	public List<TripVO> getTripList(TripVO vo) {
		// TODO Auto-generated method stub
		return tripDAO.getTripList(vo);
	}

	@Override
	public int getTripSeq() {
		// TODO Auto-generated method stub
		return tripDAO.getTripSeq();
	}

	@Override
	public void deleteFileList(int seq) {
		tripDAO.deleteFileList(seq);

	}

	@Override
	public void insertTripMembers(TripMemberVO vo) {
		tripDAO.insertTripMembers(vo);

	}

	// ����� ���� �ο��� ��ȸ
	@Override
	public int countMember(TripMemberVO vo) {
		return tripDAO.countMember(vo);
	}

	// ����� �ο� �ߺ� �˻�
	@Override
	public int checkContain(TripMemberVO vo) {
		return tripDAO.checkContain(vo);
	}

	// ���� ���� �ɹ� ��� ��ȸ
	@Override
	public List<TripMemberVO> getTripMemberList(TripVO vo) {
		return tripDAO.getTripMemberList(vo);

	}

	// ����ɹ� ���(host����)
	@Override
	public void insertTripMemberH(TripMemberVO vo) {
		tripDAO.insertTripMemberH(vo);

	}

	// ���� �ɹ� ���� ��ȸ
	@Override
	public TripMemberVO getTripMember(TripMemberVO vo) {
		return tripDAO.getTripMember(vo);

	}

	// ���� ���� ���
	@Override
	public void exitTrip(TripMemberVO vo) {
		tripDAO.exitTrip(vo);

	}

	// ���� ���� ����
	@Override
	public void changeTripStatus(TripVO vo) {
		tripDAO.changeTripStatus(vo);

	}

	// ���� ���� ���
	@Override
	public void insertReview(ReviewVO vo) {
		tripDAO.insertReview(vo);

	}

	// ���� �ߺ� �˻�
	@Override
	public int checkReview(ReviewVO vo) {
		return tripDAO.checkReview(vo);

	}

	// �Ű� ����� ACCOUNT_STATUS + 1
	@Override
	public void updateMemberStatus(ReviewVO vo) {
		tripDAO.updateMemberStatus(vo);

	}

	// ���� ���� ����Ʈ �ҷ����� (12.12 ����ȣ �߰�)
	@Override
	public List<ReviewVO> getmyReviewList(ReviewVO vo) {
		return tripDAO.getmyReviewList(vo);
	}
	// ################################ ���� ���� ##################################

	@Override
	public List<CommentVO> readComment(int seq) {
		return tripDAO.readComment(seq);
	}

	@Override
	public void insertComment(CommentVO vo) {
		tripDAO.insertComment(vo);
	}

	@Override
	public List<ScheduleVO> getscheduleList(ScheduleVO vo) {
		// TODO Auto-generated method stub
		return tripDAO.getscheduleList(vo);
	}

	@Override
	public void reinsertschedule(TripVO vo) {
		tripDAO.reinsertschedule(vo);
	}

	@Override
	public List<Map<String, String>> getClearedscheduleList(ScheduleVO vo) {
		// TODO Auto-generated method stub
		return tripDAO.getClearedscheduleList(vo);
	}

	@Override
	public Map<String, String> addGameModeSchedule(ScheduleVO vo) {
		// TODO Auto-generated method stub
		return tripDAO.addGameModeSchedule(vo);
	}

	public void updateClearTrschedule(ScheduleVO vo) {
		tripDAO.updateClearTrschedule(vo);
	}

	public void updateTrStatus(ScheduleVO vo) {
		tripDAO.updateTrStatus(vo);
	}

	// MyTripList

	@Override
	public List<TripVO> getMyTripList(int mSeq) {
		return tripDAO.getMyTripList(mSeq);
	}

	@Override
	public List<TripVO> getMyTripMember(TripVO vo) {
		return tripDAO.getMyTripMember(vo);
	}

	@Override
	public List<TripVO> getMyTripPlace(TripVO vo) {
		return tripDAO.getMyTripPlace(vo);
	}
}
