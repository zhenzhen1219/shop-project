package com.shop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shop.constant.ItemSellStatus;

import javassist.bytecode.stackmap.BasicBlock.Catch;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "comment")
@Getter
@Setter
@ToString
public class Comment extends BaseEntity {

	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idx;

	@Column(name = "item_id")
	private Long id;

	private String content;
	private String writer;


	private LocalDateTime regTime; // 등록시간
	

	private LocalDateTime updateTime; // 수정 시간
	
	private int star;

}
