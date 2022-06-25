package com.springbook.biz.place;

import java.util.List;
import java.util.Map;

public interface PlaceService {
	// CRUD ����� �޼ҵ� ����
	// �� ���
	void insertPlace(PlaceVO vo);

	// �� ��� �� ��� �� �Ϸù�ȣ ȹ��
	int getPlaceSeq();

	// �� ����
	void deletePlace(PlaceVO vo);

	// �� ����
	void updatePlace(PlaceVO vo);

	// �� �� ��ȸ
	PlaceVO getPlace(PlaceVO vo);

	// 11.25�� ���Ը�� ����
	List<PlaceVO> getPlaceAllList(PlaceVO vo);

	List<PlaceVO> getPlaceList(PlaceVO vo);

	void updatePlaceFile(PlaceVO vo);

	// ���� 1206 ����
	// �����õ�Խ��� ���� ��ȸ
	List<PlaceVO> getReviewBoardList(PlaceVO vo);

	// ��Ҹ���Խ��� ���� ��ȸ
	Map<String, Object> getReviewWriteList(PlaceVO vo);

	// �����Է�
	void insertPlaceReview(PlaceReviewCmVO vo);

	// ���� �ҷ�����
	List<PlaceReviewCmVO> ReviewReadComment(int seq, Criteria cri);

	int selectPlaceReviewCount(int pSeq);

	// ���� �� ���� �ҷ�����
	int ReviewAllComment(int seq);

	// ��� �ּ� �ҷ�����
	PlaceVO ReviewAddress(int seq);

	// ����¡ ����
	List<PlaceVO> getReviewBoardList(PlaceVO vo, Criteria cri);

	int selectPlaceBoardCount(PlaceVO vo);

	// ���� ����
	void deleteReview(PlaceReviewCmVO vo);

}
