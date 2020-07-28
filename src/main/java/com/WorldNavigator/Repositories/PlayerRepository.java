package com.WorldNavigator.Repositories;

import com.WorldNavigator.Model.PlayerModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface PlayerRepository extends CrudRepository<PlayerModel, Integer> {

    Optional<PlayerModel> findByUsernameIgnoreCase(String username);
}

