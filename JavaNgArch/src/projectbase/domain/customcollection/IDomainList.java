package projectbase.domain.customcollection;

import java.util.List;

/// <summary>
/// domainlist interface used by hibernate clients
/// </summary>
public interface IDomainList<T> extends List, IDomainCollection<T> {
}
