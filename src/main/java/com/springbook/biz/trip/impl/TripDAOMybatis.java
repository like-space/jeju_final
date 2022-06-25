package com.springbook.biz.trip.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springbook.biz.member.MemberVO;
import com.springbook.biz.trip.CommentVO;
import com.springbook.biz.trip.ReviewVO;
import com.springbook.biz.trip.ScheduleVO;

import com.springbook.biz.trip.TripMemberVO;

import com.springbook.biz.trip.TripVO;

@Repository
public class TripDAOMybatis {
	@Autowired
	private SqlSessionTemplate mybatis;

	public void insertTrip(TripVO vo) {
		System.out.println("====> Mybatis�� insertTrip() ��� ó��");
		mybatis.insert("TripDAO.insertTrip", vo);

		// 1208 ���� ����
		System.out.println("====> Mybatis�� insertTripSchedule() ��� ó��");
		int date = (vo.getTrDateSet() + 1) * 4;
		System.out.println(">>>>>>>>>>date" + date);
		System.out.println(">>>>>>>>>>trseq" + vo.getTrSeq());
		System.out.println(">>>>>>>>>>trAreaSet" + vo.getTrAreaSet());
		System.out.println(">>>>>>>>>>trMode" + vo.getTrMode());

		String mode = vo.getTrMode();
		System.out.println(mode);
		if (mode.equals("���Ӹ��") || mode.equals("������õ���")) {
			System.out.println("**********************************");
			for (int i = 0; i < date; i++) {
				mybatis.insert("TripDAO.insertTripSchedule", vo);
			}
		}
		// 1208 ���� ����

	}

	public void updateTrip(TripVO vo) {
		System.out.println("====> Mybatis�� updateTrip() ��� ó��");
		mybatis.update("TripDAO.updateTrip", vo);
	}

	public void deleteTrip(TripVO vo) {
		System.out.println("====> Mybatis�� deleteTrip() ��� ó��");
		mybatis.delete("TripDAO.deleteTrip", vo);
	}

	// ���� �� ��ȸ
	public TripVO getTrip(TripVO vo) {
		System.out.println("====> Mybatis�� getTrip() ��� ó��");
		return (TripVO) mybatis.selectOne("TripDAO.getTrip", vo);
	}

	public List<TripVO> getTripList(TripVO vo) {
		System.out.println("====> Mybatis�� getTripList() ��� ó��");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = mybatis.selectList("TripDAO.getTripList", vo);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		return mybatis.selectList("TripDAO.getTripList", vo);
	}

	public int getTripSeq() {
		System.out.println("====> Mybatis�� getTripSeq() ��� ó��");
		return mybatis.selectOne("TripDAO.getTripSeq");
	}

	public void deleteFileList(int seq) {
		mybatis.delete("TripDAO.deleteFileList", seq);

	}

	public void insertTripMembers(TripMemberVO vo) {
		mybatis.insert("TripDAO.insertTripMembers", vo);

	}

	// ����� ���� �ο��� ��ȸ
	public int countMember(TripMemberVO vo) {
		System.out.println("====> Mybatis��countMember() ��� ó��");
		return mybatis.selectOne("TripDAO.countMember", vo);

	}

	// ����� �ο� �ߺ� �˻�
	public int checkContain(TripMemberVO vo) {
		System.out.println("====> Mybatis�� checkContain() ��� ó��");
		return mybatis.selectOne("TripDAO.checkContainMember", vo);
	}

	public List<TripMemberVO> getTripMemberList(TripVO vo) {
		System.out.println("====> Mybatis�� getTripMemberList() ��� ó��");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = mybatis.selectList("TripDAO.getTripMemberList", vo);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		return mybatis.selectList("TripDAO.getTripMemberList", vo);
	}

	// ����ɹ� ���(host����)
	public void insertTripMemberH(TripMemberVO vo) {
		System.out.println("====> Mybatis�� insertTripMemberH() ��� ó��");
		mybatis.insert("TripDAO.insertTripMemberH", vo);

	}

	// ���� �ɹ� ���� ��ȸ
	public TripMemberVO getTripMember(TripMemberVO vo) {
		System.out.println("====> Mybatis�� getTripMember() ��� ó��");
		return mybatis.selectOne("TripDAO.getTripMember", vo);

	}

	// ���� ���� ���
	public void exitTrip(TripMemberVO vo) {
		System.out.println("====> Mybatis�� exitTrip() ��� ó��");
		mybatis.delete("TripDAO.exitTrip", vo);
	}

