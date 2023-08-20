package com.programming.techie.common.domain.entity;

import java.io.Serializable;
import java.util.Objects;

public abstract class BaseDomainEntity<ID> implements Serializable {

	private static final long serialVersionUID = -6468428072785187850L;
	
	protected ID id;

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseDomainEntity<?> other = (BaseDomainEntity<?>) obj;
		return Objects.equals(id, other.id);
	}

}
