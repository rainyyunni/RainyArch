package projectbase.sharparch.domain.persistencesupport;

import projectbase.sharparch.domain.domainmodel.IEntityWithTypedId;

    public interface IEntityDuplicateChecker
    {
    	<TId> boolean DoesDuplicateExistWithTypedIdOf(IEntityWithTypedId<TId> entity);
    }
