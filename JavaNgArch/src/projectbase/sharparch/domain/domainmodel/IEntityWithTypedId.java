package projectbase.sharparch.domain.domainmodel;

import java.lang.reflect.Method;

/// <summary>
///     This serves as a base interface for <see cref="EntityWithTypedId{TId}" /> and 
///     <see cref = "Entity" />. Also provides a simple means to develop your own base entity.
/// </summary>
public interface IEntityWithTypedId<TId> {
	TId getId();

	Iterable<Method> GetSignatureProperties();

	boolean IsTransient();
}
