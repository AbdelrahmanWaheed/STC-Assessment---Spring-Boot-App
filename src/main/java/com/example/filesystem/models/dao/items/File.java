package com.example.filesystem.models.dao.items;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("15")
@Setter
@Getter
public class File extends Item {

	@OneToOne(mappedBy = "item", fetch = FetchType.LAZY)
	private BinaryFile file;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Item parent;

}