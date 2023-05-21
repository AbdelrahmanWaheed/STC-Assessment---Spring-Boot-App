package com.example.filesystem.models.dao.items;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("5")
public class Space extends Item {

}