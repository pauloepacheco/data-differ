package com.waes.data.differ.entity;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * The DataDifferEntity class represents the Hibernate persistent entity for DATA_DIFFER table
 * 
 * @author Paulo Pacheco
 */

@Entity
@Table(name = "DATA_DIFFER", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class DataDifferEntity {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@NotNull
	private long id;
	
	@Column(name="left")
	private byte[] left;
	
	@Column(name="right")
	private byte[] right;
	
	public DataDifferEntity() {}
	
	public DataDifferEntity(long id) {
		super();
		this.id = id;
	}
	
	public DataDifferEntity(long id, byte[] left, byte[] right) {
		super();
		this.id = id;
		this.left = left;
		this.right = right;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getLeft() {
		return left;
	}

	public void setLeft(byte[] left) {
		this.left = left;
	}

	public byte[] getRight() {
		return right;
	}

	public void setRight(byte[] right) {
		this.right = right;
	}
		
	@Override
	public String toString() {
		return "DataDifferEntity [id=" + id + ", left=" + Arrays.toString(left) + ", right=" + Arrays.toString(right)
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + Arrays.hashCode(left);
		result = prime * result + Arrays.hashCode(right);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataDifferEntity other = (DataDifferEntity) obj;
		if (id != other.id)
			return false;
		if (!Arrays.equals(left, other.left))
			return false;
		if (!Arrays.equals(right, other.right))
			return false;
		return true;
	}



	public enum RequestSource{
		LEFT,
		RIGHT
	}
}