	// ���� ���� ����
	public void changeTripStatus(TripVO vo) {
		System.out.println("====> Mybatis�� changeTripStatus() ��� ó��");
		mybatis.update("TripDAO.changeTripStatus", vo);
	}

	// ���� ���� ����Ʈ �ҷ����� (12.12 ����ȣ �߰�)
	public List<ReviewVO> getmyReviewList(ReviewVO vo) {
		System.out.println("====> Mybatis�� getmyReviewList() ��� ó��");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = mybatis.selectList("TripDAO.getmyReviewList", vo);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		return mybatis.selectList("TripDAO.getmyReviewList", vo);
	}
	// ######################## ���� ����################################

	public List<CommentVO> readComment(int seq) {
		System.out.println("======> Mybatis�� readComment() ���ó��");
		return mybatis.selectList("TripDAO.readComment", seq);
	}

	public void insertComment(CommentVO vo) {
		System.out.println("====> Mybatis�� insertComment() ��� ó��");
		mybatis.insert("TripDAO.insertComment", vo);
	}

	// ������ ���� ��������
	public List<ScheduleVO> getscheduleList(ScheduleVO vo) {
		System.out.println("====> Mybatis�� getscheduleList() ��� ó��");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = mybatis.selectList("TripDAO.getscheduleList", vo);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		return mybatis.selectList("TripDAO.getscheduleList", vo);
	}

	// 1209 �߰�
	public void reinsertschedule(TripVO vo) {

		System.out.println("====> Mybatis�� insertTripSchedule() ��� ó��");

		mybatis.delete("TripDAO.deleteTripSchedule", vo.getTrSeq());

		int date = (vo.getTrDateSet() + 1) * 4;
		System.out.println(">>>>>>>>>>date" + date);
		System.out.println(">>>>>>>>>>trseq" + vo.getTrSeq());
		System.out.println(">>>>>>>>>>trAreaSet" + vo.getTrAreaSet());
		System.out.println(">>>>>>>>>>trMode" + vo.getTrMode());

		String mode = vo.getTrMode();
		System.out.println(mode);
		System.out.println("**********************************");
		for (int i = 0; i < date; i++) {
			mybatis.insert("TripDAO.insertTripSchedule", vo);
		}
	}

	// Ŭ����� ������ ���� ��������
	public List<Map<String, String>> getClearedscheduleList(ScheduleVO vo) {
		System.out.println("====> Mybatis�� getClearedscheduleList() ��� ó��");
		return mybatis.selectList("TripDAO.getClearedscheduleList", vo);
	}

	// Ŭ����� ������ ���� ��������
	public Map<String, String> addGameModeSchedule(ScheduleVO vo) {
		System.out.println("====> Mybatis�� addGameModeSchedule() ��� ó��");
		return mybatis.selectOne("TripDAO.addGameModeSchedule", vo);
	}

	public void updateClearTrschedule(ScheduleVO vo) {
		System.out.println("====> Mybatis�� updateClearTrschedule() ��� ó��");
		mybatis.update("TripDAO.updateClearTrschedule", vo);
	}

	public void updateTrStatus(ScheduleVO vo) {
		mybatis.update("TripDAO.updateTrStatus", vo);
	}

	// MyTripList

	public List<TripVO> getMyTripList(int mSeq) {
		return mybatis.selectList("TripDAO.getMyTripList", mSeq);
	}

	public List<TripVO> getMyTripMember(TripVO vo) {
		return mybatis.selectList("TripDAO.getMyTripMember", vo);
	}

	public List<TripVO> getMyTripPlace(TripVO vo) {
		return mybatis.selectList("TripDAO.getMyTripPlace", vo);
	}

	// ���� ���� ���
	public void insertReview(ReviewVO vo) {
		System.out.println("====> Mybatis�� insertReview() ��� ó��");
		mybatis.insert("TripDAO.insertReview", vo);
	}

	// ���� �ߺ� �˻�
	public int checkReview(ReviewVO vo) {
		System.out.println("====> Mybatis�� checkReview() ��� ó��");
		return mybatis.selectOne("TripDAO.checkReview", vo);
	}

	// �Ű� ����� ACCOUNT_STATUS + 1
	public void updateMemberStatus(ReviewVO vo) {
		mybatis.update("TripDAO.updateMemberStatus", vo);

	}
}
