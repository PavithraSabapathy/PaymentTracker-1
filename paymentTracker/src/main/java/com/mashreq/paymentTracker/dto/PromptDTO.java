package com.mashreq.paymentTracker.dto;

import java.io.Serializable;
import java.math.BigInteger;

import jakarta.validation.constraints.NotEmpty;

public class PromptDTO implements Serializable {
	/**
	 * 
	 */
	@NotEmpty
	private String promptKey;
	@NotEmpty
	private String displayName;
	// @NotEmpty
	private BigInteger promptOrder;
	@NotEmpty
	private String promptRequired;
	@NotEmpty
	private long reportId;
	// TODO @NotEmpty
	private BigInteger entityId;

	public String getPromptKey() {
		return promptKey;
	}

	public void setPromptKey(String promptKey) {
		this.promptKey = promptKey;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public BigInteger getPromptOrder() {
		return promptOrder;
	}

	public void setPromptOrder(BigInteger promptOrder) {
		this.promptOrder = promptOrder;
	}

	public String getPromptRequired() {
		return promptRequired;
	}

	public void setPromptRequired(String promptRequired) {
		this.promptRequired = promptRequired;
	}

	public long getReportId() {
		return reportId;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
	}

	public BigInteger getEntityId() {
		return entityId;
	}

	public void setEntityId(BigInteger entityId) {
		this.entityId = entityId;
	}

	@Override
	public String toString() {
		return "PromptDTO [promptKey=" + promptKey + ", displayName=" + displayName + ", promptOrder=" + promptOrder
				+ ", promptRequired=" + promptRequired + ", reportId=" + reportId + ", entityId=" + entityId + "]";
	}

	public PromptDTO(@NotEmpty String promptKey, @NotEmpty String displayName, BigInteger promptOrder,
			@NotEmpty String promptRequired, @NotEmpty long reportId, BigInteger entityId) {
		super();
		this.promptKey = promptKey;
		this.displayName = displayName;
		this.promptOrder = promptOrder;
		this.promptRequired = promptRequired;
		this.reportId = reportId;
		this.entityId = entityId;
	}

	public PromptDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
