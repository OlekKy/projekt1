package pl.olekky.projekt1.controller;

import org.springframework.web.bind.annotation.RestController;

import pl.olekky.projekt1.model.Player;
import pl.olekky.projekt1.repository.PlayerRepository;
import pl.olekky.projekt1.repository.exception.DatabaseException;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class HelloController {

	@Autowired
	PlayerRepository playerRepository;
	
	@GetMapping("/player")
    public List<Player> getPlayers() throws DatabaseException {
        //playerRepository.intializedPlayerTable();
    	List<Player> playerList = new ArrayList<Player>();
    	playerList = playerRepository.getPlayers();
        return playerList;
    }
    @GetMapping("/player/{id}")
    public Player getPlayer(@PathVariable int id) throws DatabaseException {
    	Player player = playerRepository.getPlayer(id);
        return player;
    }
	@PostMapping("/player")
    public Player postPlayer(@RequestBody Player player) throws DatabaseException {
		int id = playerRepository.addPlayer(player);
        return player;
    }
}