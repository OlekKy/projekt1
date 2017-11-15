package pl.olekky.projekt1.controller;

import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.net.SyslogOutputStream;
import pl.olekky.projekt1.model.Player;
import pl.olekky.projekt1.repository.PlayerRepository;
import pl.olekky.projekt1.repository.exception.DatabaseException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class HelloController {


	@Autowired
	PlayerRepository playerRepository;
	

	
	@GetMapping("/player")
	public List<Player> getPlayers() throws DatabaseException {
		// playerRepository.intializedPlayerTable();
		List<Player> playerList = new ArrayList<Player>();
		playerList = playerRepository.getPlayers();
		return playerList;
	}

	@GetMapping("/player/{id}")
	public ResponseEntity<Player> getPlayer(@PathVariable int id) throws DatabaseException {
		Player player = playerRepository.getPlayer(id);
		return new ResponseEntity<Player> (player, HttpStatus.OK);
	}

	@PostMapping("/player")
	public ResponseEntity<Player> postPlayer(@RequestBody Player player) throws DatabaseException {
		int id = playerRepository.addPlayer(player);
		return new ResponseEntity<Player> (player, HttpStatus.CREATED);
	}

	@PutMapping("/player/{id}")
	public ResponseEntity<Player> updateNickname(@PathVariable int id, @RequestBody String nickname) throws DatabaseException {
		Player player = playerRepository.updateNickname(id, nickname);
		return new ResponseEntity<Player> (player,HttpStatus.OK);
	}

	@DeleteMapping("/player/{id}")
	public ResponseEntity<Void> deletePlayer(@PathVariable int id) throws DatabaseException {
		playerRepository.deleteNickname(id);
		return new ResponseEntity<Void> (HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/player/{id}/x")
	public ResponseEntity<Player> changeX(@PathVariable int id, @RequestBody Integer x) throws DatabaseException {
		Player player = playerRepository.changeX(id,x);
		return new ResponseEntity<Player> (player,HttpStatus.OK);
	}
	
	@PutMapping("/player/{id}/y")
	public ResponseEntity<Player> changeY(@PathVariable int id, @RequestBody Integer y) throws DatabaseException {
		Player player = playerRepository.changeY(id,y);
		return new ResponseEntity<Player> (player,HttpStatus.OK);
	}
}