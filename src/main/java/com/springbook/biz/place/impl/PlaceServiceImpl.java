package com.springbook.biz.place.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbook.biz.place.Criteria;
import com.springbook.biz.place.PlaceReviewCmVO;
import com.springbook.biz.place.PlaceService;
import com.springbook.biz.place.PlaceVO;

@Service("placeService")
/*
 * �������� ó���� ����ϴ� Ŭ���� ex) ������ü ��� ó���� �� DB�Է��� DAO �ϰ� �Ǵµ� DB�Է� �� �ʿ��� �۾���(���¾�ȣȭ, �ݾ�
 * �ĸ� �߰�...) �ϴ� �������� ServiceImplŬ�������� ó�����ָ� ��
 */
public class PlaceServiceImpl implements PlaceService {
	@Autowired
	private PlaceDAOMybatis placeDAO;

	public void insertPlace(PlaceVO vo) {

		placeDAO.insertPlace(vo);

	}

	public int getPlaceSeq() {
		return placeDAO.getPlaceSeq();
	}

	public void deletePlace(PlaceVO vo) {
		placeDAO.deletePlace(vo);

	}

	public void updatePlace(PlaceVO vo) {
		placeDAO.updatePlace(vo);
	}

	public PlaceVO getPlace(PlaceVO vo) {
		return placeDAO.getPlace(vo);
	}

	@Override
	public List<PlaceVO> getPlaceList(PlaceVO vo) {
		return placeDAO.getPlaceList(vo);
	}

	@Override
	public List<PlaceVO> getPlaceAllList(PlaceVO vo) {
		return placeDAO.getPlaceAllList(vo);
	}

	@Override
	public void updatePlaceFile(PlaceVO vo) {
		placeDAO.updatePlaceFile(vo);

	}

//	���� 1206 ����
	// ��� ��õ �Խ��� ��� ��������
	@Override
	public List<PlaceVO> getReviewBoardList(PlaceVO vo) {
		return placeDAO.getReviewBoardList(vo);
	}

	// ��� ���� â���� ��� �ҷ�����
	@Override
	public Map<String, Object> getReviewWriteList(PlaceVO vo) {
		return placeDAO.getReviewWriteList(vo);
	}

	// �����Է�
	@Override
	public void insertPlaceReview(PlaceReviewCmVO vo) {
		placeDAO.insertPlaceReview(vo);
	}

	// ���� �ҷ�����
	@Override
	public List<PlaceReviewCmVO> ReviewReadComment(int seq, Criteria cri) {
		return placeDAO.ReviewReadComment(seq, cri);
	}

	@Override
	public int selectPlaceReviewCount(int pSeq) {
		return placeDAO.selectPlaceReviewCount(pSeq);
	}

	// ���� �� ���� �ҷ�����
	@Override
	public int ReviewAllComment(int seq) {
		return placeDAO.ReviewAllComment(seq);
	}

	// ��� �ּ� �ҷ�����
	@Override
	public PlaceVO ReviewAddress(int seq) {
		return placeDAO.ReviewAddress(seq);
	}

	// ���� ����¡
	@Override
	public List<PlaceVO> getReviewBoardList(PlaceVO vo, Criteria cri) {
		return placeDAO.getReviewBoardList(vo, cri);
	}

	@Override
	public int selectPlaceBoardCount(PlaceVO vo) {
		return placeDAO.selectPlaceBoardCount(vo);
	}

	// ���� ����
	@Override
	public void deleteReview(PlaceReviewCmVO vo) {
		placeDAO.deleteReview(vo);
	}

}
