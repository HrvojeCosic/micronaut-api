package com.base.mapper;

public interface Mapper<Entity, Dto> {

    Dto mapTo(Entity entity);

    Entity mapFrom(Dto dto);
}