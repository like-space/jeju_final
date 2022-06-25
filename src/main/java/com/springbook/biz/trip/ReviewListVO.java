package com.springbook.biz.trip;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement : �ش� ��ü�� RootElement�� �����ϰ� �̸���  tripList�� ����
@XmlRootElement(name = "myReviewList")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewListVO {
	//@XmlElement : �� ���� ������Ʈ���� �̸� ����
	@XmlElement(name = "myReviewList")
	private List<ReviewVO> myReviewList;

	
	public List<ReviewVO> getmyReviewList() {
		return myReviewList;
	}

	public void setTripMemberList(List<ReviewVO> myReviewList) {
		this.myReviewList = myReviewList;
	}
}
