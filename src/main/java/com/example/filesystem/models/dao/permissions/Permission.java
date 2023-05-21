package com.example.filesystem.models.dao.permissions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "permissions", uniqueConstraints = @UniqueConstraint(columnNames = { "user_email", "permission_group_id", "permission_level" }))
@Setter
@Getter
public class Permission {

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "permissions_seq")
	@SequenceGenerator(name = "permissions_seq", sequenceName = "permissions_seq", allocationSize = 1)
	private Long id;

	@Column(name = "user_email", nullable = false)
	private String userEmail;

	@Column(name = "permission_level", nullable = false)
	private String permissionLevel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_group_id", nullable = false)
	private PermissionGroup permissionGroup;

}