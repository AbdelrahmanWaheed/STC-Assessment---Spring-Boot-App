package com.example.filesystem.models.dao.items;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "files")
@Setter
@Getter
public class BinaryFile {

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "file_seq")
	@SequenceGenerator(name = "file_seq", sequenceName = "file_seq", allocationSize = 1)
	private Long id;

	@Lob
	@Column(name = "content", nullable = false)
	private byte[] content;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

}