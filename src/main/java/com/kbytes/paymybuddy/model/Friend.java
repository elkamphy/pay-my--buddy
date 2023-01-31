package com.kbytes.paymybuddy.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "friend")
public class Friend {

	@Id
	private FriendKey id;

}