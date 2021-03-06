package com.evolvingreality.common.persistence.domain;

import java.lang.reflect.Method;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ReflectionUtils;


/**
 * This is the base class for all the persisted entities
 * in the system.
 * @author Derek Reynolds
 * @since 1.0
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class EntityBase implements java.io.Serializable {

	protected static transient final long serialVersionUID = -4253619087259962715L;
	

    @Version
    @Column(name = "version")
    private Long version;
    
    @CreatedBy
    @NotNull
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @NotNull
    @Column(name = "CREATED_DATE")
    private Calendar createdDate = Calendar.getInstance();

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private Calendar lastModifiedDate = Calendar.getInstance();


	
	@PrePersist
	@PreUpdate
	public void updateUserNameAndTimestamp(){
		
		if(getCreatedDate() == null) {
			setCreatedDate(Calendar.getInstance());
			setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
		}	
			
		if(SecurityContextHolder.getContext().getAuthentication() != null) {		
			setLastModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
		}
		
		setLastModifiedDate(Calendar.getInstance());	
		
	}	
	
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Calendar getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Calendar lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    @Override
   	public int hashCode() { 
    	
    	Method thisMethod  = ReflectionUtils.findMethod(this.getClass(), "getId");
    	Long thisId = (Long) ReflectionUtils.invokeMethod(thisMethod, this);
    	
    	return new HashCodeBuilder()
    		.append(thisId)
    		.toHashCode();     		
   	}

	@Override
	public String toString() {
	     return new ToStringBuilder(this)
	     	.append("createdBy", this.createdBy)
	     	.append("createdDate", this.createdDate)
	     	.append("lastModifiedBy", this.lastModifiedBy)
	     	.append("lastModifiedDate", this.lastModifiedDate)
	     	.toString();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		
		if(this.getClass() != obj.getClass())
			return false;
		
		Method thisMethod  = ReflectionUtils.findMethod(this.getClass(), "getId");		
		Method otherMethod  = ReflectionUtils.findMethod(obj.getClass(), "getId");
		
		if((thisMethod == null) || (otherMethod == null))
			throw new IllegalStateException("No getId method on entity.");
		
		Long thisId = (Long) ReflectionUtils.invokeMethod(thisMethod, this);
		Long otherId = (Long) ReflectionUtils.invokeMethod(otherMethod, this);
		
		if((thisId == null) || (otherId == null)) 
			return false;
		
		if(thisId != otherId)
			return false;
		
		return true;
		
	}

}