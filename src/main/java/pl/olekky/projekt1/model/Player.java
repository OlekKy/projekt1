package pl.olekky.projekt1.model;

import java.util.List;

public class Player {
	
	private int id;
	private String nickname;
	private int gold;
	private Integer x;
	private Integer y;
	
	private List<Item> items;
	
	public Player() {};
	public Player(int id, String nickname, int gold, Integer x, Integer y) {
		this.id = id;
		this.nickname = nickname;
		this.gold = gold;
		this.x = x;
		this.y =y;
	}
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}

}
