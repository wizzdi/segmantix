package com.wizzdi.segmantix.store.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import java.time.OffsetDateTime;


@MappedSuperclass
public abstract class Basic {

	@Id
	protected String id;
	protected String name;
	protected String description;
	private boolean softDelete;
	@Column(columnDefinition = "timestamp with time zone")
	private OffsetDateTime creationDate;
	@Column(columnDefinition = "timestamp with time zone")
	private OffsetDateTime updateDate;

	@Column(name = "dtype", insertable = false, updatable = false)
	private String dtype;



	@Id
	public String getId() {
		return id;
	}

	public <T extends Basic> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	public String getName() {
		return name;
	}

	public <T extends Basic> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public String getDescription() {
		return description;
	}

	public <T extends Basic> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}

	public boolean isSoftDelete() {
		return softDelete;
	}

	public <T extends Basic> T setSoftDelete(boolean softDelete) {
		this.softDelete = softDelete;
		return (T) this;
	}

	public OffsetDateTime getCreationDate() {
		return creationDate;
	}

	public <T extends Basic> T setCreationDate(OffsetDateTime creationDate) {
		this.creationDate = creationDate;
		return (T) this;
	}

	public OffsetDateTime getUpdateDate() {
		return updateDate;
	}

	public <T extends Basic> T setUpdateDate(OffsetDateTime updateDate) {
		this.updateDate = updateDate;
		return (T) this;
	}

	@Transient
	public String getJavaType(){
		return getClass().getCanonicalName();
	}
	@Transient
	public String getJsonType(){
		return getClass().getCanonicalName();
	}

	@Column(name = "dtype", insertable = false, updatable = false)
	public String getDtype() {
		return dtype;
	}

	public <T extends Basic> T setDtype(String dtype) {
		this.dtype = dtype;
		return (T) this;
	}
}
