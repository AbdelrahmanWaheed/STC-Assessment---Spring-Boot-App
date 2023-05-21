package com.example.filesystem.models.dao.permissions;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.example.filesystem.models.dao.items.Item;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "permissions_groups")
@Setter
@Getter
@Accessors(chain = true)
public class PermissionGroup {

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "permissions_groups_seq")
	@SequenceGenerator(name = "permissions_groups_seq", sequenceName = "permissions_groups_seq", allocationSize = 1)
	private Long id;

	@Column(name = "group_name", nullable = false, unique = true)
	private String groupName;

	@OneToMany(mappedBy = "permissionGroup", fetch = FetchType.LAZY)
	private List<Item> items;

	@OneToMany(mappedBy = "permissionGroup", fetch = FetchType.LAZY)
	private List<Permission> permissions;

}