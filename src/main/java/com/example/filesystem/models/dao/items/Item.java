package com.example.filesystem.models.dao.items;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.example.filesystem.models.dao.permissions.PermissionGroup;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
@Setter
@Getter
@Accessors(chain = true)
public class Item {

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "items_seq")
	@SequenceGenerator(name = "items_seq", sequenceName = "items_seq", allocationSize = 1)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_group_id", nullable = false)
	private PermissionGroup permissionGroup;

}