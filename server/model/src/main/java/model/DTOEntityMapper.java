package model;
public interface DTOEntityMapper<E, D> {
    D toDTO(E entity);

    E toEntity(D dto);
}