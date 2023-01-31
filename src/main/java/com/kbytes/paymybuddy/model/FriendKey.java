package com.kbytes.paymybuddy.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class FriendKey implements Serializable {

	@Column(name = "user_id1")
	Long userId1;

	@Column(name = "user_id2")
	Long userId2;
}
