package com.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payments {
	
	@Id
    @Column(name = "paymentId")
    private String paymentId;          //1. imp_uid

    @Column(unique = true)
    private long orderId;            //2. order id = merchant_uid
    private String payMethod;        //3. 결제수단
    private String pgProvide;        //4. 결제승인된 pg
    private int paidAt;              //5. 결제시간
    private String payStatus;        //6. 결제상태
    private int payAmount;           //7. 결제금액
    private int cancelAmount;
    private String buyerName;        //8. 주문자 이름
    private String bankName;         //9. 가상계좌 은행명
    private String bankHorder;       //10. 가상계좌 예금주
}
